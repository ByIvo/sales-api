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
    public void negativeValue() {
        User user = new User();
        Item item = new Item();
        
        UserItem userItem = new UserItem();
        userItem.setUser(user);
        userItem.setItem(item);
        userItem.sumQuantity(-5);
        
        assertTrue(userItem.getQuantity() > 0);
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
