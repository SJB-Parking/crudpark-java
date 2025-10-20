package app.view.swing;

import app.model.Operator;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;

/**
 * Main menu Swing view - uses MainMenuFrame with visual buttons
 * This provides a better UX than the simple JOptionPane
 */
public class MainMenuView {
    
    /**
     * Show main menu with visual buttons and return selected option
     * Returns: 0 = Entry, 1 = Exit, 2 = Logout
     */
    public int showMenu(Operator operator) {
        CountDownLatch latch = new CountDownLatch(1);
        MainMenuFrame frame = new MainMenuFrame(operator.getFullName());
        
        // Add window listener to detect when frame is closed
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                latch.countDown();
            }
        });
        
        frame.showWindow();
        
        // Wait for user selection
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            frame.dispose();
            return 2; // Default to logout
        }
        
        String option = frame.getSelectedOption();
        
        if (option == null) {
            return 2; // Window closed, logout
        }
        
        // Map string options to integers
        switch (option) {
            case "1": return 0; // Vehicle Entry
            case "2": return 1; // Vehicle Exit
            case "3": return 2; // Logout
            default: return 2;
        }
    }
    
    /**
     * Show goodbye message
     */
    public void showGoodbye(String operatorName) {
        javax.swing.JOptionPane.showMessageDialog(null, 
            "Thank you for using Parking System!\nGoodbye, " + operatorName, 
            "Logout", 
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }
}
