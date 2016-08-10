/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.byivo.sales.dao;

import rocks.byivo.sales.util.ConnectionManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import rocks.byivo.sales.exceptions.DuplicatedRecord;
import rocks.byivo.sales.model.ModelValidation;
import rocks.byivo.sales.model.User;
import rocks.byivo.sales.util.MySQLErrorCode;

/**
 *
 * @author byivo
 */
@Repository
public class UserDAO extends GenericDAO<User>{

    @Autowired
    private ConnectionManager connectionManager;

    public UserDAO() {
        super("users");
    }
    
    @Override
    public User insert(User value) throws DuplicatedRecord{
        try {
            value = insertUnsafe(value);
        }catch (SQLIntegrityConstraintViolationException ex) {
            switch (ex.getErrorCode()) {

                case MySQLErrorCode.ER_DUP_ENTRY:
                    String message = ModelValidation.User.DUPLICATED_EMAIL;
                    throw new DuplicatedRecord(message);
            }
        } catch (Exception ex) {
            Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return value;
    }
    
    @Override
    public void getColumnValues(User values, Map<String, Object> map) {
        map.put("id", values.getId());
        map.put("name", values.getName());
        map.put("email", values.getEmail());
        map.put("password", values.getSecuredPassword());
    }

    @Override
    public User getEntityFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        
        user.setId(resultSet.getInt("users.id"));
        user.setName(resultSet.getString("users.name"));
        user.setEmail(resultSet.getString("users.email"));
        
        return user;
    }

    @Override
    protected ConnectionManager getConnectionManager() {
        return this.connectionManager;
    }
    
}
