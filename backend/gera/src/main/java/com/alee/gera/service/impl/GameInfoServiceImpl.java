package com.alee.gera.service.impl;

import com.alee.gera.entity.*;
import com.alee.gera.mapper.*;
import com.alee.gera.service.GameInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameInfoServiceImpl implements GameInfoService {

    @Resource
    GameMapper gameMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    VideoMapper videoMapper;
    @Resource
    PostMapper postMapper;
    @Resource
    LiveMapper liveMapper;
    @Override
    public R getGameInfo(Integer gameId) {
//        Map<String,Object> result = new HashMap<>();
        R r = new R();
        Game game = gameMapper.getGameById(gameId);
        if(game!=null){
            r.setData(game);
        }
        return r;
    }

    @Override
    public R getVideoListByGameId(Integer pageNum, Integer pageSize, String videoDesc, Integer gameId) {
        Map<String,Object> result = new HashMap<>();
        if(videoDesc==null){
            videoDesc="";
        }
        PageHelper.startPage(pageNum,pageSize);
        List<Video> videos = videoMapper.getVideoByGameId(gameId,videoDesc);
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

    @Override
    public R getLiveListByGameId(Integer pageNum, Integer pageSize, String liveDescription, Integer gameId) {
        Map<String,Object> result = new HashMap<>();
        PageHelper.startPage(pageNum,pageSize);
        List<Live> lives = liveMapper.getLiveListByGameId(gameId,liveDescription);
        PageInfo<Live> pageInfo = new PageInfo<>(lives);
        R r = new R();
        r.setMsg("成功");
        result.put("pageInfo",pageInfo);
        List<User> users = new ArrayList<>();
        List<Game> games = new ArrayList<>();
        for (int i = 0; i < lives.size(); i++) {
            User userT = userMapper.getUserById(lives.get(i).getLiveUserId());
            Game gameT = gameMapper.getGameById(lives.get(i).getLiveGameId());
            userT.setUserEmail(null);
            userT.setUserPassword(null);
            userT.setUserToken(null);
            games.add(gameT);
            users.add(userT);
        }
        result.put("users",users);
        result.put("games",games);
        r.setData(result);
        return r;
    }

    @Override
    public R getPostListByGameId(Integer pageNum, Integer pageSize, String postTitle, Integer gameId) {
        R r = new R();
        Map<String,Object> result = new HashMap<>();
        if(postTitle!=null){
//            获取帖子信息
            PageHelper.startPage(pageNum,pageSize);
            List<Post> posts = postMapper.getPostListByGameId(gameId,postTitle);
            PageInfo<Post> pageInfo = new PageInfo<>(posts);
//            获取对应游戏信息
            List<Game> games = new ArrayList<>();
            for(Post p : posts){
                games.add(gameMapper.getGameById(p.getPostGameId()));
            }
//            获取对应用户信息
            List<User> users = new ArrayList<>();
            for (Post p:posts){
                User t = userMapper.getUserById(p.getPostUserId());
                t.setUserPassword(null);
                t.setUserToken(null);
                t.setUserEmail(null);
                users.add(t);
            }
//            设置result
            result.put("pageInfo",pageInfo);
            result.put("games",games);
            result.put("users",users);

            r.setData(result);
            r.setCode(3210);
            r.setMsg("获取成功");
        }else {
            PageHelper.startPage(pageNum,pageSize);
            List<Post> posts = postMapper.getAllPost();
            PageInfo<Post> pageInfo = new PageInfo<>(posts);
//            获取对应游戏信息
            List<Game> games = new ArrayList<>();
            for(Post p : posts){
                games.add(gameMapper.getGameById(p.getPostGameId()));
            }
//            获取对应用户信息
            List<User> users = new ArrayList<>();
            for (Post p:posts){
                User t = userMapper.getUserById(p.getPostUserId());
                t.setUserPassword(null);
                t.setUserToken(null);
                t.setUserEmail(null);
                users.add(t);
            }
//            设置result
            result.put("pageInfo",pageInfo);
            result.put("games",games);
            result.put("users",users);

            r.setData(result);
            r.setCode(3210);
            r.setMsg("获取成功");
        }
        return r;
    }

    @Resource
    RateMapper rateMapper;
    @Override
    public R rate(String userToken, Integer gameId, Float rate) {
        User user = userMapper.getUserByToken(userToken);
        R r = new R();
        if(user!=null){
            Rate rate1 = rateMapper.getRateByUserIdAndGameId(user.getUserId(),gameId);
            if(rate1==null){
                Game game = gameMapper.getGameById(gameId);
                if(game!=null){
                    Rate rateT = new Rate();
                    rateT.setRateGameId(gameId);
                    rateT.setRateUserId(user.getUserId());
                    rateT.setRateRate(rate);
                    if(rateMapper.insertRate(rateT.getRateUserId(),rateT.getRateGameId(),rateT.getRateRate())){
                        Float avgRate = rateMapper.getAvgRate(gameId);
                        if(gameMapper.updateGameRate(avgRate,gameId)){
                            r.setMsg("成功");
                        }else{
                            r.setMsg("评分成功但平均错误");
                        }
                    }else{
                        r.setMsg("评分失败");
                    }
                }else {
                    r.setMsg("游戏不存在");
                }
            }else {
                r.setMsg("已评分");
            }
        }else {
            r.setMsg("用户不存在");
        }
        return r;
    }

    @Override
    public R getRateStatus(String userToken, Integer gameId) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            Rate rate = rateMapper.getRateByUserIdAndGameId(user.getUserId(),gameId);
            if(rate!=null){
                r.setMsg("存在");
            }else{
                r.setMsg("不存在");
            }
        }else {
            r.setMsg("用户不存在");
        }
        return r;
    }


}
