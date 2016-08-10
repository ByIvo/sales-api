/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.byivo.sales.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rocks.byivo.sales.dao.SellDAO;
import rocks.byivo.sales.dao.SellItemDAO;
import rocks.byivo.sales.dao.UserItemDAO;
import rocks.byivo.sales.model.Item;
import rocks.byivo.sales.model.Sell;
import rocks.byivo.sales.model.SellItem;
import rocks.byivo.sales.model.User;
import rocks.byivo.sales.model.UserItem;

/**
 *
 * @author byivo
 */
@Service
public class SellService {

    @Autowired
    private UserItemDAO userItemDao;

    @Autowired
    private SellDAO sellDao;

    @Autowired
    private SellItemDAO sellItemDao;

    @Autowired
    private MailService mailService;

    public ResponseEntity<?> listSells(User user) {
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            List<Sell> userSells = sellDao.listUserSells(user);
            return new ResponseEntity<>(userSells, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public synchronized ResponseEntity<?> finishSell(User user) {

        List<UserItem> userCart = userItemDao.getUserCart(user);
        user.setCart(userCart);

        if (userCart.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        Sell sell = this.prepareSell(user);

        return this.registerSell(sell, user);

    }

    private ResponseEntity<?> registerSell(Sell sell, User user) {
        try {
            sell = sellDao.insertUnsafe(sell);

            for (SellItem sellItem : sell.getSellItems()) {
                sellItemDao.insert(sellItem);
            }

            user.getCart().clear();
            userItemDao.clearCart(user);

            mailService.sendFinishSellEmail(sell);

            return new ResponseEntity(sell, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Sell prepareSell(User user) {
        Sell sell = new Sell();
        sell.setUser(user);
        sell.setDate(new Date());

        List<SellItem> sellItems = prepareItemSel(sell);

        sell.setSellItems(sellItems);

        return sell;
    }

    private List<SellItem> prepareItemSel(Sell sell) {
        List<SellItem> itemSells = new ArrayList<>();

        for (UserItem userItem : sell.getUser().getCart()) {
            SellItem sellItem = new SellItem();

            Item item = userItem.getItem();

            sellItem.setPrice(item.getPrice());
            sellItem.setQuantity(userItem.getQuantity());
            sellItem.setItem(item);
            sellItem.setSell(sell);

            itemSells.add(sellItem);
        }

        return itemSells;
    }
}
