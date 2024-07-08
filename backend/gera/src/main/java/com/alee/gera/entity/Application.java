package com.alee.gera.entity;

import lombok.Data;

@Data
public class Application {
    private Integer applicationId;
    private Integer applicationUserId;
    private String applicationType;
    private String applicationText;
    private Integer applicationStatus;
}
