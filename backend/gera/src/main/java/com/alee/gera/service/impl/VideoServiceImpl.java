package com.alee.gera.service.impl;

import com.alee.gera.entity.Game;
import com.alee.gera.entity.R;
import com.alee.gera.entity.User;
import com.alee.gera.entity.Video;
import com.alee.gera.mapper.*;
import com.alee.gera.service.VideoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class VideoServiceImpl implements VideoService {

    @Resource
    VideoMapper videoMapper;
    @Resource
    GameMapper gameMapper;
    @Resource
    UserMapper userMapper;
    @Override
    public R viewVideo(Integer videoId) {
        R r = new R();
        Map<String,Object> result = new HashMap<>();
        Video video = videoMapper.getVideoById(videoId);
        if(video!=null){
            User user = userMapper.getUserById(video.getVideoUserId());
            Game game = gameMapper.getGameById(video.getVideoGameId());
            user.setUserEmail(null);
            user.setUserToken(null);
            user.setUserPassword(null);
            result.put("user",user);
            result.put("game",game);
        }
        result.put("video",video);
        r.setData(result);
        return r;
    }

    @Override
    public R getPersonalUploadVideo(Integer pageNum, Integer pageSize, String videoDescription, String userToken) {
        User user = userMapper.getUserByToken(userToken);
        Map<String,Object> result = new HashMap<>();
        R r= new R();
        if(user!=null){
            PageHelper.startPage(pageNum,pageSize);
            List<Video> videos = videoMapper.getVideoByUserId(user.getUserId(),videoDescription);
            PageInfo pageInfo = new PageInfo(videos);

            List<Game> games = new ArrayList<>();
            List<User> users = new ArrayList<>();
            for (int i = 0; i < videos.size(); i++) {
                Game game = gameMapper.getGameById(videos.get(i).getVideoGameId());
                games.add(game);
                User user1 = userMapper.getUserById(videos.get(i).getVideoUserId());
                user1.setUserPassword(null);
                user1.setUserToken(null);
                user1.setUserEmail(null);
                users.add(user1);
            }
            result.put("users",users);
            result.put("games",games);
            result.put("pageInfo",pageInfo);
            result.put("videos",videos);
            r.setData(result);
        }else {
            r.setMsg("用户为空");
        }
        return r;
    }

    @Resource
    VideostarMapper videostarMapper;
    @Resource
    VideocollectMapper videocollectMapper;
    @Resource
    VideocommentMapper videocommentMapper;

    @Override
    public R deletePersonalUploadVideo(String userToken, Integer videoId) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            Video video = videoMapper.getVideoById(videoId);
            if(Objects.equals(video.getVideoUserId(), user.getUserId())){
                videostarMapper.deleteVideostarByVideoId(videoId);
                videocommentMapper.deleteVideocommentByVideoId(videoId);
                videocollectMapper.deleteVideocollectByVideoId(videoId);
                videoMapper.deleteVideoById(videoId);
                r.setMsg("成功");
            }
        }else {

        }
        return r;
    }
}
