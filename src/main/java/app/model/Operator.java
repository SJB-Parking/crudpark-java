package app.model;

/**
 * Operator entity
 */
public class Operator {
    private int id;
    private String fullName;
    private String email;
    private String username;
    private String passwordHash;
    private boolean isActive;

    public Operator() {}

    public Operator(int id, String fullName, String email, String username) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.username = username;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
