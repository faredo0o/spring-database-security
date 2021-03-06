package com.faredo0o.springdatabasesecurity.email;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService implements EmailSender{

   private final JavaMailSender mailSender;
   private final Logger logger= LoggerFactory.getLogger(EmailService.class);
@Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void send(String to, String emailBody) {

        try{
            MimeMessage mimeMessage=mailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,"utf-8");
            helper.setText(emailBody,true);
            helper.setTo(to);
            helper.setSubject("Confirmation email");
            helper.setFrom("hello@faredo0o.com");
            mailSender.send(mimeMessage);

        }catch (MessagingException e){
            logger.error("Failed to send email",e);
            throw new IllegalStateException("Failed to send email");

        }
    }
}
