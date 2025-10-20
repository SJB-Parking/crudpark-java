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
        super((Frame) null, "CrudPark - Vehicle Entry", true); // modal dialog
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
        JLabel titleLabel = new JLabel("Register Vehicle Entry", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(240, 240, 240));

        // Instructions
        JLabel instructionLabel = new JLabel("<html><div style='text-align: center;'>" +
            "Car format: <b>ABC123</b> (3 letters + 3 numbers)<br>" +
            "Motorcycle format: <b>ABC12D</b> (3 letters + 2 numbers + 1 letter)" +
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

        JLabel plateLabel = new JLabel("License Plate:");
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

        registerButton = new JButton("Register Entry");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setPreferredSize(new Dimension(150, 35));
        registerButton.setBackground(new Color(0, 120, 215));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.addActionListener(e -> handleRegister());

        cancelButton = new JButton("Cancel");
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
                "Please enter a license plate",
                "Validation Error",
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
     */
    public static void showEntrySuccess(Ticket ticket) {
        JFrame successFrame = new JFrame("Entry Registered Successfully");
        successFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        successFrame.setSize(500, 650);
        successFrame.setLocationRelativeTo(null);
        successFrame.setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);

        // Header
        JLabel headerLabel = new JLabel("✓ ENTRY REGISTERED", SwingConstants.CENTER);
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

        // Ticket info
        String ticketInfo = String.format(
            "<html><div style='font-family: Arial; padding: 20px; background-color: #f5f5f5; border-radius: 10px;'>" +
            "<table style='width: 100%%;'>" +
            "<tr><td><b>Ticket ID:</b></td><td>%06d</td></tr>" +
            "<tr><td><b>License Plate:</b></td><td><span style='font-size: 16px; color: #0078D7;'>%s</span></td></tr>" +
            "<tr><td><b>Vehicle Type:</b></td><td>%s</td></tr>" +
            "<tr><td><b>Ticket Type:</b></td><td>%s</td></tr>" +
            "<tr><td><b>Entry Time:</b></td><td>%s</td></tr>" +
            "</table>" +
            "</div></html>",
            ticket.getId(),
            ticket.getLicensePlate(),
            ticket.getVehicleType(),
            ticket.getTicketType(),
            formattedEntryTime
        );

        JLabel infoLabel = new JLabel(ticketInfo);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(infoLabel);

        // Add spacing
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // QR Code
        try {
            BufferedImage qrImage = QRCodeGenerator.generateQRCodeImage(ticket.getQrCodeData());
            JLabel qrLabel = new JLabel(new ImageIcon(qrImage));
            qrLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            qrLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            contentPanel.add(qrLabel);

            // QR description
            contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            JLabel qrDescription = new JLabel("Scan QR code for quick exit");
            qrDescription.setAlignmentX(Component.CENTER_ALIGNMENT);
            qrDescription.setFont(new Font("Arial", Font.ITALIC, 12));
            qrDescription.setForeground(Color.GRAY);
            contentPanel.add(qrDescription);
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Error generating QR code: " + e.getMessage());
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            errorLabel.setForeground(Color.RED);
            contentPanel.add(errorLabel);
        }

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Close button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setPreferredSize(new Dimension(120, 35));
        closeButton.setBackground(new Color(0, 120, 215));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> successFrame.dispose());
        buttonPanel.add(closeButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        successFrame.add(mainPanel);
        successFrame.setVisible(true);
    }
}
