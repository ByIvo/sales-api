/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.byivo.sales.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Session;
import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rocks.byivo.sales.mail.MailSender;
import rocks.byivo.sales.model.Sell;
import rocks.byivo.sales.model.SellItem;
import rocks.byivo.sales.util.ConnectionManager;

/**
 *
 * @author byivo
 */
@Service
public class MailService {

    private final NumberFormat numberFormatter;
    private final SimpleDateFormat dateFormatter;

    @Autowired
    private ConnectionManager connectionManager;

    public MailService() {
        Locale ptBr = new Locale("pt", "BR");

        numberFormatter = NumberFormat.getCurrencyInstance(ptBr);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    public void sendFinishSellEmail(Sell sell) {
        String emailContent = this.prepareEmaiLContet(sell);
        String subject = this.loadEmailSubject();
        String[] mail = {sell.getUser().getEmail()};

        MailSender sender = new MailSender();

        Session session = connectionManager.getMailSession();
        sender.sendAnEmail(session, subject, emailContent, mail);
    }

    private String prepareEmaiLContet(Sell sell) {
        String sellTemplate = this.loadSellTemplate();

        Map<String, String> sellProperties = setupSellProperties(sell);

        String finalSellEmail = buildTemplateWithProperties(sellTemplate, sellProperties);
        String rowContent = buildSellItemTemplate(sell);

        finalSellEmail = this.buildRowContent(finalSellEmail, rowContent);

        return finalSellEmail;
    }

    private String buildRowContent(String finalSellEmail, String rowContent) {
        Map<String, String> propMock = new HashMap<>();

        propMock.put("ITEM_ROW_CONTENT", rowContent);

        return this.buildTemplateWithProperties(finalSellEmail, propMock);
    }

    private String buildSellItemTemplate(Sell sell) {
        String finalBuild = "";
        String sellItemTemplate = this.loadSellItemTemplate();

        for (SellItem sellItem : sell.getSellItems()) {
            Map<String, String> sellItemProperties = this.setupSellItemProperties(sellItem);

            finalBuild += this.buildTemplateWithProperties(sellItemTemplate, sellItemProperties);
        }

        return finalBuild;
    }

    private String buildTemplateWithProperties(String template, Map<String, String> properties) {
        String keyFormat = "<%%%1$s%%>";

        for (String key : properties.keySet()) {
            String formattedKey = String.format(keyFormat, key);

            template = template.replace(formattedKey, properties.get(key));
        }

        return template;
    }

    private Map<String, String> setupSellProperties(Sell sell) {
        Map<String, String> sellProperties = new HashMap<>();

        sellProperties.put("PROJECT_VERSION", "1.0");
        sellProperties.put("NAME", sell.getUser().getName());

        sellProperties.put("SELL_TOTAL", numberFormatter.format(sell.getTotal()));
        sellProperties.put("SELL_DATE", dateFormatter.format(sell.getDate()));

        return sellProperties;
    }

    private Map<String, String> setupSellItemProperties(SellItem sellItem) {
        Map<String, String> sellItemProperties = new HashMap<>();

        sellItemProperties.put("ITEM_NAME", sellItem.getItem().getName());
        sellItemProperties.put("ITEM_QUANTITY", sellItem.getQuantity() + "");
        sellItemProperties.put("ITEM_PRICE", numberFormatter.format(sellItem.getPrice()));
        sellItemProperties.put("ITEM_TOTAL", numberFormatter.format(sellItem.getTotalPrice()));

        return sellItemProperties;
    }

    private String loadSellTemplate() {
        return this.loadSafeTemplate("sell_email_template.html");
    }

    private String loadEmailSubject() {
        return this.loadSafeTemplate("sell_email_subject.txt");
    }

    private String loadSellItemTemplate() {
        return this.loadSafeTemplate("sell_item_template.html");
    }

    private String loadSafeTemplate(String templateName) {
        try {
            return this.loadTemplate(templateName);
        } catch (IOException ex) {
            Logger.getLogger(MailService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "TEMPLATE_NOT_FOUND";
    }

    private String loadTemplate(String templateName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        final URL resource = classLoader.getResource(templateName);
        
        if (resource == null) {
            throw new IllegalArgumentException("No resource file named <" + templateName + "> could be loaded from the classpath.");
        }

        return IOUtils.toString(resource, Charsets.UTF_8);
    }
}
