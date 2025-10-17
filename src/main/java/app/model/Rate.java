package app.model;

import java.sql.Timestamp;

/**
 * Rate entity
 */
public class Rate {
    private int id;
    private double hourlyRate;
    private double fractionRate;
    private Double dailyCap;
    private Timestamp effectiveFrom;
    private boolean isActive;

    public Rate() {}

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
