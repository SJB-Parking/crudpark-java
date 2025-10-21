package app.view.swing;

import app.service.ParkingService;

import javax.swing.*;
import java.awt.*;

/**
 * Vehicle exit dialog (modal)
 * Changed from JFrame to JDialog to support modal behavior without threads
 */
public class VehicleExitFrame extends JDialog {
    private JTextField ticketIdField;
    private JButton processButton;
    private JButton cancelButton;
    
    private String ticketId;
    private boolean processed = false;

    public VehicleExitFrame() {
        super((Frame) null, "CrudPark - Salida de Vehículo", true); // modal dialog
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(450, 220);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(240, 240, 240));

        // Title
        JLabel titleLabel = new JLabel("Procesar Salida de Vehículo", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(240, 240, 240));

        // Input panel with label and field
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.setBackground(new Color(240, 240, 240));
        inputPanel.setMaximumSize(new Dimension(450, 50));

        JLabel ticketLabel = new JLabel("ID Ticket:");
        ticketLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        ticketIdField = new JTextField(15);
        ticketIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        ticketIdField.setPreferredSize(new Dimension(200, 30));

        inputPanel.add(ticketLabel);
        inputPanel.add(ticketIdField);

        formPanel.add(inputPanel);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));

        processButton = new JButton("Procesar Salida");
        processButton.setFont(new Font("Arial", Font.BOLD, 14));
        processButton.setPreferredSize(new Dimension(150, 35));
        processButton.setBackground(new Color(0, 150, 136));
        processButton.setForeground(Color.BLACK);
        processButton.setFocusPainted(false);
        processButton.addActionListener(e -> handleProcess());

        cancelButton = new JButton("Cancelar");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(processButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Enter key press triggers process
        ticketIdField.addActionListener(e -> handleProcess());
    }

    private void handleProcess() {
        ticketId = ticketIdField.getText().trim();

        if (ticketId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingrese un ID de ticket",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        processed = true;
        dispose();
    }

    public String getTicketId() {
        return ticketId;
    }

    public boolean isProcessed() {
        return processed;
    }

    /**
     * Show exit success in a new MODAL window with payment details
     */
    public static void showExitSuccess(ParkingService.ExitResult result) {
        JDialog successDialog = new JDialog((Frame) null, "Salida Procesada Exitosamente", true); // modal
        successDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        successDialog.setSize(500, 550);
        successDialog.setLocationRelativeTo(null);
        successDialog.setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);

        // Header
        JLabel headerLabel = new JLabel("SALIDA PROCESADA", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(0, 150, 136));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        // Format entry and exit datetime
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm a");
        String formattedEntryTime = dateFormat.format(new java.util.Date(result.getTicket().getEntryDatetime().getTime()));
        String formattedExitTime = dateFormat.format(new java.util.Date(result.getExitTime().getTime()));

        // Calculate duration
        long hours = result.getDurationMinutes() / 60;
        long minutes = result.getDurationMinutes() % 60;

        // Cost info
        String costInfo;
        Color costColor;
        if (result.isFree()) {
            costInfo = String.format("FREE (%s)", result.getFreeReason());
            costColor = new Color(0, 150, 0);
        } else {
            costInfo = String.format("$%.2f", result.getAmount());
            costColor = new Color(200, 0, 0);
        }

        // Ticket info with important details
        String exitInfo = String.format(
            "<html><div style='font-family: Arial; padding: 20px; background-color: #f5f5f5; border-radius: 10px;'>" +
            "<table style='width: 100%%;'>" +
            "<tr><td><b>Placa:</b></td><td><span style='font-size: 16px; color: #009688;'>%s</span></td></tr>" +
            "<tr><td colspan='2'><hr></td></tr>" +
            "<tr><td><b>Hora de Entrada:</b></td><td>%s</td></tr>" +
            "<tr><td><b>Hora de Salida:</b></td><td>%s</td></tr>" +
            "<tr><td><b>Duración:</b></td><td><b>%d horas %d minutos</b></td></tr>" +
            "</table>" +
            "</div></html>",
            result.getTicket().getLicensePlate(),
            formattedEntryTime,
            formattedExitTime,
            hours, minutes
        );

        JLabel infoLabel = new JLabel(exitInfo);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(infoLabel);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Payment info panel with better visibility
        JPanel paymentPanel = new JPanel();
        paymentPanel.setLayout(new BorderLayout(10, 10));
        paymentPanel.setBackground(new Color(255, 255, 240));
        paymentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(costColor, 3),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        paymentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Set both preferred and maximum size for better rendering
        Dimension panelSize = new Dimension(400, 120);
        paymentPanel.setPreferredSize(panelSize);
        paymentPanel.setMaximumSize(panelSize);
        paymentPanel.setMinimumSize(panelSize);

        JLabel paymentLabel = new JLabel("TOTAL A PAGAR", SwingConstants.CENTER);
        paymentLabel.setFont(new Font("Arial", Font.BOLD, 18));
        paymentLabel.setForeground(new Color(60, 60, 60));
        paymentPanel.add(paymentLabel, BorderLayout.NORTH);

        JLabel amountLabel = new JLabel(costInfo, SwingConstants.CENTER);
        amountLabel.setFont(new Font("Arial", Font.BOLD, 36));
        amountLabel.setForeground(costColor);
        paymentPanel.add(amountLabel, BorderLayout.CENTER);

        contentPanel.add(paymentPanel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Close button only
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        JButton closeButton = new JButton("Cerrar");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setPreferredSize(new Dimension(120, 35));
        closeButton.setBackground(new Color(0, 150, 136));
        closeButton.setForeground(Color.BLACK);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> successDialog.dispose());
        buttonPanel.add(closeButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        successDialog.add(mainPanel);
        successDialog.setVisible(true);
    }
}
