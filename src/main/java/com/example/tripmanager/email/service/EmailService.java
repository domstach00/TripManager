package com.example.tripmanager.email.service;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.email.model.EmailDetails;
import com.example.tripmanager.shared.model.AbstractEntity;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final Optional<MessageProducer> messageProducerOpt;
    private final ActivationLinkService activationLinkService;

    @Value("${rabbitmq.routing.email}")
    private String emailRoutingKey;

    @Autowired
    public EmailService(Optional<MessageProducer> messageProducerOpt, ActivationLinkService activationLinkService) {
        this.messageProducerOpt = messageProducerOpt;
        this.activationLinkService = activationLinkService;
    }

    public void sendWelcomeEmail(@NotNull Account account, @NotBlank String activationToken) {
        Map<String, Object> model = new HashMap<>();
        model.put("username", account.getUsername());
        String activationLink = activationLinkService.createActivationLink("token", activationToken);
        model.put("activationLink", activationLink);

        EmailDetails emailDetails = createEmailDetails(
                account.getEmail(),
                "Welcome",
                "welcome",
                model
        );

        sendMessage(emailDetails);
        log.info("Welcome email to {} has been added to message Queue", account.getEmail());
    }

    public void sendInviteEmail(Account sender, String joinToken, List<Account> accountsToInvite) {
        Map<String, Object> model = new HashMap<>();
        model.put("inviterName", sender.getUsername());
        String joinBudgetLink = activationLinkService.createJoinBudgetLink("token", joinToken);
        model.put("inviteLink", joinBudgetLink);

        List<EmailDetails> emailDetailsList = accountsToInvite.stream()
                .map(invitedAccount -> createEmailDetails(invitedAccount.getEmail(), "Budget Invitation", "budget-invite", model))
                .toList();

        sendMessage(emailDetailsList);
        log.info("Budget Invitation email from {} to {} account(s) ({}) has been added to message Queue",
                sender.getId(), accountsToInvite.size(), accountsToInvite.stream().map(AbstractEntity::getId).collect(Collectors.joining(", ")));
    }

    public void sendPasswordResetEmail(Account account, String tokenValue) {
        Map<String, Object> model = new HashMap<>();
        model.put("username", account.getUsername());
        String linkValue = activationLinkService.createResetPasswordLink("token", tokenValue);
        model.put("resetLink", linkValue);

        EmailDetails emailDetails = createEmailDetails(account.getEmail(), "Password reset", "password-reset", model);
        sendMessage(emailDetails);
        log.info("Password reset email has been sent to {}", account.getEmail());
    }

    private EmailDetails createEmailDetails(String recipientEmail, String subject, String templateName, Map<String, Object> model) {
        return new EmailDetails(recipientEmail, "TM - " + subject, templateName, model);
    }

    private void sendMessage(EmailDetails emailDetails) {
        messageProducerOpt.ifPresent(messageProducer ->
                messageProducer.sendMessage(emailRoutingKey, emailDetails));
    }
    private void sendMessage(List<EmailDetails> emailDetailsList) {
        if (emailDetailsList != null) {
            emailDetailsList.forEach(this::sendMessage);
        }
    }
}
