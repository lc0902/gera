package com.alee.gera.service;

import com.alee.gera.entity.R;

public interface GameInfoService {
    R getGameInfo(Integer gameId);

    R getVideoListByGameId(Integer pageNum, Integer pageSize, String videoDesc,Integer gameId);

    R getLiveListByGameId(Integer pageNum, Integer pageSize, String liveDescription,Integer gameId);

    R getPostListByGameId(Integer pageNum, Integer pageSize, String postTitle,Integer gameId);

    R rate(String userToken,Integer gameId,Float rate);

    R getRateStatus(String userToken,Integer gameId);
}
