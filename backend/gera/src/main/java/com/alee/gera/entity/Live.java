package com.alee.gera.entity;

import lombok.Data;

@Data
public class Live {
    private Integer liveId;
    private  Integer liveUserId;
    private Integer liveGameId;
    private String liveUrl;
    private String liveDescription;
    private String liveCoverUrl;
    private Integer liveStatus;
}
