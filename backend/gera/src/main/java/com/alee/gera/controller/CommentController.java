package com.alee.gera.controller;

import com.alee.gera.entity.Postcomment;
import com.alee.gera.entity.R;
import com.alee.gera.entity.Videocomment;
import com.alee.gera.mapper.PostcommentMapper;
import com.alee.gera.service.PostcommentService;
import com.alee.gera.service.VideocommentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class CommentController {

    @Resource
    PostcommentService postcommentService;

    @PostMapping("/commentPost")
    public R commentPost(@RequestParam Map<String,Object> map){
        Postcomment postcomment = buildPostcommentByMap(map);
        return postcommentService.commentPost(postcomment,(String) map.get("userToken"));
    }
    @PostMapping("/getPostcommentList")
    public R getPostcommentList(@RequestParam Map<String,Object> map){
        Postcomment postcomment = buildPostcommentByMap(map);

        return postcommentService.getPostcommentList(postcomment,Integer.valueOf((String) map.get("pageNum")),Integer.valueOf((String) map.get("pageSize")));
    }

    @PostMapping("/deletePostcomment")
    public R deletePostcomment(@RequestParam Map<String,Object> map){
        Postcomment postcomment = buildPostcommentByMap(map);
        return postcommentService.deletePostcomment(postcomment,(String) map.get("userToken"));
    }
    @Resource
    VideocommentService videocommentService;

    @PostMapping("/commentVideo")
    public R commentVideo(@RequestParam Map<String,Object> map){
        Videocomment videocomment = buildVideocommentByMap(map);
        return videocommentService.commentVideo(videocomment,(String) map.get("userToken"));
    }
    @PostMapping("/getVideocommentList")
    public  R getVideocommentList(@RequestParam Map<String,Object> map){
        return videocommentService.getVideocommentList(Integer.valueOf((String) map.get("videoId")),Integer.valueOf((String) map.get("pageNum")) ,Integer.valueOf((String) map.get("pageSize")));
    }
    @PostMapping("/deleteVideocomment")
    public R deleteVideocomment(@RequestParam Map<String,Object> map){
        Videocomment videocomment = buildVideocommentByMap(map);
        return videocommentService.deleteVideocomment(videocomment.getVideocommentId(), (String) map.get("userToken"));
    }








    Videocomment buildVideocommentByMap(Map<String,Object> map){
        Videocomment videocomment =new Videocomment();
        if(map.get("videocommentId")!=null) videocomment.setVideocommentId(Integer.valueOf((String)map.get("videocommentId")));
        if(map.get("videocommentVideoId")!=null) videocomment.setVideocommentVideoId(Integer.valueOf((String)map.get("videocommentVideoId")));
        if(map.get("videocommentUserId")!=null) videocomment.setVideocommentUserId(Integer.valueOf((String)map.get("videocommentUserId")));
        if (map.get("videocommentText")!=null) videocomment.setVideocommentText((String) map.get("videocommentText"));
        return videocomment;
    }



    Postcomment  buildPostcommentByMap(Map<String,Object> map){
        Postcomment postcomment = new Postcomment();
        if(map.get("postcommentText")!=null) postcomment.setPostcommentText((String) map.get("postcommentText"));
        if(map.get("postcommentPostId")!=null) postcomment.setPostcommentPostId(Integer.valueOf((String) map.get("postcommentPostId")));
        if(map.get("postcommentId")!=null) postcomment.setPostcommentId(Integer.valueOf((String) map.get("postcommentId")));
        return postcomment;
    }
}
