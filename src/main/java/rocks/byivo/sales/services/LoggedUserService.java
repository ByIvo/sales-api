/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.byivo.sales.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rocks.byivo.sales.dao.UserDAO;
import rocks.byivo.sales.model.Item;
import rocks.byivo.sales.model.LoggedInfo;
import rocks.byivo.sales.model.User;

/**
 *
 * @author byivo
 */
@Service
public class LoggedUserService {
    
    @Autowired
    private LoggedInfo loggedInfo;
    
    @Autowired
    private UserDAO userDao;
    
    public User getLoggedUser() {
        return userDao.findById(1);
    }   
}
