package com.alee.gera.service.impl;

import com.alee.gera.entity.Post;
import com.alee.gera.entity.R;
import com.alee.gera.mapper.PostMapper;
import com.alee.gera.mapper.UserMapper;
import com.alee.gera.service.UploadPostService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UploadPostServiceImpl implements UploadPostService {
//    30
    @Override
    public R uploadImg(MultipartFile file) {
        R r = new R();
        if(!file.isEmpty()){
            String fileName = UUID.randomUUID() +".img";
            File dest = new File("/D:/GraduationProject/StreamServer/nginx-rtmp/html/pic/mdpic/" + fileName);
            try {
                file.transferTo(dest);
                r.setCode(3010);
                Map<String,Object> map = new HashMap<>();
                map.put("url", "http://localhost/pic/mdpic/"+fileName);
                r.setData(map);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            r.setCode(3001);

        }
        return r;

    }
//    31
    @Resource
    PostMapper postMapper;
    @Resource
    UserMapper userMapper;
    @Override
    public R uploadPost(Post post) {
        R r = new R();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        post.setPostTime(timestamp);
        post.setPostStarNum(0);
        if (postMapper.uploadPost(post)){
            r.setCode(3110);
            r.setMsg("上传成功！");
        }else{
            r.setCode(3101);
            r.setMsg("错误");
        }
        return r;
    }
}
