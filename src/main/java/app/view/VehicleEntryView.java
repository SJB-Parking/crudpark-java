package app.view;

import app.model.Ticket;
import app.util.QRCodeGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;

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
     * Show entry success message with QR code image
     */
    public void showEntrySuccess(Ticket ticket) {
        // Create panel for message and QR code
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        // Format entry datetime: 2025-10-14 09:45 AM
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        String formattedEntryTime = dateFormat.format(new Date(ticket.getEntryDatetime().getTime()));
        
        // Add ticket information
        String ticketInfo = String.format(
            "<html><div style='font-family: monospace; padding: 10px;'>" +
            "<b>═════════════════════════════════</b><br>" +
            "<b style='font-size: 14px;'>      ENTRY REGISTERED</b><br>" +
            "<b>═════════════════════════════════</b><br><br>" +
            "<b>Ticket ID:</b> %d<br>" +
            "<b>Folio:</b> %s<br>" +
            "<b>License Plate:</b> %s<br>" +
            "<b>Vehicle Type:</b> %s<br>" +
            "<b>Type:</b> %s<br>" +
            "<b>Entry Time:</b> %s<br><br>" +
            "<b>Scan QR Code:</b>" +
            "</div></html>",
            ticket.getId(),
            ticket.getFolio(),
            ticket.getLicensePlate(),
            ticket.getVehicleType(),
            ticket.getTicketType(),
            formattedEntryTime
        );
        
        JLabel infoLabel = new JLabel(ticketInfo);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(infoLabel);
        
        // Add QR code image (generate from plain text)
        try {
            BufferedImage qrImage = QRCodeGenerator.generateQRCodeImage(ticket.getQrCodeData());
            ImageIcon qrIcon = new ImageIcon(qrImage);
            JLabel qrLabel = new JLabel(qrIcon);
            qrLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            qrLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            panel.add(qrLabel);
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("<html><i style='color: red;'>Could not generate QR code image</i></html>");
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(errorLabel);
        }
        
        // Add footer
        JLabel footer = new JLabel("<html><b>═════════════════════════════════</b></html>");
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(footer);
        
        JOptionPane.showMessageDialog(null, 
            panel, 
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
