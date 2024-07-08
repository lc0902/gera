package com.alee.gera.entity;

import lombok.Data;

@Data
public class Rate {
    private Integer rateId;
    private Integer rateUserId;
    private Integer rateGameId;
    private Float rateRate;
}
