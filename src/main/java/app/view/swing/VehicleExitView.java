package app.view.swing;

import app.controller.ExitResult;
import app.controller.SwingVehicleExitController;

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
        // Show exit form (modal dialog) with search options
        VehicleExitFrame exitFrame = new VehicleExitFrame();
        exitFrame.setVisible(true);
        
        // Modal dialog blocks here until user closes it
        
        if (!exitFrame.isProcessed()) {
            // User cancelled
            return;
        }
        
        String searchValue = exitFrame.getSearchValue();
        boolean searchById = exitFrame.isSearchById();
        
        ExitResult previewResult;
        
        // Get preview based on search type
        if (searchById) {
            // Search by ticket ID - show preview
            previewResult = controller.previewExitById(searchValue);
        } else {
            // Search by license plate - show preview
            previewResult = controller.previewExitByPlate(searchValue);
        }
        
        // Check if preview was successful
        if (!previewResult.isSuccess()) {
            showError(previewResult);
            return;
        }
        
        // Show payment preview dialog (it handles confirmation and processing internally)
        PaymentPreviewDialog previewDialog = new PaymentPreviewDialog(
            null,  // parent frame
            previewResult.getExitResult(),
            controller
        );
        previewDialog.setVisible(true);
        
        // PaymentPreviewDialog handles everything:
        // - Shows preview
        // - User selects payment method
        // - Processes exit
        // - Shows success dialog
        // So we're done here!
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
