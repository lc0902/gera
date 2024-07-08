package com.alee.gera.service.impl;

import com.alee.gera.entity.R;
import com.alee.gera.entity.User;
import com.alee.gera.mapper.UserMapper;
import com.alee.gera.service.LoginService;
import com.alee.gera.util.TokenGenerator;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    UserMapper userMapper;
//    业务编码:12
    @Override
    public R<User> login(User user) {
        R<User> r =new R();
        User t;
//        token为空时，判断用户账号密码是否正确，正确则写入新的token，错误返回错误码
        if(user.getUserToken()==null){
//              获取用户输入的登陆账号
            //            如果输入的账号为用户名
            t = userMapper.getUserByName(user.getUserName());
//            该用户名名存在
            if(t!=null){
//                账号密码正确
                if (BCrypt.checkpw(user.getUserPassword(),t.getUserPassword())) {
//                    生成token
                    String token = TokenGenerator.generateToken();
                    t.setUserToken(token);
                    userMapper.updateUserByUserId(t);
                    user.setUserToken(token);
                    user.setUserPassword(null);
                    r.setData(user);
                    r.setCode(1210);
                    r.setMsg("登录成功");
                }else {
//                    账号密码错误
                    r.setData(null);
                    r.setCode(1201);
                    r.setMsg("密码错误");
                }
            }else{
//                该用户名不存在
//                如果输入的是邮箱
                t = userMapper.getUserByEmail(user.getUserName());
                if(t!=null){
//                    该邮箱存在
//                    判断密码是否正确
                    if (BCrypt.checkpw(user.getUserPassword(),t.getUserPassword())) {
//                    生成token
                        String token = TokenGenerator.generateToken();
                        t.setUserToken(token);
                        userMapper.updateUserByUserId(t);
                        t.setUserPassword(null);
                        r.setData(t);
                        r.setCode(1210);
                        r.setMsg("登录成功");
                    }else {
//                    账号密码错误
                        r.setData(null);
                        r.setCode(1201);
                        r.setMsg("密码错误");
                    }
                }else{
                    r.setData(null);
                    r.setCode(1202);
                    r.setMsg("用户不存在");
                }
            }
        }else{
//            token存在
            t=userMapper.getUserByToken(user.getUserToken());
//            token正确
            if(t!=null){
                t.setUserPassword(null);
                r.setData(t);
                r.setMsg("登陆成功");
                r.setCode(1210);
            }else {
//           token错误
                r.setData(null);
                r.setCode(1203);
                r.setMsg("token错误");
            }
        }
        return r;
    }
}
