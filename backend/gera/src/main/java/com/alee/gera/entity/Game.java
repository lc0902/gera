package com.alee.gera.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Game {
    private Integer gameId;
    private String gameName;
    private String gameCoverUrl;
    private Timestamp gameReleaseTime;
    private String gameDescription;
    private Float gameRate;
}
