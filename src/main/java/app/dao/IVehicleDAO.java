package app.dao;

import app.exception.DataAccessException;
import app.model.Vehicle;

import java.sql.Connection;

/**
 * Interface for Vehicle Data Access Object
 */
public interface IVehicleDAO {
    
    /**
     * Find vehicle by license plate
     * @param licensePlate Vehicle license plate
     * @return Vehicle if found, null otherwise
     * @throws DataAccessException if database error occurs
     */
    Vehicle findByLicensePlate(String licensePlate) throws DataAccessException;
    
    /**
     * Create a new vehicle
     * @param conn Database connection
     * @param licensePlate Vehicle license plate
     * @param vehicleType Type of vehicle (Car, Motorcycle)
     * @return Created vehicle
     * @throws DataAccessException if database error occurs
     */
    Vehicle create(Connection conn, String licensePlate, String vehicleType) throws DataAccessException;
    
    /**
     * Find or create vehicle
     * @param conn Database connection
     * @param licensePlate Vehicle license plate
     * @param vehicleType Type of vehicle (Car, Motorcycle)
     * @return Existing or newly created vehicle
     * @throws DataAccessException if database error occurs
     */
    Vehicle findOrCreate(Connection conn, String licensePlate, String vehicleType) throws DataAccessException;
}
