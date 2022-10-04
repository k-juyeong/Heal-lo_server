package com.kh.heallo.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

//    javaMailSender.setHost("smtp.naver.com");
//    javaMailSender.setPort(465);
    javaMailSender.setUsername("heallo01");
    javaMailSender.setPassword("Heallo01!");

    javaMailSender.setJavaMailProperties(getMailProperties());

    return javaMailSender;
  }

  private Properties getMailProperties(){
    Properties properties = new Properties();
    properties.setProperty("mail.smtp.port", "465");
    properties.setProperty("mail.smtp.host", "smtp.naver.com");
    properties.setProperty("mail.smtp.auth","true");
    properties.setProperty("mail.smtp.ssl.enable","true");
    properties.setProperty("mail.transport.protocol","smtp");
    properties.setProperty("mail.smtp.starttls.enable","true");
    properties.setProperty("mail.debug","ture");
    properties.setProperty("mail.smtp.ssl.trust","smtp.naver.com");

    return properties;
  }

}
