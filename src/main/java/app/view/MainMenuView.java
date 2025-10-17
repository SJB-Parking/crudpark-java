package app.view;

import javax.swing.*;

/**
 * Main menu view
 */
public class MainMenuView {

    /**
     * Show main menu and return selected option
     * Returns: 0 = Entry, 1 = Exit, 2 = Logout, -1 = Window closed
     */
    public int showMenu() {
        String[] options = {"Vehicle Entry", "Vehicle Exit", "Logout"};
        
        return JOptionPane.showOptionDialog(null, 
            "Select an option:", 
            "Parking System - Main Menu", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            options, 
            options[0]);
    }

    /**
     * Show goodbye message
     */
    public void showGoodbye(String operatorName) {
        JOptionPane.showMessageDialog(null, 
            "Thank you for using Parking System!\nGoodbye, " + operatorName, 
            "Logout", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}
