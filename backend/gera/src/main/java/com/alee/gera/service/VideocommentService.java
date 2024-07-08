package com.alee.gera.service;

import com.alee.gera.entity.R;
import com.alee.gera.entity.Videocomment;

public interface VideocommentService {
    R commentVideo(Videocomment videocomment, String userToken);
    R getVideocommentList(Integer videoId,Integer pageNum,Integer pageSize);
    R deleteVideocomment(Integer videocommentId,String userToken);
}
