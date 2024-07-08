package com.alee.gera.controller;

import com.alee.gera.entity.R;
import com.alee.gera.entity.Video;
import com.alee.gera.service.UploadVideoService;
import com.alee.gera.service.VideoListService;
import com.alee.gera.service.VideoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class VideoController {
    @Resource
    UploadVideoService uploadVideoService;
    @PostMapping("/uploadVideo")
    public R uploadVideo(@RequestParam Map<String,Object> map, @RequestParam MultipartFile videoFile,@RequestParam MultipartFile coverFile){
        return uploadVideoService.uploadVideo(buildVideoByMap(map),videoFile,coverFile,(String) map.get("userToken"));
    }

    @Resource
    VideoListService videoListService;

    @PostMapping("/getVideoList")
    public R getVideoList(@RequestParam Map<String,Object> map){
        return videoListService.getVideoList(Integer.valueOf((String) map.get("pageNum")),Integer.valueOf((String) map.get("pageSize")),(String) map.get("videoDescription"));
    }
    @Resource
    VideoService videoService;

    @PostMapping("/viewVideo")
    public R viewVideo(@RequestParam String videoId){
        return videoService.viewVideo(Integer.valueOf(videoId));
    }

    @PostMapping("/getPersonalUploadVideo")
    public  R getPersonalUploadVideo(@RequestParam Map<String,Object> map){
        return videoService.getPersonalUploadVideo(
                Integer.valueOf((String) map.get("pageNum")),
                Integer.valueOf((String) map.get("pageSize")),
                (String) map.get("videoDescription"),
                (String) map.get("userToken")
        );
    }

    @PostMapping("/deletePersonalUploadVideo")
    public R deletePersonalUploadVideo(@RequestParam Map<String,Object> map){
        return videoService.deletePersonalUploadVideo(
                (String) map.get("userToken"),
                Integer.valueOf((String) map.get("videoId"))
        );
    }

    Video buildVideoByMap(Map<String,Object> map){
        Video video = new Video();
        if(map.get("videoId")!=null)
            video.setVideoId(Integer.valueOf((String) map.get("videoId")));
        if(map.get("videoUserId")!=null)
            video.setVideoUserId(Integer.valueOf((String) map.get("videoUserId")));
        if(map.get("videoGameId")!=null)
            video.setVideoGameId(Integer.valueOf((String) map.get("videoGameId")));
        if(map.get("videoUrl")!=null)
            video.setVideoUrl((String) map.get("videoUrl"));
        if(map.get("videoCoverUrl")!=null)
            video.setVideoCoverUrl((String) map.get("videoCoverUrl"));
        if(map.get("videoDescription")!=null)
            video.setVideoDescription((String) map.get("videoDescription"));
        return  video;
    }
}
