/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.byivo.sales.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;

/**
 *
 * @author byivo
 */
public class UserItem extends Entity {

    @JsonIgnore
    private User user;

    private Item item;

    private Integer quantity;

    public UserItem() {
        quantity = 0;
    }

    public UserItem(User user, Item item, int quantity) {
        this.user = user;
        this.item = item;
        this.quantity = 0;
        this.sumQuantity(quantity);
    }

    public void sumQuantity(int quantity) {
        this.quantity += quantity;
        
        if(this.quantity < 1) {
            this.quantity = 1;
        }
    }

    public void sumOne() {
        quantity++;
    }

    public void decressOne() {
        if (quantity > 1) {
            quantity--;
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getQuantity() {
        return quantity;
    }


    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.user);
        hash = 23 * hash + Objects.hashCode(this.item);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserItem other = (UserItem) obj;
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }

        return Objects.equals(this.item, other.item);
    }

    @JsonIgnore
    @Override
    public Integer getId() {
       return -1;
    }

    @Override
    public void setId(Integer id) {
        
    }

}
