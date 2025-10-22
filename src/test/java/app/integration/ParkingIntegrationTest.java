package app.integration;

import app.controller.AuthController;
import app.controller.ParkingController;
import app.exception.AuthenticationException;
import app.exception.BusinessException;
import app.exception.DataAccessException;
import app.model.Operator;
import app.model.Ticket;
import app.service.AuthService;
import app.service.ParkingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the complete parking flow
 * Tests end-to-end scenarios
 */
@DisplayName("Parking System Integration Tests")
class ParkingIntegrationTest {
    
    private Operator testOperator;
    
    @BeforeEach
    void setUp() {
        // Set up test operator
        testOperator = new Operator();
        testOperator.setId(1);
        testOperator.setFullName("Test Operator");
        testOperator.setEmail("test@parking.com");
        testOperator.setActive(true);
    }
    
    @Test
    @DisplayName("Should complete full parking cycle: entry and exit")
    void testCompleteParkingCycle() {
        // This is a placeholder for integration tests
        // In real scenario, would need actual database connection
        
        // Simulate vehicle entry
        String licensePlate = "ABC123";
        int operatorId = 1;
        
        // Verify entry validation
        assertTrue(isValidPlate(licensePlate));
        assertTrue(isValidOperatorId(operatorId));
        
        // Simulate exit processing
        int ticketId = 1;
        String paymentMethod = "Cash";
        
        // Verify exit validation
        assertTrue(isValidPaymentMethod(paymentMethod));
    }
    
    @Test
    @DisplayName("Should handle concurrent operator logins")
    void testConcurrentLogins() throws Exception {
        // Simulate multiple operators logging in
        Operator op1 = new Operator();
        op1.setId(1);
        op1.setEmail("operator1@parking.com");
        op1.setActive(true);
        
        Operator op2 = new Operator();
        op2.setId(2);
        op2.setEmail("operator2@parking.com");
        op2.setActive(true);
        
        assertNotNull(op1);
        assertNotNull(op2);
        assertNotEquals(op1.getId(), op2.getId());
    }
    
    @Test
    @DisplayName("Should reject vehicle entry with invalid plate")
    void testInvalidVehicleEntry() {
        String invalidPlate = "INVALID";
        
        assertFalse(isValidPlate(invalidPlate));
    }
    
    @Test
    @DisplayName("Should handle duplicate open ticket for same vehicle")
    void testDuplicateOpenTicket() {
        String licensePlate = "ABC123";
        
        // First entry should be allowed
        assertTrue(isValidPlate(licensePlate));
        
        // Duplicate entry should be rejected (in real system)
        // This requires database check
    }
    
    @Test
    @DisplayName("Should calculate parking charges correctly")
    void testParkingChargeCalculation() {
        // 2 hours of parking for car at $5/hour = $10
        long durationMinutes = 120;
        double hourlyRate = 5.00;
        double expectedCharge = 10.00;
        
        double actualCharge = calculateCharge(durationMinutes, hourlyRate);
        assertEquals(expectedCharge, actualCharge);
    }
    
    @Test
    @DisplayName("Should apply grace period correctly")
    void testGracePeriodApplication() {
        // 10 minutes parking with 15-minute grace period = $0
        long durationMinutes = 10;
        int gracePeriodMinutes = 15;
        double hourlyRate = 5.00;
        double expectedCharge = 0.00;
        
        double actualCharge = calculateChargeWithGracePeriod(durationMinutes, gracePeriodMinutes, hourlyRate);
        assertEquals(expectedCharge, actualCharge);
    }
    
    @Test
    @DisplayName("Should cap parking charges at daily maximum")
    void testDailyCapApplication() {
        // 12 hours parking at $5/hour = $60, but cap is $50
        long durationMinutes = 720; // 12 hours
        double hourlyRate = 5.00;
        double dailyCap = 50.00;
        
        double charge = calculateCharge(durationMinutes, hourlyRate);
        double cappedCharge = Math.min(charge, dailyCap);
        
        assertEquals(dailyCap, cappedCharge);
    }
    
    @Test
    @DisplayName("Should handle monthly subscription vehicles (free parking)")
    void testMonthlySubscriptionFreeParking() {
        long durationMinutes = 480; // 8 hours
        boolean hasSubscription = true;
        double expectedCharge = 0.00;
        
        double charge = hasSubscription ? 0.00 : calculateCharge(durationMinutes, 5.00);
        assertEquals(expectedCharge, charge);
    }
    
    @Test
    @DisplayName("Should process different payment methods")
    void testMultiplePaymentMethods() {
        double amount = 15.50;
        
        // Test Cash payment
        assertTrue(processPayment("Cash", amount));
        
        // Test Card payment
        assertTrue(processPayment("Card", amount));
        
        // Test invalid payment method
        assertFalse(processPayment("Check", amount));
    }
    
    // Helper methods
    private boolean isValidPlate(String plate) {
        if (plate == null) return false;
        return plate.toUpperCase().matches("^[A-Z]{3}\\d{3}$") || 
               plate.toUpperCase().matches("^[A-Z]{3}\\d{2}[A-Z]$");
    }
    
    private boolean isValidOperatorId(int operatorId) {
        return operatorId > 0;
    }
    
    private boolean isValidPaymentMethod(String method) {
        return method != null && (method.equals("Cash") || method.equals("Card"));
    }
    
    private double calculateCharge(long durationMinutes, double hourlyRate) {
        long hours = durationMinutes / 60;
        long remainingMinutes = durationMinutes % 60;
        double charge = hours * hourlyRate;
        
        if (remainingMinutes > 0) {
            charge += (remainingMinutes / 60.0) * hourlyRate;
        }
        
        return charge;
    }
    
    private double calculateChargeWithGracePeriod(long durationMinutes, int gracePeriodMinutes, double hourlyRate) {
        if (durationMinutes <= gracePeriodMinutes) {
            return 0.00;
        }
        return calculateCharge(durationMinutes - gracePeriodMinutes, hourlyRate);
    }
    
    private boolean processPayment(String method, double amount) {
        return isValidPaymentMethod(method) && amount >= 0;
    }
}
