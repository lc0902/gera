package com.alee.gera.service.impl;

import com.alee.gera.entity.Admin;
import com.alee.gera.entity.R;
import com.alee.gera.entity.User;
import com.alee.gera.mapper.AdminMapper;
import com.alee.gera.mapper.UserMapper;
import com.alee.gera.service.UserManagerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class UserManagerServiceImpl implements UserManagerService {
    @Resource
    UserMapper userMapper;
    @Override
    public R getUserList(Integer pageNum, Integer pageSize, String queryParam, String userToken) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            if(user.getUserRoleId()==5){
                PageHelper.startPage(pageNum,pageSize);
                List<User> users = userMapper.getUserByNameLike(queryParam);
                PageInfo<User> pageInfo = new PageInfo<>(users);
                r.setData(pageInfo);
            }else {
                r.setMsg("无权限，虽然恶魔不知道你是如何打开这个页面的，但是你仍将一无所获。");
            }
        }else {
            r.setMsg("用户不存在");
        }
        return r;
    }

    @Override
    public R resetUserSignature(String userToken, Integer userId) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            if(user.getUserRoleId()==5){
                if(userMapper.updateUserSignatureByUserId(userId)){
                    r.setMsg("成功");
                }
            }else {
                r.setMsg("无权限，虽然恶魔不知道你是如何打开这个页面的，但是你仍将一无所获。");
            }
        }else {
            r.setMsg("用户不存在");
        }
        return r;
    }

    @Override
    public R resetUserAvatar(String userToken, Integer userId) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            if(user.getUserRoleId()==5){
                if(userMapper.resetUserAvatarUrlByUserId(userId)){
                    r.setMsg("成功");
                }
            }else {
                r.setMsg("无权限，虽然恶魔不知道你是如何打开这个页面的，但是你仍将一无所获。");
            }
        }else {
            r.setMsg("用户不存在");
        }
        return r;
    }

    @Resource
    AdminMapper adminMapper;
    @Override
    public R modifyUserRole(String userToken, Integer userId, Integer roleId,Integer gameId) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            if(roleId == 4){
                Admin admin = adminMapper.isAdmin(userId);
                if(admin==null){
                    Admin admin1 = adminMapper.getAdminByGameId(gameId);
                    if(admin1!=null){
                        adminMapper.deleteAdmin(admin1.getAdminUserId());
                        userMapper.updateUserRoleByUserId(admin1.getAdminUserId(),4);
                    }
                    if(adminMapper.addAdmin(userId,gameId)){
                        userMapper.updateUserRoleByUserId(userId,roleId);
                        r.setMsg("成功");
                    }else{
                        r.setMsg("失败");
                    }
                }else{
                    Admin admin1 = adminMapper.getAdminByGameId(gameId);
                    if(admin1!=null){
                        adminMapper.deleteAdmin(admin1.getAdminUserId());
                        userMapper.updateUserRoleByUserId(admin1.getAdminUserId(),3);
                    }
                    if(adminMapper.updateAdmin(userId,gameId)){
                        userMapper.updateUserRoleByUserId(userId,roleId);
                        r.setMsg("成功");
                    }else{
                        r.setMsg("失败");
                    }
                }
            }else {
                Admin admin = adminMapper.isAdmin(userId);
                if(admin!=null){
                    adminMapper.deleteAdmin(userId);
                }
                userMapper.updateUserRoleByUserId(userId,roleId);
                r.setMsg("成功");
            }
        }else {
            r.setMsg("用户不存在");
        }
        return r;
    }

    @Override
    public R banUser(String userToken, Integer userId) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            if(user.getUserRoleId()==5 || user.getUserRoleId()==4){
                if(userMapper.banUserByUserId(userId)){
                    r.setMsg("成功");
                }
            }else {
                r.setMsg("无权限，虽然恶魔不知道你是如何打开这个页面的，但是你仍将一无所获。");
            }
        }else {
            r.setMsg("用户不存在");
        }
        return r;
    }

    @Override
    public R unbanUser(String userToken, Integer userId) {
        R r = new R();
        User user = userMapper.getUserByToken(userToken);
        if(user!=null){
            if(user.getUserRoleId()==5){
                if(userMapper.unBanUserByUserId(userId)){
                    r.setMsg("成功");
                }
            }else {
                r.setMsg("无权限，虽然恶魔不知道你是如何打开这个页面的，但是你仍将一无所获。");
            }
        }else {
            r.setMsg("用户不存在");
        }
        return r;
    }

    @Override
    public R getAllUser(String userToken) {
        R r = new R();

        // 获取用户信息
        User user = userMapper.getUserByToken(userToken);

        if(user!=null){
            if(user.getUserRoleId()==5){
                List<User> users =userMapper.getAllUsers();
                r.setData(users);
            }else {
                r.setMsg("用户权限不足");
            }
        }else {
            r.setMsg("用户不存在");
        }
        return r;
    }
}
