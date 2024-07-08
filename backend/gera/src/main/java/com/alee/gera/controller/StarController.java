package com.alee.gera.controller;

import com.alee.gera.entity.R;
import com.alee.gera.service.PoststarService;
import com.alee.gera.service.VideostarService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class StarController {

    @Resource
    PoststarService poststarService;
    @PostMapping("/insertPoststar")
    R insertPoststar(@RequestParam Map<String,Object> map){
        return poststarService.insertPoststar((String) map.get("userToken"),Integer.valueOf((String) map.get("postId")));
    }
    @PostMapping("/cancelPoststar")
    R canclePoststar(@RequestParam Map<String,Object> map){
        return poststarService.deletePoststar((String) map.get("userToken"),Integer.valueOf((String) map.get("postId")));
    }
    @PostMapping("/getUserPoststarList")
    R getUserPoststarList(@RequestParam Map<String,Object> map){
        return poststarService.getUserPoststarList((String) map.get("userToken"));
    }

    @Resource
    VideostarService videostarService;
    @PostMapping("/insertVideostar")
    R insertVideostar(@RequestParam Map<String,Object> map){
        return videostarService.insertVideostar((String) map.get("userToken"),Integer.valueOf((String) map.get("videoId")));
    }
    @PostMapping("/cancelVideostar")
    R cancelVideostar(@RequestParam Map<String,Object> map){
        return videostarService.deleteVideostar((String) map.get("userToken"),Integer.valueOf((String) map.get("videoId")));
    }
    @PostMapping("/getUserVideostarList")
    R getUserVideostarList(@RequestParam Map<String,Object> map){
        return videostarService.getUserVideostarList((String) map.get("userToken"));
    }
}
