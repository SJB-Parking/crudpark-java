package app.controller;

import app.exception.BusinessException;
import app.exception.DataAccessException;
import app.exception.NotFoundException;
import app.exception.ValidationException;
import app.model.Ticket;
import app.service.ParkingService;

/**
 * Parking controller
 */
public class ParkingController {
    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    /**
     * Process vehicle entry
     */
    public Ticket processEntry(String licensePlate, int operatorId) 
            throws ValidationException, BusinessException, DataAccessException {
        
        // Validate license plate
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new ValidationException("License plate cannot be empty");
        }
        
        licensePlate = licensePlate.trim().toUpperCase();
        
        if (licensePlate.length() < 6 || licensePlate.length() > 6) {
            throw new ValidationException("License plate must be 6 characters");
        }
        
        // Validate operator ID
        if (operatorId <= 0) {
            throw new ValidationException("Invalid operator ID");
        }
        
        // Call service
        try {
            return parkingService.processEntry(licensePlate, operatorId);
        } catch (BusinessException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new DataAccessException("Error processing vehicle entry: " + e.getMessage());
        }
    }

    /**
     * Process vehicle exit
     */
    public ParkingService.ExitResult processExit(String ticketIdStr, int operatorId) 
            throws ValidationException, NotFoundException, BusinessException, DataAccessException {
        
        // Validate ticket ID string
        if (ticketIdStr == null || ticketIdStr.trim().isEmpty()) {
            throw new ValidationException("Ticket ID cannot be empty");
        }
        
        // Parse and validate ticket ID
        int ticketId;
        try {
            ticketId = Integer.parseInt(ticketIdStr.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException("Ticket ID must be a valid number");
        }
        
        if (ticketId <= 0) {
            throw new ValidationException("Ticket ID must be greater than 0");
        }
        
        // Validate operator ID
        if (operatorId <= 0) {
            throw new ValidationException("Invalid operator ID");
        }
        
        // Call service
        try {
            return parkingService.processExit(ticketId, operatorId);
        } catch (NotFoundException e) {
            throw e;
        } catch (BusinessException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new DataAccessException("Error processing vehicle exit: " + e.getMessage());
        }
    }

    /**
     * Detect vehicle type (for validation)
     */
    public String detectVehicleType(String licensePlate) throws ValidationException {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new ValidationException("License plate cannot be empty");
        }
        
        String vehicleType = parkingService.detectVehicleType(licensePlate.trim());
        
        if (vehicleType == null) {
            throw new ValidationException(
                "Invalid license plate format.\n" +
                "Car: 3 letters + 3 numbers (e.g., ABC123)\n" +
                "Motorcycle: 3 letters + 2 numbers + 1 letter (e.g., ABC12D)"
            );
        }
        
        return vehicleType;
    }
}
