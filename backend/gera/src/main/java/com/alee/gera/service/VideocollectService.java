package com.alee.gera.service;

import com.alee.gera.entity.R;

public interface VideocollectService {
    R insertVideocollect(String userToken, Integer videoId);
    R deleteVideocollect(String userToken,Integer videoId);
    R getUserVideocollectList(String userToken);

    R getCollectVideoListByUserToken(Integer pageNum, Integer pageSize, String userToken,String videoTitle);
}
