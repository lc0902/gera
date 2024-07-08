package com.alee.gera.service.impl;

import com.alee.gera.entity.R;
import com.alee.gera.entity.User;
import com.alee.gera.mapper.UserMapper;
import com.alee.gera.service.RetrieveService;
import com.alee.gera.util.EmailUtil;
import com.alee.gera.util.RedisUtil;
import com.alee.gera.util.VCodeUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class RetrieveServiceImpl implements RetrieveService {
    @Resource
    EmailUtil emailUtil;
    @Resource
    UserMapper userMapper;
    @Resource
    RedisUtil redisUtil;
//    业务编码:13
    @Override
    public R sendRetrieveCode(String email) {
        R r = new R();
//        判断该email是否注册账号
        User t =userMapper.getUserByEmail(email);
        if(t!=null){
            String vCode=VCodeUtil.getVCode(6);
            redisUtil.set(email+"-retrieve",vCode,300);
            emailUtil.sendRetrieveCode(vCode,email);
            r.setMsg("发送成功");
            r.setCode(1310);
        }else {
            r.setCode(1301);
            r.setMsg("用户不存在");
        }
        return r;
    }

//    业务编码:14
    @Override
    public R retrievePassword(User user,String vCode) {
        R r =new R();
//        获取用户
        User t = userMapper.getUserByEmail(user.getUserEmail());
        if(t!=null){
            String redisK = t.getUserEmail()+"-retrieve";
            String redisV = (String) redisUtil.get(redisK);
            if(vCode.equals(redisV)){
//                验证码匹配
                String enCode = BCrypt.hashpw(user.getUserPassword(),BCrypt.gensalt());
                user.setUserPassword(enCode);
                if (userMapper.updatePasswordByEmail(user)) {
//                    修改成功
                    redisUtil.delete(redisK);
                    r.setCode(1410);
                    r.setMsg("密码修改成功");
                }else{
                    r.setCode(1409);
                    r.setMsg("发生未知错误");
                }
            }else{
                r.setCode(1401);
                r.setMsg("验证码不匹配");
            }

        }else {
            r.setCode(1408);
            r.setMsg("用户不存在");
        }
        return r;
    }
}
