/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.byivo.sales.mail;

import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author byivo
 */
public class MailSender {

    public void sendAnEmail(Session session, String subject, String emailMessage, String... toEmails) {
        try {
            String fromUsername = session.getProperty("mail.smtp.user");

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromUsername));

            Address[] toUser = prepareToEmail(toEmails);

            message.setRecipients(Message.RecipientType.TO, toUser);
            message.setSubject(subject);
            message.setContent(emailMessage, "text/html; charset=utf-8");

            this.sendMessage(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(final Message message) {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try {
                    Transport.send(message);
                } catch (Exception ex) {
                    Logger.getLogger("EMAIL", ex.getMessage());
                }
            }
        };

        new Thread(runnable).start();
    }

    private InternetAddress[] prepareToEmail(String... toEmails) {
        InternetAddress[] adresses = new InternetAddress[toEmails.length];

        for (int i = 0; i < toEmails.length; i++) {
            try {
                adresses[i] = new InternetAddress(toEmails[i]);
            } catch (Exception ex) {
                String message = "O endereço de e-mail %s não é válido!";
                System.err.println(String.format(message, toEmails[i]));
            }
        }

        return adresses;
    }
}
