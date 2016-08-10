/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.byivo.sales.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import rocks.byivo.sales.exceptions.ConstraintViolation;
import rocks.byivo.sales.exceptions.DuplicatedRecord;
import rocks.byivo.sales.model.Item;
import rocks.byivo.sales.model.User;
import rocks.byivo.sales.model.UserItem;
import rocks.byivo.sales.services.UserService;
import rocks.byivo.sales.util.MySQLErrorCode;

/**
 *
 * @author byivo
 */
@Repository
public class UserItemDAO extends GenericDAO<UserItem> {

    private static final String LIST_CART_STATEMENT = "SELECT * FROM %1$s %1$s INNER JOIN users users ON %1$s.user_id = users.id INNER JOIN items items ON %1$s.item_id = items.id WHERE %1$s.user_id = ?";
    private static final String FIND_USER_ITEM_STATEMENT = "SELECT * FROM %1$s %1$s INNER JOIN users users ON %1$s.user_id = users.id INNER JOIN items items ON %1$s.item_id = items.id WHERE %1$s.user_id = ? AND %1$s.item_id = ?";
    private static final String UPDATE_ITEM_CART_STATEMENT = "UPDATE %1$s %1$s SET %2$s WHERE %1$s.user_id = ? AND %1$s.item_id = ?";
    
    private static final String DELETE_ITEM_CART_STATEMENT = "DELETE FROM %1$s WHERE user_id = ? AND item_id = ?";

    @Autowired
    private ItemDAO itemDao;

    @Autowired
    private UserDAO userDao;

    @Autowired
    private ConnectionManager connectionManager;

    public UserItemDAO() {
        super("user_items");
    }

    @Override
    public UserItem insert(UserItem value) throws DuplicatedRecord {
        try {
            this.insertUnsafe(value);
        } catch (SQLIntegrityConstraintViolationException ex) {
            switch (ex.getErrorCode()) {
                case MySQLErrorCode.ER_DUP_ENTRY:
                    throw new DuplicatedRecord(MySQLErrorCode.errorMessages.get(MySQLErrorCode.ER_DUP_ENTRY));

                case MySQLErrorCode.CONSTRAINT_VIOLATED:
                    String message = String.format(MySQLErrorCode.errorMessages.get(MySQLErrorCode.CONSTRAINT_VIOLATED), "Item");
                    throw new ConstraintViolation(message);
            }
        } catch (Exception ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public List<UserItem> getUserCart(final User user) {
        final List<UserItem> results = new ArrayList<>();

        String queryStatemente = String.format(LIST_CART_STATEMENT, this.getTableName());
        this.executeQuery(queryStatemente, new QueryAdapter() {

            @Override
            public void setupStatement(PreparedStatement statement) throws SQLException {
                statement.setInt(1, user.getId());
            }

            @Override
            public void retrieveResults(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    UserItem newObject = UserItemDAO.this.getEntityFromResultSet(resultSet);
                    results.add(newObject);
                }
            }
        });

        return results;
    }

    public UserItem findUserItem(final User user, final Item item) {
        this.container = null;

        String queryStatemente = String.format(FIND_USER_ITEM_STATEMENT, this.getTableName());
        this.executeQuery(queryStatemente, new QueryAdapter() {

            @Override
            public void setupStatement(PreparedStatement statement) throws SQLException {
                statement.setInt(1, user.getId());
                statement.setInt(2, item.getId());
            }

            @Override
            public void retrieveResults(ResultSet resultSet) throws SQLException {
                if (resultSet.next()) {
                    UserItemDAO.this.container = UserItemDAO.this.getEntityFromResultSet(resultSet);
                }
            }
        });

        return container;
    }

    @Override
    public UserItem updateUnsafe(final UserItem value) throws SQLException, Exception {
        return this.executeUpdateUnsafe(value, new UpdateAdapter<UserItem>() {

            @Override
            public PreparedStatement prepareStatement(UserItem value, Connection connection, Map<String, Object> columns) throws SQLException, Exception {
               return UserItemDAO.this.prepareUpdateStatement(value, UPDATE_ITEM_CART_STATEMENT, connection, columns);
            }
            
            @Override
            public void setupStatement(PreparedStatement statement) throws SQLException {
                statement.setInt(4, value.getUser().getId());
                statement.setInt(5, value.getItem().getId());
            }

            @Override
            public void handleGeneratedKey(UserItem insertedValue, int key) throws SQLException {

            }
        });
    }

    @Override
    public UserItem deleteUnsafe(final UserItem value) throws SQLException, Exception {
       return this.executeUpdateUnsafe(value, new UpdateAdapter<UserItem>() {

             @Override
            public PreparedStatement prepareStatement(UserItem value, Connection connection, Map<String, Object> columns) throws SQLException {
                String deleteStatement = String.format(DELETE_ITEM_CART_STATEMENT, UserItemDAO.this.getTableName());

                return UserItemDAO.this.prepareStatement(deleteStatement, connection);
            }

            @Override
            public void setupStatement(PreparedStatement statement) throws SQLException {
                statement.setInt(1, value.getUser().getId());
                statement.setInt(2, value.getItem().getId());
            }

            @Override
            public void handleGeneratedKey(UserItem insertedValue, int key) throws SQLException {
                
            }
        });
    }
    
    

    @Override
    public void getColumnValues(UserItem values, Map<String, Object> map) {
        map.put("user_id", values.getUser().getId());
        map.put("item_id", values.getItem().getId());
        map.put("quantity", values.getQuantity());
    }

    @Override
    public UserItem getEntityFromResultSet(ResultSet resultSet) throws SQLException {
        int quantity = resultSet.getInt("quantity");
        int userId = resultSet.getInt("user_id");
        int itemId = resultSet.getInt("item_id");

        User user = this.userDao.getEntityFromResultSet(resultSet);
        Item item = this.itemDao.getEntityFromResultSet(resultSet);

        user.setId(userId);
        item.setId(itemId);

        UserItem userItem = new UserItem();
        userItem.setUser(user);
        userItem.setItem(item);
        userItem.sumQuantity(quantity);

        return userItem;
    }

    @Override
    protected ConnectionManager getConnectionManager() {
        return this.connectionManager;
    }

}
