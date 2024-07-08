package com.alee.gera.service.impl;

import com.alee.gera.entity.R;
import com.alee.gera.entity.User;
import com.alee.gera.entity.Videocomment;
import com.alee.gera.mapper.UserMapper;
import com.alee.gera.mapper.VideocommentMapper;
import com.alee.gera.service.VideoService;
import com.alee.gera.service.VideocommentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

@Service
public class VideocommentServiceImpl implements VideocommentService {
    @Resource
    UserMapper userMapper;
    @Resource
    VideocommentMapper videocommentMapper;

    @Override
    public R commentVideo(Videocomment videocomment, String userToken) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            if(user.getUserStatus()!=0){
                videocomment.setVideocommentUserId(user.getUserId());
                videocomment.setVideocommentTime(new Timestamp(System.currentTimeMillis()));
                if(videocommentMapper.insertVideocomment(videocomment)){
                    r.setMsg("成功");
                }
            }else {
                r.setMsg("账号异常");
            }
        }else {
            r.setMsg("用户不存在");
        }
        return r;
    }
    @Override
    public R getVideocommentList(Integer videoId,Integer pageNum,Integer pageSize) {
        Map<String,Object> result = new HashMap<>();
        PageHelper.startPage(pageNum,pageSize);
        List<Videocomment> videocomments = videocommentMapper.getVideocommentListByVideoId(videoId);
        PageInfo<Videocomment> pageInfo = new PageInfo<>(videocomments);
        List<User> users = new ArrayList<>();
        for (int i = 0; i < videocomments.size(); i++) {
            User user = userMapper.getUserById(videocomments.get(i).getVideocommentUserId());
            user.setUserPassword(null);
            user.setUserToken(null);
            user.setUserEmail(null);
            users.add(user);
        }
        result.put("users",users);
        result.put("pageInfo",pageInfo);
        R r = new R();
        r.setData(result);
        return r;
    }

    @Override
    public R deleteVideocomment(Integer videocommentId, String userToken) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            Videocomment videocomment = videocommentMapper.getVideocommentById(videocommentId);
            if(videocomment!=null){
                if(Objects.equals(user.getUserId(), videocomment.getVideocommentUserId())){
                   if (videocommentMapper.deleteVideocommentById(videocommentId)){
                       r.setMsg("删除成功");
                   }
                }
            }
        }
        return r;
    }
}
