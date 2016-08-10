/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.byivo.sales.model;

import java.util.Date;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 *
 * @author byivo
 */
@Component
public class LoggedInfo {
    
    private User loggedUser;
    private Date loginDate;

    public LoggedInfo() {
    }
    
    public User getLoggedUser() {
        return loggedUser;
    }

    public Date getLoginDate() {
        return loginDate;
    }
    
    public Date login(User user){
        this.loggedUser = user;
        this.loginDate = new Date();
        
        return this.loginDate;
    }
    
    public boolean isLogged() {
        return loggedUser != null;
    }
    
}
