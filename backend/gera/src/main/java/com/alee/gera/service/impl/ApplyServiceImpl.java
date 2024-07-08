package com.alee.gera.service.impl;

import com.alee.gera.entity.Application;
import com.alee.gera.entity.R;
import com.alee.gera.entity.User;
import com.alee.gera.mapper.ApplicationMapper;
import com.alee.gera.mapper.UserMapper;
import com.alee.gera.service.ApplyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class ApplyServiceImpl implements ApplyService {

    @Resource
    UserMapper userMapper;
    @Resource
    ApplicationMapper applicationMapper;
    @Override
    public R addApply(Application application, String userToken) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            List<Application> applications = applicationMapper.getNotDealApplicationByUserId(user.getUserId(),application.getApplicationType());
            if (applications.isEmpty()){
                application.setApplicationUserId(user.getUserId());
                application.setApplicationStatus(0);
                r.setData(applicationMapper.insertApplication(application));
            }
        }
        return r;
    }

    @Override
    public R dealApply(Application application, String userToken) {
        return null;
    }
}
