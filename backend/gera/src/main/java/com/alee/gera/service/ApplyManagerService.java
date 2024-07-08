package com.alee.gera.service;

import com.alee.gera.entity.R;

public interface ApplyManagerService {
    public R getApplyList(String userToken,Integer pageNum,Integer pageSize);

    public R dealApply(String userToken,Integer applyId,String dealCode);
}
