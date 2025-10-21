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
    
    /**
     * Translate ticket type to Spanish
     * @param ticketType Ticket type in English (Guest, Monthly)
     * @return Translated ticket type in Spanish (Invitado, Mensualidad)
     */
    public static String ticketTypeToSpanish(String ticketType) {
        if (ticketType == null) {
            return "";
        }
        
        switch (ticketType.toLowerCase()) {
            case "guest":
                return "Invitado";
            case "monthly":
                return "Mensualidad";
            default:
                return ticketType; // Return original if not recognized
        }
    }

    /**
     * Translate payment method to Spanish
     * @param paymentMethod Payment method in English (Cash, Card)
     * @return Translated payment method in Spanish (Efectivo, Tarjeta)
     */
    public static String paymentMethodToSpanish(String paymentMethod) {
        if (paymentMethod == null) {
            return "";
        }
        
        switch (paymentMethod.toLowerCase()) {
            case "cash":
                return "Efectivo";
            case "card":
                return "Tarjeta";
            default:
                return paymentMethod; // Return original if not recognized
        }
    }

    /**
     * Translate payment method from Spanish to English
     * @param paymentMethodSpanish Payment method in Spanish (Efectivo, Tarjeta)
     * @return Payment method in English for database (Cash, Card)
     */
    public static String paymentMethodToEnglish(String paymentMethodSpanish) {
        if (paymentMethodSpanish == null) {
            return "Cash"; // Default
        }
        
        switch (paymentMethodSpanish.toLowerCase()) {
            case "efectivo":
                return "Cash";
            case "tarjeta":
                return "Card";
            default:
                return paymentMethodSpanish; // Return original if not recognized
        }
    }
}
