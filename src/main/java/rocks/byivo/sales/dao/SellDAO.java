/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.byivo.sales.dao;

import rocks.byivo.sales.util.ConnectionManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import rocks.byivo.sales.model.Sell;
import rocks.byivo.sales.model.SellItem;
import rocks.byivo.sales.model.User;

/**
 *
 * @author byivo
 */
@Repository
public class SellDAO extends GenericDAO<Sell> {

    private static final String LIST_ALL_STATEMENT = "SELECT * FROM %1$s %1$s INNER JOIN users users ON users.id = %1$s.user_id";
    private static final String LIST_USER_SELLS = "SELECT * FROM %1$s %1$s INNER JOIN users users ON users.id = %1$s.user_id WHERE %1$s.user_id = ?";

    @Autowired
    private ConnectionManager connectionManager;

    @Autowired
    private UserDAO userDao;

    @Autowired
    private SellItemDAO sellItemDao;

    public SellDAO() {
        super("sells");
    }

    public List<Sell> listUserSells(final User user) throws SQLException {
        final List<Sell> results = new ArrayList<>();

        String queryStatemente = String.format(LIST_USER_SELLS, this.getTableName());
        this.executeQueryUnsafe(queryStatemente, new QueryAdapter() {

            @Override
            public void setupStatement(PreparedStatement statement) throws SQLException {
                statement.setInt(1, user.getId());
            }

            @Override
            public void retrieveResults(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    Sell newObject = SellDAO.this.getEntityFromResultSet(resultSet);
                    results.add(newObject);
                }
            }
        });

        return results;
    }

    @Override
    public List<Sell> listUnsafe() throws SQLException {
        final List<Sell> results = new ArrayList<>();

        String queryStatemente = String.format(LIST_ALL_STATEMENT, this.getTableName());
        this.executeQueryUnsafe(queryStatemente, new QueryAdapter() {

            @Override
            public void setupStatement(PreparedStatement statement) throws SQLException {

            }

            @Override
            public void retrieveResults(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    Sell newObject = SellDAO.this.getEntityFromResultSet(resultSet);
                    results.add(newObject);
                }
            }
        });

        return results;
    }

    @Override
    public void getColumnValues(Sell values, Map<String, Object> map) {
        map.put("id", values.getId());
        map.put("date", values.getDate());
        map.put("user_id", values.getUser().getId());
    }

    @Override
    public Sell getEntityFromResultSet(ResultSet resultSet) throws SQLException {
        Sell sell = new Sell();

        User user = userDao.getEntityFromResultSet(resultSet);

        sell.setId(resultSet.getInt("sells.id"));
        sell.setDate(resultSet.getDate("sells.date"));
        sell.setUser(user);
        
        try {
            List<SellItem> sellItems = sellItemDao.listSellItems(sell);
            sell.setSellItems(sellItems);
        } catch (Exception ex) {
        }

        return sell;
    }

    @Override
    protected ConnectionManager getConnectionManager() {
        return this.connectionManager;
    }

}
