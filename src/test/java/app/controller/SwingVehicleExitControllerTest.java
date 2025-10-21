package app.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SwingVehicleExitControllerTest {

    @Mock
    private ParkingController parkingController;

    private SwingVehicleExitController swingController;
    private final int operatorId = 1;

    @BeforeEach
    void setUp() {
        swingController = new SwingVehicleExitController(parkingController, operatorId);
    }

    @Test
    void testGetOperatorId() {
        // When
        int result = swingController.getOperatorId();

        // Then
        assertEquals(operatorId, result);
    }

    @Test
    void testProcessExit_Success() {
        // Given
        String ticketId = "123";
        ExitResult expectedResult = ExitResult.success(mock(app.service.ParkingService.ExitResult.class));
        when(parkingController.processExit(ticketId, operatorId)).thenReturn(expectedResult);

        // When
        ExitResult result = swingController.processExit(ticketId);

        // Then
        assertTrue(result.isSuccess());
        verify(parkingController).processExit(ticketId, operatorId);
    }

    @Test
    void testProcessExit_Failure() {
        // Given
        String ticketId = "999";
        ExitResult expectedResult = ExitResult.notFoundError("Ticket not found");
        when(parkingController.processExit(ticketId, operatorId)).thenReturn(expectedResult);

        // When
        ExitResult result = swingController.processExit(ticketId);

        // Then
        assertFalse(result.isSuccess());
        assertEquals(ExitResult.ErrorType.NOT_FOUND, result.getErrorType());
        assertEquals("Ticket not found", result.getErrorMessage());
    }

    @Test
    void testPreviewExitById_Success() {
        // Given
        String ticketId = "123";
        ExitResult expectedResult = ExitResult.success(mock(app.service.ParkingService.ExitResult.class));
        when(parkingController.previewExitById(ticketId, operatorId)).thenReturn(expectedResult);

        // When
        ExitResult result = swingController.previewExitById(ticketId);

        // Then
        assertTrue(result.isSuccess());
        verify(parkingController).previewExitById(ticketId, operatorId);
    }

    @Test
    void testPreviewExitById_Exception() {
        // Given
        String ticketId = "123";
        when(parkingController.previewExitById(ticketId, operatorId))
            .thenThrow(new RuntimeException("Database error"));

        // When
        ExitResult result = swingController.previewExitById(ticketId);

        // Then
        assertFalse(result.isSuccess());
        assertEquals(ExitResult.ErrorType.DATA_ACCESS, result.getErrorType());
        assertTrue(result.getErrorMessage().contains("Error al buscar ticket por ID"));
    }

    @Test
    void testPreviewExitByPlate_Success() {
        // Given
        String licensePlate = "ABC123";
        ExitResult expectedResult = ExitResult.success(mock(app.service.ParkingService.ExitResult.class));
        when(parkingController.previewExitByPlate(licensePlate, operatorId)).thenReturn(expectedResult);

        // When
        ExitResult result = swingController.previewExitByPlate(licensePlate);

        // Then
        assertTrue(result.isSuccess());
        verify(parkingController).previewExitByPlate(licensePlate, operatorId);
    }

    @Test
    void testPreviewExitByPlate_Exception() {
        // Given
        String licensePlate = "ABC123";
        when(parkingController.previewExitByPlate(licensePlate, operatorId))
            .thenThrow(new RuntimeException("Database error"));

        // When
        ExitResult result = swingController.previewExitByPlate(licensePlate);

        // Then
        assertFalse(result.isSuccess());
        assertEquals(ExitResult.ErrorType.DATA_ACCESS, result.getErrorType());
        assertTrue(result.getErrorMessage().contains("Error al buscar ticket por placa"));
    }

    @Test
    void testProcessExitWithPayment_Success() {
        // Given
        int ticketId = 123;
        String paymentMethod = "Cash";
        ExitResult expectedResult = ExitResult.success(mock(app.service.ParkingService.ExitResult.class));
        when(parkingController.processExitWithPayment(ticketId, operatorId, paymentMethod))
            .thenReturn(expectedResult);

        // When
        ExitResult result = swingController.processExitWithPayment(ticketId, paymentMethod);

        // Then
        assertTrue(result.isSuccess());
        verify(parkingController).processExitWithPayment(ticketId, operatorId, paymentMethod);
    }

    @Test
    void testProcessExitWithPayment_CardPayment() {
        // Given
        int ticketId = 123;
        String paymentMethod = "Card";
        ExitResult expectedResult = ExitResult.success(mock(app.service.ParkingService.ExitResult.class));
        when(parkingController.processExitWithPayment(ticketId, operatorId, paymentMethod))
            .thenReturn(expectedResult);

        // When
        ExitResult result = swingController.processExitWithPayment(ticketId, paymentMethod);

        // Then
        assertTrue(result.isSuccess());
        verify(parkingController).processExitWithPayment(ticketId, operatorId, "Card");
    }

    @Test
    void testProcessExitByPlateWithPayment_Success() {
        // Given
        String licensePlate = "ABC123";
        String paymentMethod = "Cash";
        ExitResult expectedResult = ExitResult.success(mock(app.service.ParkingService.ExitResult.class));
        when(parkingController.processExitByPlate(licensePlate, operatorId, paymentMethod))
            .thenReturn(expectedResult);

        // When
        ExitResult result = swingController.processExitByPlateWithPayment(licensePlate, paymentMethod);

        // Then
        assertTrue(result.isSuccess());
        verify(parkingController).processExitByPlate(licensePlate, operatorId, paymentMethod);
    }

    @Test
    void testProcessExitByPlateWithPayment_Failure() {
        // Given
        String licensePlate = "XYZ999";
        String paymentMethod = "Cash";
        ExitResult expectedResult = ExitResult.notFoundError("No se encontró un ticket abierto");
        when(parkingController.processExitByPlate(licensePlate, operatorId, paymentMethod))
            .thenReturn(expectedResult);

        // When
        ExitResult result = swingController.processExitByPlateWithPayment(licensePlate, paymentMethod);

        // Then
        assertFalse(result.isSuccess());
        assertEquals(ExitResult.ErrorType.NOT_FOUND, result.getErrorType());
    }
}
