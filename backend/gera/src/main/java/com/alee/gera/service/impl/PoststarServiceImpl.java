package com.alee.gera.service.impl;

import com.alee.gera.entity.Post;
import com.alee.gera.entity.Poststar;
import com.alee.gera.entity.R;
import com.alee.gera.entity.User;
import com.alee.gera.mapper.PostMapper;
import com.alee.gera.mapper.PoststarMapper;
import com.alee.gera.mapper.UserMapper;
import com.alee.gera.service.PoststarService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PoststarServiceImpl implements PoststarService {
    @Resource
    UserMapper userMapper;
    @Resource
    PoststarMapper poststarMapper;
    @Resource
    PostMapper postMapper;
    @Override
    public R insertPoststar(String userToken, Integer postId) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        Post post = postMapper.getPostById(postId);
        if(user!=null&&post!=null){
            Poststar poststar = poststarMapper.getPoststar(user.getUserId(),postId);
            if(poststar==null)
                if(poststarMapper.insertPoststar(user.getUserId(),postId)){
                    Integer starNum= poststarMapper.getPoststarNum(postId);
                    postMapper.updatePostStarNum(starNum,postId);
                    r.setMsg("成功");
                }
        }
        return r;
    }

    @Override
    public R deletePoststar(String userToken, Integer postId) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        Post post = postMapper.getPostById(postId);

        if(user!=null&&post!=null){
            Poststar poststar = poststarMapper.getPoststar(user.getUserId(),postId);
            if(poststar!=null){
               if( poststarMapper.deletePoststar(user.getUserId(),postId)){
                   Integer starNum= poststarMapper.getPoststarNum(postId);
                   postMapper.updatePostStarNum(starNum,postId);
                   r.setMsg("成功");
               }
            }
        }
        return r;
    }

    @Override
    public R getUserPoststarList(String userToken) {

        R r = new R();
        User user = userMapper.getUserByToken(userToken);

        if(user!=null){
            List<Poststar> poststars = poststarMapper.getPoststarListByUserId(user.getUserId());
            r.setData(poststars);
        }
        return r;
    }
}
