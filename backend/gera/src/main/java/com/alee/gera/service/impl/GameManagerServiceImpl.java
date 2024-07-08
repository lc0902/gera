package com.alee.gera.service.impl;

import com.alee.gera.entity.*;
import com.alee.gera.mapper.*;
import com.alee.gera.service.GameManagerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

@Service
public class GameManagerServiceImpl implements GameManagerService {
    @Resource
    UserMapper userMapper;
    @Resource
    GameMapper gameMapper;
    @Resource
    AdminMapper adminMapper;

    @Override
    public R getGameList(String userToken, Integer pageNum, Integer pageSize, String gameName) {
        R r = new R();
        Map<String, Object> result = new HashMap<>();
        // 获取用户信息
        User user = userMapper.getUserByToken(userToken);
        if (user != null) {
            if (user.getUserRoleId() == 5) {
                PageHelper.startPage(pageNum, pageSize);
                List<Game> games = gameMapper.getGameByName(gameName);
                PageInfo pageInfo = new PageInfo<>(games);
                List<User> users = new ArrayList<>();
                for (int i = 0; i < games.size(); i++) {
                    Admin admin = adminMapper.getAdminByGameId(games.get(i).getGameId());
                    if (admin == null) {
                        users.add(null);
                    } else {
                        User t = userMapper.getUserById(admin.getAdminUserId());
                        t.setUserPassword(null);
                        t.setUserToken(null);
                        users.add(t);
                    }
                }
                result.put("games", games);
                result.put("users", users);
                result.put("pageInfo", pageInfo);
                r.setData(result);
            }
        } else {
            r.setMsg("用户不存在");
        }
        return r;
    }

    @Override
    public R updateGame(Game game, String userToken, MultipartFile coverFile) {
        return null;
    }

