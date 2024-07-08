package com.alee.gera.websocket;

import com.alee.gera.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class LiveRoomBean {
    private String liveRoomId;
    private List<User> users;
}
