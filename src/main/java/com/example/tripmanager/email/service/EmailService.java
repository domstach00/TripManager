package com.example.tripmanager.email.service;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.email.model.EmailDetails;
import com.example.tripmanager.shared.rabbitmq.MessageProducer;
import com.example.tripmanager.shared.token.service.helper.ActivationLinkService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final MessageProducer messageProducer;
    private final ActivationLinkService activationLinkService;

    @Value("${rabbitmq.routing.email}")
    private String emailRoutingKey;

    @Autowired
    public EmailService(MessageProducer messageProducer, ActivationLinkService activationLinkService) {
        this.messageProducer = messageProducer;
        this.activationLinkService = activationLinkService;
    }

    public void sendWelcomeEmail(@NotNull Account account, @NotBlank String activationToken) {
        Map<String, Object> model = new HashMap<>();
        model.put("username", account.getUsername());
        String activationLink = activationLinkService.createActivationLink("token", activationToken);
        model.put("activationLink", activationLink);

        EmailDetails emailDetails = new EmailDetails(
                account.getEmail(),
                "Welcome",
                "welcome",
                model
        );

        sendMessage(emailDetails);
        log.info("Welcome email to {} has been added to message Queue", account.getEmail());
    }

    private void sendMessage(EmailDetails emailDetails) {
        messageProducer.sendMessage(emailRoutingKey, emailDetails);
    }
}
