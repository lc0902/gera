package com.alee.gera.service;

import com.alee.gera.entity.R;

public interface VideoService {
    R viewVideo(Integer videoId);
    R getPersonalUploadVideo(Integer pageNum,Integer pageSize,String videoDescription,String userToken);

    R deletePersonalUploadVideo(String userToken,Integer videoId);
}
