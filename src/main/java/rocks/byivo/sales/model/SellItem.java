/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.byivo.sales.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author byivo
 */
public class SellItem extends Entity {

    private Integer id;

    private Item item;

    private Double price;

    private Integer quantity;

    @JsonIgnore
    private Sell sell;

    public SellItem() {
        quantity = 1;
        price = 0d;
    }

    public Double getTotalPrice() {
        return this.quantity * this.price;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if(price < 0) {
            price = 0d;
        }
        
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if(quantity < 0) {
            quantity = 1;
        }
        
        this.quantity = quantity;
    }

    public Sell getSell() {
        return sell;
    }

    public void setSell(Sell sell) {
        this.sell = sell;
    }
}
