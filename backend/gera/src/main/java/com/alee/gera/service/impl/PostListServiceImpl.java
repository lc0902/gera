package com.alee.gera.service.impl;

import com.alee.gera.entity.Game;
import com.alee.gera.entity.Post;
import com.alee.gera.entity.R;
import com.alee.gera.entity.User;
import com.alee.gera.mapper.GameMapper;
import com.alee.gera.mapper.PostMapper;
import com.alee.gera.mapper.UserMapper;
import com.alee.gera.service.PostListService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostListServiceImpl implements PostListService {
    @Resource
    PostMapper postMapper;
    @Resource
    GameMapper gameMapper;
    @Resource
    UserMapper userMapper;
//    32
    @Override
    public R getPostList(Integer pageNum, Integer pageSize, String postTitle) {
        R r = new R();
        Map<String,Object> result = new HashMap<>();
        if(postTitle!=null){
//            获取帖子信息
            PageHelper.startPage(pageNum,pageSize);
            List<Post> posts = postMapper.getPostListByTitle(postTitle);
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

    @Override
    public R getPersonalPostList(Integer pageNum, Integer pageSize, String postTitle, String userToken) {
        R r = new R();
        Map<String,Object> result = new HashMap<>();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            PageHelper.startPage(pageNum,pageSize);
            List<Post> posts = postMapper.getPersonalPost(user.getUserId(),postTitle);
            PageInfo pageInfo = new PageInfo(posts);
            List<Game> games = new ArrayList<>();
            List<User> users = new ArrayList<>();
            for (int i = 0; i < posts.size(); i++) {
                Game game = gameMapper.getGameById(posts.get(i).getPostGameId());
                games.add(game);
                User user1 = userMapper.getUserById(posts.get(i).getPostUserId());
                user1.setUserEmail(null);
                user1.setUserPassword(null);
                user1.setUserToken(null);
                users.add(user1);
            }
            result.put("pageInfo",pageInfo);
            result.put("posts",posts);
            result.put("games",games);
            result.put("users",users);
            r.setData(result);
        }else {
            r.setMsg("用户为空");
        }
        return r;
    }
}
