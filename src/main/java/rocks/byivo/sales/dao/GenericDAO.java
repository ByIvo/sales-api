/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.byivo.sales.dao;

import rocks.byivo.sales.util.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import rocks.byivo.sales.model.Entity;

/**
 *
 * @author byivo
 */
public abstract class GenericDAO<T extends Entity> {

    private String tableName;

    private static final String INSERT_STATEMENT = "INSERT INTO %1$s (%2$s) VALUES (%3$s)";
    private static final String UPDATE_STATEMENT = "UPDATE %1$s SET %2$s WHERE id = %3$s";
    private static final String LIST_ALL_STATEMENT = "SELECT * FROM %1$s %1$s";
    private static final String FIND_BY_ID_STATEMENT = "SELECT * FROM %1$s %1$s WHERE %1$s.id = ?";
    private static final String DELETE_STATEMENT = "DELETE FROM %1$s WHERE id = ?";

    public GenericDAO(String tableName) {
        this.tableName = tableName;
    }

    protected T container;

    public synchronized T findById(final Integer id) {
        container = null;

        String queryStatemente = String.format(FIND_BY_ID_STATEMENT, tableName);
        this.executeQuery(queryStatemente, new QueryAdapter() {

            @Override
            public void setupStatement(PreparedStatement statement) throws SQLException {
                statement.setInt(1, id);
            }

            @Override
            public void retrieveResults(ResultSet resultSet) throws SQLException {
                if (resultSet.next()) {
                    container = GenericDAO.this.getEntityFromResultSet(resultSet);
                }
            }
        });

        return container;
    }

