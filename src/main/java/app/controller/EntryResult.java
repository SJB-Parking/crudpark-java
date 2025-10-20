package app.controller;

import app.model.Ticket;

/**
 * Result object for vehicle entry operation
 */
public class EntryResult {
    private final boolean success;
    private final Ticket ticket;
    private final String errorMessage;
    private final ErrorType errorType;

    /**
     * Error types for entry operations
     */
    public enum ErrorType {
        VALIDATION,      // Input validation errors
        BUSINESS,        // Business rule violations (vehicle already inside, etc.)
        DATA_ACCESS,     // Database/data access errors
        NONE            // No error
    }

    private EntryResult(boolean success, Ticket ticket, String errorMessage, ErrorType errorType) {
        this.success = success;
        this.ticket = ticket;
        this.errorMessage = errorMessage;
        this.errorType = errorType;
    }

    /**
     * Create a successful entry result
     */
    public static EntryResult success(Ticket ticket) {
        return new EntryResult(true, ticket, null, ErrorType.NONE);
    }

    /**
     * Create a validation error result
     */
    public static EntryResult validationError(String message) {
        return new EntryResult(false, null, message, ErrorType.VALIDATION);
    }

    /**
     * Create a business error result
     */
    public static EntryResult businessError(String message) {
        return new EntryResult(false, null, message, ErrorType.BUSINESS);
    }

    /**
     * Create a data access error result
     */
    public static EntryResult dataAccessError(String message) {
        return new EntryResult(false, null, message, ErrorType.DATA_ACCESS);
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
