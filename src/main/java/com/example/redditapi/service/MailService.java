package com.example.redditapi.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.redditapi.error.RedditApiException;
import com.example.redditapi.model.NotificationEmail;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class MailService {

    private final MailContextBuilder mailContextBuilder;
    private final JavaMailSender mailSender;

    @Async
    public void sendMail(NotificationEmail notificationEmail) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("springredditclone@email.com");
            messageHelper.setTo(notificationEmail.getRecipent());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(mailContextBuilder.build(notificationEmail.getBody()));
        };

        try {
            mailSender.send(messagePreparator);
            log.info("Activation Email send.");
        } catch(Exception e) {
            throw new RedditApiException("Error sending mail to user");
        }
    }   
}
