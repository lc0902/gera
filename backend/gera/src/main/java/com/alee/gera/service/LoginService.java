package com.alee.gera.service;

import com.alee.gera.entity.R;
import com.alee.gera.entity.User;

public interface LoginService {
    public R<User> login(User user);
}
