package com.alee.gera.service;

import com.alee.gera.entity.R;

public interface VideoListService {
    R getVideoList(Integer pageNum,Integer pageSize,String videoDesc);
}
