package com.alee.gera.service.impl;

import com.alee.gera.entity.*;
import com.alee.gera.mapper.*;
import com.alee.gera.service.VideoManagerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class VideoManagerServiceImpl implements VideoManagerService {
    @Resource
    UserMapper userMapper;
    @Resource
    VideoMapper videoMapper;
    @Resource
    AdminMapper adminMapper;
    @Resource
    GameMapper gameMapper;
    @Override
    public R getVideoList(String userToken,Integer pageNum,Integer pageSize,String videoDesc) {
        if(videoDesc==null){
            videoDesc="";
        }
        R r = new R();
        Map<String,Object> result = new HashMap<>();
        User user = userMapper.getUserByToken(userToken);
//        System.out.println(user);
        if(user != null){
            if(user.getUserStatus()!=0){
//                如果是demon，则返回所有内容
                if(user.getUserRoleId()==5){
//                    System.out.println(5);
                    PageHelper.startPage(pageNum,pageSize);
                    List<Video> videos = videoMapper.getVideoWithDesc(videoDesc);
//                    List<Video> videos = videoMapper.getAllVideo();
                    PageInfo<Video> pageInfo = new PageInfo<>(videos);
                    result.put("pageInfo",pageInfo);
                    List<User> users = new ArrayList<>();
                    for (int i = 0; i < videos.size(); i++) {
                        Video video = videos.get(i);
                        User user1 = userMapper.getUserById(video.getVideoUserId());
                        user1.setUserToken(null);
                        user1.setUserPassword(null);
                        users.add(user1);
                    }
                    List<Game> games = new ArrayList<>();
                    for (int i = 0; i < videos.size(); i++) {
                        Game game = gameMapper.getGameById(videos.get(i).getVideoGameId());
                        games.add(game);
                    }
                    result.put("games",games);
                    result.put("users",users);
                }
//                如果是admin，则仅返回该板块内容
                else if(user.getUserRoleId()==4){
                    Admin admin = adminMapper.isAdmin(user.getUserId());
                    if(admin!=null){
                        PageHelper.startPage(pageNum,pageSize);
                        List<Video> videos = videoMapper.getVideoByGameId(admin.getAdminGameId(),videoDesc);
//                    List<Video> videos = videoMapper.getAllVideo();
                        PageInfo<Video> pageInfo = new PageInfo<>(videos);
                        result.put("pageInfo",pageInfo);
                        List<User> users = new ArrayList<>();
                        for (int i = 0; i < videos.size(); i++) {
                            Video video = videos.get(i);
                            User user1 = userMapper.getUserById(video.getVideoUserId());
                            user1.setUserToken(null);
                            user1.setUserPassword(null);
                            users.add(user1);
                        }
                        List<Game> games = new ArrayList<>();
                        for (int i = 0; i < videos.size(); i++) {
                            Game game = gameMapper.getGameById(videos.get(i).getVideoGameId());
                            games.add(game);
                        }
                        result.put("games",games);
                        result.put("users",users);
                    }

                }
//                其他身份不返回
            }else {
                r.setMsg("状态异常");
            }
        }else {
            r.setMsg("用户不存在");
        }
        r.setData(result);
        return r;
    }

    @Resource
    VideostarMapper videostarMapper;
    @Resource
    VideocommentMapper videocommentMapper;
    @Resource
    VideocollectMapper videocollectMapper;
    @Override
    public R deleteVideo(String userToken, Integer videoId) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null&&user.getUserStatus()!=0){
            Video video = videoMapper.getVideoById(videoId);
            if(user.getUserRoleId()==5 ){

                videostarMapper.deleteVideostarByVideoId(videoId);
                videocommentMapper.deleteVideocommentByVideoId(videoId);
                videocollectMapper.deleteVideocollectByVideoId(videoId);
                videoMapper.deleteVideoById(videoId);
                r.setMsg("成功");
            }else if(user.getUserRoleId()==4){

                videostarMapper.deleteVideostarByVideoId(videoId);
                videocommentMapper.deleteVideocommentByVideoId(videoId);
                videocollectMapper.deleteVideocollectByVideoId(videoId);
                videoMapper.deleteVideoById(videoId);
                r.setMsg("成功");
            }
        }
        return r;
    }
}
