package com.alee.gera.service;

import com.alee.gera.entity.R;

public interface VideostarService {
    R insertVideostar(String userToken, Integer videoId);
    R deleteVideostar(String userToken,Integer videoId);
    R getUserVideostarList(String userToken);
}
