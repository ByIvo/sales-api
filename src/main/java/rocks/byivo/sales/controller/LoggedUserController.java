/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.byivo.sales.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rocks.byivo.sales.model.Item;
import rocks.byivo.sales.model.User;
import rocks.byivo.sales.model.UserItem;
import rocks.byivo.sales.services.LoggedUserService;
import rocks.byivo.sales.services.UserService;

/**
 *
 * @author byivo
 */
@RestController
@RequestMapping(value = "/user")
public class LoggedUserController {

    @Autowired
    private LoggedUserService loggedUserService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public ResponseEntity<?> getLoggedUser() {
        User loggedUser = loggedUserService.getLoggedUser();
        return new ResponseEntity(loggedUser, HttpStatus.OK);
    }

    @RequestMapping(value = {"/cart"}, method = RequestMethod.GET)
    public ResponseEntity<List<UserItem>> list() {
        User loggedUser = loggedUserService.getLoggedUser();
        return userService.getUserCart(loggedUser);
    }

    @RequestMapping(value = {"/cart/{item_id}/add/{quantity}"}, method = RequestMethod.POST)
    public ResponseEntity<?> addToCart(@PathVariable("item_id") Integer itemId, @PathVariable("quantity") Integer quantity) {
        User loggedUser = this.loggedUserService.getLoggedUser();
        Item item = new Item(itemId);

        return userService.addToCart(loggedUser, item, quantity);
    }

    @RequestMapping(value = {"/cart/{item_id}/add/{quantity}"}, method = RequestMethod.PUT)
    public ResponseEntity<?> sumToCart(@PathVariable("item_id") Integer itemId, @PathVariable("quantity") Integer quantity) {
        User loggedUser = this.loggedUserService.getLoggedUser();
        Item item = new Item(itemId);

        return userService.sumToCart(loggedUser, item, quantity);
    }

    @RequestMapping(value = {"/cart/{item_id}"}, method = RequestMethod.DELETE)
    public ResponseEntity<?> removeFromCart(@PathVariable("item_id") Integer itemId) {
        User loggedUser = this.loggedUserService.getLoggedUser();
        Item item = new Item(itemId);

        return userService.removeFromCart(loggedUser, item);
    }

}
