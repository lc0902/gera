package com.alee.gera.service.impl;

import com.alee.gera.entity.R;
import com.alee.gera.entity.User;
import com.alee.gera.entity.Video;
import com.alee.gera.entity.Videostar;
import com.alee.gera.mapper.UserMapper;
import com.alee.gera.mapper.VideoMapper;
import com.alee.gera.mapper.VideostarMapper;
import com.alee.gera.service.VideostarService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class VideostarServiceImpl implements VideostarService {
    @Resource
    UserMapper userMapper;
    @Resource
    VideostarMapper videostarMapper;
    @Resource
    VideoMapper videoMapper;
    @Override
    public R insertVideostar(String userToken, Integer videoId) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        Video video = videoMapper.getVideoById(videoId);
        if(user!=null&&video!=null){
            Videostar videostar = videostarMapper.getVideostar(user.getUserId(),videoId);
            if(videostar==null){
                if(videostarMapper.insertVideostar(user.getUserId(),videoId)){
                    Integer starNum = videostarMapper.getVideostarNum(videoId);
                    videoMapper.updateVideoStarNumById(starNum,videoId);
                    r.setMsg("成功");
                }
            }
        }
        return r;
    }

    @Override
    public R deleteVideostar(String userToken, Integer videoId) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        Video video = videoMapper.getVideoById(videoId);
        if(user!=null&&video!=null){
            Videostar videostar = videostarMapper.getVideostar(user.getUserId(),videoId);
            if(videostar!=null){
                if(videostarMapper.deleteVideostar(user.getUserId(),videoId)){
                    Integer starNum = videostarMapper.getVideostarNum(videoId);
                    videoMapper.updateVideoStarNumById(starNum,videoId);
                    r.setMsg("成功");
                }
            }
        }
        return r;
    }

    @Override
    public R getUserVideostarList(String userToken) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if (user!=null){
            List<Videostar> videostarList = videostarMapper.getVideostarListByUserId(user.getUserId());
            r.setData(videostarList);
        }
        return r;
    }
}
