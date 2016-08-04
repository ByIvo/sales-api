/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import rocks.byivo.sales.model.Item;
import rocks.byivo.sales.model.ModelValidation;
import rocks.byivo.sales.model.User;

/**
 *
 * @author byivo
 */
public class ItemTest {

    @Test
    public void nameNull() {
        Item item = new Item();

        boolean valid = item.validContent();

        Map<String, String> invalidMessages = item.getInvalidMessages(true);

        assertFalse(valid);
        assertEquals(ModelValidation.Item.EMPTY_NAME, invalidMessages.get("name"));
    }

    @Test
    public void nameEmpty() {
        Item item = new Item();
        item.setName("");

        boolean valid = item.validContent();

        Map<String, String> invalidMessages = item.getInvalidMessages(true);

        assertFalse(valid);
        assertEquals(ModelValidation.Item.EMPTY_NAME, invalidMessages.get("name"));
    }

    @Test
    public void nameLongest() {
        Item item = new Item();
        item.setName(TestUtil.getStringBySize(81));

        boolean valid = item.validContent();

        Map<String, String> invalidMessages = item.getInvalidMessages(true);

        assertFalse(valid);
        assertEquals(ModelValidation.Item.LONGEST_NAME, invalidMessages.get("name"));
    }

    @Test
    public void nameOkMin() {
        Item item = new Item();
        item.setName(TestUtil.getStringBySize(1));

        boolean valid = item.validContent();

        Map<String, String> invalidMessages = item.getInvalidMessages(true);

        assertFalse(valid);
        assertFalse(invalidMessages.containsKey("name"));
    }

    @Test
    public void nameOkMax() {
        Item item = new Item();
        item.setName(TestUtil.getStringBySize(80));

        boolean valid = item.validContent();

        Map<String, String> invalidMessages = item.getInvalidMessages(true);

        assertFalse(valid);
        assertFalse(invalidMessages.containsKey("name"));
    }

    @Test
    public void imageNullOk() {
        Item item = new Item();
        item.setImage(null);

        boolean valid = item.validContent();

        Map<String, String> invalidMessages = item.getInvalidMessages(true);

        assertFalse(valid);
        assertFalse(invalidMessages.containsKey("image"));
    }

    @Test
    public void imageEmptyOk() {
        Item item = new Item();
        item.setImage("");

        boolean valid = item.validContent();

        Map<String, String> invalidMessages = item.getInvalidMessages(true);

        assertFalse(valid);
        assertFalse(invalidMessages.containsKey("image"));
    }

    @Test
    public void imageMaxOk() {
        Item item = new Item();
        item.setImage(TestUtil.getStringBySize(512));

        boolean valid = item.validContent();

        Map<String, String> invalidMessages = item.getInvalidMessages(true);

        assertFalse(valid);
        assertFalse(invalidMessages.containsKey("image"));
    }

    @Test
    public void imageLongest() {
        Item item = new Item();
        item.setImage(TestUtil.getStringBySize(513));

        boolean valid = item.validContent();

        Map<String, String> invalidMessages = item.getInvalidMessages(true);

        assertFalse(valid);
        assertEquals(ModelValidation.Item.LONGEST_IMAGE, invalidMessages.get("image"));
    }

    @Test
    public void wrongPrice() {
        Item item = new Item();
        item.setPrice(0d);

        boolean valid = item.validContent();

        Map<String, String> invalidMessages = item.getInvalidMessages(true);

        assertFalse(valid);
        assertEquals(ModelValidation.Item.WRONG_PRICE, invalidMessages.get("price"));
    }

    @Test
    public void wrongNegativePrice() {
        Item item = new Item();
        item.setPrice(-100d);

        boolean valid = item.validContent();

        Map<String, String> invalidMessages = item.getInvalidMessages(true);

        assertFalse(valid);
        assertEquals(ModelValidation.Item.WRONG_PRICE, invalidMessages.get("price"));
    }

    @Test
    public void priceOk() {
        Item item = new Item();
        item.setPrice(1d);

        boolean valid = item.validContent();

        Map<String, String> invalidMessages = item.getInvalidMessages(true);

        assertFalse(valid);
        assertFalse(invalidMessages.containsKey("price"));
    }

    @Test
    public void itemMinValid() {
        Item item = new Item();
        item.setPrice(0.1);
        item.setName(TestUtil.getStringBySize(1));
        item.setImage(null);

        boolean valid = item.validContent();

        assertTrue(valid);
    }

    @Test
    public void itemMaxValid() {
        Item item = new Item();
        item.setPrice(Double.MAX_VALUE);
        item.setName(TestUtil.getStringBySize(80));
        item.setImage(TestUtil.getStringBySize(512));

        boolean valid = item.validContent();

        assertTrue(valid);
    }

    @Test
    public void itemEarly() {
        Item item = new Item();

        boolean valid = item.validContent();
        Map<String, String> invalidMessages = item.getInvalidMessages(true);

        assertFalse(valid);
        assertEquals(2, invalidMessages.size());
    }
}
