package util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailUtil {
    public static boolean sendOTPEmail(String recipientEmail, String otpCode) {
        Properties properties = new Properties();

        try (InputStream input = EmailUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return false;
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        String from = properties.getProperty("email");
        String password = properties.getProperty("app.password");
        String smtpHost = properties.getProperty("smtp.host");
        String smtpPort = properties.getProperty("smtp.port");

        if (from == null || password == null || smtpHost == null || smtpPort == null) {
            System.out.println("Required configuration not found in config.properties.");
            return false;
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", properties.getProperty("smtp.auth"));
        props.put("mail.smtp.starttls.enable", properties.getProperty("smtp.starttls.enable"));

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Your OTP Code");
            message.setText("Your OTP code is: " + otpCode);
            Transport.send(message);
            System.out.println("OTP sent successfully");
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
