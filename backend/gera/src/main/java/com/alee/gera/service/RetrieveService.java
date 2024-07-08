package com.alee.gera.service;

import com.alee.gera.entity.R;
import com.alee.gera.entity.User;

import java.util.Map;

public interface RetrieveService {
    R sendRetrieveCode(String email);
    R retrievePassword(User user,String vCode);
}
