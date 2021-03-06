/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.byivo.sales.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.mail.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.springframework.stereotype.Service;

/**
 *
 * @author byivo
 */
@Service
public class ConnectionManager {

    private DataSource dataSource;
    private Session mailSession;

    private ConnectionManager() {

    }

    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
    
    public Session getMailSession() {
        return this.mailSession;
    }

    @PostConstruct
    private void init() {
        try {
            Context initContext = new InitialContext();
            Context webContext = (Context) initContext.lookup("java:/comp/env");

            dataSource = (DataSource) webContext.lookup("jdbc/salesApiDs");
            mailSession = (Session) webContext.lookup("mail/Session");
            
        } catch (NamingException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PreDestroy
    private void destroy() {

    }
}