    public List<T> list() {
        try {
            return listUnsafe();
        } catch (SQLException ex) {
            Logger.getLogger(GenericDAO.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GenericDAO.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return new ArrayList();
    }

    public List<T> listUnsafe() throws SQLException {
        final List<T> results = new ArrayList<>();

        String queryStatemente = String.format(LIST_ALL_STATEMENT, tableName);
        this.executeQueryUnsafe(queryStatemente, new QueryAdapter() {

            @Override
            public void setupStatement(PreparedStatement statement) throws SQLException {

            }

            @Override
            public void retrieveResults(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    T newObject = GenericDAO.this.getEntityFromResultSet(resultSet);
                    results.add(newObject);
                }
            }
        });

        return results;
    }

    public T delete(T value) {
        try {
            this.deleteUnsafe(value);
        } catch (SQLException ex) {
            Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }

    public T deleteUnsafe(final T value) throws SQLException, Exception {

        return this.executeUpdateUnsafe(value, new UpdateAdapter<T>() {

            @Override
            public PreparedStatement prepareStatement(T value, Connection connection, Map<String, Object> columns) throws SQLException {
                String deleteStatement = String.format(DELETE_STATEMENT, GenericDAO.this.getTableName());

                return GenericDAO.this.prepareStatement(deleteStatement, connection);
            }

            @Override
            public void setupStatement(PreparedStatement statement) throws SQLException {
                statement.setInt(1, value.getId());
            }

            @Override
            public void handleGeneratedKey(T insertedValue, int key) throws SQLException {

            }
        });
    }

    public T update(T value) {
        try {
            value = this.updateUnsafe(value);
        } catch (SQLException ex) {
            Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }

    public T updateUnsafe(T value) throws SQLException, Exception {

        return this.executeUpdateUnsafe(value, new UpdateAdapter<T>() {

            @Override
            public PreparedStatement prepareStatement(T value, Connection connection, Map<String, Object> columns) throws SQLException, Exception {
                return GenericDAO.this.prepareUpdateStatement(value, connection, columns);
            }

            @Override
            public void setupStatement(PreparedStatement statement) throws SQLException {
            }

            @Override
            public void handleGeneratedKey(T insertedValue, int key) throws SQLException {

            }
        });
    }

    public T insert(T value) {
        try {
            value = insertUnsafe(value);
        } catch (SQLException ex) {
            Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return value;
    }

    public T insertUnsafe(final T value) throws SQLException, Exception {
        return this.executeUpdateUnsafe(value, new UpdateAdapter<T>() {

            @Override
            public PreparedStatement prepareStatement(T value, Connection connection, Map<String, Object> columns) throws SQLException, Exception {
                return GenericDAO.this.prepareInsertStatement(value, connection, columns);
            }

            @Override
            public void setupStatement(PreparedStatement statement) throws SQLException {
            }

            @Override
            public void handleGeneratedKey(T insertedValue, int key) throws SQLException {
                insertedValue.setId(key);
            }
        });
    }

    private int getGeneratedKey(PreparedStatement preparedStatement) throws Exception {
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }

        return -1;
    }

    private PreparedStatement prepareInsertStatement(T value, Connection connection, Map<String, Object> columns) throws SQLException, Exception {
        String columnNames = this.parseColumnNames(columns);
        String valueNames = this.createValueContainer(columns.size());

        String insertStatement = String.format(INSERT_STATEMENT, tableName, columnNames, valueNames);

        PreparedStatement preparedStatement = prepareStatement(insertStatement, connection);

        this.setStatementValues(columns, preparedStatement);

        return preparedStatement;
    }

    private PreparedStatement prepareUpdateStatement(T value, Connection connection, Map<String, Object> columns) throws SQLException, Exception {
        String columnNames = this.parseUpdateColumnNames(columns);

        String updateStatement = String.format(UPDATE_STATEMENT, tableName, columnNames, value.getId());

        PreparedStatement preparedStatement = prepareStatement(updateStatement, connection);

        this.setStatementValues(columns, preparedStatement);

        return preparedStatement;
    }

    public PreparedStatement prepareUpdateStatement(T value, String statement, Connection connection, Map<String, Object> columns) throws SQLException, Exception {
        String columnNames = this.parseUpdateColumnNames(columns);

        String updateStatement = String.format(statement, tableName, columnNames);

        PreparedStatement preparedStatement = prepareStatement(updateStatement, connection);

        this.setStatementValues(columns, preparedStatement);

        return preparedStatement;
    }

    public PreparedStatement prepareStatement(String statement, Connection connection) throws SQLException {
        return connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
    }

    private void setStatementValues(Map<String, Object> columns, PreparedStatement preparedStatement) throws Exception {
        int i = 1;
        for (String key : columns.keySet()) {
            Object value = columns.get(key);
            preparedStatement.setObject(i, value);
            i++;
        }
    }

    private String createValueContainer(int size) {
        StringBuilder stbReturn = new StringBuilder();
        for (int i = 0; i < size; i++) {
            stbReturn.append('?');
            if (i != size - 1) {
                stbReturn.append(',');
            }
        }

        return stbReturn.toString();
    }

    private String parseUpdateColumnNames(Map<String, Object> columns) {
        StringBuilder stbReturn = new StringBuilder();

        Iterator<String> itKey = columns.keySet().iterator();
        while (true) {
            String key = "";
            if (itKey.hasNext()) {
                key = itKey.next();
                if (stbReturn.length() != 0) {
                    stbReturn.append(",");
                }
            } else {
                break;
            }

            stbReturn.append(key);
            stbReturn.append('=');
            stbReturn.append('?');
        }

        return stbReturn.toString();
    }

    private String parseColumnNames(Map<String, Object> columns) {
        StringBuilder stbReturn = new StringBuilder();

        Iterator<String> itKey = columns.keySet().iterator();
        while (true) {
            String key = "";
            if (itKey.hasNext()) {
                key = itKey.next();
                if (stbReturn.length() != 0) {
                    stbReturn.append(",");
                }
            } else {
                break;
            }

            stbReturn.append(key);
        }

        return stbReturn.toString();
    }

    public T executeUpdate(T value, UpdateAdapter<T> updateAdapter) {

        try {
            value = this.executeUpdateUnsafe(value, updateAdapter);
        } catch (SQLException ex) {
            Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }

    public T executeUpdateUnsafe(T value, UpdateAdapter<T> updateAdapter) throws SQLException, Exception {
        Map<String, Object> columns = new HashMap<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            this.getColumnValues(value, columns);
            connection = this.getConnectionManager().getConnection();

            preparedStatement = updateAdapter.prepareStatement(value, connection, columns);
            updateAdapter.setupStatement(preparedStatement);

            boolean isObj = preparedStatement.execute();

            if (!isObj) {
                int newId = this.getGeneratedKey(preparedStatement);
                updateAdapter.handleGeneratedKey(value, newId);
            }
        } finally {

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException ex) {
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                }
            }
        }

        return value;
    }

    public void executeQuery(String queryStatemente, QueryAdapter queryAdapter) {
        try {
            this.executeQueryUnsafe(queryStatemente, queryAdapter);

        } catch (SQLException ex) {
            Logger.getLogger(GenericDAO.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GenericDAO.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void executeQueryUnsafe(String queryStatemente, QueryAdapter queryAdapter) throws SQLException {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;

        try {
            connection = this.getConnectionManager().getConnection();

            statement = connection.prepareStatement(queryStatemente);
            queryAdapter.setupStatement(statement);

            resultSet = statement.executeQuery();
            queryAdapter.retrieveResults(resultSet);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                }
            }
        }
    }

    public String getTableName() {
        return tableName;
    }

    public abstract void getColumnValues(T values, Map<String, Object> map);

    public abstract T getEntityFromResultSet(ResultSet resultSet) throws SQLException;

    protected abstract ConnectionManager
            getConnectionManager();

    protected interface QueryAdapter {

        void setupStatement(PreparedStatement statement) throws SQLException;

        void retrieveResults(ResultSet resultSet) throws SQLException;
    }

    protected interface UpdateAdapter<T> {

        PreparedStatement prepareStatement(T value, Connection connection, Map<String, Object> columns) throws SQLException, Exception;

        void setupStatement(PreparedStatement statement) throws SQLException;

        void handleGeneratedKey(T insertedValue, int key) throws SQLException;
    }

}
