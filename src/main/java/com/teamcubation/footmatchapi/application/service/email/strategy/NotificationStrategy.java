package com.teamcubation.footmatchapi.application.service.email.strategy;

import com.teamcubation.footmatchapi.application.dto.request.EmailContentDTO;

public interface NotificationStrategy {

    boolean appliesTo(String message);

    EmailContentDTO createEmail(String message);
}
