package com.example.tripmanager.email.model;

import java.util.Map;

public class EmailDetails {
    private String recipient;
    private String subject;
    private String templateName;
    private Map<String, Object> model;

    public EmailDetails() {
    }

    public EmailDetails(String recipient, String subject, String templateName, Map<String, Object> model) {
        this.recipient = recipient;
        this.subject = subject;
        this.templateName = templateName;
        this.model = model;
    }

    public String getRecipient() {
        return recipient;
    }

    @Override
    public String toString() {
        return "EmailDetails{" +
                "recipient='" + recipient + '\'' +
                ", subject='" + subject + '\'' +
                ", templateName='" + templateName + '\'' +
                ", model=" + model +
                '}';
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
}
