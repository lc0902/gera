package com.alee.gera.service;

import com.alee.gera.entity.R;

public interface UserManagerService {

//    获取用户列表
    R getUserList(Integer pageNum, Integer pageSize,String queryParam,String userToken);
//    重置用户签名
    R resetUserSignature(String userToken,Integer userId);
//    重置用户头像
    R resetUserAvatar(String userToken,Integer userId);
//    修改用户身份
    R modifyUserRole(String userToken,Integer userId,Integer roleId,Integer gameId);
//    封号
    R banUser(String userToken,Integer userId);
//    解封
    R unbanUser(String userToken,Integer userId);
    R getAllUser(String userToken);
}
