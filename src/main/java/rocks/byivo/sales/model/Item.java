/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.byivo.sales.model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author byivo
 */
public class Item extends Entity {

    private Integer id;
    private String name;

    private String image;

    private Double price;

    public Item() {
        price = 0d;
    }

    public Item(Integer id) {
        this.id = id;
        price = 0d;
    }

    private Item createMock(Item newValues) {
        Item mockValues = new Item();
        mockValues.setId(this.id);
        mockValues.setPrice(newValues.getPrice());
        mockValues.setName(newValues.getName());
        mockValues.setImage(newValues.getImage());

        return mockValues;
    }

    public Map<String, String> update(Item newValues) {
        Item mockValues = createMock(newValues);

        boolean valid = mockValues.validContent();

        if (valid) {
            this.setName(newValues.name);
            this.setImage(newValues.getImage());
            this.setPrice(newValues.getPrice());
            return new HashMap<>();
        }

        return mockValues.getInvalidMessages(true);
    }

    @Override
    public boolean validContent() {
        this.clearInvalidMessages();

        boolean invalidName = false;
        boolean invalidImage = false;
        boolean invalidPrice = false;

        //NAME VALIDATION
        if (invalidName |= name == null) {
            this.putInvalidMessage("name", ModelValidation.Item.EMPTY_NAME);
        } else if (invalidName |= name.isEmpty()) {
            this.putInvalidMessage("name", ModelValidation.Item.EMPTY_NAME);
        } else if (invalidName |= name.length() > 80) {
            this.putInvalidMessage("name", ModelValidation.Item.LONGEST_NAME);
        }

        //IMAGE VALIDATION
        if (image != null) {
            if (invalidImage |= image.length() > 512) {
                this.putInvalidMessage("image", ModelValidation.Item.LONGEST_IMAGE);
            }
        }

        //PASSWORD VALIDATION
        if (invalidPrice |= price <= 0) {
            this.putInvalidMessage("price", ModelValidation.Item.WRONG_PRICE);
        }

        return !(invalidImage || invalidPrice || invalidName);
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
