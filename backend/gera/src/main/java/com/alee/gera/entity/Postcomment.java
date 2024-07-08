package com.alee.gera.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Postcomment {
    private Integer postcommentId;
    private Integer postcommentUserId;
    private Integer postcommentPostId;
    private String postcommentText;
    private Timestamp postcommentTime;
}
