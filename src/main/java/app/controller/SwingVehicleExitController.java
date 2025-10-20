package app.controller;

import app.exception.BusinessException;
import app.exception.DataAccessException;
import app.exception.NotFoundException;
import app.exception.ValidationException;
import app.service.ParkingService;
import app.view.swing.VehicleExitFrame;

import javax.swing.*;

/**
 * Swing vehicle exit controller
 */
public class SwingVehicleExitController {
    private final ParkingController parkingController;
    private final int operatorId;

    public SwingVehicleExitController(ParkingService parkingService,
                                     ParkingController parkingController,
                                     int operatorId) {
        this.parkingController = parkingController;
        this.operatorId = operatorId;
    }

    /**
     * Handle vehicle exit with Swing window
     */
    public void processExit() {
        try {
            VehicleExitFrame exitFrame = new VehicleExitFrame();
            exitFrame.showWindow();
            
            // Wait for window to close
            while (exitFrame.isVisible()) {
                Thread.sleep(100);
            }
            
            if (!exitFrame.isProcessed()) {
                return; // User cancelled
            }
            
            String ticketIdStr = exitFrame.getTicketId();
            
            // Process exit using parking controller
            ParkingService.ExitResult result = parkingController.processExit(ticketIdStr, operatorId);
            
            // Show success window with payment details
            VehicleExitFrame.showExitSuccess(result);
            
        } catch (ValidationException e) {
            JOptionPane.showMessageDialog(null,
                "Validation Error: " + e.getMessage(),
                "Exit Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (NotFoundException e) {
            JOptionPane.showMessageDialog(null,
                "Not Found: " + e.getMessage(),
                "Exit Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(null,
                "Business Error: " + e.getMessage(),
                "Exit Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (DataAccessException e) {
            JOptionPane.showMessageDialog(null,
                "Database Error: " + e.getMessage(),
                "Exit Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
