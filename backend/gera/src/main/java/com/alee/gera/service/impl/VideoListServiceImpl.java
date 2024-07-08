package com.alee.gera.service.impl;

import com.alee.gera.entity.Game;
import com.alee.gera.entity.R;
import com.alee.gera.entity.User;
import com.alee.gera.entity.Video;
import com.alee.gera.mapper.GameMapper;
import com.alee.gera.mapper.UserMapper;
import com.alee.gera.mapper.VideoMapper;
import com.alee.gera.service.VideoListService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VideoListServiceImpl implements VideoListService {
    @Resource
    VideoMapper videoMapper;
    @Resource
    GameMapper gameMapper;
    @Resource
    UserMapper userMapper;
    @Override
    public R getVideoList(Integer pageNum, Integer pageSize, String videoDesc) {
        Map<String,Object> result = new HashMap<>();
        if(videoDesc==null){
            videoDesc="";
        }
        PageHelper.startPage(pageNum,pageSize);
        List<Video> videos = videoMapper.getVideoWithDesc(videoDesc);
        PageInfo<Video> pageInfo = new PageInfo<>(videos);
        List<Game> games = new ArrayList<>();
        List<User> users = new ArrayList<>();

        for (int i = 0; i < videos.size(); i++) {
            games.add(gameMapper.getGameById(videos.get(i).getVideoGameId()));
            User t= userMapper.getUserById(videos.get(i).getVideoUserId());
            t.setUserEmail(null);
            t.setUserToken(null);
            t.setUserPassword(null);
            users.add(t);
        }

        result.put("pageInfo",pageInfo);
        result.put("users",users);
        result.put("games",games);
        R r = new R();
        r.setData(result);

        return r;
    }
}
