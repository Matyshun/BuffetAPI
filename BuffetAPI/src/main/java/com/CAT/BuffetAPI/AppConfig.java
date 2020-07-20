package com.CAT.BuffetAPI;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;

//Archivo de configuracion del Mail Sender.
//En este archivo se crea una instancia de MailSender utilizando datos ubicados en src/main/resources/aplication.properties

@Configuration
public class AppConfig {

    @Autowired
    private Environment env;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public JavaMailSenderImpl javaMailSenderImpl() {
        final JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
        mailSenderImpl.setHost(env.getProperty("Spring.mail.host"));
        mailSenderImpl.setPort(env.getProperty("Spring.mail.port", Integer.class));
        mailSenderImpl.setProtocol(env.getProperty("Spring.mail.protocol"));
        mailSenderImpl.setUsername(env.getProperty("Spring.mail.username"));
        mailSenderImpl.setPassword(env.getProperty("Spring.mail.password"));
        final Properties javaMailProps = new Properties();
        javaMailProps.put("mail.smtp.auth", true);
        javaMailProps.put("mail.smtp.starttls.enable", true);
        mailSenderImpl.setJavaMailProperties(javaMailProps);
        return mailSenderImpl;
    }

}
