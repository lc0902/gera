package com.alee.gera.service.impl;

import com.alee.gera.entity.R;
import com.alee.gera.entity.User;
import com.alee.gera.mapper.UserMapper;
import com.alee.gera.service.RegisterService;
import com.alee.gera.util.EmailUtil;
import com.alee.gera.util.RedisUtil;
import com.alee.gera.util.VCodeUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Resource
    UserMapper userMapper;
    @Resource
    EmailUtil emailUtil;
    @Resource
    RedisUtil redisUtil;
//

    //    业务编码：10
    @Override
    public R getEnrollCode(String userEmail) {
        R r = new R();
        User user = userMapper.getUserByEmail(userEmail);
        if(user == null){
            String vCode =VCodeUtil.getVCode(6);
            String v = (String) redisUtil.get(userEmail+"-enroll");
            if(v == null){
                redisUtil.set(userEmail+"-enroll",vCode,300);
                emailUtil.sendEnrollCode(vCode,userEmail);
                r.setCode(1010);
                r.setMsg("发送成功");
            }else {
                r.setCode(1001);
                r.setMsg("请勿短时间内重复提交");
            }

        }else{
            r.setMsg("该邮箱已被注册");
            r.setCode(1002);
        }
        return r;
    }
    //    业务编码：11
    @Override
    public R enroll(User user, String vCode) {
        R r = new R();
        User t = userMapper.getUserByName(user.getUserName());
        if(t == null){
            String redisV = (String) redisUtil.get(user.getUserEmail()+"-enroll");
            if (redisV!=null){
                if(redisV.equals(vCode)){
                    user.setUserPassword(BCrypt.hashpw(user.getUserPassword(),BCrypt.gensalt()));
                    redisUtil.delete(user.getUserEmail()+"-enroll");
                    if(userMapper.insertUser(user)){
                        r.setCode(1110);
                        r.setMsg("注册成功");

                    }
                    else {
                        r.setCode(1109);
                        r.setMsg("发生未知错误");
                    }

                }else{
                    r.setCode(1101);
                    r.setMsg("验证码错误");
                }
            }else {
                r.setCode(1102);
                r.setMsg("验证码不存在");
            }


        }else {
            r.setCode(1103);
            r.setMsg("用户名重复");
        }
        return r;
    }
}
