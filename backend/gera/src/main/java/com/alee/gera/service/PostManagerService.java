package com.alee.gera.service;

import com.alee.gera.entity.R;

public interface PostManagerService {
    public R getPostList(String userToken,Integer pageNum,Integer pageSize,String postTitle);

    public R deletePost(String userToken,Integer postId);
}
