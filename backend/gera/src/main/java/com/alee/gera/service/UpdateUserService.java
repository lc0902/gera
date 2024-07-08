package com.alee.gera.service;

import com.alee.gera.entity.R;
import com.alee.gera.entity.User;

public interface UpdateUserService {
    R updateUserInfoByToken(User user);

    R changePassword(String userToken,String oldPassword,String newPassword);
}
