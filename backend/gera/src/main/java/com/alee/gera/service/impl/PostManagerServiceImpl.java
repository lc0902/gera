package com.alee.gera.service.impl;

import com.alee.gera.entity.*;
import com.alee.gera.mapper.*;
import com.alee.gera.service.PostManagerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class PostManagerServiceImpl implements PostManagerService {
    @Resource
    UserMapper userMapper;
    @Resource
    PostMapper postMapper;
    @Resource
    GameMapper gameMapper;
    @Resource
    AdminMapper adminMapper;
    @Override
    public R getPostList(String userToken,Integer pageNum,Integer pageSize,String postTitle){
        if(postTitle==null){
            postTitle="";
        }
        R r = new R();
        Map<String,Object> result = new HashMap<>();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            if(user.getUserRoleId()==5){
                PageHelper.startPage(pageNum,pageSize);
                List<Post> posts = postMapper.getPostListByTitle(postTitle);
                PageInfo<Post> pageInfo = new PageInfo<>(posts);
                result.put("pageInfo",pageInfo);
                List<User> users = new ArrayList<>();
                List<Game> games = new ArrayList<>();
                for (int i = 0; i < posts.size(); i++) {
                    Game game = gameMapper.getGameById(posts.get(i).getPostGameId());
                    games.add(game);
                    User user1 = userMapper.getUserById(posts.get(i).getPostUserId());
                    user1.setUserPassword(null);
                    user1.setUserToken(null);
                    users.add(user1);
                }
                result.put("users",users);
                result.put("games",games);
            }else if(user.getUserRoleId()==4){
                if(user.getUserStatus()!=0){
                    Admin admin = adminMapper.isAdmin(user.getUserId());
                    if(admin!=null){
                        PageHelper.startPage(pageNum,pageSize);
                        List<Post> posts = postMapper.getPostListByGameIdAndTitle(admin.getAdminGameId(),postTitle);
                        PageInfo<Post> pageInfo = new PageInfo<>(posts);
                        result.put("pageInfo",pageInfo);
                        List<User> users = new ArrayList<>();
                        List<Game> games = new ArrayList<>();
                        for (int i = 0; i < posts.size(); i++) {
                            Game game = gameMapper.getGameById(posts.get(i).getPostGameId());
                            games.add(game);
                            User user1 = userMapper.getUserById(posts.get(i).getPostUserId());
                            user1.setUserPassword(null);
                            user1.setUserToken(null);
                            users.add(user1);
                        }
                        result.put("users",users);
                        result.put("games",games);
                    }

                }
            }
        }else {
            r.setMsg("用户不存在");
        }
        r.setData(result);
        return r;
    }

    @Resource
    PostcollectMapper postcollectMapper;
    @Resource
    PostcommentMapper postcommentMapper;
    @Resource
    PoststarMapper poststarMapper;
    @Override
    public R deletePost(String userToken, Integer postId) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            Post post = postMapper.getPostById(postId);
            if(user.getUserRoleId()==5){
//                删除所有外键data
                postcollectMapper.deletePostcollectByPostId(postId);
                postcommentMapper.deletePostcommentByPostId(postId);
                poststarMapper.deletePoststarByPostId(postId);
                postMapper.deletePost(postId);
                r.setMsg("成功");
            }else if(user.getUserRoleId()==4&&user.getUserStatus()!=0){
//                删除所有外键data
                postcollectMapper.deletePostcollectByPostId(postId);
                postcommentMapper.deletePostcommentByPostId(postId);
                poststarMapper.deletePoststarByPostId(postId);
                postMapper.deletePost(postId);
                r.setMsg("成功");
            }else
            {
                r.setMsg("用户无权限");
            }
        }
        return r;
    }
}
