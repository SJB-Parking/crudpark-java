package app.service;

import app.dao.*;
import app.database.DatabaseConnection;
import app.exception.BusinessException;
import app.exception.DataAccessException;
import app.exception.NotFoundException;
import app.model.Rate;
import app.model.Ticket;
import app.model.Vehicle;
import app.util.Logger;
import app.util.QRCodeGenerator;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.regex.Pattern;

/**
 * Parking service - handles vehicle entry and exit
 */
public class ParkingService {
    private static final Logger logger = Logger.getLogger(ParkingService.class);
    private static final Pattern CAR_PATTERN = Pattern.compile("^[A-Z]{3}\\d{3}$");
    private static final Pattern MOTORCYCLE_PATTERN = Pattern.compile("^[A-Z]{3}\\d{2}[A-Z]$");

    private final VehicleDAO vehicleDAO;
    private final TicketDAO ticketDAO;
    private final SubscriptionDAO subscriptionDAO;
    private final RateDAO rateDAO;
    private final PaymentDAO paymentDAO;

    public ParkingService(VehicleDAO vehicleDAO, TicketDAO ticketDAO, 
                         SubscriptionDAO subscriptionDAO, RateDAO rateDAO, 
                         PaymentDAO paymentDAO) {
        this.vehicleDAO = vehicleDAO;
        this.ticketDAO = ticketDAO;
        this.subscriptionDAO = subscriptionDAO;
        this.rateDAO = rateDAO;
        this.paymentDAO = paymentDAO;
    }

    /**
     * Detect vehicle type from license plate
     */
    public String detectVehicleType(String licensePlate) {
        licensePlate = licensePlate.toUpperCase().trim();
        
        if (CAR_PATTERN.matcher(licensePlate).matches()) {
            return "Car";
        } else if (MOTORCYCLE_PATTERN.matcher(licensePlate).matches()) {
            return "Motorcycle";
        }
        
        return null;
    }

