package app.util;

/**
 * Utility class to translate vehicle types from English to Spanish
 */
public class VehicleTypeTranslator {
    
    /**
     * Translate vehicle type to Spanish
     * @param vehicleType Vehicle type in English (Car, Motorcycle)
     * @return Translated vehicle type in Spanish (Automóvil, Moto)
     */
    public static String toSpanish(String vehicleType) {
        if (vehicleType == null) {
            return "";
        }
        
        switch (vehicleType.toLowerCase()) {
            case "car":
                return "Automóvil";
            case "motorcycle":
                return "Moto";
            default:
                return vehicleType; // Return original if not recognized
        }
    }
    
    /**
     * Translate vehicle type to Spanish (short version)
     * @param vehicleType Vehicle type in English (Car, Motorcycle)
     * @return Translated vehicle type in Spanish (Carro, Moto)
     */
    public static String toSpanishShort(String vehicleType) {
        if (vehicleType == null) {
            return "";
        }
        
        switch (vehicleType.toLowerCase()) {
            case "car":
                return "Carro";
            case "motorcycle":
                return "Moto";
            default:
                return vehicleType; // Return original if not recognized
        }
    }
}
