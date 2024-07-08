package com.alee.gera.service.impl;

import com.alee.gera.entity.*;
import com.alee.gera.mapper.AdminMapper;
import com.alee.gera.mapper.GameMapper;
import com.alee.gera.mapper.LiveMapper;
import com.alee.gera.mapper.UserMapper;
import com.alee.gera.service.LiveControlService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LiveControlServiceImpl implements LiveControlService {
    @Resource
    UserMapper userMapper;
    @Resource
    GameMapper gameMapper;
    @Resource
    LiveMapper liveMapper;
    @Resource
    AdminMapper adminMapper;

//    fix 根据版主身份获取直播间列表
    @Override
    public R getLiveList(String userToken, Integer pageNum, Integer pageSize, String liveName) {
        if(liveName==null){
            liveName="";
        }
        User user = userMapper.getUserByToken(userToken);
        R r = new R();
        Map<String,Object> result = new HashMap<>();
        if (user != null){
            if (user.getUserRoleId()==5){
                PageHelper.startPage(pageNum,pageSize);
                List<Live> lives = liveMapper.getLivingList(liveName);
                PageInfo<Live> pageInfo = new PageInfo<>(lives);
                result.put("pageInfo",pageInfo);
                List<User> users = new ArrayList<>();
                List<Game> games = new ArrayList<>();
                for (int i = 0; i < lives.size(); i++) {
                    User user1 = userMapper.getUserById(lives.get(i).getLiveUserId());
                    Game game = gameMapper.getGameById(lives.get(i).getLiveGameId());
                    user1.setUserToken(null);
                    users.add(user1);
                    games.add(game);
                }
                result.put("users",users);
                result.put("games",games);

            }
            if (user.getUserRoleId()==4){
                Admin admin = adminMapper.isAdmin(user.getUserId());
                PageHelper.startPage(pageNum,pageSize);
                List<Live> lives = liveMapper.getLiveListByGameId(admin.getAdminGameId(),liveName);
                PageInfo<Live> pageInfo = new PageInfo<>(lives);
                result.put("pageInfo",pageInfo);
                List<User> users = new ArrayList<>();
                List<Game> games = new ArrayList<>();
                for (int i = 0; i < lives.size(); i++) {
                    User user1 = userMapper.getUserById(lives.get(i).getLiveUserId());
                    Game game = gameMapper.getGameById(lives.get(i).getLiveGameId());
                    user1.setUserToken(null);
                    users.add(user1);
                    games.add(game);
                }
                result.put("users",users);
                result.put("games",games);

            }
        }else {

        }
        r.setData(result);
        return r;
    }

    @Override
    public R closeLive(String userToken, String liveId) {
        return null;
    }
}
