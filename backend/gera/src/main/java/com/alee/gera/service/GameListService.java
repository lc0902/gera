package com.alee.gera.service;

import com.alee.gera.entity.Game;
import com.alee.gera.entity.R;

import java.util.List;

public interface GameListService {

    R getGameList(Integer pageNum,Integer pageSize,String gameName);

    R getAllGame();
}
