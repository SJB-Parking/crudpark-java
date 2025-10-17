package app.dao;

import app.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Data Access Object for Payment entity
 */
public class PaymentDAO {

    /**
     * Create a payment record
     */
    public void create(Connection conn, int ticketId, int operatorId, double amount, String paymentMethod) 
            throws DataAccessException {
        String sql = "INSERT INTO payments (ticket_id, operator_id, amount, payment_method, " +
                     "payment_datetime, created_at) VALUES (?, ?, ?, ?, NOW(), NOW())";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ticketId);
            stmt.setInt(2, operatorId);
            stmt.setDouble(3, amount);
            stmt.setString(4, paymentMethod);
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new DataAccessException("Error creating payment record", e);
        }
    }
}
