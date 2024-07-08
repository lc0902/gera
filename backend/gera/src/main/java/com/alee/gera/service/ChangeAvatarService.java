package com.alee.gera.service;

import com.alee.gera.entity.R;
import org.springframework.web.multipart.MultipartFile;

public interface ChangeAvatarService {
    public R changeAvatarByToken(MultipartFile multipartFile,String token);
}
