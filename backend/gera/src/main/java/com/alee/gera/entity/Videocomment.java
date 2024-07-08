package com.alee.gera.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Videocomment {
    private Integer videocommentId;
    private Integer videocommentUserId;
    private Integer videocommentVideoId;
    private String videocommentText;
    private Timestamp videocommentTime;
}
