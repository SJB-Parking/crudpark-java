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

    /**
     * Process vehicle exit by license plate - returns ExitResult instead of throwing exceptions
     * Controller handles all exceptions and converts them to result objects
     */
    public ExitResult processExitByPlate(String licensePlate, int operatorId, String paymentMethod) {
        try {
            // Validate license plate
            if (licensePlate == null || licensePlate.trim().isEmpty()) {
                return ExitResult.validationError("License plate cannot be empty");
            }
            
            licensePlate = licensePlate.trim().toUpperCase();
            
            if (licensePlate.length() != 6) {
                return ExitResult.validationError("License plate must be 6 characters");
            }
            
            // Validate operator ID
            if (operatorId <= 0) {
                return ExitResult.validationError("Invalid operator ID");
            }
            
            // Validate payment method
            if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
                return ExitResult.validationError("Payment method cannot be empty");
            }
            
            // Call service
            ParkingService.ExitResult result = parkingService.processExitByPlate(licensePlate, operatorId, paymentMethod);
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

    /**
     * Process vehicle exit by ticket ID with payment method
     */
    public ExitResult processExitWithPayment(int ticketId, int operatorId, String paymentMethod) {
        try {
            // Validate ticket ID
            if (ticketId <= 0) {
                return ExitResult.validationError("Ticket ID must be greater than 0");
            }
            
            // Validate operator ID
            if (operatorId <= 0) {
                return ExitResult.validationError("Invalid operator ID");
            }
            
            // Validate payment method
            if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
                return ExitResult.validationError("Payment method cannot be empty");
            }
            
            // Call service
            ParkingService.ExitResult result = parkingService.processExitWithPayment(ticketId, operatorId, paymentMethod);
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

    /**
     * Preview exit by plate (find ticket and calculate amount, but don't process yet)
     */
    public ExitResult previewExitByPlate(String licensePlate, int operatorId) {
        try {
            // Validate license plate
            if (licensePlate == null || licensePlate.trim().isEmpty()) {
                return ExitResult.validationError("License plate cannot be empty");
            }
            
            licensePlate = licensePlate.trim().toUpperCase();
            
            if (licensePlate.length() != 6) {
                return ExitResult.validationError("License plate must be 6 characters");
            }
            
            // Validate operator ID
            if (operatorId <= 0) {
                return ExitResult.validationError("Invalid operator ID");
            }
            
            // Call service to preview (this will find the ticket and calculate but not save)
            ParkingService.ExitResult result = parkingService.previewExitByPlate(licensePlate, operatorId);
            return ExitResult.success(result);
            
        } catch (NotFoundException e) {
            return ExitResult.notFoundError(e.getMessage());
        } catch (BusinessException e) {
            return ExitResult.businessError(e.getMessage());
        } catch (DataAccessException e) {
            return ExitResult.dataAccessError("Error previewing vehicle exit: " + e.getMessage());
        } catch (Exception e) {
            return ExitResult.dataAccessError("Unexpected error: " + e.getMessage());
        }
    }

    /**
     * Preview exit by ID (find ticket and calculate amount, but don't process yet)
     */
    public ExitResult previewExitById(String ticketIdStr, int operatorId) {
        try {
            // Validate ticket ID
            if (ticketIdStr == null || ticketIdStr.trim().isEmpty()) {
                return ExitResult.validationError("Ticket ID cannot be empty");
            }
            
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
            
            // Call service to preview (this will find the ticket and calculate but not save)
            ParkingService.ExitResult result = parkingService.previewExitById(ticketId, operatorId);
            return ExitResult.success(result);
            
        } catch (NotFoundException e) {
            return ExitResult.notFoundError(e.getMessage());
        } catch (BusinessException e) {
            return ExitResult.businessError(e.getMessage());
        } catch (DataAccessException e) {
            return ExitResult.dataAccessError("Error previewing vehicle exit: " + e.getMessage());
        } catch (Exception e) {
            return ExitResult.dataAccessError("Unexpected error: " + e.getMessage());
        }
    }
}
