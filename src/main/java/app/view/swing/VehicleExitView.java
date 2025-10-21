package app.view.swing;

import app.controller.ExitResult;
import app.controller.SwingVehicleExitController;
import app.util.TicketPrinter;

import javax.swing.*;

/**
 * Vehicle Exit View - Handles ALL UI logic for vehicle exit process
 * This includes: showing forms, displaying errors, showing success with payment details
 */
public class VehicleExitView {
    private final SwingVehicleExitController controller;

    public VehicleExitView(SwingVehicleExitController controller) {
        this.controller = controller;
    }

    /**
     * Show vehicle exit form and process the exit
     */
    public void showExitForm() {
        // Show exit form (modal dialog)
        VehicleExitFrame exitFrame = new VehicleExitFrame();
        exitFrame.setVisible(true);
        
        // Modal dialog blocks here until user closes it
        
        if (!exitFrame.isProcessed()) {
            // User cancelled
            return;
        }
        
        String ticketId = exitFrame.getTicketId();
        
        // Process exit through controller (no UI logic in controller)
        ExitResult result = controller.processExit(ticketId);
        
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
    private void showSuccessWindow(ExitResult result) {
        // Ask FIRST if user wants to print
        int printChoice = JOptionPane.showConfirmDialog(null,
            "¡Salida procesada exitosamente!\n\n" +
            "¿Desea imprimir el recibo?\n" +
            "(Si selecciona NO, se mostrará en pantalla)",
            "¿Imprimir Recibo?",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (printChoice == JOptionPane.YES_OPTION) {
            // User wants to PRINT - don't show on screen
            TicketPrinter.printExitTicket(result.getExitResult());
        } else {
            // User wants to SEE on screen - show the window
            VehicleExitFrame.showExitSuccess(result.getExitResult());
        }
    }

    /**
     * Show error message to user
     */
    private void showError(ExitResult result) {
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
    private String getErrorTitle(ExitResult.ErrorType errorType) {
        switch (errorType) {
            case VALIDATION:
                return "Error de Validación";
            case NOT_FOUND:
                return "No Encontrado";
            case BUSINESS:
                return "Error de Negocio";
            case DATA_ACCESS:
                return "Error de Base de Datos";
            default:
                return "Error de Salida";
        }
    }
}
