package com.teamcubation.footmatchapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailContentDTO {
    private String to;
    private String subject;
    private String body;
}
