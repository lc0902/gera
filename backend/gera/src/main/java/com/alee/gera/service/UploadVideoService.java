package com.alee.gera.service;

import com.alee.gera.entity.R;
import com.alee.gera.entity.Video;
import org.springframework.web.multipart.MultipartFile;

public interface UploadVideoService {
    R uploadVideo(Video video, MultipartFile videoFile,MultipartFile coverFile,String userToken);
}
