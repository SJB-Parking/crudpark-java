package app.view.swing;

import app.service.ParkingService;
import app.util.VehicleTypeTranslator;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog to show successful vehicle exit details
 */
public class VehicleExitSuccessDialog extends JDialog {
    
    public VehicleExitSuccessDialog(Frame parent, ParkingService.ExitResult result, String paymentMethod) {
        super(parent, "Salida Procesada Exitosamente", true);
        initComponents(result, paymentMethod);
    }

    private void initComponents(ParkingService.ExitResult result, String paymentMethod) {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(550, 650);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);

        // Header
        JLabel headerLabel = new JLabel("SALIDA PROCESADA", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(0, 150, 136));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        // Ticket info panel
        JPanel infoPanel = createInfoPanel(result, paymentMethod);
        contentPanel.add(infoPanel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Close button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton closeButton = new JButton("Cerrar");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setPreferredSize(new Dimension(120, 40));
        closeButton.setBackground(new Color(0, 150, 136));
        closeButton.setForeground(Color.BLACK);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createInfoPanel(ParkingService.ExitResult result, String paymentMethod) {
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 15));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font valueFont = new Font("Arial", Font.PLAIN, 14);
        Font plateFont = new Font("Arial", Font.BOLD, 16);
        Font amountFont = new Font("Arial", Font.BOLD, 20);

        // Ticket ID
        addRow(panel, "ID Ticket:", String.format("%06d", result.getTicket().getId()), 
               labelFont, valueFont);

        // License Plate
        JLabel plateLabel = new JLabel("Placa:");
        plateLabel.setFont(labelFont);
        JLabel plateValue = new JLabel(result.getTicket().getLicensePlate());
        plateValue.setFont(plateFont);
        plateValue.setForeground(new Color(0, 120, 215));
        panel.add(plateLabel);
        panel.add(plateValue);

        // Vehicle Type
        addRow(panel, "Tipo de Vehículo:", 
               VehicleTypeTranslator.toSpanish(result.getTicket().getVehicleType()),
               labelFont, valueFont);

        // Duration
        int hours = result.getDurationMinutes() / 60;
        int minutes = result.getDurationMinutes() % 60;
        String duration = String.format("%d h %d min", hours, minutes);
        addRow(panel, "Tiempo Total:", duration, labelFont, valueFont);

        // Entry Time
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm a");
        String entryTime = dateFormat.format(result.getTicket().getEntryDatetime());
        addRow(panel, "Hora de Entrada:", entryTime, labelFont, valueFont);

        // Exit Time
        String exitTime = dateFormat.format(result.getExitTime());
        addRow(panel, "Hora de Salida:", exitTime, labelFont, valueFont);

        // Amount Paid
        JLabel amountLabel = new JLabel("Total Pagado:");
        amountLabel.setFont(labelFont);
        
        String amountText;
        Color amountColor;
        if (result.isFree()) {
            amountText = "GRATIS";
            amountColor = new Color(0, 150, 0);
        } else {
            amountText = String.format("$%.2f", result.getAmount());
            amountColor = new Color(220, 50, 50);
        }
        
        JLabel amountValue = new JLabel(amountText);
        amountValue.setFont(amountFont);
        amountValue.setForeground(amountColor);
        panel.add(amountLabel);
        panel.add(amountValue);

        // Payment Method (only if not free)
        if (!result.isFree()) {
            addRow(panel, "Método de Pago:", paymentMethod, labelFont, valueFont);
        } else {
            JLabel reasonLabel = new JLabel("Motivo:");
            reasonLabel.setFont(labelFont);
            JLabel reasonValue = new JLabel(translateFreeReason(result.getFreeReason()));
            reasonValue.setFont(valueFont);
            reasonValue.setForeground(new Color(0, 150, 0));
            panel.add(reasonLabel);
            panel.add(reasonValue);
        }

        return panel;
    }

    private void addRow(JPanel panel, String label, String value, Font labelFont, Font valueFont) {
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(labelFont);
        JLabel valueComp = new JLabel(value);
        valueComp.setFont(valueFont);
        panel.add(labelComp);
        panel.add(valueComp);
    }

    private String translateFreeReason(String reason) {
        if (reason == null) return "";
        
        if (reason.contains("Monthly Subscription")) {
            return "Mensualidad Activa";
        } else if (reason.contains("Grace Period")) {
            return "Período de Gracia";
        }
        return reason;
    }
}
