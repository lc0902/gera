package com.alee.gera.websocket;

import com.alee.gera.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.websocket.Session;
import java.util.List;

@Data
public class RoomBean  {
    String roomId;
    String roomName;
    Integer ownerId;
    Integer gameId;
    Boolean hasPassword;
    String password;
    List<User> users;
}
