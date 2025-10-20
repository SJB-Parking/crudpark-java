package app.controller;

import app.util.Logger;

/**
 * Swing authentication controller
 * This controller has NO UI logic - it only coordinates between view and business logic
 */
public class SwingAuthController {
    private static final Logger logger = Logger.getLogger(SwingAuthController.class);
    private final AuthController authController;

    public SwingAuthController(AuthController authController) {
        this.authController = authController;
    }

    /**
     * Process login request - NO UI logic here
     * Returns LoginResult for the view to handle
     */
    public LoginResult processLogin(String email, String password) {
        logger.info("Processing login request");
        LoginResult result = authController.login(email, password);
        
        if (result.isSuccess()) {
            logger.info("Login successful for: {}", result.getOperator().getFullName());
        } else {
            logger.warn("Login failed: {} ({})", result.getErrorMessage(), result.getErrorType());
        }
        
        return result;
    }
}
