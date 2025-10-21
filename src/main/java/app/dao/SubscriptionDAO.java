package app.dao;

import app.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object for Subscription entity
 */
public class SubscriptionDAO {

    /**
     * Find active subscription for a vehicle
     */
    public Integer findActiveSubscriptionByVehicleId(Connection conn, int vehicleId) 
            throws DataAccessException {
        String sql = "SELECT ms.id FROM monthly_subscriptions ms " +
                     "INNER JOIN subscription_vehicles sv ON ms.id = sv.subscription_id " +
                     "WHERE sv.vehicle_id = ? AND ms.is_active = true " +
                     "AND ms.start_date <= NOW() AND ms.end_date >= NOW() LIMIT 1";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vehicleId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("id") : null;
        } catch (SQLException e) {
            throw new DataAccessException("Error finding active subscription", e);
        }
    }
}
