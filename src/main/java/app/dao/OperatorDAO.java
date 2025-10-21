package app.dao;

import app.database.DatabaseConnection;
import app.exception.DataAccessException;
import app.model.Operator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation of IOperatorDAO
 */
public class OperatorDAO implements IOperatorDAO {

    @Override
    public Operator findByEmail(String email) throws DataAccessException {
        String sql = "SELECT id, full_name, email, username, password_hash, is_active " +
                     "FROM operators WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Operator operator = new Operator();
                operator.setId(rs.getInt("id"));
                operator.setFullName(rs.getString("full_name"));
                operator.setEmail(rs.getString("email"));
                operator.setUsername(rs.getString("username"));
                operator.setPasswordHash(rs.getString("password_hash"));
                operator.setActive(rs.getBoolean("is_active"));
                return operator;
            }
            
            return null;
            
        } catch (SQLException e) {
            throw new DataAccessException("Error accessing operator data", e);
        }
    }

    @Override
    public Operator findById(int id) throws DataAccessException {
        String sql = "SELECT id, full_name, email, username, is_active " +
                     "FROM operators WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Operator operator = new Operator();
                operator.setId(rs.getInt("id"));
                operator.setFullName(rs.getString("full_name"));
                operator.setEmail(rs.getString("email"));
                operator.setUsername(rs.getString("username"));
                operator.setActive(rs.getBoolean("is_active"));
                return operator;
            }
            
            return null;
            
        } catch (SQLException e) {
            throw new DataAccessException("Error accessing operator data", e);
        }
    }
}
