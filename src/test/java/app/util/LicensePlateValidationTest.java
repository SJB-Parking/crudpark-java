package app.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for license plate validation
 */
@DisplayName("License Plate Validation Tests")
class LicensePlateValidationTest {
    
    @Test
    @DisplayName("Should accept valid car plates (3 letters + 3 numbers)")
    void testValidCarPlates() {
        assertTrue(isValidCarPlate("ABC123"));
        assertTrue(isValidCarPlate("XYZ789"));
        assertTrue(isValidCarPlate("DEF456"));
    }
    
    @Test
    @DisplayName("Should accept valid motorcycle plates (3 letters + 2 numbers + 1 letter)")
    void testValidMotorcyclePlates() {
        assertTrue(isValidMotorcyclePlate("ABC12D"));
        assertTrue(isValidMotorcyclePlate("XYZ67E"));
        assertTrue(isValidMotorcyclePlate("DEF45M"));
    }
    
    @Test
    @DisplayName("Should reject invalid car plates")
    void testInvalidCarPlates() {
        assertFalse(isValidCarPlate("ABC"));
        assertFalse(isValidCarPlate("AB123"));
        assertFalse(isValidCarPlate("1234567"));
        assertFalse(isValidCarPlate("ABC1234"));
    }
    
    @Test
    @DisplayName("Should reject invalid motorcycle plates")
    void testInvalidMotorcyclePlates() {
        assertFalse(isValidMotorcyclePlate("ABC123"));
        assertFalse(isValidMotorcyclePlate("ABC1"));
        assertFalse(isValidMotorcyclePlate("ABC12DE"));
        assertFalse(isValidMotorcyclePlate("XY123A"));
    }
    
    @Test
    @DisplayName("Should handle null plate gracefully")
    void testNullPlate() {
        assertFalse(isValidCarPlate(null));
        assertFalse(isValidMotorcyclePlate(null));
    }
    
    @Test
    @DisplayName("Should handle empty plate gracefully")
    void testEmptyPlate() {
        assertFalse(isValidCarPlate(""));
        assertFalse(isValidMotorcyclePlate(""));
    }
    
    @Test
    @DisplayName("Should be case insensitive")
    void testCaseInsensitive() {
        assertTrue(isValidCarPlate("abc123"));
        assertTrue(isValidCarPlate("ABC123"));
        assertTrue(isValidMotorcyclePlate("abc12d"));
        assertTrue(isValidMotorcyclePlate("ABC12D"));
    }
    
    private boolean isValidCarPlate(String plate) {
        if (plate == null || plate.isEmpty()) return false;
        return plate.toUpperCase().matches("^[A-Z]{3}\\d{3}$");
    }
    
    private boolean isValidMotorcyclePlate(String plate) {
        if (plate == null || plate.isEmpty()) return false;
        return plate.toUpperCase().matches("^[A-Z]{3}\\d{2}[A-Z]$");
    }
}
