/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import rocks.byivo.sales.model.User;
import static org.junit.Assert.*;
import rocks.byivo.sales.model.ModelValidation;

/**
 *
 * @author byivo
 */
public class UserTest {

    public UserTest() {
    }

    @Test
    public void testMessagesCleared() {
        User user = new User();

        boolean valid = user.validContent();

        assertFalse(valid);
        assertFalse(user.getInvalidMessages(true).isEmpty());
        assertTrue(user.getInvalidMessages(true).isEmpty());
    }

    @Test
    public void testMessagesNotCleared() {
        User user = new User();

        boolean valid = user.validContent();

        assertFalse(valid);
        assertFalse(user.getInvalidMessages(false).isEmpty());
        assertFalse(user.getInvalidMessages(false).isEmpty());
    }

    @Test
    public void nullName() {
        User user = new User();

        boolean valid = user.validContent();

        Map<String, String> invalidMessages = user.getInvalidMessages(true);

        assertFalse(valid);
        assertEquals(ModelValidation.User.EMPTY_NAME, invalidMessages.get("name"));
    }

    @Test
    public void longestName() {
        User user = new User();
        user.setName(TestUtil.getStringBySize(81));
        boolean valid = user.validContent();

        Map<String, String> invalidMessages = user.getInvalidMessages(true);

        assertFalse(valid);
        assertEquals(ModelValidation.User.LONGEST_NAME, invalidMessages.get("name"));
    }

    @Test
    public void emptyName() {
        User user = new User();
        user.setName("");

        boolean valid = user.validContent();

        Map<String, String> invalidMessages = user.getInvalidMessages(true);

        assertFalse(valid);
        assertEquals(ModelValidation.User.EMPTY_NAME, invalidMessages.get("name"));
    }

    @Test
    public void nameOkMin() {
        User user = new User();
        user.setName(TestUtil.getStringBySize(1));

        user.validContent();

        Map<String, String> invalidMessages = user.getInvalidMessages(true);

        assertFalse(invalidMessages.containsKey("name"));
    }

    @Test
    public void nameOkLimit() {
        User user = new User();
        user.setName(TestUtil.getStringBySize(80));

        user.validContent();

        Map<String, String> invalidMessages = user.getInvalidMessages(true);

        assertFalse(invalidMessages.containsKey("name"));
    }

    @Test
    public void emptyEmail() {
        User user = new User();

        boolean valid = user.validContent();

        Map<String, String> invalidMessages = user.getInvalidMessages(true);

        assertFalse(valid);
        assertEquals(ModelValidation.User.EMPTY_EMAIL, invalidMessages.get("email"));
    }

    @Test
    public void longestEmail() {
        User user = new User();
        user.setEmail(TestUtil.getEmailBySize(101));

        boolean valid = user.validContent();

        Map<String, String> invalidMessages = user.getInvalidMessages(true);

        assertFalse(valid);
        assertEquals(ModelValidation.User.LONGEST_EMAIL, invalidMessages.get("email"));
    }

    @Test
    public void emailOkayMin() {
        User user = new User();
        user.setEmail(TestUtil.getEmailBySize(0));

        user.validContent();

        Map<String, String> invalidMessages = user.getInvalidMessages(true);

        assertEquals(6, user.getEmail().length());
        assertFalse(invalidMessages.containsKey("email"));
    }

    @Test
    public void emailOkayLimit() {
        User user = new User();
        user.setEmail(TestUtil.getEmailBySize(100));

        user.validContent();

        Map<String, String> invalidMessages = user.getInvalidMessages(true);

        assertEquals(100, user.getEmail().length());
        assertFalse(invalidMessages.containsKey("email"));
    }

    @Test
    public void notAnEmail() {
        User user = new User();
        user.setEmail(TestUtil.getStringBySize(20));

        boolean valid = user.validContent();

        Map<String, String> invalidMessages = user.getInvalidMessages(true);

        assertFalse(valid);
        assertEquals(ModelValidation.User.NOT_AN_EMAIL, invalidMessages.get("email"));
    }

    @Test
    public void duplicatedEmail() {
        User user = new User();
        user.putInvalidMessage("email", ModelValidation.User.DUPLICATED_EMAIL);

        Map<String, String> invalidMessages = user.getInvalidMessages(true);

        assertEquals(ModelValidation.User.DUPLICATED_EMAIL, invalidMessages.get("email"));
    }

