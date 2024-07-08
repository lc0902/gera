package com.alee.gera.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Video {
    private Integer videoId;
    private Integer videoUserId;
    private Integer videoGameId;
    private String videoUrl;
    private String videoCoverUrl;
    private String videoDescription;
    private Timestamp videoUpTime;
    private Integer videoStarNumber;

}
