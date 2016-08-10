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
import rocks.byivo.sales.model.Item;
import rocks.byivo.sales.model.Sell;
import rocks.byivo.sales.model.SellItem;

/**
 *
 * @author byivo
 */
@Repository
public class SellItemDAO extends GenericDAO<SellItem> {

    private static final String LIST_ALL_STATEMENT = "SELECT * FROM %1$s %1$s INNER JOIN items items ON items.id = %1$s.item_id INNER JOIN sells sells ON sells.id = %1$s.sell_id";

    @Autowired
    private ConnectionManager connectionManager;

    @Autowired
    private SellDAO sellDao;

    @Autowired
    private ItemDAO itemDao;

    public SellItemDAO() {
        super("sell_items");
    }

    @Override
    public List<SellItem> listUnsafe() throws SQLException {
        final List<SellItem> results = new ArrayList<>();

        String queryStatemente = String.format(LIST_ALL_STATEMENT, this.getTableName());
        this.executeQueryUnsafe(queryStatemente, new QueryAdapter() {

            @Override
            public void setupStatement(PreparedStatement statement) throws SQLException {

            }

            @Override
            public void retrieveResults(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    SellItem newObject = SellItemDAO.this.getEntityFromResultSet(resultSet);
                    results.add(newObject);
                }
            }
        });

        return results;
    }

    @Override
    public void getColumnValues(SellItem values, Map<String, Object> map) {
        map.put("id", values.getId());
        map.put("price", values.getPrice());
        map.put("quantity", values.getQuantity());
        map.put("item_id", values.getItem().getId());
        map.put("sell_id", values.getSell().getId());
    }

    @Override
    public SellItem getEntityFromResultSet(ResultSet resultSet) throws SQLException {
        SellItem sellItem = new SellItem();

        Sell sell = sellDao.getEntityFromResultSet(resultSet);
        Item item = itemDao.getEntityFromResultSet(resultSet);

        sellItem.setId(resultSet.getInt("sell_items.id"));
        sellItem.setQuantity(resultSet.getInt("sell_items.quantity"));
        sellItem.setPrice(resultSet.getDouble("sell_items.price"));
        sellItem.setItem(item);
        sellItem.setSell(sell);

        return sellItem;
    }

    @Override
    protected ConnectionManager getConnectionManager() {
        return this.connectionManager;
    }

}