    @Test
    public void emptyPassword() {
        User user = new User();

        boolean valid = user.validContent();

        Map<String, String> invalidMessages = user.getInvalidMessages(true);

        assertFalse(valid);
        assertEquals(ModelValidation.User.EMPTY_PASSWORD, invalidMessages.get("password"));
    }

    @Test
    public void unmatchedPassword() {
        User user = new User();

        user.setPassword(TestUtil.getStringBySize(10));
        user.setConfirmPassword(TestUtil.getStringBySize(11));

        boolean valid = user.validContent();

        Map<String, String> invalidMessages = user.getInvalidMessages(true);

        assertFalse(valid);
        assertEquals(ModelValidation.User.UNMATCH_PASSWORD, invalidMessages.get("password"));
    }

    @Test
    public void passwordOk() {
        User user = new User();

        user.setPassword(TestUtil.getStringBySize(10));
        user.setConfirmPassword(TestUtil.getStringBySize(10));

        boolean valid = user.validContent();

        Map<String, String> invalidMessages = user.getInvalidMessages(true);

        assertFalse(valid);
        assertFalse(invalidMessages.containsKey("password"));
    }

    @Test
    public void earlyUserCreation() {
        User user = new User();

        boolean valid = user.validContent();
        Map<String, String> invalidMessages = user.getInvalidMessages(true);
        Map<String, String> exceptedInvalidMessages = new HashMap<>();

        exceptedInvalidMessages.put("name", ModelValidation.User.EMPTY_NAME);
        exceptedInvalidMessages.put("email", ModelValidation.User.EMPTY_EMAIL);
        exceptedInvalidMessages.put("password", ModelValidation.User.EMPTY_PASSWORD);

        assertFalse(valid);
        assertEquals(exceptedInvalidMessages, invalidMessages);
    }

    @Test
    public void validUserMin() {
        User user = new User();
        user.setName(TestUtil.getStringBySize(1));
        user.setEmail(TestUtil.getEmailBySize(0));
        user.setPassword(TestUtil.getStringBySize(1));
        user.setConfirmPassword(TestUtil.getStringBySize(1));

        assertTrue(user.validContent());
    }

    @Test
    public void validUserMax() {
        User user = new User();
        user.setName(TestUtil.getStringBySize(80));
        user.setEmail(TestUtil.getEmailBySize(100));
        user.setPassword(TestUtil.getStringBySize(2));
        user.setConfirmPassword(TestUtil.getStringBySize(2));

        assertTrue(user.validContent());
    }
    
    @Test
    public void updateUserSuccessfull() {
        
        String name = "Ivo";
        String email= "irb@asdasd.com";
        String password = "asd";
        
        User user = new User();
        user.setName("nome");
        user.setEmail("mail@mail.com");
        user.setPassword(password);
        user.setConfirmPassword(password);
        
        User changes = new User();
        changes.setName(name);
        changes.setEmail(email);
        changes.setPassword("another");
        changes.setConfirmPassword("another");

        assertTrue(user.validContent());
        assertTrue(changes.validContent());
        assertTrue(user.update(changes).isEmpty());
        
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getSecuredPassword());
    }
    
     @Test
    public void updateUserUnsuccessfull() {
        
        String name = "Ivo";
        String email= "irbsdasd.com";
        String password = "asd";
        
        User user = new User();
        user.setName("nome");
        user.setEmail("mail@mail.com");
        user.setPassword(password);
        user.setConfirmPassword(password);
        
        User changes = new User();
        changes.setName(name);
        changes.setEmail(email);
        changes.setPassword("another");
        changes.setConfirmPassword("another");

        assertTrue(user.validContent());
        assertFalse(changes.validContent());
        assertFalse(user.update(changes).isEmpty());
        
        assertEquals("nome", user.getName());
        assertEquals("mail@mail.com", user.getEmail());
        assertEquals(password, user.getSecuredPassword());
    }

        @Test
    public void updateUserSuccessfullTest() {
        
        String name = "Ivo";
        String email= "irb@asdasd.com";
        String password = "asd";
        
        User user = new User();
        user.setName("nome");
        user.setEmail("mail@mail.com");
        user.setPassword(password);
        user.setConfirmPassword(password);
        
        User changes = new User();
        changes.setName(name);
        changes.setEmail(email);

        assertTrue(user.validContent());
        assertTrue(user.update(changes).isEmpty());
        
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getSecuredPassword());
    }
    
    @Test
    public void emptyCar() {
        User user = new User();

        assertTrue(user.getCart().isEmpty());
    }

}
