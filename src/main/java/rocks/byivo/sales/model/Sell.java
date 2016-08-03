/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.byivo.sales.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author byivo
 */
public class Sell extends Entity{
    
    private Integer id;
    
    private User user;
    
    private String payment;
    
    private Date date;
    
    private List<SellItem> sellItems;

    public Sell() {
        sellItems = new ArrayList<>();
    }
    
    public double getTotal() {
        double total = 0d;
        
        for(SellItem itemSell : sellItems) {
            total+= itemSell.getTotalPrice();
        }
        return total;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<SellItem> getSellItems() {
        return sellItems;
    }

    public void setSellItems(List<SellItem> sellItems) {
        this.sellItems = sellItems;
    }
}
