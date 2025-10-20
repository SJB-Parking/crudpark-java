package app.view.swing;

import app.controller.LoginResult;
import app.controller.SwingAuthController;
import app.model.Operator;

import javax.swing.*;

/**
 * Login View - Handles ALL UI logic for login process
 * This includes: showing forms, displaying errors, handling retries, showing success messages
 */
public class LoginView {
    private final SwingAuthController controller;

    public LoginView(SwingAuthController controller) {
        this.controller = controller;
    }

    /**
     * Show login window and handle the entire login flow
     * Returns the logged-in Operator or null if cancelled/failed
     */
    public Operator showLoginAndAuthenticate() {
        while (true) {
            // Show login form
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            
            // LoginFrame now handles its own modal behavior
            // When it closes, we can check the result
            
            if (!loginFrame.isLoginSuccessful()) {
                // User cancelled
                return null;
            }
            
            String email = loginFrame.getEmail();
            String password = loginFrame.getPassword();
            
            // Process login through controller (no UI logic in controller)
            LoginResult result = controller.processLogin(email, password);
            
            // Handle result - ALL UI LOGIC HERE
            if (result.isSuccess()) {
                showSuccessMessage(result.getOperator());
                return result.getOperator();
            } else {
                // Show error and ask if user wants to retry
                boolean retry = showErrorAndAskRetry(result);
                if (!retry) {
                    return null;
                }
                // Loop continues for retry
            }
        }
    }

    /**
     * Show success message to user
     */
    private void showSuccessMessage(Operator operator) {
        JOptionPane.showMessageDialog(null,
            "Welcome, " + operator.getFullName() + "!",
            "Login Successful",
            JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Show error message and ask user if they want to retry
     * Returns true if user wants to retry, false otherwise
     */
    private boolean showErrorAndAskRetry(LoginResult result) {
        String title = getErrorTitle(result.getErrorType());
        String message = result.getErrorMessage();
        
        int option = JOptionPane.showConfirmDialog(null,
            message + "\n\nDo you want to try again?",
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.ERROR_MESSAGE);
        
        return option == JOptionPane.YES_OPTION;
    }

    /**
     * Get appropriate error title based on error type
     */
    private String getErrorTitle(LoginResult.ErrorType errorType) {
        switch (errorType) {
            case VALIDATION:
                return "Validation Error";
            case AUTHENTICATION:
                return "Authentication Error";
            case DATA_ACCESS:
                return "Database Error";
            default:
                return "Login Error";
        }
    }
}
