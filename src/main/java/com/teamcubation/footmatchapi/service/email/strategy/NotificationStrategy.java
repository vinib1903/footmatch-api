package com.teamcubation.footmatchapi.service.email.strategy;

import com.teamcubation.footmatchapi.dto.request.EmailContentDTO;

public interface NotificationStrategy {

    boolean appliesTo(String message);

    EmailContentDTO createEmail(String message);
}
