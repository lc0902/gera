package com.alee.gera.service.impl;

import com.alee.gera.entity.R;
import com.alee.gera.entity.User;
import com.alee.gera.mapper.UserMapper;
import com.alee.gera.service.UpdateUserService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UpdateUserServiceImpl implements UpdateUserService {
    @Resource
    UserMapper userMapper;
//    业务编码15
//    更新用户信息
    @Override
    public R updateUserInfoByToken(User user) {
        R r = new R();
        User t = userMapper.getUserByToken(user.getUserToken());
        if (t!=null){
//            token验证通过
            User sameNameUser = userMapper.getUserByName(user.getUserName());
            if(sameNameUser!=null){
//                存在同名用户
                String sameNameUserToken = sameNameUser.getUserToken();
                if(sameNameUserToken!=null){
//                    同名用户token存在
                    if(!sameNameUserToken.equals(user.getUserToken())){
//                        token不相等
                        r.setCode(1502);
                        r.setMsg("用户名重复");
                    }else{
//                        token相等
                        if(userMapper.updateUserInfoByToken(user)){
                            r.setCode(1510);
                            r.setMsg("更新成功");
                        }else {
                            r.setCode(1501);
                            r.setMsg("更新失败");
                        }
                    }

                }
                else {
//                    同名用户token为空
                    r.setCode(1503);
                    r.setMsg("更新失败");
                }
            }else {
//                不存在同名用户
                if(userMapper.updateUserInfoByToken(user)){
                    r.setCode(1510);
                    r.setMsg("更新成功");
                }else {
                    r.setCode(1501);
                    r.setMsg("更新失败");
                }
            }


        }else{
            r.setCode(1502);
            r.setMsg("找不到用户");
        }
        return r;
    }

    @Override
    public R changePassword(String userToken, String oldPassword, String newPassword) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            if(BCrypt.checkpw(oldPassword, user.getUserPassword())){
                String enCode = BCrypt.hashpw(newPassword,BCrypt.gensalt());
                user.setUserPassword(enCode);
                userMapper.updatePasswordByEmail(user);
                r.setMsg("修改成功");
            }else {
                r.setMsg("原密码错误");
            }
        }
        return r;
    }
}
