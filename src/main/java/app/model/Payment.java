package app.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Payment entity - represents a payment record
 */
public class Payment {
    private Integer id;
    private Integer ticketId;
    private BigDecimal amount;
    private Timestamp paymentDatetime;
    private String paymentMethod;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Constructors
    public Payment() {
    }

    public Payment(Integer ticketId, BigDecimal amount, Timestamp paymentDatetime, String paymentMethod) {
        this.ticketId = ticketId;
        this.amount = amount;
        this.paymentDatetime = paymentDatetime;
        this.paymentMethod = paymentMethod;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Timestamp getPaymentDatetime() {
        return paymentDatetime;
    }

    public void setPaymentDatetime(Timestamp paymentDatetime) {
        this.paymentDatetime = paymentDatetime;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", ticketId=" + ticketId +
                ", amount=" + amount +
                ", paymentDatetime=" + paymentDatetime +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
