package com.EMS.Employee.Management.System.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration // Marks this class as a Spring configuration class
public class MailConfig {

    @Value("${spring.mail.username}") // Injects the username from application properties
    private String username;

    @Value("${spring.mail.password}") // Injects the password from application properties
    private String password;

    @Bean // Creates a Spring bean that can be injected elsewhere
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // Configure SMTP server details for Gmail
        mailSender.setHost("smtp.gmail.com"); // Gmail SMTP server
        mailSender.setPort(587); // Standard port for SMTP with TLS
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        // Configure additional mail properties
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true"); // Enable authentication
        props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS for secure connection
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // Trust the Gmail SMTP server
        props.put("mail.smtp.connectiontimeout", "5000"); // 5s connection timeout
        props.put("mail.smtp.timeout", "5000"); // 5s timeout for socket read operations
        props.put("mail.smtp.writetimeout", "5000"); // 5s timeout for socket write operations
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // Use SSL socket factory
        props.put("mail.debug", "true"); // Enable debug logging for troubleshooting

        return mailSender;
    }
}