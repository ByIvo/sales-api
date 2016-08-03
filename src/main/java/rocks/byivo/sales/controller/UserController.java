/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.byivo.sales.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rocks.byivo.sales.model.Item;
import rocks.byivo.sales.model.User;
import rocks.byivo.sales.model.UserItem;

/**
 *
 * @author byivo
 */
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private static final List<User> users;
    private static final List<Item> items;
    private static int id = 0;

    static {
        users = new ArrayList<>();
        items = new ArrayList<>();

        Item item = new Item();

        item.setName("Smartphone");
        item.setId(1);
        item.setPrice(20.0);

        items.add(item);

        item = new Item();

        item.setName("Computador");
        item.setId(2);
        item.setPrice(220.0);
        items.add(item);

        User user = new User();

        user.setName("Ivo");
        user.setId(++id);
        user.setEmail("irbatistela@gmail.com");
        user.setPassword("ai");
        user.setConfirmPassword("ai");
        users.add(user);

        user = new User();

        user.setName("Ricardo");
        user.setId(++id);
        user.setEmail("ricardo@test.com");
        user.setPassword("ai");
        user.setConfirmPassword("ai");
        users.add(user);

        user = new User();

        user.setName("user");
        user.setId(++id);
        user.setEmail("user@test.com");
        user.setPassword("ai");
        user.setConfirmPassword("ai");
        users.add(user);
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody User user) {
        boolean valid = user.validContent();

        if (valid) {
            user.setId(++id);
            users.add(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } else {
            Map<String, String> invalidMessages = user.getInvalidMessages(true);
            return new ResponseEntity<>(invalidMessages, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @RequestMapping(value = {"/{id}"}, method = RequestMethod.PUT)
    public ResponseEntity<?> update(@PathVariable(value = "id") Integer id, @RequestBody User newValues) {
        User usertoSet = new User(id);
        int i = users.indexOf(usertoSet);

        if (i == -1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        usertoSet = users.get(i);

        Map<String, String> invalidUpdateMEssages = usertoSet.update(newValues);

        if (invalidUpdateMEssages.isEmpty()) {
            return new ResponseEntity<>(usertoSet, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(invalidUpdateMEssages, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public ResponseEntity<List<User>> list() {
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @RequestMapping(value = {"/{id}"}, method = RequestMethod.GET)
    public ResponseEntity<User> findById(@PathVariable(value = "id") Integer id) {
        User usertoSet = new User(id);
        int i = users.indexOf(usertoSet);

        if (i == -1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(users.get(i), HttpStatus.OK);
    }

    @RequestMapping(value = {"/{id}"}, method = RequestMethod.DELETE)
    public ResponseEntity<User> remove(@PathVariable(value = "id") Integer id) {
        User usertoSet = new User(id);
        int i = users.indexOf(usertoSet);

        if (i == -1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(users.remove(i), HttpStatus.OK);
    }

    @RequestMapping(value = {"/{id}/cart"}, method = RequestMethod.GET)
    public ResponseEntity<?> listCart(@PathVariable(value = "id") Integer userId) {
        User usertoSet = new User(userId);
        int i = users.indexOf(usertoSet);

        if (i == -1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(users.get(i).getCart(), HttpStatus.OK);
    }

    @RequestMapping(value = {"/{user_id}/cart/{item_id}/{quantity}"}, method = RequestMethod.POST)
    public ResponseEntity<List<UserItem>> addToCart(@PathVariable(value = "user_id") Integer userId, @PathVariable(value = "item_id") Integer itemId, @PathVariable(value = "quantity") Integer quantity) {
        User usertoSet = new User(userId);
        int i = users.indexOf(usertoSet);

        if (i == -1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        usertoSet = this.users.get(i);

        Item item = new Item(itemId);
        i = items.indexOf(item);

        if (i == -1) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        
        item = items.get(i);

        usertoSet.addToCart(item, quantity);
        return new ResponseEntity<>(usertoSet.getCart(), HttpStatus.OK);
    }

    @RequestMapping(value = {"/{user_id}/cart/{item_id}"}, method = RequestMethod.DELETE)
    public ResponseEntity<List<UserItem>> removeFromCart(@PathVariable(value = "user_id")Integer userId, @PathVariable(value = "item_id")Integer itemId) {
        User usertoSet = new User(userId);
        int i = users.indexOf(usertoSet);

        if (i == -1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        usertoSet = this.users.get(i);

        Item item = new Item(itemId);
        i = items.indexOf(item);

        if (i == -1) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        item = items.get(i);

        UserItem userItem = new UserItem();
        userItem.setUser(usertoSet);
        userItem.setItem(item);

        if (usertoSet.getCart().contains(userItem)) {
            usertoSet.getCart().remove(userItem);
            return new ResponseEntity<>(usertoSet.getCart(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = {"/{user_id}/cart/{item_id}/{quantity}"}, method = RequestMethod.PUT)
    public ResponseEntity<List<UserItem>> sumInCart(@PathVariable(value = "user_id") Integer userId, @PathVariable(value = "item_id") Integer itemId, @PathVariable(value = "quantity") Integer quantity) {
        User usertoSet = new User(userId);
        int i = users.indexOf(usertoSet);

        if (i == -1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        usertoSet = this.users.get(i);

        Item item = new Item(itemId);
        i = items.indexOf(item);

        if (i == -1) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        item = items.get(i);

        UserItem userItem = new UserItem();
        userItem.setUser(usertoSet);
        userItem.setItem(item);

        if (usertoSet.getCart().contains(userItem)) {
            userItem = usertoSet.getCart().get(usertoSet.getCart().indexOf(userItem));
            userItem.sumQuantity(quantity);
            
            return new ResponseEntity<>(usertoSet.getCart(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
