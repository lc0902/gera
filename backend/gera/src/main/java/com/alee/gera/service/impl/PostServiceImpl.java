package com.alee.gera.service.impl;

import com.alee.gera.entity.Game;
import com.alee.gera.entity.Post;
import com.alee.gera.entity.R;
import com.alee.gera.entity.User;
import com.alee.gera.mapper.*;
import com.alee.gera.service.PostService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class PostServiceImpl implements PostService {

    @Resource
    PostMapper postMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    GameMapper gameMapper;
//    33
    @Override
    public R viewPost(Integer postId) {
        R r = new R();
        Map<String,Object> result = new HashMap<>();
        Post post = postMapper.getPostById(postId);
        if(post!=null){
            result.put("post",post);
            User user = userMapper.getUserById(post.getPostUserId());
            result.put("user",user);
            Game game = gameMapper.getGameById(post.getPostGameId());
            result.put("game",game);
            r.setCode(3310);
        }else {
            r.setCode(3301);
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
    public R deletePost(Integer postId, String userToken) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            Post post = postMapper.getPostById(postId);
            if(Objects.equals(post.getPostUserId(), user.getUserId())){
//                删除所有外键data
                postcollectMapper.deletePostcollectByPostId(postId);
                postcommentMapper.deletePostcommentByPostId(postId);
                poststarMapper.deletePoststarByPostId(postId);
                postMapper.deletePost(postId);
                r.setMsg("成功");
            }else{
                r.setMsg("用户无权限");
            }
        }
        return r;
    }
}
