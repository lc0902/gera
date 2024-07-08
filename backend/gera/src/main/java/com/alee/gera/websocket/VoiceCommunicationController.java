package com.alee.gera.websocket;

import cn.hutool.core.util.IdUtil;
import com.alee.gera.entity.Game;
import com.alee.gera.entity.R;
import com.alee.gera.entity.User;
import com.alee.gera.mapper.GameMapper;
import com.alee.gera.mapper.UserMapper;
import com.alee.gera.util.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.core.ApplicationContext;
import org.slf4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;


@RestController
public class VoiceCommunicationController {
    Logger logger = org.slf4j.LoggerFactory.getLogger(VoiceCommunicationController.class);

    @Resource
    UserMapper userMapper;
    @Resource
    GameMapper gameMapper;
    @Resource
    RedisUtil redisUtil;

    @PostMapping("/createRoom")
    public R createRoom(@RequestParam Map<String, Object> map) throws JsonProcessingException {
        R r = new R();
        r.setMsg("");
        User user = userMapper.getUserByToken((String) map.get("userToken"));
        if(user!=null){
            if(user.getUserStatus()!=0){
                RoomBean roomBean = new RoomBean();
                String roomId = IdUtil.randomUUID();
                roomBean.setRoomId(roomId);
                roomBean.setOwnerId(user.getUserId());
                roomBean.setRoomName((String) map.get("roomName"));
                if(map.get("password")!=null){
                    roomBean.setHasPassword(true);
                    roomBean.setPassword((String) map.get("password"));
                }else {
                    roomBean.setHasPassword(false);
                }
                Game game = gameMapper.getGameById(Integer.valueOf((String) map.get("gameId")));
                if(game!=null){
                    roomBean.setGameId(game.getGameId());
                }else {
                    r.setMsg(r.getMsg()+"游戏不存在");
                }
                roomBean.setUsers(new ArrayList<>());
                redisUtil.set("room:"+roomId,new ObjectMapper().writeValueAsString(roomBean));
                r.setMsg(r.getMsg()+"房间创建成功");
                logger.info("房间创建成功!"+redisUtil.get("room:"+roomId));
            }else {
                r.setMsg(r.getMsg()+"用户状态异常");
            }
        }else {
            r.setMsg(r.getMsg()+"用户不存在");
        }
        return r;
    }

    @Resource
    RedisTemplate redisTemplate;

    @PostMapping("/getVoiceRoom")
    public R getVoiceRoom(@RequestParam Map<String, Object> map) throws JsonProcessingException {
        R r = new R();
        r.setMsg("");
        List<RoomBean> roomBeans = new ArrayList<>();
        Set<String> keys = redisTemplate.keys("room:*");
        for (String key : keys) {
            String value = (String) redisUtil.get(key);
            roomBeans.add(new ObjectMapper().readValue(value,RoomBean.class));
        }
//        System.out.println(roomBeans);
        r.setData(roomBeans);
        return r;
    }

    @PostMapping("/checkRoomPwd")
    public R checkRoomPwd(@RequestParam Map<String, Object> map) throws JsonProcessingException {
        R r = new R();
        r.setMsg("");
        if(map.get("userToken")!=null){
            User user = userMapper.getUserByToken((String) map.get("userToken"));
            if(user.getUserStatus()!=0){
                String roomBeanJSON = (String) redisUtil.get("room:"+(String) map.get("roomId"));
                System.out.println(roomBeanJSON);
                RoomBean roomBean = new ObjectMapper().readValue(roomBeanJSON,RoomBean.class);
                if (roomBean.hasPassword){
                    if(roomBean.getPassword().equals((String) map.get("roomPwd"))){
                        r.setMsg(r.getMsg()+"密码正确");
                        r.setCode(1);
                    }else {
                        r.setMsg(r.getMsg()+"密码错误");
                    }
                }else {
                    r.setMsg(r.getMsg()+"房间未设置密码");
                }
            }else {
                r.setMsg(r.getMsg()+"用户状态异常");
            }
        }else {
            r.setMsg(r.getMsg()+"用户不存在");
        }
        return r;
    }

    @PostMapping("/getRoomInfo")
    public R getRoomInfo(@RequestParam Map<String, Object> map) throws JsonProcessingException {
        R r = new R();
        String roomBeanJson = (String) redisUtil.get("room:"+(String) map.get("roomId"));
        RoomBean roomBean = new ObjectMapper().readValue(roomBeanJson,RoomBean.class);
        User user = userMapper.getUserById(roomBean.getOwnerId());
        user.setUserToken(null);
        Map<String,Object> result = new HashMap<>();
        result.put("owner",user);
        result.put("roomBean",roomBean);
        r.setData(result);
        return r;
    }
}
