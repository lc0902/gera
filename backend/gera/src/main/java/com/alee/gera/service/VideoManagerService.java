package com.alee.gera.service;

import com.alee.gera.entity.R;

public interface VideoManagerService {
    public R getVideoList(String userToken,Integer pageNum,Integer pageSize,String videoDesc);
    public R deleteVideo(String userToken,Integer videoId);
}
