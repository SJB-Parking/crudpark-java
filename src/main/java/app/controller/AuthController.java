package app.controller;

import app.exception.AuthenticationException;
import app.exception.DataAccessException;
import app.exception.ValidationException;
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
     * Login operator
     */
    public Operator login(String email, String password) throws ValidationException, 
            AuthenticationException, DataAccessException {
        
        // Validate email
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email cannot be empty");
        }
        
        email = email.trim();
        
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Invalid email format");
        }
        
        // Validate password
        if (password == null || password.isEmpty()) {
            throw new ValidationException("Password cannot be empty");
        }
        
        if (password.length() < 6) {
            throw new ValidationException("Password must be at least 6 characters");
        }
        
        // Call service
        try {
            return authService.login(email, password);
        } catch (AuthenticationException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new DataAccessException("Error accessing authentication data: " + e.getMessage());
        }
    }
}
