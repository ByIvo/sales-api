/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import org.junit.Test;
import static org.junit.Assert.*;
import rocks.byivo.sales.model.Item;
import rocks.byivo.sales.model.SellItem;

/**
 *
 * @author byivo
 */
public class SellItemTest {
    
    public SellItemTest() {
    }

    
    @Test
    public void testTotalPrice() {
        Item item = new Item();
        item.setPrice(200d);
        
        SellItem sellItem = new SellItem();
        
        sellItem.setItem(item);
        sellItem.setPrice(item.getPrice());
        sellItem.setQuantity(20);
        
        assertEquals((Double)4000d, sellItem.getTotalPrice());
    }
    
    @Test
    public void testTotalPrice2() {
        Item item = new Item();
        item.setPrice(1d);
        
        SellItem sellItem = new SellItem();
        
        sellItem.setItem(item);
        sellItem.setPrice(item.getPrice());
        sellItem.setQuantity(100);
        
        assertEquals((Double)100d, sellItem.getTotalPrice());
    }
    
    @Test
    public void testNegativePrice() {        
        SellItem sellItem = new SellItem();
        
        sellItem.setPrice(-200d);
        
        assertEquals((Double)0d, sellItem.getPrice());
    }
    
    @Test
    public void testNegativeQuantity() {        
        SellItem sellItem = new SellItem();
        
        sellItem.setQuantity(-1);
        
        assertEquals((Integer)1, sellItem.getQuantity());
    }
    
    @Test
    public void testNullParams() {        
        SellItem sellItem = new SellItem();
        
        sellItem.setQuantity(0);
        sellItem.setPrice(0);
        
        assertEquals((Double)0d, sellItem.getTotalPrice());
    }
}
