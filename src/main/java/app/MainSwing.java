package app;

import app.controller.*;
import app.dao.*;
import app.model.Operator;
import app.service.*;
import app.view.swing.MainMenuFrame;

import javax.swing.*;

/**
 * Main application entry point with Swing UI
 */
public class MainSwing {

    public static void main(String[] args) {
        try {
            // Set Look and Feel to system default
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Initialize DAOs
            OperatorDAO operatorDAO = new OperatorDAO();
            VehicleDAO vehicleDAO = new VehicleDAO();
            TicketDAO ticketDAO = new TicketDAO();
            RateDAO rateDAO = new RateDAO();
            SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
            PaymentDAO paymentDAO = new PaymentDAO();
            
            // Initialize Services
            AuthService authService = new AuthService(operatorDAO);
            ParkingService parkingService = new ParkingService(vehicleDAO, ticketDAO, 
                                                              subscriptionDAO, rateDAO, paymentDAO);
            
            // Initialize Controllers
            AuthController authController = new AuthController(authService);
            ParkingController parkingController = new ParkingController(parkingService);
            
            // Initialize Swing Controllers
            SwingAuthController swingAuthController = new SwingAuthController(authService, authController);
            
            // Login
            Operator operator = swingAuthController.login();
            
            if (operator == null) {
                System.out.println("Login cancelled. Exiting...");
                System.exit(0);
            }
            
            // Initialize entry/exit controllers
            SwingVehicleEntryController entryController = new SwingVehicleEntryController(
                parkingService, parkingController, operator.getId());
            SwingVehicleExitController exitController = new SwingVehicleExitController(
                parkingService, parkingController, operator.getId());
            
            // Main menu loop
            while (true) {
                MainMenuFrame menuFrame = new MainMenuFrame(operator.getFullName());
                menuFrame.showWindow();
                
                // Wait for selection
                while (menuFrame.isVisible()) {
                    Thread.sleep(100);
                }
                
                String option = menuFrame.getSelectedOption();
                
                if (option == null || option.equals("3")) {
                    JOptionPane.showMessageDialog(null,
                        "Thank you for using CrudPark!",
                        "Goodbye",
                        JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
                
                switch (option) {
                    case "1": // Vehicle Entry
                        entryController.processEntry();
                        break;
                    case "2": // Vehicle Exit
                        exitController.processExit();
                        break;
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Application Error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}
