package app.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Manages database connections using HikariCP connection pool
 * HikariCP uses JDBC internally for all database operations
 */
public class DatabaseConnection {
    private static HikariDataSource dataSource;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream input = DatabaseConnection.class.getClassLoader()
                .getResourceAsStream("database.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            
            String url = prop.getProperty("db.url");
            String username = prop.getProperty("db.username");
            String password = prop.getProperty("db.password");
            
            initializeDataSource(url, username, password);
            
        } catch (Exception e) {
            System.err.println("Error loading database properties: " + e.getMessage());
            // Default values
            initializeDataSource(
                "jdbc:postgresql://localhost:5432/crudpark",
                "postgres",
                "postgres"
            );
        }
    }

    /**
     * Initialize HikariCP DataSource with connection pool configuration
     */
    private static void initializeDataSource(String url, String username, String password) {
        HikariConfig config = new HikariConfig();
        
        // JDBC connection settings
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        
        // Connection pool settings
        config.setMaximumPoolSize(10);              // Maximum connections in pool
        config.setMinimumIdle(2);                   // Minimum idle connections
        config.setConnectionTimeout(30000);         // 30 seconds
        config.setIdleTimeout(600000);              // 10 minutes
        config.setMaxLifetime(1800000);             // 30 minutes
        
        // Performance settings
        config.setAutoCommit(false);                // Manual transaction control
        config.setConnectionTestQuery("SELECT 1");  // Test query for PostgreSQL
        
        // Pool name for logging
        config.setPoolName("ParkingDB-Pool");
        
        dataSource = new HikariDataSource(config);
        
        System.out.println("[INFO] HikariCP Connection Pool initialized successfully");
        System.out.println("  - JDBC URL: " + url);
        System.out.println("  - Pool Size: 2-10 connections");
    }

    /**
     * Get a connection from the pool
     * This connection uses JDBC internally and should be used with try-with-resources
     * 
     * @return JDBC Connection from the pool
     * @throws SQLException if connection cannot be obtained
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource not initialized");
        }
        return dataSource.getConnection();
    }

    /**
     * Close the connection pool (call on application shutdown)
     */
    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("[INFO] HikariCP Connection Pool closed");
        }
    }

    /**
     * Get pool statistics (for monitoring)
     */
    public static String getPoolStats() {
        if (dataSource != null) {
            return String.format(
                "Pool Stats - Active: %d, Idle: %d, Total: %d, Waiting: %d",
                dataSource.getHikariPoolMXBean().getActiveConnections(),
                dataSource.getHikariPoolMXBean().getIdleConnections(),
                dataSource.getHikariPoolMXBean().getTotalConnections(),
                dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection()
            );
        }
        return "Pool not initialized";
    }
}
