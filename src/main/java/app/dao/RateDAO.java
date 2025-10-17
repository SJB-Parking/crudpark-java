package app.dao;

import app.database.DatabaseConnection;
import app.exception.DataAccessException;
import app.model.Rate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object for Rate entity
 */
public class RateDAO {

    /**
     * Get the current active rate
     */
    public Rate getActiveRate() throws DataAccessException {
        String sql = "SELECT id, hourly_rate, fraction_rate, daily_cap, effective_from " +
                     "FROM rates WHERE is_active = true ORDER BY effective_from DESC LIMIT 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Rate rate = new Rate();
                rate.setId(rs.getInt("id"));
                rate.setHourlyRate(rs.getDouble("hourly_rate"));
                rate.setFractionRate(rs.getDouble("fraction_rate"));
                
                Double dailyCap = rs.getObject("daily_cap") != null ? 
                                 rs.getDouble("daily_cap") : null;
                rate.setDailyCap(dailyCap);
                
                rate.setEffectiveFrom(rs.getTimestamp("effective_from"));
                rate.setActive(true);
                return rate;
            }
            
            return null;
            
        } catch (SQLException e) {
            throw new DataAccessException("Error accessing rate data", e);
        }
    }
}
