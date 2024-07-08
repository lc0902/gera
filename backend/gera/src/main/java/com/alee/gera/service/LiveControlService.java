package com.alee.gera.service;

import com.alee.gera.entity.R;

public interface LiveControlService {
    public R getLiveList(String userToken,Integer pageNum,Integer pageSize,String liveName);

    public R closeLive(String userToken,String liveId);
}
