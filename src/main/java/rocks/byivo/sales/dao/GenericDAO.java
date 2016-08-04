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

    public GenericDAO(String tableName) {
        this.tableName = tableName;
    }

    public T findById(Integer id) {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        T entity = null;

        try {
            connection = this.getConnectionManager().getConnection();

            String queryStatemente = String.format(FIND_BY_ID_STATEMENT, tableName);

            statement = connection.prepareStatement(queryStatemente);
            statement.setInt(1, id);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                entity = this.getEntityFromResultSet(resultSet);
            }

        } catch (SQLException ex) {
            Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, null, ex);
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

        return entity;
    }

    public List<T> list() {
        Connection connection = null;
        ResultSet resultSet = null;
        Statement statement = null;
        List<T> results = new ArrayList<>();

        try {
            connection = this.getConnectionManager().getConnection();

            String queryStatemente = String.format(LIST_ALL_STATEMENT, tableName);

            statement = connection.createStatement();
            resultSet = statement.executeQuery(queryStatemente);

            while (resultSet.next()) {
                T newObject = this.getEntityFromResultSet(resultSet);
                results.add(newObject);
            }

        } catch (SQLException ex) {
            Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, null, ex);
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

        return results;
    }

    public T update(T value) {
        Map<String, Object> columns = new HashMap<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            this.getColumnValues(value, columns);
            connection = this.getConnectionManager().getConnection();

            preparedStatement = this.prepareUpdateStatement(value, connection, columns);
            this.setStatementValues(columns, preparedStatement);
            preparedStatement.execute();

        } catch (SQLException ex) {
            Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, null, ex);
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

    public T insert(T value) {
        Map<String, Object> columns = new HashMap<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            this.getColumnValues(value, columns);
            connection = this.getConnectionManager().getConnection();

            preparedStatement = this.prepareInsertStatement(value, connection, columns);
            this.setStatementValues(columns, preparedStatement);
            boolean isObj = preparedStatement.execute();

            if (!isObj) {
                int newId = this.getGeneratedKey(preparedStatement);
                value.setId(newId);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, null, ex);
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

    private int getGeneratedKey(PreparedStatement preparedStatement) throws Exception {
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }

        return -1;
    }

    private PreparedStatement prepareInsertStatement(T value, Connection connection, Map<String, Object> columns) throws SQLException {
        String columnNames = this.parseColumnNames(columns);
        String valueNames = this.createValueContainer(columns.size());

        String insertStatement = String.format(INSERT_STATEMENT, tableName, columnNames, valueNames);

        return prepareStatement(insertStatement, connection);
    }

    private PreparedStatement prepareUpdateStatement(T value, Connection connection, Map<String, Object> columns) throws SQLException {
        String columnNames = this.parseUpdateColumnNames(columns);

        String updateStatement = String.format(UPDATE_STATEMENT, tableName, columnNames, value.getId());

        return prepareStatement(updateStatement, connection);
    }

    private PreparedStatement prepareStatement(String statement, Connection connection) throws SQLException {
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

    public abstract void getColumnValues(T values, Map<String, Object> map);

    public abstract T getEntityFromResultSet(ResultSet resultSet) throws SQLException;

    protected abstract ConnectionManager getConnectionManager();

}
