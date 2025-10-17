package app.dao;

import app.database.DatabaseConnection;
import app.exception.DataAccessException;
import app.model.Vehicle;

import java.sql.*;

/**
 * Data Access Object for Vehicle entity
 */
public class VehicleDAO {

    /**
     * Find vehicle by license plate
     */
    public Vehicle findByLicensePlate(String licensePlate) throws DataAccessException {
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
                return vehicle;
            }
            
            return null;
            
        } catch (SQLException e) {
            throw new DataAccessException("Error accessing vehicle data", e);
        }
    }

    /**
     * Create a new vehicle
     */
    public Vehicle create(Connection conn, String licensePlate, String vehicleType) 
            throws DataAccessException {
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
                return vehicle;
            }
            
            throw new DataAccessException("Failed to create vehicle");
            
        } catch (SQLException e) {
            throw new DataAccessException("Error creating vehicle", e);
        }
    }

    /**
     * Find or create vehicle
     */
    public Vehicle findOrCreate(Connection conn, String licensePlate, String vehicleType) 
            throws DataAccessException {
        Vehicle vehicle = findByLicensePlate(licensePlate);
        if (vehicle != null) {
            return vehicle;
        }
        return create(conn, licensePlate, vehicleType);
    }
}
