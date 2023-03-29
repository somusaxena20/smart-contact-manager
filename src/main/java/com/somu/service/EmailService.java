package com.somu.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

    public boolean sendEmail(String email, String password, String to, String subject, String text) {
        boolean flag = false;

        //logic
        // Recipient's email address

        // SMTP server details
        String host = "smtp.gmail.com";
        String port = "587";

        // Create properties object to set SMTP server details
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        // Get session object to authenticate the sender's email ID and password
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

        try {
            // Create MimeMessage object
            Message message = new MimeMessage(session);

            // Set From Address
            message.setFrom(new InternetAddress(email));

            // Set To Address
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            // Set Subject
            message.setSubject(subject);

            // Set message body
//            message.setText(text);
            message.setContent(text, "text/html");

            // Send message
            Transport.send(message);

            System.out.println("Email sent successfully!");
            flag = true;

        } catch (MessagingException e) {
            throw new RuntimeException(e.getMessage());
        }


        return flag;
    }
}
