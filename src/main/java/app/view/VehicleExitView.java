package app.view;

import app.service.ParkingService;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Vehicle exit view
 */
public class VehicleExitView {

    /**
     * Show ticket ID input dialog
     */
    public String showTicketIdInput() {
        return JOptionPane.showInputDialog(null, 
            "Enter Ticket ID:", 
            "Vehicle Exit", 
            JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Show exit success message
     */
    public void showExitSuccess(ParkingService.ExitResult result) {
        long hours = result.getDurationMinutes() / 60;
        long minutes = result.getDurationMinutes() % 60;
        
        String costInfo;
        if (result.isFree()) {
            costInfo = String.format("FREE (%s)", result.getFreeReason());
        } else {
            costInfo = String.format("$%.2f", result.getAmount());
        }
        
        // Format entry and exit datetime: 2025-10-14 09:45 AM
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        String formattedEntryTime = dateFormat.format(new Date(result.getTicket().getEntryDatetime().getTime()));
        String formattedExitTime = dateFormat.format(new Date(result.getExitTime().getTime()));
        
        String message = String.format(
            "═══════════════════════════════════\n" +
            "          EXIT DETAILS\n" +
            "═══════════════════════════════════\n\n" +
            "Ticket ID: %d\n" +
            "Folio: %s\n" +
            "License Plate: %s\n" +
            "Vehicle Type: %s\n\n" +
            "Entry: %s\n" +
            "Exit: %s\n\n" +
            "Duration: %d hours %d minutes\n" +
            "Type: %s\n\n" +
            "TOTAL TO PAY: %s\n" +
            "═══════════════════════════════════",
            result.getTicket().getId(),
            result.getTicket().getFolio(),
            result.getTicket().getLicensePlate(),
            result.getTicket().getVehicleType(),
            formattedEntryTime,
            formattedExitTime,
            hours, minutes,
            result.getTicket().getTicketType(),
            costInfo
        );
        
        JOptionPane.showMessageDialog(null, 
            message, 
            "Exit Processed", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Show error message
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(null, 
            message, 
            "Exit Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}
