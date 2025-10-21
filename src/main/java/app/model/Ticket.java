package app.model;

import java.sql.Timestamp;

/**
 * Ticket entity
 */
public class Ticket {
    private int id;
    private String folio;
    private int vehicleId;
    private int operatorId;
    private Integer subscriptionId;
    private Timestamp entryDatetime;
    private Timestamp exitDatetime;
    private String ticketType;
    private String status;
    private Integer parkingDurationMinutes;
    private String qrCodeData;
    
    // For display
    private String licensePlate;
    private String vehicleType;

    public Ticket() {}

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public Timestamp getEntryDatetime() {
        return entryDatetime;
    }

    public void setEntryDatetime(Timestamp entryDatetime) {
        this.entryDatetime = entryDatetime;
    }

    public Timestamp getExitDatetime() {
        return exitDatetime;
    }

    public void setExitDatetime(Timestamp exitDatetime) {
        this.exitDatetime = exitDatetime;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getParkingDurationMinutes() {
        return parkingDurationMinutes;
    }

    public void setParkingDurationMinutes(Integer parkingDurationMinutes) {
        this.parkingDurationMinutes = parkingDurationMinutes;
    }

    public String getQrCodeData() {
        return qrCodeData;
    }

    public void setQrCodeData(String qrCodeData) {
        this.qrCodeData = qrCodeData;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Integer subscriptionId) {
        this.subscriptionId = subscriptionId;
    }
}
