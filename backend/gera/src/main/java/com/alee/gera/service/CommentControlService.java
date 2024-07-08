package com.alee.gera.service;

import com.alee.gera.entity.R;

public interface CommentControlService {
    public R getVideoComment(String userToken,Integer pageNum,Integer pageSize,String content);
    public R getPostComment(String userToken,Integer pageNum,Integer pageSize,String content);

    public R deleteVideoComment(String userToken,Integer commentId);

    public R deletePostComment(String userToken,Integer commentId);
}
