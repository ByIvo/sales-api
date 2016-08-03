/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import org.junit.Test;
import static org.junit.Assert.*;
import rocks.byivo.sales.model.Item;
import rocks.byivo.sales.model.User;
import rocks.byivo.sales.model.UserItem;

/**
 *
 * @author byivo
 */
public class UserItemTest {

    public UserItemTest() {
    }

    @Test
    public void emptyCar() {
        User user = new User();

        assertTrue(user.getCart().isEmpty());
    }

    @Test
    public void fullCart() {
        User user = new User();

        Item item1 = new Item();
        item1.setId(1);

        Item item2 = new Item();
        item2.setId(2);

        user.addToCart(item1, 1);
        user.addToCart(item2, 1);

        assertEquals(2, user.getCart().size());
    }

    @Test
    public void fullCartDoubleAdding() {
        User user = new User();

        Item item1 = new Item();
        item1.setId(1);

        Item item2 = new Item();
        item2.setId(2);

        user.addToCart(item1, 1);
        user.addToCart(item2, 1);
        user.addToCart(item2, 1);

        assertEquals(2, user.getCart().size());
    }

    @Test
    public void addingItem() {
        User user = new User();

        Item item1 = new Item();
        item1.setId(1);

        user.addToCart(item1, 5);
        user.addToCart(item1, 5);

        assertEquals((Integer) 10, user.getCart().get(0).getQuantity());
    }

    @Test
    public void worngQuantity() {
        User user = new User();

        Item item1 = new Item();
        item1.setId(1);

        user.addToCart(item1, 0);

        assertEquals((Integer) 1, user.getCart().get(0).getQuantity());
    }

    @Test
    public void sumItem() {
        User user = new User();

        Item item1 = new Item();

        UserItem userItem = new UserItem();
        userItem.setItem(item1);
        userItem.setUser(user);

        assertEquals((Integer) 0, userItem.getQuantity());

        userItem.sumOne();
        assertEquals((Integer) 1, userItem.getQuantity());

        userItem.sumOne();
        userItem.sumOne();
        assertEquals((Integer) 3, userItem.getQuantity());
    }

    @Test
    public void decressItem() {
        User user = new User();

        Item item1 = new Item();

        UserItem userItem = new UserItem();
        userItem.setItem(item1);
        userItem.setUser(user);
        userItem.sumQuantity(2);

        assertEquals((Integer) 2, userItem.getQuantity());

        userItem.sumOne();
        assertEquals((Integer) 3, userItem.getQuantity());

        userItem.decressOne();
        userItem.decressOne();
        assertEquals((Integer) 1, userItem.getQuantity());

        userItem.decressOne();
        userItem.decressOne();
        userItem.decressOne();
        userItem.decressOne();
        assertEquals((Integer) 1, userItem.getQuantity());
    }
}
