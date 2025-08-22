package com.teamcubation.footmatchapi.application.usecase;

public interface EmailUseCases {

    void sendEmail(String to, String subject, String body);
}
