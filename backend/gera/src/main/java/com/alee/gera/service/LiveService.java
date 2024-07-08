package com.alee.gera.service;

import com.alee.gera.entity.Live;
import com.alee.gera.entity.R;
import org.springframework.web.multipart.MultipartFile;

public interface LiveService {
    R getLiveUrl(String userToken);

    R uploadLiveInfo(Live live, String userToken, MultipartFile multipartFile);

    R startLive(String userToken);
    R closeLive(String userToken);

    R getLiveList(String userToken,String liveDescription,Integer pageNum,Integer pageSize);

    R getLiveStatus(String userToken);

    R viewLive(String userToken,Integer liveId);
}
