/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.byivo.sales.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import rocks.byivo.sales.model.Sell;
import rocks.byivo.sales.model.User;

/**
 *
 * @author byivo
 */
@Repository
public class SellDAO extends GenericDAO<Sell> {

    private static final String LIST_ALL_STATEMENT = "SELECT * FROM %1$s %1$s INNER JOIN users users ON users.id = %1$s.user_id";

    @Autowired
    private ConnectionManager connectionManager;

    @Autowired
    private UserDAO userDao;

    public SellDAO() {
        super("sells");
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

        return sell;
    }

    @Override
    protected ConnectionManager getConnectionManager() {
        return this.connectionManager;
    }

}
