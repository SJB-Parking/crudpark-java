package app.dao;

import app.database.DatabaseConnection;
import app.exception.DataAccessException;
import app.model.Ticket;
import app.util.Logger;

import java.sql.*;

/**
 * Data Access Object for Ticket entity
 */
public class TicketDAO {
    private static final Logger logger = Logger.getLogger(TicketDAO.class);

    /**
     * Check if vehicle has an open ticket
     */
    public boolean hasOpenTicket(Connection conn, int vehicleId) throws DataAccessException {
        logger.debug("Checking open tickets for vehicle ID: {}", vehicleId);
        String sql = "SELECT COUNT(*) FROM tickets WHERE vehicle_id = ? AND status = 'OPEN'";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vehicleId);
            ResultSet rs = stmt.executeQuery();
            boolean hasOpen = rs.next() && rs.getInt(1) > 0;
            logger.debug("Vehicle {} has open ticket: {}", vehicleId, hasOpen);
            return hasOpen;
        } catch (SQLException e) {
            logger.error("Error checking open tickets for vehicle {}: {}", vehicleId, e.getMessage());
            throw new DataAccessException("Error checking open tickets", e);
        }
    }

    /**
     * Generate next folio number
     */
    public String generateNextFolio(Connection conn) throws DataAccessException {
        String sql = "SELECT MAX(CAST(SUBSTRING(folio FROM 4) AS INTEGER)) " +
                     "FROM tickets WHERE folio LIKE 'TKT%'";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            int nextNumber = rs.next() ? rs.getInt(1) + 1 : 1;
            return String.format("TKT%06d", nextNumber);
        } catch (SQLException e) {
            throw new DataAccessException("Error generating folio", e);
        }
    }

    /**
     * Create a new ticket
     */
    public Ticket create(Connection conn, String folio, int vehicleId, int operatorId,
                        Integer subscriptionId, String ticketType, String qrCodeData) 
            throws DataAccessException {
        logger.info("Creating ticket: {} for vehicle ID {} (Type: {})", folio, vehicleId, ticketType);
        String sql = "INSERT INTO tickets (folio, vehicle_id, operator_id, subscription_id, " +
                     "entry_datetime, ticket_type, status, qr_code_data, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, NOW(), ?, 'OPEN', ?, NOW(), NOW()) " +
                     "RETURNING id, entry_datetime";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, folio);
            stmt.setInt(2, vehicleId);
            stmt.setInt(3, operatorId);
            if (subscriptionId != null) {
                stmt.setInt(4, subscriptionId);
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setString(5, ticketType);
            stmt.setString(6, qrCodeData);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setId(rs.getInt(1));
                ticket.setFolio(folio);
                ticket.setVehicleId(vehicleId);
                ticket.setOperatorId(operatorId);
                ticket.setEntryDatetime(rs.getTimestamp(2));
                ticket.setTicketType(ticketType);
                ticket.setStatus("OPEN");
                ticket.setQrCodeData(qrCodeData);
                logger.info("Ticket created successfully: ID {} - {}", ticket.getId(), folio);
                return ticket;
            }
            
            logger.error("Failed to create ticket: {}", folio);
            throw new DataAccessException("Failed to create ticket");
            
        } catch (SQLException e) {
            logger.error("SQL error creating ticket {}: {}", folio, e.getMessage());
            throw new DataAccessException("Error creating ticket", e);
        }
    }

    /**
     * Find ticket by ID with vehicle information
     */
    public Ticket findByIdWithVehicle(int ticketId) throws DataAccessException {
        logger.debug("Finding ticket by ID: {}", ticketId);
        String sql = "SELECT t.id, t.folio, t.vehicle_id, t.operator_id, t.subscription_id, " +
                     "t.entry_datetime, t.exit_datetime, t.ticket_type, t.status, " +
                     "t.parking_duration_minutes, t.qr_code_data, " +
                     "v.license_plate, v.vehicle_type " +
                     "FROM tickets t " +
                     "INNER JOIN vehicles v ON t.vehicle_id = v.id " +
                     "WHERE t.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ticketId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setId(rs.getInt("id"));
                ticket.setFolio(rs.getString("folio"));
                ticket.setVehicleId(rs.getInt("vehicle_id"));
                ticket.setOperatorId(rs.getInt("operator_id"));
                
                Integer subId = rs.getObject("subscription_id") != null ? 
                               rs.getInt("subscription_id") : null;
                ticket.setSubscriptionId(subId);
                
                ticket.setEntryDatetime(rs.getTimestamp("entry_datetime"));
                ticket.setExitDatetime(rs.getTimestamp("exit_datetime"));
                ticket.setTicketType(rs.getString("ticket_type"));
                ticket.setStatus(rs.getString("status"));
                ticket.setParkingDurationMinutes(rs.getObject("parking_duration_minutes") != null ?
                                                rs.getInt("parking_duration_minutes") : null);
                ticket.setQrCodeData(rs.getString("qr_code_data"));
                ticket.setLicensePlate(rs.getString("license_plate"));
                ticket.setVehicleType(rs.getString("vehicle_type"));
                logger.debug("Ticket found: {} - {} ({})", ticketId, ticket.getFolio(), ticket.getStatus());
                return ticket;
            }
            
            logger.warn("Ticket not found: {}", ticketId);
            return null;
            
        } catch (SQLException e) {
            logger.error("Error finding ticket {}: {}", ticketId, e.getMessage());
            throw new DataAccessException("Error finding ticket", e);
        }
    }

    /**
     * Update ticket QR code
     */
    public void updateQRCode(Connection conn, int ticketId, String qrCodeData) 
            throws DataAccessException {
        String sql = "UPDATE tickets SET qr_code_data = ?, updated_at = NOW() WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, qrCodeData);
            stmt.setInt(2, ticketId);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DataAccessException("Ticket not found");
            }
            
        } catch (SQLException e) {
            throw new DataAccessException("Error updating ticket QR code", e);
        }
    }

    /**
     * Update ticket exit information
     */
    public void updateExit(Connection conn, int ticketId, Timestamp exitTime, int durationMinutes) 
            throws DataAccessException {
        String sql = "UPDATE tickets SET exit_datetime = ?, parking_duration_minutes = ?, " +
                     "status = 'CLOSED', updated_at = NOW() WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, exitTime);
            stmt.setInt(2, durationMinutes);
            stmt.setInt(3, ticketId);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DataAccessException("Ticket not found or already closed");
            }
            
        } catch (SQLException e) {
            throw new DataAccessException("Error updating ticket exit", e);
        }
    }
}
