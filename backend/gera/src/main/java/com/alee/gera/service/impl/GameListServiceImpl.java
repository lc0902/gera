package com.alee.gera.service.impl;

import com.alee.gera.entity.Game;
import com.alee.gera.entity.R;
import com.alee.gera.mapper.GameMapper;
import com.alee.gera.service.GameListService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameListServiceImpl implements GameListService {
    @Resource
    GameMapper gameMapper;
//    业务编码20
    @Override
    public R getGameList(Integer pageNum, Integer pageSize, String gameName) {
        R r = new R();
        if(gameName != null ){
//            无查询条件
            PageHelper.startPage(pageNum,pageSize);
            List<Game> games =gameMapper.getGameByName(gameName);
            PageInfo<Game> pageInfo = new PageInfo<>(games);
            r.setData(pageInfo);
            r.setCode(2010);
        }else {
            PageHelper.startPage(pageNum, pageSize);
            List<Game> games =gameMapper.getAllGame();
            PageInfo<Game> pageInfo = new PageInfo<>(games);
            r.setData(pageInfo);
            r.setCode(2010);
        }
        return r;
    }

//    21
    @Override
    public R getAllGame() {
        R r =new R();
        r.setData(gameMapper.getAllGame());
        return r;
    }


}
