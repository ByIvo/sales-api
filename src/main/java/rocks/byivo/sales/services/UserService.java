/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.byivo.sales.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rocks.byivo.sales.dao.UserDAO;
import rocks.byivo.sales.dao.UserItemDAO;
import rocks.byivo.sales.exceptions.ConstraintViolation;
import rocks.byivo.sales.exceptions.DuplicatedRecord;
import rocks.byivo.sales.model.Item;
import rocks.byivo.sales.model.User;
import rocks.byivo.sales.model.UserItem;

/**
 *
 * @author byivo
 */
@Service
public class UserService {

    @Autowired
    private UserDAO userDao;

    @Autowired
    private UserItemDAO userItemDao;

    @Autowired
    private LoggedUserService loggedUserService;

    public ResponseEntity<?> create(User user) {
        boolean valid = user.validContent();

        if (valid) {
            boolean isPasswordEncrypted = user.encryptPassword();

            if (!isPasswordEncrypted) {
                return new ResponseEntity<>(user.getInvalidMessages(true), HttpStatus.UNPROCESSABLE_ENTITY);
            }

            try {
                User created = userDao.insert(user);
                return new ResponseEntity<>(created, HttpStatus.CREATED);
            } catch (DuplicatedRecord ex) {
                user.putInvalidMessage("email", ex.getErrorMessage());
                return new ResponseEntity<>(user.getInvalidMessages(true), HttpStatus.UNPROCESSABLE_ENTITY);
            }

        }

        Map<String, String> invalidMessages = user.getInvalidMessages(true);
        return new ResponseEntity<>(invalidMessages, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public ResponseEntity<List<UserItem>> getUserCart(User user) {
        List<UserItem> results = userItemDao.getUserCart(user);

        if (results != null) {
            return new ResponseEntity<>(results, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> addToCart(User user, Item item, Integer quantity) {
        UserItem userItem = new UserItem(user, item, quantity);

        Map<String, String> errors = new HashMap<>();
        try {
            this.userItemDao.insert(userItem);
            List<UserItem> cart = this.userItemDao.getUserCart(user);

            return new ResponseEntity(cart, HttpStatus.CREATED);
        } catch (DuplicatedRecord ex) {
            errors.put("error", ex.getErrorMessage());
            return new ResponseEntity(errors, HttpStatus.CONFLICT);
        } catch (ConstraintViolation ex) {
            errors.put("error", ex.getErrorMessage());
            return new ResponseEntity(errors, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> sumToCart(User user, Item item, Integer quantity) {
        UserItem userItem = this.userItemDao.findUserItem(user, item);

        if (userItem == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        userItem.sumQuantity(quantity);

        try {
            this.userItemDao.updateUnsafe(userItem);

            return new ResponseEntity(userItem, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> removeFromCart(User user, Item item) {
        UserItem userItem = this.userItemDao.findUserItem(user, item);

        if (userItem == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        try {
            this.userItemDao.delete(userItem);

            return new ResponseEntity(userItem, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> clearCart(User user) {
        if (user == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        try {
            user = userItemDao.clearCartUnsafe(user);
            return new ResponseEntity(user, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
