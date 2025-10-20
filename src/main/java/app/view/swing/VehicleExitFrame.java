package app.view.swing;

import app.service.ParkingService;
import app.util.TicketPrinter;

import javax.swing.*;
import java.awt.*;

/**
 * Vehicle exit window
 */
public class VehicleExitFrame extends JFrame {
    private JTextField ticketIdField;
    private JButton processButton;
    private JButton cancelButton;
    
    private String ticketId;
    private boolean processed = false;

    public VehicleExitFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("CrudPark - Vehicle Exit");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 220);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(240, 240, 240));

        // Title
        JLabel titleLabel = new JLabel("Process Vehicle Exit", SwingConstants.CENTER);
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

        JLabel ticketLabel = new JLabel("Ticket ID:");
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

        processButton = new JButton("Process Exit");
        processButton.setFont(new Font("Arial", Font.BOLD, 14));
        processButton.setPreferredSize(new Dimension(130, 35));
        processButton.setBackground(new Color(0, 150, 136));
        processButton.setForeground(Color.WHITE);
        processButton.setFocusPainted(false);
        processButton.addActionListener(e -> handleProcess());

        cancelButton = new JButton("Cancel");
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
                "Please enter a ticket ID",
                "Validation Error",
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

    public void showWindow() {
        setVisible(true);
    }

    /**
     * Print exit receipt to selected printer
     */
    public static void showExitSuccess(ParkingService.ExitResult result) {
        // Format the payment info
        String paymentInfo;
        if (result.isFree()) {
            paymentInfo = String.format("FREE (%s)", result.getFreeReason());
        } else {
            paymentInfo = String.format("$%.2f", result.getAmount());
        }
        
        // Calculate duration
        long hours = result.getDurationMinutes() / 60;
        long minutes = result.getDurationMinutes() % 60;
        
        // Show confirmation dialog
        int confirmResult = JOptionPane.showConfirmDialog(null,
            String.format("Exit processed successfully!\n\nTicket ID: %06d\nLicense Plate: %s\nDuration: %d hours %d minutes\nAmount: %s\n\nDo you want to print the receipt?",
                result.getTicket().getId(), 
                result.getTicket().getLicensePlate(),
                hours, 
                minutes,
                paymentInfo),
            "Exit Processed",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE);
        
        if (confirmResult == JOptionPane.YES_OPTION) {
            // Print the receipt
            TicketPrinter.printExitTicket(result);
        }
    }
}
