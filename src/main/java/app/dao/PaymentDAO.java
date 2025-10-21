package app.dao;

import app.database.DatabaseConnection;
import app.exception.DataAccessException;
import app.model.Payment;

import java.math.BigDecimal;
import java.sql.*;

/**
 * Implementation of IPaymentDAO
 */
public class PaymentDAO implements IPaymentDAO {

    @Override
    public Payment create(Connection conn, int ticketId, int operatorId, double amount, String paymentMethod) 
            throws DataAccessException {
        String sql = "INSERT INTO payments (ticket_id, operator_id, amount, payment_method, " +
                     "payment_datetime, created_at) VALUES (?, ?, ?, ?, NOW(), NOW()) RETURNING id, payment_datetime, created_at";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ticketId);
            stmt.setInt(2, operatorId);
            stmt.setDouble(3, amount);
            stmt.setString(4, paymentMethod);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Payment payment = new Payment(ticketId, BigDecimal.valueOf(amount), rs.getTimestamp("payment_datetime"), paymentMethod);
                    payment.setId(rs.getInt("id"));
                    payment.setCreatedAt(rs.getTimestamp("created_at"));
                    return payment;
                }
            }
            
        } catch (SQLException e) {
            throw new DataAccessException("Error creating payment record", e);
        }
        
        throw new DataAccessException("Failed to create payment record", null);
    }

    @Override
    public Payment findByTicketId(int ticketId) throws DataAccessException {
        String sql = "SELECT * FROM payments WHERE ticket_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ticketId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Payment payment = new Payment();
                    payment.setId(rs.getInt("id"));
                    payment.setTicketId(rs.getInt("ticket_id"));
                    payment.setAmount(rs.getBigDecimal("amount"));
                    payment.setPaymentDatetime(rs.getTimestamp("payment_datetime"));
                    payment.setPaymentMethod(rs.getString("payment_method"));
                    payment.setCreatedAt(rs.getTimestamp("created_at"));
                    return payment;
                }
            }
            
        } catch (SQLException e) {
            throw new DataAccessException("Error finding payment by ticket ID", e);
        }
        
        return null;
    }
}
