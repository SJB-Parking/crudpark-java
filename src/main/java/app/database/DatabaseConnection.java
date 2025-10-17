package app.database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Manages database connections
 */
public class DatabaseConnection {
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream input = DatabaseConnection.class.getClassLoader()
                .getResourceAsStream("database.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            
            URL = prop.getProperty("db.url");
            USERNAME = prop.getProperty("db.username");
            PASSWORD = prop.getProperty("db.password");
        } catch (Exception e) {
            System.err.println("Error loading database properties: " + e.getMessage());
            // Default values
            URL = "jdbc:postgresql://localhost:5432/crudpark";
            USERNAME = "postgres";
            PASSWORD = "postgres";
        }
    }

    /**
     * Get a new database connection
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
