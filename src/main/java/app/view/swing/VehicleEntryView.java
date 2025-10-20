package app.view.swing;

import app.controller.EntryResult;
import app.controller.SwingVehicleEntryController;
import app.util.TicketPrinter;

import javax.swing.*;

/**
 * Vehicle Entry View - Handles ALL UI logic for vehicle entry process
 * This includes: showing forms, displaying errors, showing success with QR code
 */
public class VehicleEntryView {
    private final SwingVehicleEntryController controller;

    public VehicleEntryView(SwingVehicleEntryController controller) {
        this.controller = controller;
    }

    /**
     * Show vehicle entry form and process the entry
     */
    public void showEntryForm() {
        // Show entry form (modal dialog)
        VehicleEntryFrame entryFrame = new VehicleEntryFrame();
        entryFrame.setVisible(true);
        
        // Modal dialog blocks here until user closes it
        
        if (!entryFrame.isRegistered()) {
            // User cancelled
            return;
        }
        
        String licensePlate = entryFrame.getLicensePlate();
        
        // Process entry through controller (no UI logic in controller)
        EntryResult result = controller.processEntry(licensePlate);
        
        // Handle result - ALL UI LOGIC HERE
        if (result.isSuccess()) {
            showSuccessWindow(result);
        } else {
            showError(result);
        }
    }

    /**
     * Show success - ask first if user wants to print or see on screen
     */
    private void showSuccessWindow(EntryResult result) {
        // Ask FIRST if user wants to print
        int printChoice = JOptionPane.showConfirmDialog(null,
            "Ticket registered successfully!\n\n" +
            "Would you like to print the ticket?\n" +
            "(If you select NO, it will be displayed on screen)",
            "Print Ticket?",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (printChoice == JOptionPane.YES_OPTION) {
            // User wants to PRINT - don't show on screen
            TicketPrinter.printEntryTicket(result.getTicket());
        } else {
            // User wants to SEE on screen - show the window
            VehicleEntryFrame.showEntrySuccess(result.getTicket());
        }
    }

    /**
     * Show error message to user
     */
    private void showError(EntryResult result) {
        String title = getErrorTitle(result.getErrorType());
        String message = result.getErrorMessage();
        
        JOptionPane.showMessageDialog(null,
            message,
            title,
            JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Get appropriate error title based on error type
     */
    private String getErrorTitle(EntryResult.ErrorType errorType) {
        switch (errorType) {
            case VALIDATION:
                return "Validation Error";
            case BUSINESS:
                return "Business Error";
            case DATA_ACCESS:
                return "Database Error";
            default:
                return "Entry Error";
        }
    }
}
