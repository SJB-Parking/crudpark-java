package app.controller;

import app.exception.BusinessException;
import app.exception.DataAccessException;
import app.exception.NotFoundException;
import app.service.ParkingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingControllerTest {

    @Mock
    private ParkingService parkingService;

    private ParkingController parkingController;

    @BeforeEach
    void setUp() {
        parkingController = new ParkingController(parkingService);
    }

    // ========== Preview Exit By ID Tests ==========

    @Test
    void testPreviewExitById_ValidInput_Success() throws Exception {
        // Given
        String ticketId = "123";
        int operatorId = 1;
        
        ParkingService.ExitResult mockExitResult = mock(ParkingService.ExitResult.class);
        when(parkingService.previewExitById(123, operatorId)).thenReturn(mockExitResult);

        // When
        ExitResult result = parkingController.previewExitById(ticketId, operatorId);

        // Then
        assertTrue(result.isSuccess());
        assertEquals(mockExitResult, result.getExitResult());
        verify(parkingService).previewExitById(123, operatorId);
    }

    @Test
    void testPreviewExitById_EmptyTicketId_ValidationError() {
        // Given
        String ticketId = "";
        int operatorId = 1;

        // When
        ExitResult result = parkingController.previewExitById(ticketId, operatorId);

        // Then
        assertFalse(result.isSuccess());
        assertEquals(ExitResult.ErrorType.VALIDATION, result.getErrorType());
        assertEquals("Ticket ID cannot be empty", result.getErrorMessage());
        verifyNoInteractions(parkingService);
    }

    @Test
    void testPreviewExitById_NullTicketId_ValidationError() {
        // Given
        String ticketId = null;
        int operatorId = 1;

        // When
        ExitResult result = parkingController.previewExitById(ticketId, operatorId);

        // Then
        assertFalse(result.isSuccess());
        assertEquals(ExitResult.ErrorType.VALIDATION, result.getErrorType());
        verifyNoInteractions(parkingService);
    }

    @Test
    void testPreviewExitById_InvalidTicketIdFormat_ValidationError() {
        // Given
        String ticketId = "ABC";
        int operatorId = 1;

        // When
        ExitResult result = parkingController.previewExitById(ticketId, operatorId);

        // Then
        assertFalse(result.isSuccess());
        assertEquals(ExitResult.ErrorType.VALIDATION, result.getErrorType());
        assertEquals("Ticket ID must be a valid number", result.getErrorMessage());
        verifyNoInteractions(parkingService);
    }

    @Test
    void testPreviewExitById_NegativeTicketId_ValidationError() {
        // Given
        String ticketId = "-5";
        int operatorId = 1;

        // When
        ExitResult result = parkingController.previewExitById(ticketId, operatorId);

        // Then
        assertFalse(result.isSuccess());
        assertEquals(ExitResult.ErrorType.VALIDATION, result.getErrorType());
        assertEquals("Ticket ID must be greater than 0", result.getErrorMessage());
        verifyNoInteractions(parkingService);
    }

    @Test
    void testPreviewExitById_InvalidOperatorId_ValidationError() {
        // Given
        String ticketId = "123";
        int operatorId = 0;

        // When
        ExitResult result = parkingController.previewExitById(ticketId, operatorId);

        // Then
        assertFalse(result.isSuccess());
        assertEquals(ExitResult.ErrorType.VALIDATION, result.getErrorType());
        assertEquals("Invalid operator ID", result.getErrorMessage());
        verifyNoInteractions(parkingService);
    }

    @Test
    void testPreviewExitById_TicketNotFound_NotFoundError() throws Exception {
        // Given
        String ticketId = "999";
        int operatorId = 1;
        
        when(parkingService.previewExitById(999, operatorId))
            .thenThrow(new NotFoundException("No se encontró el ticket"));

        // When
        ExitResult result = parkingController.previewExitById(ticketId, operatorId);

        // Then
        assertFalse(result.isSuccess());
        assertEquals(ExitResult.ErrorType.NOT_FOUND, result.getErrorType());
        assertEquals("No se encontró el ticket", result.getErrorMessage());
    }

    @Test
    void testPreviewExitById_TicketClosed_BusinessError() throws Exception {
        // Given
        String ticketId = "123";
        int operatorId = 1;
        
        when(parkingService.previewExitById(123, operatorId))
            .thenThrow(new BusinessException("El ticket ya está cerrado"));

        // When
        ExitResult result = parkingController.previewExitById(ticketId, operatorId);

        // Then
        assertFalse(result.isSuccess());
        assertEquals(ExitResult.ErrorType.BUSINESS, result.getErrorType());
        assertEquals("El ticket ya está cerrado", result.getErrorMessage());
    }

    // ========== Preview Exit By Plate Tests ==========

    @Test
    void testPreviewExitByPlate_ValidInput_Success() throws Exception {
        // Given
        String licensePlate = "abc123";
        int operatorId = 1;
        
        ParkingService.ExitResult mockExitResult = mock(ParkingService.ExitResult.class);
        when(parkingService.previewExitByPlate("ABC123", operatorId)).thenReturn(mockExitResult);

        // When
        ExitResult result = parkingController.previewExitByPlate(licensePlate, operatorId);

        // Then
        assertTrue(result.isSuccess());
        assertEquals(mockExitResult, result.getExitResult());
        verify(parkingService).previewExitByPlate("ABC123", operatorId);
    }

    @Test
    void testPreviewExitByPlate_EmptyPlate_ValidationError() {
        // Given
        String licensePlate = "";
        int operatorId = 1;

        // When
        ExitResult result = parkingController.previewExitByPlate(licensePlate, operatorId);

        // Then
        assertFalse(result.isSuccess());
        assertEquals(ExitResult.ErrorType.VALIDATION, result.getErrorType());
        assertEquals("License plate cannot be empty", result.getErrorMessage());
        verifyNoInteractions(parkingService);
    }

    @Test
    void testPreviewExitByPlate_InvalidPlateLength_ValidationError() {
        // Given
        String licensePlate = "ABC12";
        int operatorId = 1;

        // When
        ExitResult result = parkingController.previewExitByPlate(licensePlate, operatorId);

        // Then
        assertFalse(result.isSuccess());
        assertEquals(ExitResult.ErrorType.VALIDATION, result.getErrorType());
        assertEquals("License plate must be 6 characters", result.getErrorMessage());
        verifyNoInteractions(parkingService);
    }

    @Test
    void testPreviewExitByPlate_PlateNotFound_NotFoundError() throws Exception {
        // Given
        String licensePlate = "XYZ999";
        int operatorId = 1;
        
        when(parkingService.previewExitByPlate("XYZ999", operatorId))
            .thenThrow(new NotFoundException("No se encontró un ticket abierto para esta placa"));

        // When
        ExitResult result = parkingController.previewExitByPlate(licensePlate, operatorId);

        // Then
        assertFalse(result.isSuccess());
        assertEquals(ExitResult.ErrorType.NOT_FOUND, result.getErrorType());
    }

    // ========== Process Exit With Payment Tests ==========

    @Test
    void testProcessExitWithPayment_ValidInput_Success() throws Exception {
        // Given
        int ticketId = 123;
        int operatorId = 1;
        String paymentMethod = "Cash";
        
        ParkingService.ExitResult mockExitResult = mock(ParkingService.ExitResult.class);
        when(parkingService.processExitWithPayment(ticketId, operatorId, paymentMethod))
            .thenReturn(mockExitResult);

        // When
        ExitResult result = parkingController.processExitWithPayment(ticketId, operatorId, paymentMethod);

        // Then
        assertTrue(result.isSuccess());
        assertEquals(mockExitResult, result.getExitResult());
    }

    @Test
    void testProcessExitWithPayment_InvalidTicketId_ValidationError() {
        // Given
        int ticketId = -1;
        int operatorId = 1;
        String paymentMethod = "Cash";

        // When
        ExitResult result = parkingController.processExitWithPayment(ticketId, operatorId, paymentMethod);

        // Then
        assertFalse(result.isSuccess());
        assertEquals(ExitResult.ErrorType.VALIDATION, result.getErrorType());
        verifyNoInteractions(parkingService);
    }

    @Test
    void testProcessExitWithPayment_EmptyPaymentMethod_ValidationError() {
        // Given
        int ticketId = 123;
        int operatorId = 1;
        String paymentMethod = "";

        // When
        ExitResult result = parkingController.processExitWithPayment(ticketId, operatorId, paymentMethod);

        // Then
        assertFalse(result.isSuccess());
        assertEquals(ExitResult.ErrorType.VALIDATION, result.getErrorType());
        assertEquals("Payment method cannot be empty", result.getErrorMessage());
        verifyNoInteractions(parkingService);
    }

    // ========== Process Exit By Plate Tests ==========

    @Test
    void testProcessExitByPlate_ValidInput_Success() throws Exception {
        // Given
        String licensePlate = "ABC123";
        int operatorId = 1;
        String paymentMethod = "Card";
        
        ParkingService.ExitResult mockExitResult = mock(ParkingService.ExitResult.class);
        when(parkingService.processExitByPlate(licensePlate, operatorId, paymentMethod))
            .thenReturn(mockExitResult);

        // When
        ExitResult result = parkingController.processExitByPlate(licensePlate, operatorId, paymentMethod);

        // Then
        assertTrue(result.isSuccess());
        assertEquals(mockExitResult, result.getExitResult());
    }

    @Test
    void testProcessExitByPlate_DatabaseError_DataAccessError() throws Exception {
        // Given
        String licensePlate = "ABC123";
        int operatorId = 1;
        String paymentMethod = "Cash";
        
        when(parkingService.processExitByPlate(licensePlate, operatorId, paymentMethod))
            .thenThrow(new DataAccessException("Database connection error"));

        // When
        ExitResult result = parkingController.processExitByPlate(licensePlate, operatorId, paymentMethod);

        // Then
        assertFalse(result.isSuccess());
        assertEquals(ExitResult.ErrorType.DATA_ACCESS, result.getErrorType());
        assertTrue(result.getErrorMessage().contains("Database connection error"));
    }
}
