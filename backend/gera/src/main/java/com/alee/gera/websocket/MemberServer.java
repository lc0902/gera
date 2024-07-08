package com.alee.gera.websocket;


import cn.hutool.json.JSON;
import com.alee.gera.entity.User;
import com.alee.gera.mapper.UserMapper;
import com.alee.gera.util.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@ServerEndpoint(value = "/member/{userToken}/{roomId}")
public class MemberServer {

    private static Map<String, Session> webSocketSet = new HashMap<>();
    private String token;
    private String roomID;
    private static UserMapper userMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        MemberServer.userMapper = userMapper;
    }

    private static RedisUtil redisUtil;
    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        MemberServer.redisUtil = redisUtil;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userToken") String userToken, @PathParam("roomId") String roomId) throws IOException {
//        判断用户合法性
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            if(user.getUserStatus()!=0){
//                用户身份合法
//                存入set和redis
                webSocketSet.put(userToken, session);
//                从redis中获取原有数据
                String memberDataJSON = (String) redisUtil.get("member:"+roomId);
                if(memberDataJSON!=null){
                    RoomBean roomBean = new ObjectMapper().readValue(memberDataJSON, RoomBean.class);
                    roomBean.getUsers().add(user);
                    memberDataJSON = new ObjectMapper().writeValueAsString(roomBean);
                    redisUtil.set("member:"+roomId, memberDataJSON);
                }else {
                    RoomBean roomBean = new RoomBean();
                    roomBean.setRoomId(roomId);
                    List<User> users = new ArrayList<>();
                    users.add(user);
                    roomBean.setUsers(users);
                    memberDataJSON = new ObjectMapper().writeValueAsString(roomBean);
                    redisUtil.set("member:"+roomId, memberDataJSON);
                }
                memberDataJSON = (String) redisUtil.get("member:"+roomId);
                RoomBean roomBean = new ObjectMapper().readValue(memberDataJSON, RoomBean.class);
                for (User user1 : roomBean.getUsers()) {
                    Session session1 = webSocketSet.get(user1.getUserToken());
                    session1.getBasicRemote().sendText(memberDataJSON);
                }
            }
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("userToken") String userToken, @PathParam("roomId") String roomId) throws IOException {
        webSocketSet.remove(userToken);
        User user = userMapper.getUserByToken(userToken);
        String memberDataJSON = (String) redisUtil.get("member:"+roomId);

        RoomBean roomBean = new ObjectMapper().readValue(memberDataJSON, RoomBean.class);

        roomBean.getUsers().remove(user);

        memberDataJSON = new ObjectMapper().writeValueAsString(roomBean);

        redisUtil.set("member:"+roomId, memberDataJSON);

        memberDataJSON = (String) redisUtil.get("member:"+roomId);
        roomBean = new ObjectMapper().readValue(memberDataJSON, RoomBean.class);

        for (User user1 : roomBean.getUsers()) {
            Session session1 = webSocketSet.get(user1.getUserToken());
            session1.getBasicRemote().sendText(memberDataJSON);
        }
    }


}