    /**
     * Process vehicle entry
     */
    public Ticket processEntry(String licensePlate, int operatorId) 
            throws BusinessException, DataAccessException {
        
        licensePlate = licensePlate.toUpperCase().trim();
        
        String vehicleType = detectVehicleType(licensePlate);
        if (vehicleType == null) {
            throw new BusinessException(
                "Invalid license plate format.\n" +
                "Car: 3 letters + 3 numbers (e.g., ABC123)\n" +
                "Motorcycle: 3 letters + 2 numbers + 1 letter (e.g., ABC12D)"
            );
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Find or create vehicle
                Vehicle vehicle = vehicleDAO.findOrCreate(conn, licensePlate, vehicleType);
                
                // Check for open tickets
                if (ticketDAO.hasOpenTicket(conn, vehicle.getId())) {
                    throw new BusinessException("Vehicle already has an open ticket");
                }
                
                // Check for active subscription
                Integer subscriptionId = subscriptionDAO.findActiveSubscriptionByVehicleId(
                    conn, vehicle.getId()
                );
                String ticketType = subscriptionId != null ? "Monthly" : "Guest";
                
                if (subscriptionId != null) {
                    logger.info("Active subscription found for {}: Subscription ID {}", licensePlate, subscriptionId);
                } else {
                    logger.debug("No active subscription for {}, ticket type: Guest", licensePlate);
                }
                
                // Generate folio
                String folio = ticketDAO.generateNextFolio(conn);
                Timestamp now = new Timestamp(System.currentTimeMillis());
                
                // Create ticket with temporary QR (we need the ticket ID first)
                String tempQrCodeData = "PENDING";
                Ticket ticket = ticketDAO.create(conn, folio, vehicle.getId(), operatorId, 
                                                subscriptionId, ticketType, tempQrCodeData);
                
                // Generate QR code content with ticket ID (6-digit formatted: 000001, 000002, etc.)
                String qrCodeData = String.format("TICKET:%06d|PLATE:%s|DATE:%d", 
                                                ticket.getId(), licensePlate, now.getTime() / 1000);
                
                // Update ticket with final QR code
                ticketDAO.updateQRCode(conn, ticket.getId(), qrCodeData);
                ticket.setQrCodeData(qrCodeData);
                
                // Save QR code image to file system (backup)
                try {
                    QRCodeGenerator.saveQRCodeToFile(qrCodeData, ticket.getId());
                } catch (Exception e) {
                    // Log but don't fail - file backup is optional
                    System.err.println("Warning: Could not save QR code to file: " + e.getMessage());
                }
                
                ticket.setLicensePlate(licensePlate);
                ticket.setVehicleType(vehicleType);
                
                conn.commit();
                return ticket;
                
            } catch (DataAccessException | BusinessException e) {
                conn.rollback();
                throw e;
            } catch (Exception e) {
                conn.rollback();
                throw new DataAccessException("Error processing entry", e);
            }
        } catch (Exception e) {
            if (e instanceof DataAccessException) throw (DataAccessException) e;
            if (e instanceof BusinessException) throw (BusinessException) e;
            throw new DataAccessException("Database connection error", e);
        }
    }

    /**
     * Process vehicle exit
     */
    public ExitResult processExit(int ticketId, int operatorId) 
            throws NotFoundException, BusinessException, DataAccessException {
        logger.info("Processing vehicle exit: Ticket ID {} (Operator ID: {})", ticketId, operatorId);
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Get ticket
                Ticket ticket = ticketDAO.findByIdWithVehicle(ticketId);
                
                if (ticket == null) {
                    logger.warn("Ticket not found: ID {}", ticketId);
                    throw new NotFoundException("Ticket not found");
                }
                
                logger.debug("Ticket found: {} - Plate: {} (Status: {})", 
                            ticketId, ticket.getLicensePlate(), ticket.getStatus());
                
                if (!"OPEN".equals(ticket.getStatus())) {
                    logger.warn("Attempted exit with closed ticket: ID {}", ticketId);
                    throw new BusinessException("Ticket is already closed");
                }
                
                // Calculate duration
                Timestamp now = new Timestamp(System.currentTimeMillis());
                long durationMinutes = (now.getTime() - ticket.getEntryDatetime().getTime()) / (60 * 1000);
                
                // Update ticket
                ticketDAO.updateExit(conn, ticketId, now, (int) durationMinutes);
                
                // Calculate cost
                double amount = 0.0;
                boolean isFree = false;
                String freeReason = null;
                
                // Get the appropriate rate for this vehicle type
                Rate rate = rateDAO.findActiveRateByVehicleType(ticket.getVehicleType(), conn);
                
                if (rate == null) {
                    logger.error("No active rate found for vehicle type: {}", ticket.getVehicleType());
                    throw new DataAccessException("No active rate found for vehicle type: " + ticket.getVehicleType());
                }
                
                logger.debug("Rate applied: {} (ID: {}) for vehicle type: {}", 
                            rate.getRateName(), rate.getId(), ticket.getVehicleType());
                
                if ("Monthly".equals(ticket.getTicketType())) {
                    isFree = true;
                    freeReason = "Monthly Subscription";
                    logger.info("Exit free - Monthly subscription: {}", ticket.getLicensePlate());
                } else if (durationMinutes <= rate.getGracePeriodMinutes()) {
                    isFree = true;
                    freeReason = String.format("Grace Period (first %d minutes)", rate.getGracePeriodMinutes());
                    logger.info("Exit free - Grace period: {} (Duration: {}min)", 
                               ticket.getLicensePlate(), durationMinutes);
                } else {
                    amount = calculateAmount(durationMinutes, rate);
                    logger.debug("Payment calculated: ${} for {} (Duration: {}min)", 
                                amount, ticket.getLicensePlate(), durationMinutes);
                    
                    // Record payment
                    if (amount > 0) {
                        paymentDAO.create(conn, ticketId, operatorId, amount, "Cash");
                        logger.info("Payment recorded: ${} for {} (Ticket: {})", 
                                   amount, ticket.getLicensePlate(), ticketId);
                    }
                }
                
                conn.commit();
                
                logger.info("Vehicle exit successful: {} - Ticket ID: {} (Amount: ${}, Free: {})", 
                           ticket.getLicensePlate(), ticketId, amount, isFree);
                
                return new ExitResult(ticket, now, (int) durationMinutes, amount, isFree, freeReason);
                
            } catch (NotFoundException | BusinessException | DataAccessException e) {
                conn.rollback();
                logger.error("Vehicle exit failed for Ticket ID {}: {}", ticketId, e.getMessage());
                throw e;
            } catch (Exception e) {
                conn.rollback();
                throw new DataAccessException("Error processing exit", e);
            }
        } catch (Exception e) {
            if (e instanceof NotFoundException) throw (NotFoundException) e;
            if (e instanceof BusinessException) throw (BusinessException) e;
            if (e instanceof DataAccessException) throw (DataAccessException) e;
            throw new DataAccessException("Database connection error", e);
        }
    }

    /**
     * Calculate payment amount based on duration and rate
     * @param durationMinutes Total parking duration in minutes
     * @param rate The rate to apply
     * @return Amount to charge
     */
    private double calculateAmount(long durationMinutes, Rate rate) {
        long chargeableMinutes = durationMinutes - rate.getGracePeriodMinutes();
        long hours = chargeableMinutes / 60;
        long remainingMinutes = chargeableMinutes % 60;
        
        double amount = hours * rate.getHourlyRate();
        if (remainingMinutes > 0) {
            amount += rate.getFractionRate();
        }
        
        // Apply daily cap if configured
        if (rate.getDailyCap() != null && amount > rate.getDailyCap()) {
            amount = rate.getDailyCap();
        }
        
        return amount;
    }

    /**
     * Exit result data class
     */
    public static class ExitResult {
        private final Ticket ticket;
        private final Timestamp exitTime;
        private final int durationMinutes;
        private final double amount;
        private final boolean isFree;
        private final String freeReason;

        public ExitResult(Ticket ticket, Timestamp exitTime, int durationMinutes, 
                         double amount, boolean isFree, String freeReason) {
            this.ticket = ticket;
            this.exitTime = exitTime;
            this.durationMinutes = durationMinutes;
            this.amount = amount;
            this.isFree = isFree;
            this.freeReason = freeReason;
        }

        public Ticket getTicket() {
            return ticket;
        }

        public Timestamp getExitTime() {
            return exitTime;
        }

        public int getDurationMinutes() {
            return durationMinutes;
        }

        public double getAmount() {
            return amount;
        }

        public boolean isFree() {
            return isFree;
        }

        public String getFreeReason() {
            return freeReason;
        }
    }
}
