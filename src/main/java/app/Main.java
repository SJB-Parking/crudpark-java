package app;

import app.controller.AuthController;
import app.controller.ParkingController;
import app.dao.*;
import app.exception.AuthenticationException;
import app.exception.BusinessException;
import app.exception.DataAccessException;
import app.exception.NotFoundException;
import app.exception.ValidationException;
import app.model.Operator;
import app.service.AuthService;
import app.service.ParkingService;
import app.view.LoginView;
import app.view.MainMenuView;
import app.view.VehicleEntryView;
import app.view.VehicleExitView;

import javax.swing.*;

/**
 * Main application - Parking System with dependency injection
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
    private static ParkingController parkingController;
    
    // Views
    private static LoginView loginView;
    private static MainMenuView mainMenuView;
    private static VehicleEntryView vehicleEntryView;
    private static VehicleExitView vehicleExitView;
    
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
        parkingController = new ParkingController(parkingService);
        
        // Initialize Views
        loginView = new LoginView();
        mainMenuView = new MainMenuView();
        vehicleEntryView = new VehicleEntryView();
        vehicleExitView = new VehicleExitView();
    }

    /**
     * Run application
     */
    private static void run() {
        // Login process
        Operator operator = performLogin();
        if (operator == null) {
            return; // Login cancelled or failed
        }
        
        // Show welcome
        loginView.showWelcome(operator.getFullName());
        
        // Main menu loop
        runMainMenu(operator);
    }

    /**
     * Perform login with retry logic
     */
    private static Operator performLogin() {
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;
        
        while (attempts < MAX_ATTEMPTS) {
            LoginView.LoginCredentials credentials = loginView.showLoginDialog();
            
            if (credentials == null) {
                loginView.showCancelled();
                return null;
            }
            
            try {
                return authController.login(credentials.getEmail(), credentials.getPassword());
                
            } catch (ValidationException e) {
                loginView.showError("Validation Error:\n" + e.getMessage());
                attempts++;
                
            } catch (AuthenticationException e) {
                attempts++;
                String message = e.getMessage() + "\n\nAttempts remaining: " + (MAX_ATTEMPTS - attempts);
                loginView.showError(message);
                
            } catch (DataAccessException e) {
                loginView.showError("Database Error:\n" + e.getMessage());
                attempts++;
            }
        }
        
        loginView.showTooManyAttempts();
        return null;
    }

    /**
     * Run main menu loop
     */
    private static void runMainMenu(Operator operator) {
        while (true) {
            int choice = mainMenuView.showMenu();
            
            if (choice == -1 || choice == 2) {
                // Logout
                mainMenuView.showGoodbye(operator.getFullName());
                break;
            }
            
            if (choice == 0) {
                handleVehicleEntry(operator);
            } else if (choice == 1) {
                handleVehicleExit(operator);
            }
        }
    }

    /**
     * Handle vehicle entry
     */
    private static void handleVehicleEntry(Operator operator) {
        String licensePlate = vehicleEntryView.showLicensePlateInput();
        
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            return; // User cancelled
        }
        
        try {
            var ticket = parkingController.processEntry(licensePlate, operator.getId());
            vehicleEntryView.showEntrySuccess(ticket);
            
        } catch (ValidationException e) {
            vehicleEntryView.showError("Validation Error:\n" + e.getMessage());
            
        } catch (BusinessException e) {
            vehicleEntryView.showError("Business Error:\n" + e.getMessage());
            
        } catch (DataAccessException e) {
            vehicleEntryView.showError("Database Error:\n" + e.getMessage());
        }
    }

    /**
     * Handle vehicle exit
     */
    private static void handleVehicleExit(Operator operator) {
        String ticketId = vehicleExitView.showTicketIdInput();
        
        if (ticketId == null || ticketId.trim().isEmpty()) {
            return; // User cancelled
        }
        
        try {
            var exitResult = parkingController.processExit(ticketId, operator.getId());
            vehicleExitView.showExitSuccess(exitResult);
            
        } catch (ValidationException e) {
            vehicleExitView.showError("Validation Error:\n" + e.getMessage());
            
        } catch (NotFoundException e) {
            vehicleExitView.showError("Not Found:\n" + e.getMessage());
            
        } catch (BusinessException e) {
            vehicleExitView.showError("Business Error:\n" + e.getMessage());
            
        } catch (DataAccessException e) {
            vehicleExitView.showError("Database Error:\n" + e.getMessage());
        }
    }
}
