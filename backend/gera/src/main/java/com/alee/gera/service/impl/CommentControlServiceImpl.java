package com.alee.gera.service.impl;

import com.alee.gera.entity.*;
import com.alee.gera.mapper.*;
import com.alee.gera.service.CommentControlService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentControlServiceImpl implements CommentControlService {
    @Resource
    UserMapper userMapper;
    @Resource
    VideoMapper videoMapper;
    @Resource
    PostMapper postMapper;
    @Resource
    PostcommentMapper postcommentMapper;
    @Resource
    VideocommentMapper videocommentMapper;
    @Resource
    AdminMapper adminMapper;
    @Override
    public R getVideoComment(String userToken, Integer pageNum, Integer pageSize, String content) {
        if(content==null){
            content="";
        }
        R r = new R();
        Map<String,Object> result = new HashMap<>();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            if(user.getUserRoleId()==5){
//                System.out.println(5);
                PageHelper.startPage(pageNum,pageSize);
                List<Videocomment> videocomments = videocommentMapper.getVideocommentListByText(content);
                PageInfo<Videocomment> pageInfo = new PageInfo<>(videocomments);
                result.put("pageInfo",pageInfo);
                List<User> users = new ArrayList<>();
                List<Video> videos = new ArrayList<>();
                for (int i = 0; i < videocomments.size(); i++) {
                    User user1 = userMapper.getUserById(videocomments.get(i).getVideocommentUserId());
                    user1.setUserToken(null);
                    user1.setUserPassword(null);
                    users.add(user1);
                    Video video = videoMapper.getVideoById(videocomments.get(i).getVideocommentVideoId());
                    videos.add(video);
                }
                result.put("users",users);
                result.put("videos",videos);
            }else if(user.getUserRoleId()==4){
                if(user.getUserStatus()!=0){
                    Admin admin = adminMapper.isAdmin(user.getUserId());
                    PageHelper.startPage(pageNum,pageSize);
                    List<Videocomment> videocomments = videocommentMapper.getVideocommentListByTextAndGameId(content,admin.getAdminGameId());
                    PageInfo<Videocomment> pageInfo = new PageInfo<>(videocomments);
                    result.put("pageInfo",pageInfo);
                    List<User> users = new ArrayList<>();
                    List<Video> videos = new ArrayList<>();
                    for (int i = 0; i < videocomments.size(); i++) {
                        User user1 = userMapper.getUserById(videocomments.get(i).getVideocommentUserId());
                        user1.setUserToken(null);
                        user1.setUserPassword(null);
                        users.add(user1);
                        Video video = videoMapper.getVideoById(videocomments.get(i).getVideocommentVideoId());
                        videos.add(video);
                    }
                    result.put("users",users);
                    result.put("videos",videos);
                }
            }
        }
        r.setData(result);
        return r;
    }

    @Override
    public R getPostComment(String userToken, Integer pageNum, Integer pageSize, String content) {
        if (content==null){
            content="";
        }
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        Map<String,Object> result = new HashMap<>();
        if(user!=null){
            if(user.getUserRoleId()==5){
                PageHelper.startPage(pageNum,pageSize);
                List<Postcomment> postcomments = postcommentMapper.getPostcommentByText(content);
                PageInfo<Postcomment> pageInfo = new PageInfo<>(postcomments);
                result.put("pageInfo",pageInfo);
                List<User> users = new ArrayList<>();
                List<Post> posts = new ArrayList<>();
                for (int i = 0; i < postcomments.size(); i++) {
                    User user1 = userMapper.getUserById(postcomments.get(i).getPostcommentUserId());
                    user1.setUserToken(null);
                    user1.setUserPassword(null);
                    users.add(user1);
                    Post post = postMapper.getPostById(postcomments.get(i).getPostcommentPostId());
                    posts.add(post);
                }
                result.put("users",users);
                result.put("posts",posts);
            }else if(user.getUserRoleId()==4){
                Admin admin = adminMapper.isAdmin(user.getUserId());
                PageHelper.startPage(pageNum,pageSize);
                List<Postcomment> postcomments = postcommentMapper.getPostcommentByTextAndGameId(content,admin.getAdminGameId());
                PageInfo<Postcomment> pageInfo = new PageInfo<>(postcomments);
                result.put("pageInfo",pageInfo);
                List<User> users = new ArrayList<>();
                List<Post> posts = new ArrayList<>();
                for (int i = 0; i < postcomments.size(); i++) {
                    User user1 = userMapper.getUserById(postcomments.get(i).getPostcommentUserId());
                    user1.setUserToken(null);
                    user1.setUserPassword(null);
                    users.add(user1);
                    Post post = postMapper.getPostById(postcomments.get(i).getPostcommentPostId());
                    posts.add(post);
                }
                result.put("users",users);
                result.put("posts",posts);
            }
        }
        r.setData(result);
        return r;
    }

    @Override
    public R deleteVideoComment(String userToken, Integer commentId) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            if(user.getUserRoleId()==5){
                videocommentMapper.deleteVideocommentById(commentId);
                r.setMsg("删除成功");
            }else if(user.getUserRoleId()==4){
                videocommentMapper.deleteVideocommentById(commentId);
                r.setMsg("删除成功");
            }
        }
        return r;
    }

    @Override
    public R deletePostComment(String userToken, Integer commentId) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            if(user.getUserRoleId()==5){
                postcommentMapper.deletePostcommentById(commentId);
                r.setMsg("删除成功");
            }else if(user.getUserRoleId()==4){
                postcommentMapper.deletePostcommentById(commentId);
                r.setMsg("删除成功");
            }
        }
        return r;
    }
}
