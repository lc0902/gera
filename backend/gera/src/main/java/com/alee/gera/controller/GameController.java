package com.alee.gera.controller;

import com.alee.gera.entity.R;
import com.alee.gera.service.GameListService;
import com.alee.gera.service.impl.GameInfoServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class GameController {
    @Resource
    GameListService gameListService;
    @Resource
    GameInfoServiceImpl gameInfoService;

    @PostMapping("/gameList")
    public R gameList(@RequestParam Map<String,Object> map){

        return gameListService.getGameList(Integer.parseInt((String) map.get("pageNum")),Integer.parseInt((String) map.get("pageSize")), (String) map.get("gameName"));
    }

    @PostMapping("/getAllGame")
    public R getAllGame(){
        return gameListService.getAllGame();
    }


    @PostMapping("/getGameInfo")
    public R getGameInfo(@RequestParam Map<String,Object> map){
        return gameInfoService.getGameInfo(Integer.valueOf((String) map.get("gameId")));
    }
    @PostMapping("/getVideoListByGameId")
    public  R getVideoListByGameId(@RequestParam Map<String,Object> map){
        return gameInfoService.getVideoListByGameId(Integer.valueOf((String) map.get("pageNum")),Integer.valueOf((String) map.get("pageSize")),(String) map.get("videoDescription"),Integer.valueOf((String) map.get("gameId")));
    }
    @PostMapping("/getLiveListByGameId")
    public R getLiveListByGameId(@RequestParam Map<String,Object> map){
        return gameInfoService.getLiveListByGameId(Integer.valueOf((String) map.get("pageNum")),Integer.valueOf((String) map.get("pageSize")),(String) map.get("liveDescription"),Integer.valueOf((String) map.get("gameId")));

    }

    @PostMapping("/getPostListByGameId")
    public R getPostListByGameId(@RequestParam Map<String,Object> map){
        return  gameInfoService.getPostListByGameId(Integer.valueOf((String) map.get("pageNum")),Integer.valueOf((String) map.get("pageSize")),(String) map.get("postTitle"),Integer.valueOf((String) map.get("gameId")));

    }

    @PostMapping("/getRateStatus")
    public R getRateStatus(@RequestParam Map<String,Object> map){
        return  gameInfoService.getRateStatus((String) map.get("userToken"),Integer.valueOf((String) map.get("gameId")));
    }
    @PostMapping("/rateGame")
    public R rateGame(@RequestParam Map<String,Object> map){
        return gameInfoService.rate((String) map.get("userToken"),Integer.valueOf((String) map.get("gameId")),Float.valueOf((String) map.get("rate")));
    }
}
