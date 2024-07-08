package com.alee.gera.websocket;

import com.alee.gera.entity.User;
import com.alee.gera.mapper.LiveMapper;
import com.alee.gera.mapper.UserMapper;
import com.alee.gera.util.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@ServerEndpoint(value = "/liveRoom/{liveRoomId}/{userToken}")
public class LiveRoomControlWS {
    private static Map<String, Session> sessionMap = new HashMap<>();
    private static UserMapper userMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper){
        LiveRoomControlWS.userMapper = userMapper;
    }
    private static LiveMapper liveMapper;
    @Autowired
    public void setLiveMapper(LiveMapper liveMapper){
        LiveRoomControlWS.liveMapper = liveMapper;
    }
    private static RedisUtil redisUtil;
    @Autowired
    public void setRedisUtil(RedisUtil redisUtil){
        LiveRoomControlWS.redisUtil = redisUtil;
    }



    @OnOpen
    public void onOpen(Session session, @PathParam("liveRoomId") String liveRoomId,@PathParam("userToken") String userToken) throws JsonProcessingException {
        sessionMap.put(userToken, session);
        String liveRoomJSON = (String) redisUtil.get("liveRoom:"+liveRoomId);
        if(liveRoomJSON!=null){
//            redis中存在数据
            LiveRoomBean liveRoomBean = new ObjectMapper().readValue(liveRoomJSON, LiveRoomBean.class);
            liveRoomBean.getUsers().add(userMapper.getUserByToken(userToken));
            liveRoomJSON = new ObjectMapper().writeValueAsString(liveRoomBean);
            redisUtil.set("liveRoom:"+liveRoomId, liveRoomJSON);
        }else {
//            redis中不存在数据
            LiveRoomBean liveRoomBean = new LiveRoomBean();
            liveRoomBean.setLiveRoomId(liveRoomId);
            liveRoomBean.setUsers(new ArrayList<>());
            liveRoomBean.getUsers().add(userMapper.getUserByToken(userToken));
            liveRoomJSON = new ObjectMapper().writeValueAsString(liveRoomBean);
            redisUtil.set("liveRoom:"+liveRoomId, liveRoomJSON);
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("liveRoomId") String liveRoomId,@PathParam("userToken") String userToken) throws JsonProcessingException {
        sessionMap.remove(userToken);
        String liveRoomJSON = (String) redisUtil.get("liveRoom:"+liveRoomId);
        if(liveRoomJSON!=null){
            LiveRoomBean liveRoomBean = new ObjectMapper().readValue(liveRoomJSON, LiveRoomBean.class);
            liveRoomBean.getUsers().remove(userMapper.getUserByToken(userToken));
            liveRoomJSON = new ObjectMapper().writeValueAsString(liveRoomBean);
            redisUtil.set("liveRoom:"+liveRoomId, liveRoomJSON);
        }
    }
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("liveRoomId") String liveRoomId,@PathParam("userToken") String userToken) throws IOException {
        User user = userMapper.getUserByToken(userToken);
        if (user.getUserRoleId() ==5) {
            String liveRoomJSON = (String) redisUtil.get("liveRoom:"+liveRoomId);
            if(liveRoomJSON!=null){
                LiveRoomBean liveRoomBean = new ObjectMapper().readValue(liveRoomJSON, LiveRoomBean.class);
                if (message.equals("close")){
                    System.out.println("关闭直播间"+liveRoomId);
                    for (int i = 0; i < liveRoomBean.getUsers().size(); i++) {
                        Session session1 = sessionMap.get(liveRoomBean.getUsers().get(i).getUserToken());
                        if (session1!=null){
                            session1.getBasicRemote().sendText("close");
                        }
                    }
                    liveMapper.closeLiveByLiveId(Integer.valueOf(liveRoomId));
                }
            }
        }
        if (user.getUserRoleId() ==4) {
            String liveRoomJSON = (String) redisUtil.get("liveRoom:"+liveRoomId);
            if(liveRoomJSON!=null){
                LiveRoomBean liveRoomBean = new ObjectMapper().readValue(liveRoomJSON, LiveRoomBean.class);
                if (message.equals("close")){
                    System.out.println("关闭直播间"+liveRoomId);
                    for (int i = 0; i < liveRoomBean.getUsers().size(); i++) {
                        Session session1 = sessionMap.get(liveRoomBean.getUsers().get(i).getUserToken());
                        if (session1!=null){
                            session1.getBasicRemote().sendText("close");
                        }
                    }
                    liveMapper.closeLiveByLiveId(Integer.valueOf(liveRoomId));
                }
            }
        }
    }
}
