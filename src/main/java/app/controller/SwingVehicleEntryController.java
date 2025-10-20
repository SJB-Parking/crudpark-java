package app.controller;

import app.exception.BusinessException;
import app.exception.DataAccessException;
import app.exception.ValidationException;
import app.model.Ticket;
import app.service.ParkingService;
import app.view.swing.VehicleEntryFrame;

import javax.swing.*;

/**
 * Swing vehicle entry controller
 */
public class SwingVehicleEntryController {
    private final ParkingController parkingController;
    private final int operatorId;

    public SwingVehicleEntryController(ParkingService parkingService, 
                                      ParkingController parkingController,
                                      int operatorId) {
        this.parkingController = parkingController;
        this.operatorId = operatorId;
    }

    /**
     * Handle vehicle entry with Swing window
     */
    public void processEntry() {
        try {
            VehicleEntryFrame entryFrame = new VehicleEntryFrame();
            entryFrame.showWindow();
            
            // Wait for window to close
            while (entryFrame.isVisible()) {
                Thread.sleep(100);
            }
            
            if (!entryFrame.isRegistered()) {
                return; // User cancelled
            }
            
            String licensePlate = entryFrame.getLicensePlate();
            
            // Process entry using parking controller
            Ticket ticket = parkingController.processEntry(licensePlate, operatorId);
            
            // Show success window with QR code
            VehicleEntryFrame.showEntrySuccess(ticket);
            
        } catch (ValidationException e) {
            JOptionPane.showMessageDialog(null,
                "Validation Error: " + e.getMessage(),
                "Entry Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(null,
                "Business Error: " + e.getMessage(),
                "Entry Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (DataAccessException e) {
            JOptionPane.showMessageDialog(null,
                "Database Error: " + e.getMessage(),
                "Entry Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
