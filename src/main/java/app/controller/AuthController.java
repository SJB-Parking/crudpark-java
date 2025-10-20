package app.controller;

import app.exception.AuthenticationException;
import app.exception.DataAccessException;
import app.model.Operator;
import app.service.AuthService;

import java.util.regex.Pattern;

/**
 * Authentication controller
 */
public class AuthController {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Login operator - returns LoginResult instead of throwing exceptions
     * Controller handles all exceptions and converts them to result objects
     */
    public LoginResult login(String email, String password) {
        try {
            // Validate email
            if (email == null || email.trim().isEmpty()) {
                return LoginResult.validationError("Email cannot be empty");
            }
            
            email = email.trim();
            
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                return LoginResult.validationError("Invalid email format");
            }
            
            // Validate password
            if (password == null || password.isEmpty()) {
                return LoginResult.validationError("Password cannot be empty");
            }
            
            if (password.length() < 6) {
                return LoginResult.validationError("Password must be at least 6 characters");
            }
            
            // Call service
            Operator operator = authService.login(email, password);
            return LoginResult.success(operator);
            
        } catch (AuthenticationException e) {
            return LoginResult.authenticationError(e.getMessage());
        } catch (DataAccessException e) {
            return LoginResult.dataAccessError("Error accessing authentication data: " + e.getMessage());
        } catch (Exception e) {
            return LoginResult.dataAccessError("Unexpected error: " + e.getMessage());
        }
    }
}
