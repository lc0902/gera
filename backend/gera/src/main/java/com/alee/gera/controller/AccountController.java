package com.alee.gera.controller;

import com.alee.gera.entity.Application;
import com.alee.gera.entity.R;
import com.alee.gera.entity.User;
import com.alee.gera.service.*;
import com.alee.gera.service.impl.UpdateUserServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;
import java.util.UUID;

@RestController
public class AccountController {

    @Resource
    RegisterService registerServiceImpl;
    @Resource
    LoginService loginService;
    @Resource
    RetrieveService retrieveService;

//    获取注册验证码
    @PostMapping("/enrollCode")
    public R enrollCode(@RequestParam String userEmail){
        R r = registerServiceImpl.getEnrollCode(userEmail);
        return r;
    }
//    注册
    @PostMapping("/enroll")
    public R enroll(@RequestParam Map<String,Object> map){
//        构建User
        User user = buildUserByMap(map);
        user.setUserStatus(1);
        user.setUserRoleId(1);
        R r = registerServiceImpl.enroll(user,(String) map.get("vCode"));
        return r;
    }
//    登录
    @PostMapping("/login")
    public R<User> login(@RequestParam Map<String,Object> map){
        R<User> r = new R();
        User user = buildUserByMap(map);
        r=(loginService.login(user));
        return r;
    }

//获取找回密码验证码
    @PostMapping("/retrieveCode")
    public R retrieveCode(@RequestParam String email){
        return retrieveService.sendRetrieveCode(email);
    }
//    找回密码
    @PostMapping("retrievePassword")
    public R retrievePassword(@RequestParam Map<String ,Object> map){
        User user = buildUserByMap(map);
        return retrieveService.retrievePassword(user,(String)map.get("vCode"));
    }

    @Resource
    UpdateUserService updateUserService;
//    修改个人信息
    @PostMapping("/modifyPersonalInformation")
    public R modifyPersonalInformation(@RequestParam Map<String,Object> map){
        User user = buildUserByMap(map);
        return updateUserService.updateUserInfoByToken(user);
    }

    @Resource
    ChangeAvatarService changeAvatarService;
    @PostMapping("/changeAvatar")
    public R changeAvatar(@RequestParam MultipartFile file,@RequestParam String userToken) {
        return changeAvatarService.changeAvatarByToken(file,userToken);
    }

//    修改密码
    @PostMapping("/modifyPassword")
    public R modifyPassword(@RequestParam Map<String,Object> map){
        return updateUserService.changePassword(
                (String) map.get("userToken"),
                (String) map.get("oldPassword"),
                (String) map.get("newPassword")
        );
    }






@Resource
ApplyService applyService;
//    TODO
    @PostMapping("/apply")
    public R apply(@RequestParam Map<String,Object> map){
        return applyService.addApply(
                buildApplicationByMap(map),
                (String) map.get("userToken")
        );
    }

    Application buildApplicationByMap(Map<String,Object> map){
        Application application = new Application();
        if(map.get("applicationId")!=null)
            application.setApplicationId(Integer.valueOf((String) map.get("applicationId")));
        if(map.get("applicationUserId")!=null)
            application.setApplicationUserId(Integer.valueOf((String) map.get("applicationUserId")));
        if(map.get("applicationType")!=null)
            application.setApplicationType((String) map.get("applicationType"));
        if(map.get("applicationText")!=null)
            application.setApplicationText((String) map.get("applicationText"));
        if(map.get("applicationStatus")!=null)
            application.setApplicationStatus(Integer.valueOf((String) map.get("applicationStatus")));
        return application;
    }

    //从前端传入map构建User对象
    public User buildUserByMap(Map<String,Object> map){
        User user = new User();
        if (map.get("userId")!=null)
            user.setUserId(Integer.valueOf((String) map.get("userId")));
        user.setUserName((String) map.get("userName"));
        user.setUserPassword((String) map.get("userPassword"));
        user.setUserGender((String) map.get("userGender"));
        user.setUserEmail((String) map.get("userEmail"));
        if (map.get("userRoleId")!=null)
            user.setUserRoleId(Integer.valueOf((String) map.get("userRoleId")));
        user.setUserToken((String) map.get("userToken"));
        user.setUserAvatarUrl((String) map.get("userAvatarUrl"));
        user.setUserSignature((String) map.get("userSignature"));
        if (map.get("userStatus")!=null)
            user.setUserStatus(Integer.valueOf((String) map.get("userStatus")));
        return user;
    }
}
