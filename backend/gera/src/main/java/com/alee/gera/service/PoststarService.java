package com.alee.gera.service;

import com.alee.gera.entity.R;

public interface PoststarService {
    R insertPoststar(String userToken,Integer postId);
    R deletePoststar(String userToken,Integer postId);
    R getUserPoststarList(String userToken);
}
