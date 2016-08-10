/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.byivo.sales.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rocks.byivo.sales.model.User;
import rocks.byivo.sales.services.UserService;

/**
 *
 * @author byivo
 */
@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody User user) {
        return this.userService.create(user);
    }
   

}
