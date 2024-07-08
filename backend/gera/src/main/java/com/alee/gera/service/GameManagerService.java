package com.alee.gera.service;

import com.alee.gera.entity.Game;
import com.alee.gera.entity.R;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;

public interface GameManagerService {
    R getGameList(String userToken,Integer pageNum,Integer pageSize,String gameName);
    R updateGame(Game game, String userToken, MultipartFile coverFile);
    R addGame(Game game,String userToken,MultipartFile coverFile);
    R setGameModerator(String userToken,Integer gameId,Integer moderatorId);
    R removeGameModerator(String userToken,Integer gameId,Integer moderatorId);
    R delGame(String userToken,Integer gameId);

    R updateGameName(String userToken,Integer gameId,String gameName);
    R updateGameDesc(String userToken,Integer gameId,String gameDesc);
    R updateGameCover(String userToken,Integer gameId,MultipartFile coverFile);
    R updateGameReleaseTime(String userToken,Integer gameId,String releaseTime) throws ParseException;

//    dangerous!!!
    R deleteGameByGameId(Integer gameId,String userToken);
}
