package app.controller;

import app.util.Logger;

/**
 * Swing vehicle entry controller
 * This controller has NO UI logic - it only coordinates between view and business logic
 */
public class SwingVehicleEntryController {
    private static final Logger logger = Logger.getLogger(SwingVehicleEntryController.class);
    private final ParkingController parkingController;
    private final int operatorId;

    public SwingVehicleEntryController(ParkingController parkingController, int operatorId) {
        this.parkingController = parkingController;
        this.operatorId = operatorId;
    }

    /**
     * Process vehicle entry request - NO UI logic here
     * Returns EntryResult for the view to handle
     */
    public EntryResult processEntry(String licensePlate) {
        logger.info("Processing vehicle entry: {} (Operator ID: {})", licensePlate, operatorId);
        EntryResult result = parkingController.processEntry(licensePlate, operatorId);
        
        if (result.isSuccess()) {
            logger.info("Entry successful - Ticket: {} ({})", 
                       result.getTicket().getFolio(), result.getTicket().getLicensePlate());
        } else {
            logger.warn("Entry failed: {} ({})", result.getErrorMessage(), result.getErrorType());
        }
        
        return result;
    }

    public int getOperatorId() {
        return operatorId;
    }
}
