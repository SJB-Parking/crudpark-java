package app.view;

import app.model.Ticket;

import javax.swing.*;

/**
 * Vehicle entry view
 */
public class VehicleEntryView {

    /**
     * Show license plate input dialog
     */
    public String showLicensePlateInput() {
        return JOptionPane.showInputDialog(null, 
            "Enter license plate:\n\n" +
            "Car format: ABC123 (3 letters + 3 numbers)\n" +
            "Motorcycle format: ABC12D (3 letters + 2 numbers + 1 letter)", 
            "Vehicle Entry", 
            JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Show entry success message
     */
    public void showEntrySuccess(Ticket ticket) {
        String message = String.format(
            "═══════════════════════════════════\n" +
            "      ENTRY REGISTERED\n" +
            "═══════════════════════════════════\n\n" +
            "Ticket ID: %d\n" +
            "Folio: %s\n" +
            "License Plate: %s\n" +
            "Vehicle Type: %s\n" +
            "Type: %s\n" +
            "Entry Time: %s\n\n" +
            "QR Code Data:\n%s\n" +
            "═══════════════════════════════════",
            ticket.getId(),
            ticket.getFolio(),
            ticket.getLicensePlate(),
            ticket.getVehicleType(),
            ticket.getTicketType(),
            ticket.getEntryDatetime().toString(),
            ticket.getQrCodeData()
        );
        
        JOptionPane.showMessageDialog(null, 
            message, 
            "Entry Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Show error message
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(null, 
            message, 
            "Entry Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}
