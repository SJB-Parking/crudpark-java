package app.dao;

import app.exception.DataAccessException;
import app.model.Payment;

import java.sql.Connection;

/**
 * Interface for Payment Data Access Object
 */
public interface IPaymentDAO {
    
    /**
     * Create a payment record within an existing transaction
     * @param conn Database connection (transaction managed externally)
     * @param ticketId ID of the ticket
     * @param operatorId ID of the operator processing the payment
     * @param amount Amount paid
     * @param paymentMethod Payment method (e.g., "Cash")
     * @return The created Payment with generated ID
     * @throws DataAccessException if database error occurs
     */
    Payment create(Connection conn, int ticketId, int operatorId, double amount, String paymentMethod) 
            throws DataAccessException;
    
    /**
     * Find payment by ticket ID
     * @param ticketId ID of the ticket
     * @return Payment or null if not found
     * @throws DataAccessException if database error occurs
     */
    Payment findByTicketId(int ticketId) throws DataAccessException;
}
