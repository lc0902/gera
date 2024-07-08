package com.alee.gera.service.impl;

import com.alee.gera.entity.R;
import com.alee.gera.entity.User;
import com.alee.gera.entity.Video;
import com.alee.gera.mapper.UserMapper;
import com.alee.gera.mapper.VideoMapper;
import com.alee.gera.service.UploadVideoService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UploadVideoServiceImpl implements UploadVideoService {
    @Resource
    VideoMapper videoMapper;
    @Resource
    UserMapper userMapper;
    @Override
    public R uploadVideo(Video video, MultipartFile videoFile, MultipartFile coverFile,String userToken) {
        R r = new R();
        String videoFileName = UUID.randomUUID()+".mp4";
        String coverFileName = UUID.randomUUID()+".cover";
        File videoF = new File("/D:/GraduationProject/StreamServer/nginx-rtmp/html/video/"+videoFileName);
        File coverF = new File("/D:/GraduationProject/StreamServer/nginx-rtmp/html/video/cover/"+coverFileName);
        video.setVideoUpTime(new Timestamp(System.currentTimeMillis()));
        video.setVideoUrl("http://localhost/video/"+videoFileName);
        video.setVideoCoverUrl("http://localhost/video/cover/"+coverFileName);
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            if (user.getUserRoleId()>1&&user.getUserStatus()==1){
                try{
                    video.setVideoUserId(user.getUserId());
                    videoFile.transferTo(videoF);
                    coverFile.transferTo(coverF);
                    videoMapper.insertVideo(video);
                    r.setData(video);
                    r.setMsg("成功");

                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                r.setMsg("用户无权限");
            }
        }else {
            r.setMsg("用户不存在");
        }

        return r;
    }
}
