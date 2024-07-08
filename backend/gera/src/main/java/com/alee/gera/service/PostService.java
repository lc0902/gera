package com.alee.gera.service;

import com.alee.gera.entity.R;

public interface PostService {
    R viewPost(Integer postId);

    R deletePost(Integer postId,String userToken);
}
