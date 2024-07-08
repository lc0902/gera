package com.alee.gera.service.impl;

import com.alee.gera.entity.Application;
import com.alee.gera.entity.R;
import com.alee.gera.entity.User;
import com.alee.gera.mapper.ApplicationMapper;
import com.alee.gera.mapper.UserMapper;
import com.alee.gera.service.ApplyManagerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApplyManagerServiceImpl implements ApplyManagerService {
    @Resource
    UserMapper userMapper;
    @Resource
    ApplicationMapper applicationMapper;
    @Override
    public R getApplyList(String userToken, Integer pageNum, Integer pageSize) {
        User user = userMapper.getUserByToken(userToken);
        Map<String,Object> result = new HashMap<>();
        R r = new R();
        if(user!=null){
            if (user.getUserRoleId()==5){
                PageHelper.startPage(pageNum,pageSize);
                List<Application> applications = applicationMapper.getNotDealApplication();
                PageInfo<Application> pageInfo = new PageInfo<>(applications);
                result.put("pageInfo",pageInfo);
                List<User> users = new ArrayList<>();
                for (int i = 0; i < applications.size(); i++) {
                    User user1 = userMapper.getUserById(applications.get(i).getApplicationUserId());
                    user1.setUserToken(null);
                    user1.setUserPassword(null);

                    users.add(user1);
                }
                result.put("users",users);
            }else {
                r.setMsg("权限不足");
            }
        }else {
            //用户不存在
            r.setMsg("用户不存在");
        }
        r.setData(result);
        return r;
    }

    @Override
    public R dealApply(String userToken, Integer applyId, String dealCode) {
        User user = userMapper.getUserByToken(userToken);
        R r = new R();
        if(user!=null){
            if(user.getUserRoleId()==5){
                Application application = applicationMapper.getApplicationById(applyId);
                User user1 = userMapper.getUserById(application.getApplicationUserId());
                if(application!=null){
                    if(application.getApplicationType().equals("1")){
                        if (dealCode.equals("1")){
                            if (user1.getUserRoleId()==1){
                                user1.setUserRoleId(2);
                                userMapper.updateUserRoleByUserId(user1.getUserId(),2);
                                applicationMapper.dealApplicationStatus(applyId);
                                r.setMsg("成功");
                            }
                        }else {
                            applicationMapper.dealApplicationStatus(applyId);
                            r.setMsg("成功");

                        }

                    }else if (application.getApplicationType().equals("2")){
                        if (dealCode.equals("1")){
                            if (user1.getUserRoleId()==2){
                                user1.setUserRoleId(3);
                                userMapper.updateUserRoleByUserId(user1.getUserId(),3);
                                applicationMapper.dealApplicationStatus(applyId);
                                r.setMsg("成功");
                            }
                        }else {
                            applicationMapper.dealApplicationStatus(applyId);
                            r.setMsg("成功");


                        }
                    }else if (application.getApplicationType().equals("3")){
                        if(dealCode.equals("1")){
                            userMapper.unbanUserById(user1.getUserId());
                            r.setMsg("成功");
                            applicationMapper.dealApplicationStatus(applyId);

                        }else {
                            applicationMapper.dealApplicationStatus(applyId);
                            r.setMsg("成功");
                            applicationMapper.dealApplicationStatus(applyId);

                        }
                    }
                }
            }
        }
        return r;
    }
}
