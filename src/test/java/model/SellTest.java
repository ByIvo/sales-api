/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import org.junit.Test;
import static org.junit.Assert.*;
import rocks.byivo.sales.model.Item;
import rocks.byivo.sales.model.Sell;
import rocks.byivo.sales.model.SellItem;

/**
 *
 * @author byivo
 */
public class SellTest {
    
    public SellTest() {
    }

    @Test
    public void testTotalSellPrice() {
        Sell sell = new Sell();
        
        Item item1 = new Item();
        item1.setPrice(200d);
        
        Item item2 = new Item();
        item2.setPrice(120d);
        
        SellItem sellItem1 = new SellItem();
        sellItem1.setItem(item1);
        sellItem1.setPrice(item1.getPrice());
        sellItem1.setQuantity(12);
        
        SellItem sellItem2 = new SellItem();
        sellItem2.setItem(item2);
        sellItem2.setPrice(item2.getPrice());
        sellItem2.setQuantity(5);
        
        sell.addItem(sellItem1);        
        sell.addItem(sellItem2);
        
        assertEquals((Double)2400d, sellItem1.getTotalPrice());
        assertEquals((Double)600d, sellItem2.getTotalPrice());
        assertEquals((Double)3000d, sell.getTotal());
    }
}
