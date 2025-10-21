package app.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Simple logging utility for application tracking and debugging
 * Logs to both console and file without external dependencies
 */
public class Logger {
    
    public enum Level {
        DEBUG("DEBUG", "\u001B[36m"),   // Cyan
        INFO("INFO ", "\u001B[32m"),    // Green
        WARN("WARN ", "\u001B[33m"),    // Yellow
        ERROR("ERROR", "\u001B[31m");   // Red
        
        private final String label;
        private final String colorCode;
        
        Level(String label, String colorCode) {
            this.label = label;
            this.colorCode = colorCode;
        }
    }
    
    private static final String RESET_COLOR = "\u001B[0m";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE = "crudpark.log";
    private static Level currentLevel = Level.INFO; // Default level
    
    private final String className;
    
    /**
     * Constructor
     * @param clazz The class that will use this logger
     */
    public Logger(Class<?> clazz) {
        this.className = clazz.getSimpleName();
    }
    
    /**
     * Get a logger instance for a class
     */
    public static Logger getLogger(Class<?> clazz) {
        return new Logger(clazz);
    }
    
    /**
     * Set the minimum logging level
     */
    public static void setLevel(Level level) {
        currentLevel = level;
    }
    
    /**
     * Log a debug message
     */
    public void debug(String message) {
        log(Level.DEBUG, message, null);
    }
    
    public void debug(String message, Object... args) {
        log(Level.DEBUG, formatMessage(message, args), null);
    }
    
    /**
     * Log an info message
     */
    public void info(String message) {
        log(Level.INFO, message, null);
    }
    
    public void info(String message, Object... args) {
        log(Level.INFO, formatMessage(message, args), null);
    }
    
    /**
     * Log a warning message
     */
    public void warn(String message) {
        log(Level.WARN, message, null);
    }
    
    public void warn(String message, Object... args) {
        log(Level.WARN, formatMessage(message, args), null);
    }
    
    /**
     * Log an error message
     */
    public void error(String message) {
        log(Level.ERROR, message, null);
    }
    
    public void error(String message, Throwable throwable) {
        log(Level.ERROR, message, throwable);
    }
    
    public void error(String message, Object... args) {
        log(Level.ERROR, formatMessage(message, args), null);
    }
    
    /**
     * Main logging method
     */
    private void log(Level level, String message, Throwable throwable) {
        // Check if this level should be logged
        if (level.ordinal() < currentLevel.ordinal()) {
            return;
        }
        
        String timestamp = LocalDateTime.now().format(DATE_FORMAT);
        String threadName = Thread.currentThread().getName();
        
        // Format log entry
        String logEntry = String.format("[%s] [%s] [%-5s] %s - %s",
            timestamp,
            threadName,
            level.label,
            className,
            message
        );
        
        // Console output with color
        String consoleEntry = String.format("%s[%s] [%s] [%-5s] %s - %s%s",
            level.colorCode,
            timestamp,
            threadName,
            level.label,
            className,
            message,
            RESET_COLOR
        );
        
        System.out.println(consoleEntry);
        
        // Write to file
        writeToFile(logEntry);
        
        // If there's a throwable, log the stack trace
        if (throwable != null) {
            String stackTrace = getStackTrace(throwable);
            System.err.println(stackTrace);
            writeToFile(stackTrace);
        }
    }
    
    /**
     * Format message with arguments (similar to SLF4J)
     */
    private String formatMessage(String message, Object... args) {
        if (args == null || args.length == 0) {
            return message;
        }
        
        String result = message;
        for (Object arg : args) {
            result = result.replaceFirst("\\{\\}", arg != null ? arg.toString() : "null");
        }
        return result;
    }
    
    /**
     * Get stack trace as string
     */
    private String getStackTrace(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        sb.append("Exception: ").append(throwable.getClass().getName())
          .append(": ").append(throwable.getMessage()).append("\n");
        
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append("\tat ").append(element.toString()).append("\n");
        }
        
        if (throwable.getCause() != null) {
            sb.append("Caused by: ").append(getStackTrace(throwable.getCause()));
        }
        
        return sb.toString();
    }
    
    /**
     * Write log entry to file
     */
    private void writeToFile(String logEntry) {
        try {
            // Ensure log directory exists
            Path logDir = Paths.get(LOG_DIR);
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
            }
            
            // Append to log file
            Path logFile = logDir.resolve(LOG_FILE);
            try (FileWriter fw = new FileWriter(logFile.toFile(), true);
                 PrintWriter pw = new PrintWriter(fw)) {
                pw.println(logEntry);
            }
        } catch (IOException e) {
            // If we can't write to file, at least print to stderr
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }
    
    /**
     * Log application startup
     */
    public static void logStartup(String appName, String version) {
        Logger logger = new Logger(Logger.class);
        logger.info("========================================");
        logger.info("{} v{} - Starting...", appName, version);
        logger.info("========================================");
    }
    
    /**
     * Log application shutdown
     */
    public static void logShutdown(String appName) {
        Logger logger = new Logger(Logger.class);
        logger.info("========================================");
        logger.info("{} - Shutting down...", appName);
        logger.info("========================================");
    }
}
