package app.view;

import javax.swing.*;

/**
 * Login view
 */
public class LoginView {

    /**
     * Show login dialog and return credentials
     */
    public LoginCredentials showLoginDialog() {
        // Email input
        String email = JOptionPane.showInputDialog(null, 
            "Enter your email:", 
            "Login - Parking System", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (email == null) {
            return null; // User cancelled
        }
        
        // Password input
        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(null, 
            passwordField, 
            "Enter your password:", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.QUESTION_MESSAGE);
        
        if (option != JOptionPane.OK_OPTION) {
            return null; // User cancelled
        }
        
        String password = new String(passwordField.getPassword());
        
        return new LoginCredentials(email, password);
    }

    /**
     * Show welcome message
     */
    public void showWelcome(String operatorName) {
        JOptionPane.showMessageDialog(null, 
            "Welcome, " + operatorName + "!", 
            "Parking System", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Show error message
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(null, 
            message, 
            "Login Error", 
            JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Show login cancelled message
     */
    public void showCancelled() {
        JOptionPane.showMessageDialog(null, 
            "Login cancelled", 
            "Parking System", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Show too many attempts message
     */
    public void showTooManyAttempts() {
        JOptionPane.showMessageDialog(null, 
            "Too many failed attempts. Exiting...", 
            "Access Denied", 
            JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Login credentials data class
     */
    public static class LoginCredentials {
        private final String email;
        private final String password;

        public LoginCredentials(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }
}
