package com.example.tripmanager.email.service;

import com.example.tripmanager.config.EmailConfig;
import com.example.tripmanager.email.model.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {
    private static final Logger log = LoggerFactory.getLogger(EmailSenderServiceImpl.class);

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public EmailSenderServiceImpl(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendEmail(EmailDetails emailDetails) {
        if (emailDetails == null || StringUtils.isBlank(emailDetails.getRecipient())) {
            log.warn("No recipient address. Cannot send email. EmailDetails={}", emailDetails);
            return;
        }
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, EmailConfig.characterEncoding);

            Context context = new Context();
            if (emailDetails.getModel() != null) {
                context.setVariables(emailDetails.getModel());
            }
            String htmlBody = templateEngine.process(emailDetails.getTemplateName(), context);

            helper.setTo(emailDetails.getRecipient());
            helper.setSubject(emailDetails.getSubject());
            helper.setText(htmlBody, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            log.warn("Exception while trying to send email ({}) with Exception={}", emailDetails, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
