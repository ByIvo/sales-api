/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.byivo.sales.services;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rocks.byivo.sales.dao.ItemDAO;
import rocks.byivo.sales.model.Item;

/**
 *
 * @author byivo
 */
@Service
public class ItemService {

    @Autowired
    private ItemDAO itemDao;

    public ResponseEntity<List<Item>> list() {
        List<Item> results = itemDao.list();

        if (results != null) {
            return new ResponseEntity<>(results, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Item> findById(Integer id) {
        Item item = itemDao.findById(id);

        if (item == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    public ResponseEntity<?> create(Item item) {
        boolean valid = item.validContent();

        if (valid) {
            Item created = itemDao.insert(item);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        }

        Map<String, String> invalidMessages = item.getInvalidMessages(true);
        return new ResponseEntity<>(invalidMessages, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public ResponseEntity<?> update(Integer id, Item newValues) {
        Item itemToSet = itemDao.findById(id);

        if (itemToSet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Map<String, String> invalidUpdateMEssages = itemToSet.update(newValues);

        if (invalidUpdateMEssages.isEmpty()) {
            Item updated = itemDao.update(itemToSet);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(invalidUpdateMEssages, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
