package app.dao;

import app.exception.DataAccessException;
import app.model.Operator;

/**
 * Interface for Operator Data Access Object
 */
public interface IOperatorDAO {
    
    /**
     * Find operator by email
     * @param email Operator email
     * @return Operator if found, null otherwise
     * @throws DataAccessException if database error occurs
     */
    Operator findByEmail(String email) throws DataAccessException;
    
    /**
     * Find operator by ID
     * @param id Operator ID
     * @return Operator if found, null otherwise
     * @throws DataAccessException if database error occurs
     */
    Operator findById(int id) throws DataAccessException;
}
