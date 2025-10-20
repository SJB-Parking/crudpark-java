package app.dao;

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
     * Get the active rate for a specific vehicle type
     * @param vehicleType The vehicle type ("Car" or "Motorcycle")
     * @param conn Database connection
     * @return Rate object or null if not found
     */
    public Rate findActiveRateByVehicleType(String vehicleType, Connection conn) throws DataAccessException {
        String sql = "SELECT id, rate_name, hourly_rate, fraction_rate, daily_cap, " +
                     "grace_period_minutes, effective_from, is_active, vehicle_type " +
                     "FROM rates WHERE is_active = true AND vehicle_type = ? " +
                     "ORDER BY effective_from DESC LIMIT 1";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, vehicleType);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Rate rate = new Rate();
                rate.setId(rs.getInt("id"));
                rate.setRateName(rs.getString("rate_name"));
                rate.setHourlyRate(rs.getDouble("hourly_rate"));
                rate.setFractionRate(rs.getDouble("fraction_rate"));
                
                Double dailyCap = rs.getObject("daily_cap") != null ? 
                                 rs.getDouble("daily_cap") : null;
                rate.setDailyCap(dailyCap);
                
                rate.setGracePeriodMinutes(rs.getInt("grace_period_minutes"));
                rate.setEffectiveFrom(rs.getTimestamp("effective_from"));
                rate.setActive(rs.getBoolean("is_active"));
                rate.setVehicleType(rs.getString("vehicle_type"));
                return rate;
            }
            
            return null;
            
        } catch (SQLException e) {
            throw new DataAccessException("Error accessing rate data for vehicle type: " + vehicleType, e);
        }
    }
}
