package util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailUtil {
    public static boolean sendOTPEmail(String recipientEmail, String otpCode) {
        Properties properties = new Properties();

        // Load properties from the config file
        try (InputStream input = EmailUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return false; // Return false if loading properties fails
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Return false if loading properties fails
        }


        // Fetch email and password from properties file
        String from = properties.getProperty("email");
        String password = properties.getProperty("app.password");
        String smtpHost = properties.getProperty("smtp.host");
        String smtpPort = properties.getProperty("smtp.port");

        if (from == null || password == null || smtpHost == null || smtpPort == null) {
            System.out.println("Required configuration not found in config.properties.");
            return false; // Return false if any required configuration is not found
        }

        // Setup mail server properties
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", properties.getProperty("smtp.auth"));
        props.put("mail.smtp.starttls.enable", properties.getProperty("smtp.starttls.enable"));

        // Get the Session object
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            // Create a default MimeMessage object
            Message message = new MimeMessage(session);

            // Set From: header field
            message.setFrom(new InternetAddress(from));

            // Set To: header field
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

            // Set Subject: header field
            message.setSubject("Your OTP Code");

            // Set the actual message
            message.setText("Your OTP code is: " + otpCode);

            // Send message
            Transport.send(message);
            System.out.println("OTP sent successfully");
            return true; // Return true if email is sent successfully

        } catch (MessagingException e) {
            e.printStackTrace();
            return false; // Return false if sending fails
        }
    }
}
