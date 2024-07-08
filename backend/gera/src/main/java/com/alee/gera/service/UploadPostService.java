package com.alee.gera.service;

import com.alee.gera.entity.Post;
import com.alee.gera.entity.R;
import org.springframework.web.multipart.MultipartFile;

public interface UploadPostService {

    R uploadImg(MultipartFile file);

    R uploadPost(Post post);

}
