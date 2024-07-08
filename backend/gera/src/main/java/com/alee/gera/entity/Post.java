package com.alee.gera.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Post {
    private Integer postId;
    private Integer postUserId;
    private Integer postGameId;
    private String postTitle;
    private String postText;
    private Timestamp postTime;
    private Integer postStarNum;
}
