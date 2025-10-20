package app.view.swing;

import app.model.Ticket;
import app.util.TicketPrinter;

import javax.swing.*;
import java.awt.*;

/**
 * Vehicle entry window
 */
public class VehicleEntryFrame extends JFrame {
    private JTextField licensePlateField;
    private JButton registerButton;
    private JButton cancelButton;
    
    private String licensePlate;
    private boolean registered = false;

    public VehicleEntryFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("CrudPark - Vehicle Entry");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

    public void showWindow() {
        setVisible(true);
    }

    /**
     * Print entry ticket to selected printer
     */
    public static void showEntrySuccess(Ticket ticket) {
        // Show confirmation dialog
        int result = JOptionPane.showConfirmDialog(null,
            String.format("Entry registered successfully!\n\nTicket ID: %06d\nLicense Plate: %s\n\nDo you want to print the ticket?",
                ticket.getId(), ticket.getLicensePlate()),
            "Entry Registered",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            // Print the ticket
            TicketPrinter.printEntryTicket(ticket);
        }
    }
}
