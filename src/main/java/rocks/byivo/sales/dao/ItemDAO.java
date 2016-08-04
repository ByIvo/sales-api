/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.byivo.sales.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import rocks.byivo.sales.model.Item;

/**
 *
 * @author byivo
 */
@Repository
public class ItemDAO extends GenericDAO<Item> {

    @Autowired
    private ConnectionManager connectionManager;

    public ItemDAO() {
        super("items");
    }

    @Override
    public void getColumnValues(Item values, Map<String, Object> map) {
        map.put("id", values.getId());
        map.put("name", values.getName());
        map.put("image", values.getImage());
        map.put("price", values.getPrice());
    }

    @Override
    protected ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    @Override
    public Item getEntityFromResultSet(ResultSet resultSet) throws SQLException {
        Item item = new Item();

        item.setId(resultSet.getInt("id"));
        item.setName(resultSet.getString("name"));
        item.setPrice(resultSet.getDouble("price"));
        item.setImage(resultSet.getString("image"));

        return item;
    }
}
