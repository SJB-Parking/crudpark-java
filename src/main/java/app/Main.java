package app;

import app.controller.AuthController;
import app.controller.ParkingController;
import app.controller.SwingAuthController;
import app.controller.SwingVehicleEntryController;
import app.controller.SwingVehicleExitController;
import app.dao.*;
import app.model.Operator;
import app.service.AuthService;
import app.service.ParkingService;

import javax.swing.*;

/**
 * Main application - Parking System with dependency injection
 * Refactored to use new MVC architecture with clean separation of concerns
 */
public class Main {
    // DAOs
    private static OperatorDAO operatorDAO;
    private static VehicleDAO vehicleDAO;
    private static TicketDAO ticketDAO;
    private static SubscriptionDAO subscriptionDAO;
    private static RateDAO rateDAO;
    private static PaymentDAO paymentDAO;
    
    // Services
    private static AuthService authService;
    private static ParkingService parkingService;
    
    // Controllers
    private static AuthController authController;
    private static SwingAuthController swingAuthController;
    private static ParkingController parkingController;
    private static SwingVehicleEntryController swingVehicleEntryController;
    private static SwingVehicleExitController swingVehicleExitController;
    
    // Views (Swing)
    private static app.view.swing.LoginView swingLoginView;
    private static app.view.swing.VehicleEntryView swingVehicleEntryView;
    private static app.view.swing.VehicleExitView swingVehicleExitView;
    private static app.view.swing.MainMenuView swingMainMenuView;
    
    public static void main(String[] args) {
        try {
            // Set look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Initialize dependencies
            initializeDependencies();
            
            // Start application
            run();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Fatal error: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Dependency injection - Initialize all dependencies
     */
    private static void initializeDependencies() {
        // Initialize DAOs
        operatorDAO = new OperatorDAO();
        vehicleDAO = new VehicleDAO();
        ticketDAO = new TicketDAO();
        subscriptionDAO = new SubscriptionDAO();
        rateDAO = new RateDAO();
        paymentDAO = new PaymentDAO();
        
        // Initialize Services (inject DAOs)
        authService = new AuthService(operatorDAO);
        parkingService = new ParkingService(vehicleDAO, ticketDAO, subscriptionDAO, 
                                           rateDAO, paymentDAO);
        
        // Initialize Controllers (inject Services)
        authController = new AuthController(authService);
        swingAuthController = new SwingAuthController(authController);
        parkingController = new ParkingController(parkingService);
        
        // Note: SwingVehicle controllers will be created per operator session
        // because they need operator ID
        
        // Initialize Views (inject Controllers)
        swingLoginView = new app.view.swing.LoginView(swingAuthController);
        swingMainMenuView = new app.view.swing.MainMenuView();
    }

    /**
     * Run application
     */
    private static void run() {
        // Login process - View handles all UI logic including retries
        Operator operator = swingLoginView.showLoginAndAuthenticate();
        if (operator == null) {
            return; // Login cancelled or failed
        }
        
        // Main menu loop
        runMainMenu(operator);
    }

    /**
     * Run main menu loop
     */
    private static void runMainMenu(Operator operator) {
        // Initialize vehicle controllers with operator ID
        swingVehicleEntryController = new SwingVehicleEntryController(parkingController, operator.getId());
        swingVehicleExitController = new SwingVehicleExitController(parkingController, operator.getId());
        
        // Initialize vehicle views with their controllers
        swingVehicleEntryView = new app.view.swing.VehicleEntryView(swingVehicleEntryController);
        swingVehicleExitView = new app.view.swing.VehicleExitView(swingVehicleExitController);
        
        while (true) {
            int choice = swingMainMenuView.showMenu(operator);
            
            if (choice == -1 || choice == 2) {
                // Logout
                swingMainMenuView.showGoodbye(operator.getFullName());
                break;
            }
            
            if (choice == 0) {
                handleVehicleEntry();
            } else if (choice == 1) {
                handleVehicleExit();
            }
        }
    }

    /**
     * Handle vehicle entry - View handles all UI logic
     */
    private static void handleVehicleEntry() {
        swingVehicleEntryView.showEntryForm();
    }

    /**
     * Handle vehicle exit - View handles all UI logic
     */
    private static void handleVehicleExit() {
        swingVehicleExitView.showExitForm();
    }
}
