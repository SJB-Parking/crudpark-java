package app.view.swing;

import javax.swing.*;
import java.awt.*;

/**
 * Main menu window
 */
public class MainMenuFrame extends JFrame {
    private String selectedOption = null;

    public MainMenuFrame(String operatorName) {
        initComponents(operatorName);
    }

    private void initComponents(String operatorName) {
        setTitle("CrudPark - Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(240, 240, 240));

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(240, 240, 240));

        JLabel titleLabel = new JLabel("Parking Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel operatorLabel = new JLabel("Operator: " + operatorName, SwingConstants.CENTER);
        operatorLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        operatorLabel.setForeground(new Color(100, 100, 100));
        operatorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        operatorLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 20, 0));

        headerPanel.add(titleLabel);
        headerPanel.add(operatorLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(3, 1, 10, 15));
        menuPanel.setBackground(new Color(240, 240, 240));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        JButton vehicleEntryButton = createMenuButton("🚗 Vehicle Entry", new Color(0, 120, 215));
        vehicleEntryButton.addActionListener(e -> selectOption("1"));

        JButton vehicleExitButton = createMenuButton("🚪 Vehicle Exit", new Color(0, 150, 136));
        vehicleExitButton.addActionListener(e -> selectOption("2"));

        JButton exitButton = createMenuButton("❌ Exit System", new Color(200, 50, 50));
        exitButton.addActionListener(e -> selectOption("3"));

        menuPanel.add(vehicleEntryButton);
        menuPanel.add(vehicleExitButton);
        menuPanel.add(exitButton);

        mainPanel.add(menuPanel, BorderLayout.CENTER);

        // Footer
        JLabel footerLabel = new JLabel("Select an option", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerLabel.setForeground(new Color(100, 100, 100));
        mainPanel.add(footerLabel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JButton createMenuButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(300, 60));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    private void selectOption(String option) {
        selectedOption = option;
        setVisible(false);
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void showWindow() {
        setVisible(true);
    }
}
