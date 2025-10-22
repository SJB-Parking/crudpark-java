package app.service;

import app.dao.OperatorDAO;
import app.exception.AuthenticationException;
import app.exception.DataAccessException;
import app.model.Operator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AuthService
 */
@DisplayName("AuthService Unit Tests")
class AuthServiceTest {
    
    @Mock
    private OperatorDAO operatorDAO;
    
    private AuthService authService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService(operatorDAO);
    }
    
    @Test
    @DisplayName("Should successfully login with correct credentials")
    void testLoginSuccess() throws AuthenticationException, DataAccessException {
        // Arrange
        String email = "admin@parking.com";
        String password = "admin123";
        Operator operator = new Operator();
        operator.setId(1);
        operator.setFullName("Admin User");
        operator.setEmail(email);
        operator.setPasswordHash("$2a$11$kH3ulu5AEQGioyzXDx.pg.4JDqE9/mACqZbtdymRdAm.zgUN2rX7.");
        operator.setActive(true);
        
        when(operatorDAO.findByEmail(email)).thenReturn(operator);
        
        // Act
        Operator result = authService.login(email, password);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Admin User", result.getFullName());
        verify(operatorDAO, times(1)).findByEmail(email);
    }
    
    @Test
    @DisplayName("Should fail login with incorrect password")
    void testLoginIncorrectPassword() throws DataAccessException {
        // Arrange
        String email = "admin@parking.com";
        String wrongPassword = "wrongpassword";
        Operator operator = new Operator();
        operator.setId(1);
        operator.setEmail(email);
        operator.setPasswordHash("$2a$11$kH3ulu5AEQGioyzXDx.pg.4JDqE9/mACqZbtdymRdAm.zgUN2rX7.");
        operator.setActive(true);
        
        when(operatorDAO.findByEmail(email)).thenReturn(operator);
        
        // Act & Assert
        assertThrows(AuthenticationException.class, () -> {
            authService.login(email, wrongPassword);
        });
        verify(operatorDAO, times(1)).findByEmail(email);
    }
    
    @Test
    @DisplayName("Should fail login with non-existent operator")
    void testLoginNonExistentOperator() throws DataAccessException {
        // Arrange
        String email = "nonexistent@parking.com";
        when(operatorDAO.findByEmail(email)).thenReturn(null);
        
        // Act & Assert
        assertThrows(AuthenticationException.class, () -> {
            authService.login(email, "anypassword");
        });
        verify(operatorDAO, times(1)).findByEmail(email);
    }
    
    @Test
    @DisplayName("Should fail login with inactive operator")
    void testLoginInactiveOperator() throws DataAccessException {
        // Arrange
        String email = "inactive@parking.com";
        Operator operator = new Operator();
        operator.setId(2);
        operator.setEmail(email);
        operator.setActive(false);
        operator.setPasswordHash("$2a$11$kH3ulu5AEQGioyzXDx.pg.4JDqE9/mACqZbtdymRdAm.zgUN2rX7.");
        
        when(operatorDAO.findByEmail(email)).thenReturn(operator);
        
        // Act & Assert
        assertThrows(AuthenticationException.class, () -> {
            authService.login(email, "admin123");
        });
        verify(operatorDAO, times(1)).findByEmail(email);
    }
    
    @Test
    @DisplayName("Should hash password correctly")
    void testPasswordHashing() {
        // Arrange
        String password = "testpassword123";
        
        // Act
        String hash = AuthService.hashPassword(password);
        
        // Assert
        assertNotNull(hash);
        assertTrue(hash.startsWith("$2a$11$"));
        assertNotEquals(password, hash);
    }
    
    @Test
    @DisplayName("Should fail login with empty password")
    void testLoginEmptyPassword() throws DataAccessException {
        // Arrange
        String email = "admin@parking.com";
        Operator operator = new Operator();
        operator.setId(1);
        operator.setEmail(email);
        operator.setPasswordHash("$2a$11$kH3ulu5AEQGioyzXDx.pg.4JDqE9/mACqZbtdymRdAm.zgUN2rX7.");
        operator.setActive(true);
        
        when(operatorDAO.findByEmail(email)).thenReturn(operator);
        
        // Act & Assert
        assertThrows(AuthenticationException.class, () -> {
            authService.login(email, "");
        });
    }
}
