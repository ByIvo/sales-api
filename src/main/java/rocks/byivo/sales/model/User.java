/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.byivo.sales.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author byivo
 */
public class User extends Entity {

    private static final String EMAIL_PATTERN
            = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private Integer id;
    private String name;
    private String email;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private String password;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private String confirmPassword;

    private List<UserItem> cart;

    public User() {
        cart = new ArrayList<>();
    }

    public User(Integer id) {
        this.id = id;
    }

    private User createMock(User newValues) {
        User mockValues = new User();
        mockValues.setPassword(this.password);
        mockValues.setConfirmPassword(this.confirmPassword);
        mockValues.setId(this.id);

        mockValues.setName(newValues.getName());
        mockValues.setEmail(newValues.getEmail());

        return mockValues;
    }

    public Map<String,String> update(User newValues) {
        User mockValues = createMock(newValues);

        boolean valid = mockValues.validContent();

        if (valid) {
            this.setName(newValues.name);
            this.setEmail(newValues.email);
            return new HashMap<>();
        }

        return mockValues.getInvalidMessages(true);
    }

    @Override
    public boolean validContent() {
        this.clearInvalidMessages();
        boolean invalidName = false;
        boolean invalidPassword = false;
        boolean invalidEmail = false;

        //NAME VALIDATION
        if (invalidName |= name == null) {
            this.putInvalidMessage("name", ModelValidation.User.EMPTY_NAME);
        } else if (invalidName |= name.isEmpty()) {
            this.putInvalidMessage("name", ModelValidation.User.EMPTY_NAME);
        } else if (invalidName |= name.length() > 80) {
            this.putInvalidMessage("name", ModelValidation.User.LONGEST_NAME);
        }

        //EMAIL VALIDATION
        if (invalidEmail |= email == null) {
            this.putInvalidMessage("email", ModelValidation.User.EMPTY_EMAIL);
        } else if (invalidEmail |= email.isEmpty()) {
            this.putInvalidMessage("email", ModelValidation.User.EMPTY_EMAIL);
        } else if (invalidEmail |= email.length() > 100) {
            this.putInvalidMessage("email", ModelValidation.User.LONGEST_EMAIL);
        } else if (invalidEmail |= !this.isEmail()) {
            this.putInvalidMessage("email", ModelValidation.User.NOT_AN_EMAIL);
        }

        //PASSWORD VALIDATION
        if (invalidPassword |= password == null) {
            this.putInvalidMessage("password", ModelValidation.User.EMPTY_PASSWORD);
        } else if (invalidPassword |= password.isEmpty()) {
            this.putInvalidMessage("password", ModelValidation.User.EMPTY_PASSWORD);
        } else if (invalidPassword |= !this.passwordMatches()) {
            this.putInvalidMessage("password", ModelValidation.User.UNMATCH_PASSWORD);
        }

        return !(invalidPassword || invalidEmail || invalidName);
    }

    private boolean passwordMatches() {
        return Objects.equals(this.password, this.confirmPassword);
    }

    private boolean isEmail() {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(this.email);
        return matcher.matches();
    }

    public UserItem addToCart(Item item, int quantity) {
        quantity = quantity < 1 ? 1 : quantity;

        UserItem userItem = new UserItem();
        userItem.setItem(item);
        userItem.setUser(this);

        if (this.cart.contains(userItem)) {
            int i = this.cart.indexOf(userItem);
            userItem = this.cart.get(i);
        } else {
            this.cart.add(userItem);
        }

        userItem.sumQuantity(quantity);

        return userItem;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return null;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return null;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    public String getSecuredPassword() {
        return this.password;
    }

    public List<UserItem> getCart() {
        return cart;
    }

    public void setCart(List<UserItem> cart) {
        this.cart = cart;
    }

}
