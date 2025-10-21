package app.dao;

import app.exception.DataAccessException;

import java.sql.Connection;

/**
 * Interface for Subscription Data Access Object
 */
public interface ISubscriptionDAO {
    
    /**
     * Find active subscription by vehicle ID
     * @param conn Database connection
     * @param vehicleId Vehicle ID
     * @return Subscription ID if found, null otherwise
     * @throws DataAccessException if database error occurs
     */
    Integer findActiveSubscriptionByVehicleId(Connection conn, int vehicleId) throws DataAccessException;
}
