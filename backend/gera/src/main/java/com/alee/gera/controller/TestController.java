package com.alee.gera.controller;

import com.alee.gera.entity.User;
import com.alee.gera.websocket.RoomBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


@RestController
public class TestController {
//    @GetMapping("/test")
//    public void test() throws IOException {
//        RoomBean roomBean = new RoomBean();
//        roomBean.setRoomName("test");
//        roomBean.setRoomId("123456");
//        roomBean.setPassword("123456");
//        List<User> users = new ArrayList<>();
//        User user = new User();
//        user.setUserToken("sadas");
//        roomBean.setUsers(users);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json =  objectMapper.writeValueAsString(roomBean);
//        System.out.println(json);
//
//        RoomBean roomBean1 = objectMapper.readValue(json, RoomBean.class);
//        System.out.println(roomBean1);
//    }
}
