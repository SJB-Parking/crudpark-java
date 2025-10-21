package app.controller;

import app.model.Operator;

/**
 * Result object for login operation
 */
public class LoginResult {
    private final boolean success;
    private final Operator operator;
    private final String errorMessage;
    private final ErrorType errorType;

    /**
     * Error types for login operations
     */
    public enum ErrorType {
        VALIDATION,      // Input validation errors
        AUTHENTICATION,  // Authentication failures
        DATA_ACCESS,     // Database/data access errors
        NONE            // No error
    }

    private LoginResult(boolean success, Operator operator, String errorMessage, ErrorType errorType) {
        this.success = success;
        this.operator = operator;
        this.errorMessage = errorMessage;
        this.errorType = errorType;
    }

    /**
     * Create a successful login result
     */
    public static LoginResult success(Operator operator) {
        return new LoginResult(true, operator, null, ErrorType.NONE);
    }

    /**
     * Create a validation error result
     */
    public static LoginResult validationError(String message) {
        return new LoginResult(false, null, message, ErrorType.VALIDATION);
    }

    /**
     * Create an authentication error result
     */
    public static LoginResult authenticationError(String message) {
        return new LoginResult(false, null, message, ErrorType.AUTHENTICATION);
    }

    /**
     * Create a data access error result
     */
    public static LoginResult dataAccessError(String message) {
        return new LoginResult(false, null, message, ErrorType.DATA_ACCESS);
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public Operator getOperator() {
        return operator;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
