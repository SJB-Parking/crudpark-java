package app.dao;

import app.exception.DataAccessException;
import app.model.Rate;

import java.sql.Connection;

/**
 * Interface for Rate Data Access Object
 */
public interface IRateDAO {
    
    /**
     * Find active rate by vehicle type
     * @param vehicleType Type of vehicle (Car, Motorcycle)
     * @param conn Database connection
     * @return Active rate for the vehicle type if found, null otherwise
     * @throws DataAccessException if database error occurs
     */
    Rate findActiveRateByVehicleType(String vehicleType, Connection conn) throws DataAccessException;
}
