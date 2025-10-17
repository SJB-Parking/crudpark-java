package app.service;

import app.dao.OperatorDAO;
import app.exception.AuthenticationException;
import app.exception.DataAccessException;
import app.model.Operator;
import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Authentication service
 */
public class AuthService {
    private final OperatorDAO operatorDAO;

    public AuthService(OperatorDAO operatorDAO) {
        this.operatorDAO = operatorDAO;
    }

    /**
     * Login operator by email and password
     */
    public Operator login(String email, String password) 
            throws AuthenticationException, DataAccessException {
        
        Operator operator = operatorDAO.findByEmail(email);
        
        if (operator == null) {
            throw new AuthenticationException("Invalid email or password");
        }
        
        if (!operator.isActive()) {
            throw new AuthenticationException("Operator account is inactive");
        }
        
        BCrypt.Result result = BCrypt.verifyer().verify(
            password.toCharArray(), 
            operator.getPasswordHash()
        );
        
        if (!result.verified) {
            throw new AuthenticationException("Invalid email or password");
        }
        
        return operator;
    }

    /**
     * Hash password using BCrypt with cost factor 11
     * Cost factor 11 = 2^11 = 2,048 rounds
     * 
     * @param password Plain text password
     * @return BCrypt hash with cost factor 11
     */
    public static String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(11, password.toCharArray());
    }
}