    @Override
    public R addGame(Game game, String userToken, MultipartFile coverFile) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            if(user.getUserRoleId()==5){

                String fileName = UUID.randomUUID().toString();
                File dest = new File("/D:/GraduationProject/StreamServer/nginx-rtmp/html/pic/cover/" + fileName + ".img");
                try {
                    game.setGameCoverUrl("http://localhost/pic/cover/" + fileName + ".img");
                    gameMapper.addGame(game);
                    coverFile.transferTo(dest);
                    System.out.println("图片上传成功");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }else {
                r.setMsg("用户权限不足");
            }

        }else {
            r.setMsg("用户不存在");
        }
        return r;
    }

    @Override
    public R setGameModerator(String userToken, Integer gameId, Integer moderatorId) {
        return null;
    }

    @Override
    public R removeGameModerator(String userToken, Integer gameId, Integer moderatorId) {
        return null;
    }

    @Override
    public R delGame(String userToken, Integer gameId) {
        return null;
    }

    @Override
    public R updateGameName(String userToken, Integer gameId, String gameName) {
        R r = new R();

        // 获取用户信息
        User user = userMapper.getUserByToken(userToken);

        if (user != null) {
            if (user.getUserRoleId() == 5) {
                if (gameMapper.updateGameName(gameName, gameId)) {
                    r.setMsg("修改成功");
                } else {

                    r.setMsg("修改失败");
                }
            } else {
                r.setMsg("用户权限不足");
            }
        } else {
            r.setMsg("用户不存在");
        }
        return r;
    }

    @Override
    public R updateGameDesc(String userToken, Integer gameId, String gameDesc) {
        R r = new R();

        // 获取用户信息
        User user = userMapper.getUserByToken(userToken);

        if (user != null) {
            if (user.getUserRoleId() == 5) {
                if (gameMapper.updateGameDescription(gameDesc, gameId)) {
                    r.setMsg("修改成功");
                } else {

                    r.setMsg("修改失败");
                }
            } else {
                r.setMsg("用户权限不足");
            }
        } else {
            r.setMsg("用户不存在");
        }
        return r;
    }

    @Override
    public R updateGameCover(String userToken, Integer gameId, MultipartFile coverFile) {
        R r = new R();
        // 获取用户信息
        User user = userMapper.getUserByToken(userToken);

        if (user != null) {
            if (user.getUserRoleId() == 5) {
//                逻辑位置
                String fileName = UUID.randomUUID().toString();
                File dest = new File("/D:/GraduationProject/StreamServer/nginx-rtmp/html/pic/cover/" + fileName + ".img");
                try {
                    coverFile.transferTo(dest);
                    gameMapper.updateGameCoverUrl("http://localhost/pic/cover/" + fileName + ".img", gameId);
                    r.setMsg("修改成功");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                r.setMsg("用户权限不足");
            }
        } else {
            r.setMsg("用户不存在");
        }
        return r;

    }

    @Override
    public R updateGameReleaseTime(String userToken, Integer gameId, String releaseTime) throws ParseException {
        R r = new R();
        // 获取用户信息
        User user = userMapper.getUserByToken(userToken);
        System.out.println(releaseTime);

        if (user != null) {
            if (user.getUserRoleId() == 5) {
//                逻辑位置
                Timestamp timestamp = new Timestamp(new Date(Long.parseLong(releaseTime)).getTime());
                if (gameMapper.updateGameReleaseTime(timestamp, gameId)) {
                    r.setMsg("修改成功");
                } else {

                    r.setMsg("修改失败");
                }
            } else {
                r.setMsg("用户权限不足");
            }
        } else {
            r.setMsg("用户不存在");
        }
        return r;
    }

    @Resource
    PostcollectMapper postcollectMapper;
    @Resource
    PostMapper postMapper;
    @Resource
    PostcommentMapper postcommentMapper;
    @Resource
    PoststarMapper poststarMapper;
    @Resource
    VideoMapper videoMapper;
    @Resource
    VideostarMapper videostarMapper;
    @Resource
    VideocollectMapper videocollectMapper;
    @Resource
    VideocommentMapper videocommentMapper;
    @Resource
    LiveMapper liveMapper;
    @Resource
    RateMapper rateMapper;
    @Override
    public R deleteGameByGameId(Integer gameId, String userToken) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);

        if (user != null) {
            if (user.getUserRoleId() == 5) {
//                todo
//                删除post 照搬的postservice代码，若报错查看该部分代码
                List<Post> posts = postMapper.getPostByGameId(gameId);
                for (int i = 0; i < posts.size(); i++) {
                    Integer postId = posts.get(i).getPostId();
                    postcollectMapper.deletePostcollectByPostId(postId);
                    postcommentMapper.deletePostcommentByPostId(postId);
                    poststarMapper.deletePoststarByPostId(postId);
                    postMapper.deletePost(postId);
                }
                System.out.println("post清除成功");
//                删除video
                List<Video> videos = videoMapper.getVideoByGameId2(gameId);
                for (int i = 0; i < videos.size(); i++) {
                    Integer videoId = videos.get(i).getVideoId();
                    videostarMapper.deleteVideostarByVideoId(videoId);
                    videocommentMapper.deleteVideocommentByVideoId(videoId);
                    videocollectMapper.deleteVideocollectByVideoId(videoId);
                    videoMapper.deleteVideoById(videoId);
                }
                System.out.println("video清除成功");

//                删除live
                liveMapper.resetLiveByGameId(gameId);
                System.out.println("live清除成功");

//                删除rate
                rateMapper.deleteRateByGameId(gameId);
                System.out.println("rate清除成功");

//                删除admin
                Admin admin = adminMapper.getAdminByGameId(gameId);
                if (admin!=null){
                    System.out.println(admin);
                    User user1 = userMapper.getUserById(admin.getAdminUserId());
                    userMapper.updateUserRoleByUserId(admin.getAdminUserId(), 3);
                    adminMapper.deleteAdmin(admin.getAdminUserId());
                    System.out.println("admin清除成功");

                }

//                删除game
                gameMapper.deleteGameById(gameId);
                System.out.println("game删除成功");

//                不建议后续扩充表外键绑定到该表，否则需修改此处代码
                r.setMsg("删除成功");
            } else {
                r.setMsg("用户权限不足");
            }
        } else {
            r.setMsg("用户不存在");
        }
        return r;
    }


}
