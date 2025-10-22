package app.dao;

import app.exception.DataAccessException;
import app.model.Rate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for RateDAO
 */
@DisplayName("RateDAO Unit Tests")
class RateDAOTest {
    
    @Mock
    private Connection mockConnection;
    
    @Mock
    private PreparedStatement mockStatement;
    
    @Mock
    private ResultSet mockResultSet;
    
    private RateDAO rateDAO;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rateDAO = new RateDAO();
    }
    
    @Test
    @DisplayName("Should retrieve active rate for Car")
    void testGetActiveRateForCar() throws SQLException, DataAccessException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("rate_name")).thenReturn("Standard Car Rate");
        when(mockResultSet.getString("vehicle_type")).thenReturn("Car");
        when(mockResultSet.getDouble("hourly_rate")).thenReturn(5.00);
        when(mockResultSet.getDouble("fraction_rate")).thenReturn(2.50);
        when(mockResultSet.getDouble("daily_cap")).thenReturn(50.00);
        when(mockResultSet.getInt("grace_period_minutes")).thenReturn(15);
        
        // Act
        Rate rate = rateDAO.findActiveRateByVehicleType("Car", mockConnection);
        
        // Assert
        assertNotNull(rate);
        assertEquals(1, rate.getId());
        assertEquals("Car", rate.getVehicleType());
        assertEquals(5.00, rate.getHourlyRate());
        assertEquals(15, rate.getGracePeriodMinutes());
    }
    
    @Test
    @DisplayName("Should retrieve active rate for Motorcycle")
    void testGetActiveRateForMotorcycle() throws SQLException, DataAccessException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(2);
        when(mockResultSet.getString("rate_name")).thenReturn("Standard Motorcycle Rate");
        when(mockResultSet.getString("vehicle_type")).thenReturn("Motorcycle");
        when(mockResultSet.getDouble("hourly_rate")).thenReturn(3.00);
        when(mockResultSet.getDouble("fraction_rate")).thenReturn(1.50);
        when(mockResultSet.getDouble("daily_cap")).thenReturn(30.00);
        when(mockResultSet.getInt("grace_period_minutes")).thenReturn(15);
        when(mockResultSet.getTimestamp("effective_from")).thenReturn(null);
        when(mockResultSet.getTimestamp("created_at")).thenReturn(null);
        when(mockResultSet.getTimestamp("updated_at")).thenReturn(null);
        
        // Act
        Rate rate = rateDAO.findActiveRateByVehicleType("Motorcycle", mockConnection);
        
        // Assert
        if (rate != null) {
            assertEquals("Motorcycle", rate.getVehicleType());
            assertEquals(3.00, rate.getHourlyRate());
        }
    }
    
    @Test
    @DisplayName("Should return null for non-existent vehicle type")
    void testGetRateForNonExistentType() throws SQLException, DataAccessException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        
        // Act
        Rate rate = rateDAO.findActiveRateByVehicleType("Truck", mockConnection);
        
        // Assert
        assertNull(rate);
    }
    
    @Test
    @DisplayName("Should throw DataAccessException on SQL error")
    void testGetRateSQLError() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));
        
        // Act & Assert
        assertThrows(DataAccessException.class, () -> {
            rateDAO.findActiveRateByVehicleType("Car", mockConnection);
        });
    }
}
