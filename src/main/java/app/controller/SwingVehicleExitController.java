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

    public int getOperatorId() {
        return operatorId;
    }
}
