package app.controller;

import app.util.Logger;

/**
 * Swing vehicle exit controller
 * This controller has NO UI logic - it only coordinates between view and business logic
 */
public class SwingVehicleExitController {
    private static final Logger logger = Logger.getLogger(SwingVehicleExitController.class);
    private final ParkingController parkingController;
    private final int operatorId;

    public SwingVehicleExitController(ParkingController parkingController, int operatorId) {
        this.parkingController = parkingController;
        this.operatorId = operatorId;
    }

    /**
     * Process vehicle exit request - NO UI logic here
     * Returns ExitResult for the view to handle
     */
    public ExitResult processExit(String ticketId) {
        logger.info("Processing vehicle exit: Ticket {} (Operator ID: {})", ticketId, operatorId);
        ExitResult result = parkingController.processExit(ticketId, operatorId);
        
        if (result.isSuccess()) {
            logger.info("Exit successful - Amount: ${}", result.getExitResult().getAmount());
        } else {
            logger.warn("Exit failed: {} ({})", result.getErrorMessage(), result.getErrorType());
        }
        
        return result;
    }

    /**
     * Process vehicle exit with custom payment method
     */
    public ExitResult processExitWithPayment(int ticketId, String paymentMethod) {
        logger.info("Processing vehicle exit: Ticket {} with {} (Operator ID: {})", 
                   ticketId, paymentMethod, operatorId);
        ExitResult result = parkingController.processExitWithPayment(ticketId, operatorId, paymentMethod);
        
        if (result.isSuccess()) {
            logger.info("Exit successful - Amount: ${}, Method: {}", 
                       result.getExitResult().getAmount(), paymentMethod);
        } else {
            logger.warn("Exit failed: {} ({})", result.getErrorMessage(), result.getErrorType());
        }
        
        return result;
    }

    /**
     * Find open ticket by license plate for preview (doesn't process exit yet)
     */
    public ExitResult previewExitByPlate(String licensePlate) {
        logger.info("Previewing exit for plate: {} (Operator ID: {})", licensePlate, operatorId);
        
        try {
            // We'll use processExitByPlate but with a temporary payment method
            // The actual payment will be processed in processExitByPlateWithPayment
            return parkingController.previewExitByPlate(licensePlate, operatorId);
        } catch (Exception e) {
            logger.error("Error previewing exit by plate: {}", e.getMessage());
            return ExitResult.dataAccessError("Error al buscar ticket por placa: " + e.getMessage());
        }
    }

    /**
     * Preview exit by ticket ID (doesn't process exit yet)
     */
    public ExitResult previewExitById(String ticketId) {
        logger.info("Previewing exit for ticket ID: {} (Operator ID: {})", ticketId, operatorId);
        
        try {
            return parkingController.previewExitById(ticketId, operatorId);
        } catch (Exception e) {
            logger.error("Error previewing exit by ID: {}", e.getMessage());
            return ExitResult.dataAccessError("Error al buscar ticket por ID: " + e.getMessage());
        }
    }

    /**
     * Process exit by plate with payment method
     */
    public ExitResult processExitByPlateWithPayment(String licensePlate, String paymentMethod) {
        logger.info("Processing vehicle exit by plate: {} with {} (Operator ID: {})", 
                   licensePlate, paymentMethod, operatorId);
        ExitResult result = parkingController.processExitByPlate(licensePlate, operatorId, paymentMethod);
        
        if (result.isSuccess()) {
            logger.info("Exit successful - Amount: ${}, Method: {}", 
                       result.getExitResult().getAmount(), paymentMethod);
        } else {
            logger.warn("Exit failed: {} ({})", result.getErrorMessage(), result.getErrorType());
        }
        
        return result;
    }

    public int getOperatorId() {
        return operatorId;
    }
}
