package app.dao;

import app.exception.DataAccessException;
import app.model.Ticket;

import java.sql.Connection;
import java.sql.Timestamp;

/**
 * Interface for Ticket Data Access Object
 */
public interface ITicketDAO {
    
    /**
     * Check if vehicle has an open ticket
     * @param conn Database connection
     * @param vehicleId Vehicle ID
     * @return true if vehicle has open ticket, false otherwise
     * @throws DataAccessException if database error occurs
     */
    boolean hasOpenTicket(Connection conn, int vehicleId) throws DataAccessException;
    
    /**
     * Generate next folio number
     * @param conn Database connection
     * @return Next folio number
     * @throws DataAccessException if database error occurs
     */
    String generateNextFolio(Connection conn) throws DataAccessException;
    
    /**
     * Create a new ticket
     * @param conn Database connection
     * @param folio Ticket folio
     * @param vehicleId Vehicle ID
     * @param operatorId Operator ID
     * @param subscriptionId Subscription ID (null for regular tickets)
     * @param ticketType Ticket type (Regular, Monthly)
     * @param qrCodeData QR code data
     * @return Created ticket
     * @throws DataAccessException if database error occurs
     */
    Ticket create(Connection conn, String folio, int vehicleId, int operatorId,
                  Integer subscriptionId, String ticketType, String qrCodeData) throws DataAccessException;
    
    /**
     * Find ticket by ID with vehicle information
     * @param ticketId Ticket ID
     * @return Ticket with vehicle data if found, null otherwise
     * @throws DataAccessException if database error occurs
     */
    Ticket findByIdWithVehicle(int ticketId) throws DataAccessException;
    
    /**
     * Find open ticket by license plate
     * @param conn Database connection
     * @param licensePlate Vehicle license plate
     * @return Open ticket if found, null otherwise
     * @throws DataAccessException if database error occurs
     */
    Ticket findOpenTicketByPlate(Connection conn, String licensePlate) throws DataAccessException;
    
    /**
     * Update ticket QR code
     * @param conn Database connection
     * @param ticketId Ticket ID
     * @param qrCodeData QR code data
     * @throws DataAccessException if database error occurs
     */
    void updateQRCode(Connection conn, int ticketId, String qrCodeData) throws DataAccessException;
    
    /**
     * Update ticket exit information
     * @param conn Database connection
     * @param ticketId Ticket ID
     * @param exitTime Exit timestamp
     * @param durationMinutes Duration in minutes
     * @throws DataAccessException if database error occurs
     */
    void updateExit(Connection conn, int ticketId, Timestamp exitTime, int durationMinutes) 
            throws DataAccessException;
}
