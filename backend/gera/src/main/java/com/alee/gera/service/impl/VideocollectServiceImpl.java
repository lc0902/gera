package com.alee.gera.service.impl;

import com.alee.gera.entity.*;
import com.alee.gera.mapper.GameMapper;
import com.alee.gera.mapper.UserMapper;
import com.alee.gera.mapper.VideoMapper;
import com.alee.gera.mapper.VideocollectMapper;
import com.alee.gera.service.VideocollectService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VideocollectServiceImpl implements VideocollectService {
    @Resource
    VideocollectMapper videocollectMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    VideoMapper videoMapper;
    @Override
    public R insertVideocollect(String userToken, Integer videoId) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        Video video = videoMapper.getVideoById(videoId);
        if(user!=null&&video!=null){
            Videocollect videocollect = videocollectMapper.getVideocollect(user.getUserId(),videoId);
            if(videocollect==null){
                if(videocollectMapper.insertVideocollect(user.getUserId(),videoId)){
                    r.setMsg("成功");
                }
            }
        }
        return r;
    }

    @Override
    public R deleteVideocollect(String userToken, Integer videoId) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        Video video = videoMapper.getVideoById(videoId);
        if(user!=null&&video!=null){
            Videocollect videocollect = videocollectMapper.getVideocollect(user.getUserId(),videoId);
            if(videocollect!=null){
                if(videocollectMapper.deleteVideocollect(user.getUserId(),videoId)){
                    r.setMsg("成功");
                }
            }
        }
        return r;
    }

    @Override
    public R getUserVideocollectList(String userToken) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            List<Videocollect> videocollects = videocollectMapper.getVideocollectListByUserId(user.getUserId());
            r.setData(videocollects);
        }
        return r;
    }


    @Resource
    GameMapper gameMapper;

    @Override
    public R getCollectVideoListByUserToken(Integer pageNum, Integer pageSize, String userToken,String videoTitle) {
        R r = new R();
        Map<String,Object> result =new HashMap<>();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            PageHelper.startPage(pageNum,pageSize);
            List<Videocollect> videocollects = videocollectMapper.getVideocollectListByUserIdWithDesc(user.getUserId(),videoTitle);
            PageInfo<Videocollect> pageInfo = new PageInfo(videocollects);

            List<Video> videos = new ArrayList<>();
            List<Game> games = new ArrayList<>();
            List<User> users = new ArrayList<>();

            for (int i = 0; i < videocollects.size(); i++) {
                Video video = videoMapper.getVideoById(videocollects.get(i).getVideocollectVideoId());
                videos.add(video);
                Game game = gameMapper.getGameById(video.getVideoGameId());
                games.add(game);
                User user1 = userMapper.getUserById(video.getVideoUserId());
                user1.setUserPassword(null);
                user1.setUserToken(null);
                user1.setUserEmail(null);
                users.add(user1);
            }
            result.put("pageInfo",pageInfo);
            result.put("users",users);
            result.put("games",games);
            result.put("videos",videos);
            r.setData(result);
        }else {
            r.setMsg("用户为空");
        }
        return  r;
    }
}
