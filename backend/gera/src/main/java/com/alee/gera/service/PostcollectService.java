package com.alee.gera.service;

import com.alee.gera.entity.R;

public interface PostcollectService {
    R insertPostcollect(String userToken, Integer postId);
    R deletePostcollect(String userToken,Integer postId);
    R getUserPostcollectList(String userToken);

    R getCollectPostListByUserToken(Integer pageNum,Integer pageSize,String userToken,String postTitle);
}
