package com.alee.gera.entity;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class User {
    private Integer userId;
    private String userName;
    private String userPassword;
    private String userGender;
    private String userEmail;
    private String userToken;
    private String userSignature;
    private String userAvatarUrl;
    private Integer userRoleId;
    private Integer userStatus;
}
