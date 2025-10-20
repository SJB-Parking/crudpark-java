package app.model;

import java.sql.Timestamp;

/**
 * Rate entity
 */
public class Rate {
    private int id;
    private String rateName;
    private double hourlyRate;
    private double fractionRate;
    private Double dailyCap;
    private int gracePeriodMinutes;
    private Timestamp effectiveFrom;
    private boolean isActive;
    private String vehicleType;

    public Rate() {}

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRateName() {
        return rateName;
    }

    public void setRateName(String rateName) {
        this.rateName = rateName;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getFractionRate() {
        return fractionRate;
    }

    public void setFractionRate(double fractionRate) {
        this.fractionRate = fractionRate;
    }

    public Double getDailyCap() {
        return dailyCap;
    }

    public void setDailyCap(Double dailyCap) {
        this.dailyCap = dailyCap;
    }

    public int getGracePeriodMinutes() {
        return gracePeriodMinutes;
    }

    public void setGracePeriodMinutes(int gracePeriodMinutes) {
        this.gracePeriodMinutes = gracePeriodMinutes;
    }

    public Timestamp getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(Timestamp effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}
