package com.alee.gera.controller;

import com.alee.gera.entity.Game;
import com.alee.gera.entity.R;
import com.alee.gera.service.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

@RestController
public class AdminController {
    @Resource
    UserManagerService userManagerService;

    @PostMapping("/getUserList")
    public R getUserList(@RequestParam Map<String, Object> map) {
        return userManagerService.getUserList(
                Integer.valueOf((String) map.get("pageNum")),
                Integer.valueOf((String) map.get("pageSize")),
                (String) map.get("queryParam"),
                (String) map.get("userToken")
        );
    }

    @PostMapping("/resetSignature")
    public R resetSignature(@RequestParam Map<String, Object> map) {
        return userManagerService.resetUserSignature(
                (String) map.get("userToken"),
                Integer.valueOf((String) map.get("userId"))
        );
    }

    @PostMapping("/resetAvatar")
    public R resetAvatar(@RequestParam Map<String, Object> map) {
        return userManagerService.resetUserAvatar(
                (String) map.get("userToken"),
                Integer.valueOf((String) map.get("userId"))
        );
    }

    @PostMapping("/banUser")
    public R banUser(@RequestParam Map<String, Object> map) {
        return userManagerService.banUser(
                (String) map.get("userToken"),
                Integer.valueOf((String) map.get("userId"))
        );
    }

    @PostMapping("/unbanUser")
    public R unbanUser(@RequestParam Map<String, Object> map) {

        return userManagerService.unbanUser(
                (String) map.get("userToken"),
                Integer.valueOf((String) map.get("userId"))
        );

    }

    @PostMapping("/modifyUserRole")
    public R modifyUserRole(@RequestParam Map<String, Object> map) {

        Integer gameId = null;

        try {
            if (map.get("gameId") != null) {

                gameId = Integer.valueOf((String) map.get("gameId"));
            }
        } catch (Exception e) {
            gameId = null;
        }
        return userManagerService.modifyUserRole(
                (String) map.get("userToken"),
                Integer.valueOf((String) map.get("userId")),
                Integer.valueOf((String) map.get("roleId")),
                gameId
        );

    }

    @Resource
    GameManagerService gameManagerService;

    @PostMapping("/adminGetGameList")
    public R adminGetGameList(@RequestParam Map<String, Object> map) {
//        System.out.println(map);
        return gameManagerService.getGameList(
                (String) map.get("userToken"),
                Integer.valueOf((String) map.get("pageNum")),
                Integer.valueOf((String) map.get("pageSize")),
                (String) map.get("gameName")
        );
    }

    @PostMapping("/updateGameName")
    public R updateGameName(@RequestParam Map<String, Object> map) {
        return gameManagerService.updateGameName(
                (String) map.get("userToken"),
                Integer.valueOf((String) map.get("gameId")),
                (String) map.get("gameName")
        );
    }

    @PostMapping("/updateGameDesc")
    public R updateGameDesc(@RequestParam Map<String, Object> map) {
        return gameManagerService.updateGameDesc(
                (String) map.get("userToken"),
                Integer.valueOf((String) map.get("gameId")),
                (String) map.get("gameDesc")
        );
    }

    @PostMapping("/updateGameCover")
    public R updateGameCover(@RequestParam String userToken, @RequestParam String gameId, @RequestParam MultipartFile coverFile) {
        System.out.println(userToken);
        System.out.println(gameId);
        return gameManagerService.updateGameCover(
                userToken,
                Integer.valueOf(gameId),
                coverFile
        );
    }

    @PostMapping("/updateGameReleaseTime")
    public R updateGameReleaseTime(@RequestParam Map<String, Object> map) throws ParseException {

        return gameManagerService.updateGameReleaseTime(
                (String) map.get("userToken"),
                Integer.valueOf((String) map.get("gameId")),
                (String) map.get("releaseTime")
        );
    }

    @PostMapping("/getAllUser")
    public R getAllUser(@RequestParam Map<String, Object> map) {
        return userManagerService.getAllUser(
                (String) map.get("userToken")
        );
    }

    //    dangerous function
    @PostMapping("/deleteGame")
    public R deleteGame(@RequestParam Map<String, Object> map) {
        return gameManagerService.deleteGameByGameId(
                Integer.valueOf((String) map.get("gameId")),
                (String) map.get("userToken")
        );
    }

    @PostMapping("/addGame")
    public R addGame(@RequestParam Map<String, Object> map, @RequestParam MultipartFile coverFile) {
        System.out.println(map);
        return gameManagerService.addGame(buildGameByMap(map), (String) map.get("userToken"),coverFile);

    }
    @Resource
    VideoManagerService videoManagerService;

