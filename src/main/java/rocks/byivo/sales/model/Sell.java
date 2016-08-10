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
    
    private Date date;
    
    private List<SellItem> sellItems;

    public Sell() {
        sellItems = new ArrayList<>();
    }
    
    public Double getTotal() {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    public void addItem(SellItem sellItem) {
        this.getSellItems().add(sellItem);
        sellItem.setSell(this);
    }

    public List<SellItem> getSellItems() {
        return sellItems;
    }

    public void setSellItems(List<SellItem> sellItems) {
        this.sellItems = sellItems;
    }
}
