package com.alee.gera.controller;

import com.alee.gera.entity.R;
import com.alee.gera.service.PostListService;
import com.alee.gera.service.PostcollectService;
import com.alee.gera.service.VideocollectService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class CollectController {
    @Resource
    PostcollectService postcollectService;
    @PostMapping("/collectPost")
    R collectPost(@RequestParam Map<String,Object> map){
        return postcollectService.insertPostcollect((String) map.get("userToken"),Integer.valueOf((String) map.get("postId")));
    }

    @PostMapping("/cancelCollectPost")
    R cancelCollectPost(@RequestParam Map<String,Object> map){
        return postcollectService.deletePostcollect((String) map.get("userToken"),Integer.valueOf((String) map.get("postId")));
    }

    @PostMapping("/getCollectPostList")
    R getCollectPostList(@RequestParam Map<String,Object> map){
        return postcollectService.getUserPostcollectList((String) map.get("userToken"));
    }

    @Resource
    VideocollectService videocollectService;
    @PostMapping("/collectVideo")
    R collectVideo(@RequestParam Map<String,Object> map){
        return videocollectService.insertVideocollect((String) map.get("userToken"),Integer.valueOf((String) map.get("videoId")));
    }
    @PostMapping("/cancelCollectVideo")
    R cancelCollectVideo(@RequestParam Map<String,Object> map){
        return videocollectService.deleteVideocollect((String) map.get("userToken"),Integer.valueOf((String) map.get("videoId")));
    }
    @PostMapping("/getCollectVideoList")
    R getCollectVideoList(@RequestParam Map<String,Object> map){
        return videocollectService.getUserVideocollectList((String) map.get("userToken"));
    }

//    done 用户收藏列表
    @PostMapping("/getCollectPostListByUserId")
    R getCollectPostListByUserId(@RequestParam Map<String,Object> map){
        return postcollectService.getCollectPostListByUserToken(
                Integer.valueOf((String) map.get("pageNum")),
                Integer.valueOf((String) map.get("pageSize")),
                (String) map.get("userToken"),
                (String) map.get("postTitle")
        );
    }
    @PostMapping("/getCollectVideoListByUserId")
    R getCollectVideoListByUserId(@RequestParam Map<String,Object> map){
        return  videocollectService.getCollectVideoListByUserToken(
                Integer.valueOf((String) map.get("pageNum")),
                Integer.valueOf((String) map.get("pageSize")),
                (String) map.get("userToken"),
                (String) map.get("videoDescription")
        );
    }
}
