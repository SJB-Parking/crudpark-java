package app.controller;

import app.exception.BusinessException;
import app.exception.DataAccessException;
import app.exception.NotFoundException;
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
     * Process vehicle entry - returns EntryResult instead of throwing exceptions
     * Controller handles all exceptions and converts them to result objects
     */
    public EntryResult processEntry(String licensePlate, int operatorId) {
        try {
            // Validate license plate
            if (licensePlate == null || licensePlate.trim().isEmpty()) {
                return EntryResult.validationError("License plate cannot be empty");
            }
            
            licensePlate = licensePlate.trim().toUpperCase();
            
            if (licensePlate.length() != 6) {
                return EntryResult.validationError("License plate must be 6 characters");
            }
            
            // Validate operator ID
            if (operatorId <= 0) {
                return EntryResult.validationError("Invalid operator ID");
            }
            
            // Call service
            Ticket ticket = parkingService.processEntry(licensePlate, operatorId);
            return EntryResult.success(ticket);
            
        } catch (BusinessException e) {
            return EntryResult.businessError(e.getMessage());
        } catch (DataAccessException e) {
            return EntryResult.dataAccessError("Error processing vehicle entry: " + e.getMessage());
        } catch (Exception e) {
            return EntryResult.dataAccessError("Unexpected error: " + e.getMessage());
        }
    }

    /**
     * Process vehicle exit - returns ExitResult instead of throwing exceptions
     * Controller handles all exceptions and converts them to result objects
     */
    public ExitResult processExit(String ticketIdStr, int operatorId) {
        try {
            // Validate ticket ID string
            if (ticketIdStr == null || ticketIdStr.trim().isEmpty()) {
                return ExitResult.validationError("Ticket ID cannot be empty");
            }
            
            // Parse and validate ticket ID
            int ticketId;
            try {
                ticketId = Integer.parseInt(ticketIdStr.trim());
            } catch (NumberFormatException e) {
                return ExitResult.validationError("Ticket ID must be a valid number");
            }
            
            if (ticketId <= 0) {
                return ExitResult.validationError("Ticket ID must be greater than 0");
            }
            
            // Validate operator ID
            if (operatorId <= 0) {
                return ExitResult.validationError("Invalid operator ID");
            }
            
            // Call service
            ParkingService.ExitResult result = parkingService.processExit(ticketId, operatorId);
            return ExitResult.success(result);
            
        } catch (NotFoundException e) {
            return ExitResult.notFoundError(e.getMessage());
        } catch (BusinessException e) {
            return ExitResult.businessError(e.getMessage());
        } catch (DataAccessException e) {
            return ExitResult.dataAccessError("Error processing vehicle exit: " + e.getMessage());
        } catch (Exception e) {
            return ExitResult.dataAccessError("Unexpected error: " + e.getMessage());
        }
    }
}
