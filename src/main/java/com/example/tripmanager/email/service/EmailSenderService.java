package com.example.tripmanager.email.service;

import com.example.tripmanager.email.model.EmailDetails;

public interface EmailSenderService {
    void sendEmail(EmailDetails emailDetails);
}
