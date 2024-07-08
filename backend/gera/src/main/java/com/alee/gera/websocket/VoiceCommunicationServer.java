package com.alee.gera.websocket;
import com.alee.gera.entity.User;
import com.alee.gera.mapper.UserMapper;
import com.alee.gera.util.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

@Controller
@ServerEndpoint(value = "/voice/{userToken}/{roomId}")
public class VoiceCommunicationServer {
    private static Map<String, Session> webSocketSet = new HashMap<>();
    private String token;
    private String roomID;
    private static UserMapper userMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        VoiceCommunicationServer.userMapper = userMapper;
    }

    private static RedisUtil redisUtil;
    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        VoiceCommunicationServer.redisUtil = redisUtil;
    }




//    房间密码核验成功，用户加入语音房间
    @OnOpen
    public void onOpen(@PathParam("userToken")String userToken, @PathParam("roomId")String roomId, Session session) throws JsonProcessingException {
        User user = userMapper.getUserByToken(userToken);

        if (user != null) {
            if(user.getUserStatus()!=0){
//                用户状态无误，将用户加入到set中
                webSocketSet.put(userToken, session);
//                获取房间信息
                String roomBeanJson = (String) redisUtil.get("room:"+roomId);
                RoomBean roomBean = new ObjectMapper().readValue(roomBeanJson, RoomBean.class);
//                更新roomBean，反写到redis中
                roomBean.getUsers().add(user);
                roomBeanJson = new ObjectMapper().writeValueAsString(roomBean);
                redisUtil.set("room:"+roomId, roomBeanJson);
                this.token = userToken;
                this.roomID = roomId;
                System.out.println("用户"+userToken+"加入房间"+roomId);
            }else {
//                用户账号异常
            }
        }else {
//用户未登录
        }
    }


//    用户退出语音房间或者异常
    @OnClose
    public void onclose() throws IOException {
//        获取房间信息
        String roomBeanJson = (String) redisUtil.get("room:"+roomID);
        RoomBean roomBean = new ObjectMapper().readValue(roomBeanJson, RoomBean.class);

//        判断用户是否在房间中
        List<User> users = roomBean.getUsers();
        for (User user : users){
            if(user.getUserToken().equals(token)){
//                如果用户在房间中
//                如果用户是房主
                if (Objects.equals(user.getUserId(), roomBean.getOwnerId())){
//                    移除房间所有用户，并删除redis内容
                    redisUtil.delete("room:"+roomID);
                    System.out.println("房间"+roomID+"被删除");
                    for (User user1 : users){
                        webSocketSet.get(user1.getUserToken()).close();
                        System.out.println("用户"+user1.getUserToken()+"被移除");
                    }
                }else {
                    //                移除用户，反写到redis中
                    users.remove(user);
                    roomBeanJson = new ObjectMapper().writeValueAsString(roomBean);
                    redisUtil.set("room:"+roomID, roomBeanJson);
                    System.out.println("用户"+token+"退出房间"+roomID);

                }

                break;
            }
        }

    }


    @OnMessage(maxMessageSize=5242880)
    public void onMessage(@PathParam("userToken")String userToken, @PathParam("roomId")String roomId, Session session,InputStream inputStream) {
        try {
            String roomBeanJson = (String) redisUtil.get("room:"+roomId);
            RoomBean roomBean = new ObjectMapper().readValue(roomBeanJson, RoomBean.class);
            for(User user : roomBean.getUsers()){
                if(user.getUserToken().equals(userToken)){
//                    是本人则不发送
                }else {
//                    非本人则发送
                    byte[] buff = new byte[inputStream.available()];
                    inputStream.read(buff, 0, inputStream.available());
                    Session session1 = webSocketSet.get(user.getUserToken());
                    synchronized (session1){
                    session1.getBasicRemote().sendBinary(ByteBuffer.wrap(buff));
                }

                }
            }
//            byte[] buff = new byte[inputStream.available()];
//            inputStream.read(buff, 0, inputStream.available());
////            Session session = webSocketSet.get(recipientId);
//            synchronized (session) {
//                //给2号发送
//                session.getBasicRemote().sendBinary(ByteBuffer.wrap(buff));
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
