package app.controller;

import app.exception.AuthenticationException;
import app.exception.DataAccessException;
import app.exception.ValidationException;
import app.model.Operator;
import app.service.AuthService;
import app.view.swing.LoginFrame;

import javax.swing.*;

/**
 * Swing authentication controller
 */
public class SwingAuthController {
    private final AuthController authController;

    public SwingAuthController(AuthService authService, AuthController authController) {
        this.authController = authController;
    }

    /**
     * Handle operator login with Swing window
     */
    public Operator login() {
        try {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.showWindow();
            
            // Wait for window to close
            while (loginFrame.isVisible()) {
                Thread.sleep(100);
            }
            
            if (!loginFrame.isLoginSuccessful()) {
                return null; // User cancelled
            }
            
            String email = loginFrame.getEmail();
            String password = loginFrame.getPassword();
            
            // Authenticate using existing controller
            Operator operator = authController.login(email, password);
            
            JOptionPane.showMessageDialog(null,
                "Welcome, " + operator.getFullName() + "!",
                "Login Successful",
                JOptionPane.INFORMATION_MESSAGE);
            
            return operator;
            
        } catch (ValidationException e) {
            JOptionPane.showMessageDialog(null,
                "Validation Error: " + e.getMessage(),
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
            return login(); // Retry
        } catch (AuthenticationException e) {
            JOptionPane.showMessageDialog(null,
                "Authentication Error: " + e.getMessage(),
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
            return login(); // Retry
        } catch (DataAccessException e) {
            JOptionPane.showMessageDialog(null,
                "Database Error: " + e.getMessage(),
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
}
