package app.dao;

import app.database.DatabaseConnection;
import app.exception.DataAccessException;
import app.model.Vehicle;
import app.util.Logger;

import java.sql.*;

/**
 * Implementation of IVehicleDAO
 */
public class VehicleDAO implements IVehicleDAO {
    private static final Logger logger = Logger.getLogger(VehicleDAO.class);

    @Override
    public Vehicle findByLicensePlate(String licensePlate) throws DataAccessException {
        logger.debug("Finding vehicle by license plate: {}", licensePlate);
        String sql = "SELECT id, license_plate, vehicle_type, created_at, updated_at " +
                     "FROM vehicles WHERE license_plate = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, licensePlate);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Vehicle vehicle = new Vehicle();
                vehicle.setId(rs.getInt("id"));
                vehicle.setLicensePlate(rs.getString("license_plate"));
                vehicle.setVehicleType(rs.getString("vehicle_type"));
                logger.debug("Vehicle found: {} - {} (ID: {})", licensePlate, vehicle.getVehicleType(), vehicle.getId());
                return vehicle;
            }
            
            logger.debug("Vehicle not found: {}", licensePlate);
            return null;
            
        } catch (SQLException e) {
            logger.error("Error finding vehicle {}: {}", licensePlate, e.getMessage());
            throw new DataAccessException("Error accessing vehicle data", e);
        }
    }

    @Override
    public Vehicle create(Connection conn, String licensePlate, String vehicleType) 
            throws DataAccessException {
        logger.info("Creating new vehicle: {} (Type: {})", licensePlate, vehicleType);
        String sql = "INSERT INTO vehicles (license_plate, vehicle_type, created_at, updated_at) " +
                     "VALUES (?, ?::text, NOW(), NOW()) RETURNING id";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, licensePlate);
            stmt.setString(2, vehicleType);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Vehicle vehicle = new Vehicle();
                vehicle.setId(rs.getInt(1));
                vehicle.setLicensePlate(licensePlate);
                vehicle.setVehicleType(vehicleType);
                logger.info("Vehicle created: {} - ID {}", licensePlate, vehicle.getId());
                return vehicle;
            }
            
            logger.error("Failed to create vehicle: {}", licensePlate);
            throw new DataAccessException("Failed to create vehicle");
            
        } catch (SQLException e) {
            logger.error("SQL error creating vehicle {}: {}", licensePlate, e.getMessage());
            throw new DataAccessException("Error creating vehicle", e);
        }
    }

    @Override
    public Vehicle findOrCreate(Connection conn, String licensePlate, String vehicleType) 
            throws DataAccessException {
        Vehicle vehicle = findByLicensePlate(licensePlate);
        if (vehicle != null) {
            return vehicle;
        }
        return create(conn, licensePlate, vehicleType);
    }
}