    @PostMapping("/adminGetVideoList")
    public R adminGetVideoList(@RequestParam Map<String, Object> map) {
//        System.out.println(map);
        return videoManagerService.getVideoList(
                (String) map.get("userToken"),
                Integer.valueOf((String) map.get("pageNum")),
                Integer.valueOf((String) map.get("pageSize")),
                (String) map.get("videoDesc")
        );
    }
    @Resource
    PostManagerService postManagerService;
    @PostMapping("/adminGetPostList")
    public R adminGetPostList(@RequestParam Map<String, Object> map) {
        return postManagerService.getPostList(
                (String) map.get("userToken"),
                Integer.valueOf((String) map.get("pageNum")),
                Integer.valueOf((String) map.get("pageSize")),
                (String) map.get("postTitle")
        );
    }
    @PostMapping("/adminDeleteVideo")
    public R adminDeleteVideo(@RequestParam Map<String, Object> map) {
//        System.out.println(map);
        return videoManagerService.deleteVideo(
                (String) map.get("userToken"),
                Integer.valueOf((String) map.get("videoId"))
        );
    }
    @PostMapping("/adminDeletePost")
    public R adminDeletePost(@RequestParam Map<String, Object> map) {
        return postManagerService.deletePost(
                (String) map.get("userToken"),
                Integer.valueOf((String) map.get("postId"))
        );
    }
    @Resource
    CommentControlService commentControlService;
    @PostMapping("/adminGetVideoCommentList")
    public R adminGetVideoCommentList(@RequestParam Map<String, Object> map) {
//        System.out.println(map);
        return commentControlService.getVideoComment(
                (String) map.get("userToken"),
                Integer.valueOf((String) map.get("pageNum")),
                Integer.valueOf((String) map.get("pageSize")),
                (String) map.get("content")
        );
    }

    @PostMapping("/adminDeleteVideoComment")
    public R adminDeleteVideoComment(@RequestParam Map<String, Object> map) {
        return  commentControlService.deleteVideoComment(
                (String) map.get("userToken"),
                Integer.valueOf((String) map.get("commentId"))
        );
    }
    @PostMapping("/adminGetPostCommentList")
    public R adminGetPostCommentList(@RequestParam Map<String, Object> map) {
        return  commentControlService.getPostComment(
                (String) map.get("userToken"),
                Integer.valueOf((String) map.get("pageNum")),
                Integer.valueOf((String) map.get("pageSize")),
                (String) map.get("content")
        );
    }
    @PostMapping("/adminDeletePostComment")
    public R adminDeletePostComment(@RequestParam Map<String, Object> map) {
        return  commentControlService.deletePostComment(
                (String) map.get("userToken"),
                Integer.valueOf((String) map.get("commentId"))
        );
    }
    @Resource
    ApplyManagerService applyManagerService;
    @PostMapping("/adminGetApplyList")
    public R adminGetApplyList(@RequestParam Map<String, Object> map) {
        return applyManagerService.getApplyList(
                (String) map.get("userToken"),
                Integer.valueOf((String) map.get("pageNum")),
                Integer.valueOf((String) map.get("pageSize"))
        );
    }
    @PostMapping("/adminDealApply")
    public R adminDealApply(@RequestParam Map<String, Object> map) {
        return applyManagerService.dealApply(
                (String) map.get("userToken"),
                Integer.valueOf((String) map.get("applyId")),
                (String) map.get("dealCode")
        );
    }
    @Resource
    LiveControlService liveControlService;

    @PostMapping("/adminGetLiveList")
    public R adminGetLiveList(@RequestParam Map<String, Object> map) {
        return liveControlService.getLiveList(
                (String) map.get("userToken"),
                Integer.valueOf((String) map.get("pageNum")),
                Integer.valueOf((String) map.get("pageSize")),
                (String) map.get("liveName")
        );
    }
    Game buildGameByMap(Map<String, Object> map) {
        Game game = new Game();
        if(map.get("gameName")!=null){
            game.setGameName((String) map.get("gameName"));
        }
        if (map.get("gameDescription") != null){
            game.setGameDescription((String) map.get("gameDescription"));
        }
        if (map.get("gameReleaseTime") != null){
            game.setGameReleaseTime(new Timestamp(new Date(Long.parseLong((String) map.get("gameReleaseTime"))).getTime()));
        }
        if (map.get("gameId") != null){
            game.setGameId(Integer.valueOf((String) map.get("gameId")));
        }
        return game;
    }

}
