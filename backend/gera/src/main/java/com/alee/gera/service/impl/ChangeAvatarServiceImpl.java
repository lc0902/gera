package com.alee.gera.service.impl;

import com.alee.gera.entity.R;
import com.alee.gera.entity.User;
import com.alee.gera.mapper.UserMapper;
import com.alee.gera.service.ChangeAvatarService;
import com.alee.gera.util.AvatarUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Service
public class ChangeAvatarServiceImpl implements ChangeAvatarService {
    @Resource
    UserMapper userMapper;
    @Resource
    AvatarUtil avatarUtil;
//    16
    @Override
    public R changeAvatarByToken(MultipartFile multipartFile, String token) {
        R r = new R();
        User user = userMapper.getUserByToken(token);
        if(user!=null){
            String t= avatarUtil.createAvatarName();
            String path="D:/GraduationProject/StreamServer/nginx-rtmp/html/pic/avatar/"+t;

            try{
                avatarUtil.saveAvatar(multipartFile,path);
                user.setUserAvatarUrl("http://localhost/pic/avatar/"+t);
                userMapper.updateAvatarByToken(user);
                r.setMsg("成功");

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
