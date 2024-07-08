package com.alee.gera.service.impl;

import com.alee.gera.entity.Post;
import com.alee.gera.entity.Postcomment;
import com.alee.gera.entity.R;
import com.alee.gera.entity.User;
import com.alee.gera.mapper.PostcommentMapper;
import com.alee.gera.mapper.UserMapper;
import com.alee.gera.service.PostcommentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

@Service

public class PostcommentServiceImpl implements PostcommentService {
    @Resource
    PostcommentMapper postcommentMapper;
    @Resource
    UserMapper userMapper;
//    34
    @Override
    public R commentPost(Postcomment postcomment,String userToken) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if (user != null) {
            if (user.getUserStatus() != 0) {
                postcomment.setPostcommentUserId(user.getUserId());
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                postcomment.setPostcommentTime(timestamp);
                if (postcommentMapper.commentPost(postcomment)) {

                    r.setMsg("评论成功");
                } else {
                    r.setMsg("发生错误");
                }
            } else {
                r.setMsg("账号状态异常");

            }


        } else {
            r.setMsg("用户未登录");
        }
        return r;
    }
//    35
    @Override
    public R getPostcommentList(Postcomment postcomment,Integer pageNum,Integer pageSize){
        Map<String,Object> result = new HashMap<>();
        PageHelper.startPage(pageNum,pageSize);
        List<Postcomment> postcomments = postcommentMapper.getPostcommentByPostId(postcomment.getPostcommentPostId());
        PageInfo<Postcomment> pageInfo = new PageInfo<>(postcomments);
        List<User> users = new ArrayList();
        for(Postcomment postcomment1:postcomments){
            User t =userMapper.getUserById(postcomment1.getPostcommentUserId());
            t.setUserPassword(null);
            t.setUserToken(null);
            t.setUserEmail(null);
            users.add(t);
        }
        result.put("pageInfo",pageInfo);
        result.put("users",users);
        R r= new R();
        r.setData(result);
        return r;
    }
//36
    @Override
    public R deletePostcomment(Postcomment postcomment, String userToken) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            Postcomment postcomment1 = postcommentMapper.getPostcommentById(postcomment.getPostcommentId());
            if(Objects.equals(user.getUserId(), postcomment1.getPostcommentUserId())){
                if (postcommentMapper.deletePostcommentById(postcomment1.getPostcommentId())){
                    r.setMsg("删除成功");
                    r.setCode(3610);
                }else {
                    System.out.println("ERROR");
                }
            }else {
                System.out.println("not ==");
            }
        }
        return r;
    }

}
