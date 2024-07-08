package com.alee.gera.service;

import com.alee.gera.entity.R;
import com.alee.gera.entity.User;

import java.util.Map;

public interface RegisterService {
    R getEnrollCode(String userEmail);

    R enroll(User user,String vCode);

}
