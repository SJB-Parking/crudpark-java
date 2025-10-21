package app.controller;

import app.service.ParkingService;

/**
 * Result object for vehicle exit operation
 */
public class ExitResult {
    private final boolean success;
    private final ParkingService.ExitResult exitResult;
    private final String errorMessage;
    private final ErrorType errorType;

    /**
     * Error types for exit operations
     */
    public enum ErrorType {
        VALIDATION,      // Input validation errors
        NOT_FOUND,       // Ticket not found
        BUSINESS,        // Business rule violations
        DATA_ACCESS,     // Database/data access errors
        NONE            // No error
    }

    private ExitResult(boolean success, ParkingService.ExitResult exitResult, 
                      String errorMessage, ErrorType errorType) {
        this.success = success;
        this.exitResult = exitResult;
        this.errorMessage = errorMessage;
        this.errorType = errorType;
    }

    /**
     * Create a successful exit result
     */
    public static ExitResult success(ParkingService.ExitResult exitResult) {
        return new ExitResult(true, exitResult, null, ErrorType.NONE);
    }

    /**
     * Create a validation error result
     */
    public static ExitResult validationError(String message) {
        return new ExitResult(false, null, message, ErrorType.VALIDATION);
    }

    /**
     * Create a not found error result
     */
    public static ExitResult notFoundError(String message) {
        return new ExitResult(false, null, message, ErrorType.NOT_FOUND);
    }

    /**
     * Create a business error result
     */
    public static ExitResult businessError(String message) {
        return new ExitResult(false, null, message, ErrorType.BUSINESS);
    }

    /**
     * Create a data access error result
     */
    public static ExitResult dataAccessError(String message) {
        return new ExitResult(false, null, message, ErrorType.DATA_ACCESS);
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public ParkingService.ExitResult getExitResult() {
        return exitResult;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
