package com.alee.gera.service;

import com.alee.gera.entity.Postcomment;
import com.alee.gera.entity.R;
import org.springframework.stereotype.Service;

@Service
public interface PostcommentService {
    R commentPost(Postcomment postcomment,String userToken);
    R getPostcommentList(Postcomment postcomment,Integer pageNum,Integer pageSize);

    R deletePostcomment(Postcomment postcomment,String userToken);
}
