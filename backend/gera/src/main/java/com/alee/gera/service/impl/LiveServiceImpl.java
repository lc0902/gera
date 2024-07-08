package com.alee.gera.service.impl;

import com.alee.gera.entity.Game;
import com.alee.gera.entity.Live;
import com.alee.gera.entity.R;
import com.alee.gera.entity.User;
import com.alee.gera.mapper.GameMapper;
import com.alee.gera.mapper.LiveMapper;
import com.alee.gera.mapper.UserMapper;
import com.alee.gera.service.LiveService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class LiveServiceImpl implements LiveService {
    @Value("${live.prefix}")
    String liveUrl;
    @Resource
    LiveMapper liveMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    GameMapper gameMapper;
    @Override
    public R getLiveUrl(String userToken) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            if(user.getUserRoleId()>=3){
                Live live = liveMapper.getLiveByUserId(user.getUserId());
                if(live==null){//用户第一次开启直播
                    String url = UUID.randomUUID().toString();
                    url=liveUrl+url;
                    Live t= new Live();
                    t.setLiveUserId(user.getUserId());
                    t.setLiveUrl(url);
                    if(liveMapper.initLiveByUserId(t)){
                        r.setMsg("成功");
                        r.setData(t);
                    }else{
                        r.setMsg("发生未知错误");
                    }
                }else{//用户以前直播过
                    r.setMsg("成功");
                    r.setData(live);
                }
            }else {
                r.setMsg("用户权限不足");
            }
        }else {
            r.setMsg("用户不存在");
        }
        return r;
    }

    @Override
    public R uploadLiveInfo(Live live, String userToken, MultipartFile multipartFile) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        String url = UUID.randomUUID().toString();
        String path = "/D:/GraduationProject/StreamServer/nginx-rtmp/html/pic/liveFile/cover/"+url+".jpg";
        url="http://localhost/pic/liveFile/cover/"+url+".jpg";
        live.setLiveCoverUrl(url);
        if(user!=null){
            try {
                Live t = liveMapper.getLiveByUserId(user.getUserId());
                if(t!=null){
                    t.setLiveCoverUrl(live.getLiveCoverUrl());
                    t.setLiveGameId(live.getLiveGameId());
                    if(live.getLiveDescription()!=null)
                        t.setLiveDescription(live.getLiveDescription());
                    else t.setLiveDescription("");
                    multipartFile.transferTo(new File(path));
                    liveMapper.updateLiveInfo(t);
                    r.setMsg("成功");
                    r.setData(t);

                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }else {
            r.setMsg("用户不存在");
        }
        return r;
    }

    @Override
    public R startLive(String userToken) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            if (user.getUserStatus()!=0){
                if ((liveMapper.startLive(user.getUserId()))){
                    r.setData(liveMapper.getLiveByUserId(user.getUserId()));
                    r.setMsg("成功");
                }
            }else {
                r.setMsg("用户状态异常");
            }
        }
        return r;
    }

    @Override
    public R closeLive(String userToken) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            if ((liveMapper.closeLive(user.getUserId()))){
                r.setData(liveMapper.getLiveByUserId(user.getUserId()));
                r.setMsg("成功");
            }
        }
        return r;
    }

    @Override
    public R getLiveList(String userToken, String liveDescription,Integer pageNum,Integer pageSize) {
        Map<String,Object> result = new HashMap<>();
        PageHelper.startPage(pageNum,pageSize);
        List<Live> lives = liveMapper.getLivingList(liveDescription);
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
    public R getLiveStatus(String userToken) {
        R r =new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            r.setData(liveMapper.getLiveByUserId(user.getUserId()));
        }
        return r;
    }

    @Override
    public R viewLive(String userToken, Integer liveId) {
        Map<String,Object> result = new HashMap<>();
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null&&user.getUserStatus()!=0){
            Live live = liveMapper.getLiveById(liveId);
            if(live!=null){
                result.put("live",live);
                Game game = gameMapper.getGameById(live.getLiveGameId());
                result.put("game",game);
                user.setUserToken(null);
                user.setUserEmail(null);
                user.setUserPassword(null);
                result.put("user",user);
                r.setData(result);
                r.setMsg("成功");
            }
        }
        return r;
    }


}
