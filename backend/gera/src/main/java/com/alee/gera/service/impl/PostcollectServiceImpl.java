package com.alee.gera.service.impl;

import com.alee.gera.entity.*;
import com.alee.gera.mapper.GameMapper;
import com.alee.gera.mapper.PostMapper;
import com.alee.gera.mapper.PostcollectMapper;
import com.alee.gera.mapper.UserMapper;
import com.alee.gera.service.PostcollectService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostcollectServiceImpl implements PostcollectService {
    @Resource
    PostcollectMapper postcollectMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    PostMapper postMapper;
    @Resource
    GameMapper gameMapper;
    @Override
    public R insertPostcollect(String userToken, Integer postId) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        Post post = postMapper.getPostById(postId);
        if(user!=null&&post!=null){
            Postcollect postcollect= postcollectMapper.getPostcollect(user.getUserId(),postId);
            if(postcollect==null){
                if (postcollectMapper.insertPostcollect(user.getUserId(),postId)){
                    r.setMsg("成功");
                }
            }
        }
        return r;
    }

    @Override
    public R deletePostcollect(String userToken, Integer postId) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        Post post = postMapper.getPostById(postId);
        if(user!=null&&post!=null){
            Postcollect postcollect= postcollectMapper.getPostcollect(user.getUserId(),postId);
            if(postcollect!=null){
                if(postcollectMapper.deletePostcollect(user.getUserId(),postId)){
                    r.setMsg("成功");
                }
            }
        }
        return r;
    }

    @Override
    public R getUserPostcollectList(String userToken) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            List<Postcollect> postcollects = postcollectMapper.getPostcollectListByUserId(user.getUserId());
            r.setData(postcollects);
        }
        return r;
    }

    @Override
    public R getCollectPostListByUserToken(Integer pageNum, Integer pageSize, String userToken,String postTitle) {
        Map<String,Object> result = new HashMap<>();
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
          PageHelper.startPage(pageNum,pageSize);
          List<Postcollect> postcollects = postcollectMapper.getPostcollectListByUserIdWithTitle(user.getUserId(),postTitle);
          PageInfo pageInfo = new PageInfo(postcollects);
          List<Post> posts = new ArrayList<>();
          List<Game> games = new ArrayList<>();
          List<User> users = new ArrayList<>();
          for (int i = 0; i < postcollects.size(); i++) {
              Post post = postMapper.getPostById(postcollects.get(i).getPostcollectPostId());
              posts.add(post);
              Game game = gameMapper.getGameById(post.getPostGameId());
              games.add(game);
              User user1 = userMapper.getUserById(post.getPostUserId());
              user1.setUserPassword(null);
              user1.setUserToken(null);
              user1.setUserEmail(null);
              users.add(user1);
          }
          result.put("posts",posts);
          result.put("games",games);
          result.put("users",users);
          result.put("pageInfo",pageInfo);
          r.setData(result);
      }else {
          r.setMsg("用户为空");
      }
      return r;
    }



}
