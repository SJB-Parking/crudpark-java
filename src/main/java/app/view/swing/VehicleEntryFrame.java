package app.view.swing;

import app.model.Ticket;
import app.util.QRCodeGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Vehicle entry dialog (modal)
 * Changed from JFrame to JDialog to support modal behavior without threads
 */
public class VehicleEntryFrame extends JDialog {
    private JTextField licensePlateField;
    private JButton registerButton;
    private JButton cancelButton;
    
    private String licensePlate;
    private boolean registered = false;

    public VehicleEntryFrame() {
        super((Frame) null, "CrudPark - Entrada de Vehículo", true); // modal dialog
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(500, 280);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(240, 240, 240));

        // Title
        JLabel titleLabel = new JLabel("Registrar Entrada de Vehículo", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(240, 240, 240));

        // Instructions
        JLabel instructionLabel = new JLabel("<html><div style='text-align: center;'>" +
            "Formato Carro: <b>ABC123</b> (3 letras + 3 números)<br>" +
            "Formato Moto: <b>ABC12D</b> (3 letras + 2 números + 1 letra)" +
            "</div></html>", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        formPanel.add(instructionLabel);

        // Input panel with label and field
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.setBackground(new Color(240, 240, 240));
        inputPanel.setMaximumSize(new Dimension(500, 50));

        JLabel plateLabel = new JLabel("Placa:");
        plateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        licensePlateField = new JTextField(15);
        licensePlateField.setFont(new Font("Arial", Font.PLAIN, 16));
        licensePlateField.setPreferredSize(new Dimension(200, 30));

        inputPanel.add(plateLabel);
        inputPanel.add(licensePlateField);

        formPanel.add(inputPanel);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));

        registerButton = new JButton("Registrar Entrada");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setPreferredSize(new Dimension(150, 35));
        registerButton.setBackground(new Color(0, 120, 215));
        registerButton.setForeground(Color.BLACK);
        registerButton.setFocusPainted(false);
        registerButton.addActionListener(e -> handleRegister());

        cancelButton = new JButton("Cancelar");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Enter key press triggers register
        licensePlateField.addActionListener(e -> handleRegister());
    }

    private void handleRegister() {
        licensePlate = licensePlateField.getText().trim().toUpperCase();

        if (licensePlate.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingrese una placa",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        registered = true;
        dispose();
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public boolean isRegistered() {
        return registered;
    }

    /**
     * Show entry success in a new window with ticket details and QR code
     * Uses modal dialog to block until user closes the window
     */
    public static void showEntrySuccess(Ticket ticket) {
        JDialog successFrame = new JDialog((JFrame) null, "Entry Registered Successfully", true); // Modal dialog
        successFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        successFrame.setSize(500, 650);
        successFrame.setLocationRelativeTo(null);
        successFrame.setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);

        // Header
        JLabel headerLabel = new JLabel("ENTRADA REGISTRADA", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(0, 120, 215));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        // Format entry datetime
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm a");
        String formattedEntryTime = dateFormat.format(new java.util.Date(ticket.getEntryDatetime().getTime()));

        // Ticket info panel with consistent styling
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(5, 2, 10, 15));
        infoPanel.setBackground(new Color(245, 245, 245));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Define consistent font sizes
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font valueFont = new Font("Arial", Font.PLAIN, 14);
        Font plateFont = new Font("Arial", Font.BOLD, 16);

        // Ticket ID
        JLabel ticketIdLabel = new JLabel("ID Ticket:");
        ticketIdLabel.setFont(labelFont);
        JLabel ticketIdValue = new JLabel(String.format("%06d", ticket.getId()));
        ticketIdValue.setFont(valueFont);
        infoPanel.add(ticketIdLabel);
        infoPanel.add(ticketIdValue);

        // License Plate (destacado)
        JLabel plateLabel = new JLabel("Placa:");
        plateLabel.setFont(labelFont);
        JLabel plateValue = new JLabel(ticket.getLicensePlate());
        plateValue.setFont(plateFont);
        plateValue.setForeground(new Color(0, 120, 215));
        infoPanel.add(plateLabel);
        infoPanel.add(plateValue);

        // Vehicle Type
        JLabel typeLabel = new JLabel("Tipo de Vehículo:");
        typeLabel.setFont(labelFont);
        JLabel typeValue = new JLabel(app.util.VehicleTypeTranslator.toSpanish(ticket.getVehicleType()));
        typeValue.setFont(valueFont);
        infoPanel.add(typeLabel);
        infoPanel.add(typeValue);

        // Ticket Type
        JLabel ticketTypeLabel = new JLabel("Tipo de Ticket:");
        ticketTypeLabel.setFont(labelFont);
        JLabel ticketTypeValue = new JLabel(ticket.getTicketType());
        ticketTypeValue.setFont(valueFont);
        infoPanel.add(ticketTypeLabel);
        infoPanel.add(ticketTypeValue);

        // Entry Time
        JLabel entryLabel = new JLabel("Hora de Entrada:");
        entryLabel.setFont(labelFont);
        JLabel entryValue = new JLabel(formattedEntryTime);
        entryValue.setFont(valueFont);
        infoPanel.add(entryLabel);
        infoPanel.add(entryValue);

        contentPanel.add(infoPanel);

        // Add spacing
        contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // QR Code section - centered
        JPanel qrPanel = new JPanel();
        qrPanel.setLayout(new BoxLayout(qrPanel, BoxLayout.Y_AXIS));
        qrPanel.setBackground(Color.WHITE);
        
        try {
            BufferedImage qrImage = QRCodeGenerator.generateQRCodeImage(ticket.getQrCodeData());
            JLabel qrLabel = new JLabel(new ImageIcon(qrImage));
            qrLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            qrLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            qrPanel.add(qrLabel);

            // QR description
            qrPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            JLabel qrDescription = new JLabel("Escanea el código QR para salida rápida");
            qrDescription.setAlignmentX(Component.CENTER_ALIGNMENT);
            qrDescription.setFont(new Font("Arial", Font.ITALIC, 12));
            qrDescription.setForeground(Color.GRAY);
            qrPanel.add(qrDescription);
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Error al generar código QR: " + e.getMessage());
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            errorLabel.setForeground(Color.RED);
            qrPanel.add(errorLabel);
        }

        contentPanel.add(qrPanel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Close button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        JButton closeButton = new JButton("Cerrar");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setPreferredSize(new Dimension(120, 35));
        closeButton.setBackground(new Color(0, 120, 215));
        closeButton.setForeground(Color.BLACK);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> successFrame.dispose());
        buttonPanel.add(closeButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        successFrame.add(mainPanel);
        successFrame.setVisible(true);
    }
}
