package app.view.swing;

import app.controller.ExitResult;
import app.controller.SwingVehicleExitController;
import app.service.ParkingService;
import app.util.VehicleTypeTranslator;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog to preview payment details before confirming vehicle exit
 */
public class PaymentPreviewDialog extends JDialog {
    private final ParkingService.ExitResult exitResult;
    private final SwingVehicleExitController controller;
    
    private JComboBox<String> paymentMethodCombo;
    private boolean confirmed = false;

    public PaymentPreviewDialog(Frame parent, ParkingService.ExitResult exitResult, 
                                SwingVehicleExitController controller) {
        super(parent, "Previsualización de Pago", true);
        this.exitResult = exitResult;
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(550, 680);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);

        // Header
        JLabel headerLabel = new JLabel("RESUMEN DE SALIDA", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(0, 150, 136));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        // Ticket information panel
        JPanel infoPanel = createInfoPanel();
        contentPanel.add(infoPanel);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Payment section
        JPanel paymentPanel = createPaymentPanel();
        contentPanel.add(paymentPanel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton confirmButton = new JButton("Confirmar Salida");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setPreferredSize(new Dimension(160, 40));
        confirmButton.setBackground(new Color(0, 150, 136));
        confirmButton.setForeground(Color.BLACK);
        confirmButton.setFocusPainted(false);
        confirmButton.addActionListener(e -> handleConfirm());

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 15));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                "Información del Ticket",
                0, 0, new Font("Arial", Font.BOLD, 14)
            ),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        Font labelFont = new Font("Arial", Font.BOLD, 13);
        Font valueFont = new Font("Arial", Font.PLAIN, 13);
        Font plateFont = new Font("Arial", Font.BOLD, 15);

        // Ticket ID
        addInfoRow(panel, "ID Ticket:", String.format("%06d", exitResult.getTicket().getId()), 
                   labelFont, valueFont);

        // License Plate
        JLabel plateLabel = new JLabel("Placa:");
        plateLabel.setFont(labelFont);
        JLabel plateValue = new JLabel(exitResult.getTicket().getLicensePlate());
        plateValue.setFont(plateFont);
        plateValue.setForeground(new Color(0, 120, 215));
        panel.add(plateLabel);
        panel.add(plateValue);

        // Vehicle Type
        addInfoRow(panel, "Tipo de Vehículo:", 
                   VehicleTypeTranslator.toSpanish(exitResult.getTicket().getVehicleType()),
                   labelFont, valueFont);

        // Ticket Type
        addInfoRow(panel, "Tipo de Ticket:", 
                   VehicleTypeTranslator.ticketTypeToSpanish(exitResult.getTicket().getTicketType()),
                   labelFont, valueFont);

        // Duration
        int hours = exitResult.getDurationMinutes() / 60;
        int minutes = exitResult.getDurationMinutes() % 60;
        String duration = String.format("%d hora%s y %d minuto%s", 
                                       hours, hours != 1 ? "s" : "",
                                       minutes, minutes != 1 ? "s" : "");
        addInfoRow(panel, "Tiempo Estacionado:", duration, labelFont, valueFont);

        // Entry Time
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm a");
        String entryTime = dateFormat.format(exitResult.getTicket().getEntryDatetime());
        addInfoRow(panel, "Hora de Entrada:", entryTime, labelFont, valueFont);

        return panel;
    }

    private JPanel createPaymentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        // Amount to pay panel
        JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        amountPanel.setBackground(new Color(245, 250, 255));
        amountPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 150, 136), 2),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        JLabel amountLabel = new JLabel("TOTAL A PAGAR:");
        amountLabel.setFont(new Font("Arial", Font.BOLD, 18));

        String amountText;
        Color amountColor;
        
        if (exitResult.isFree()) {
            amountText = "GRATIS";
            amountColor = new Color(0, 150, 0);
        } else {
            amountText = String.format("$%.2f", exitResult.getAmount());
            amountColor = new Color(220, 50, 50);
        }

        JLabel amountValue = new JLabel(amountText);
        amountValue.setFont(new Font("Arial", Font.BOLD, 32));
        amountValue.setForeground(amountColor);

        amountPanel.add(amountLabel);
        amountPanel.add(amountValue);

        panel.add(amountPanel);

        // Free reason if applicable
        if (exitResult.isFree()) {
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
            JLabel reasonLabel = new JLabel("Motivo: " + translateFreeReason(exitResult.getFreeReason()));
            reasonLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            reasonLabel.setForeground(new Color(100, 100, 100));
            reasonLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(reasonLabel);
        }

        // Payment method selector (only if amount > 0)
        if (!exitResult.isFree() && exitResult.getAmount() > 0) {
            panel.add(Box.createRigidArea(new Dimension(0, 30)));

            JPanel methodPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
            methodPanel.setBackground(Color.WHITE);
            methodPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

            JLabel methodLabel = new JLabel("Método de Pago:");
            methodLabel.setFont(new Font("Arial", Font.BOLD, 14));

            paymentMethodCombo = new JComboBox<>(new String[]{"Efectivo", "Tarjeta"});
            paymentMethodCombo.setFont(new Font("Arial", Font.PLAIN, 14));
            paymentMethodCombo.setPreferredSize(new Dimension(150, 35));

            methodPanel.add(methodLabel);
            methodPanel.add(paymentMethodCombo);

            panel.add(methodPanel);
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        return panel;
    }

    private void addInfoRow(JPanel panel, String label, String value, Font labelFont, Font valueFont) {
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

    private void handleConfirm() {
        // Get payment method (Cash if free or no selection)
        String paymentMethodSpanish;
        if (exitResult.isFree() || exitResult.getAmount() == 0) {
            paymentMethodSpanish = "Efectivo"; // Default to Cash for free exits
        } else {
            paymentMethodSpanish = (String) paymentMethodCombo.getSelectedItem();
        }

        // Translate to English for database
        String paymentMethodEnglish = VehicleTypeTranslator.paymentMethodToEnglish(paymentMethodSpanish);

        // Process exit with selected payment method
        ExitResult result = controller.processExitWithPayment(
            exitResult.getTicket().getId(), 
            paymentMethodEnglish
        );

        if (result.isSuccess()) {
            confirmed = true;
            // Close preview dialog FIRST
            dispose();
            // THEN show success message
            showSuccessDialog(result.getExitResult(), paymentMethodSpanish);
        } else {
            // Show error
            JOptionPane.showMessageDialog(this,
                result.getErrorMessage(),
                "Error al Procesar Salida",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showSuccessDialog(ParkingService.ExitResult result, String paymentMethod) {
        VehicleExitSuccessDialog successDialog = new VehicleExitSuccessDialog(
            null, result, paymentMethod);
        successDialog.setVisible(true);
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
