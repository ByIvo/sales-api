/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.byivo.sales.model;

import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author byivo
 */
public abstract class Entity implements Serializable {

    private Map<String, String> invalidMessages;

    public Entity() {
        invalidMessages = new HashMap<>();
    }

    public boolean validContent() {
        
        return true;
    }
    
    public void putInvalidMessage(String field, String message) {
        invalidMessages.put(field, message);
    }
    
    public void clearInvalidMessages() {
        this.invalidMessages.clear();
    }
    
    public Map<String, String> getInvalidMessages(boolean clear) {
        Map<String, String> invalidMessagestoReturn = new HashMap<>(invalidMessages);
        
        if(clear) this.clearInvalidMessages();
        
        return invalidMessagestoReturn;
    }

    public abstract Integer getId();

    public abstract void setId(Integer id);

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.getId());
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
        final Entity other = (Entity) obj;
        return Objects.equals(this.getId(), other.getId());
    }

}
