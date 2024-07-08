package com.alee.gera.service;

import com.alee.gera.entity.R;

import java.util.List;

public interface PostListService {
    R getPostList(Integer pageNum,Integer pageSize,String postTitle);
    R getPersonalPostList(Integer pageNum,Integer pageSize,String postTitle,String userToken);
}
