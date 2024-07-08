package com.alee.gera.mapper;

import com.alee.gera.entity.User;
import org.apache.ibatis.annotations.*;

import javax.annotation.Resource;
import java.util.List;

@Mapper
public interface UserMapper {
    @Update("update user set user_status = 1 where user_id = #{userId}")
    void unbanUserById(Integer userId);
    @Select("select * from user")
    List<User> getAllUser();
    @Select("select * from user where user_id = #{userId}")
    User getUserById(Integer userId);

    @Select("SELECT * from user where user_status = 0")
    List<User> getAllBannedUser();

    @Select("Select * from user where user_status = 1")
    List<User> getAllNormalStatusUser();

    @Select("SELECT * from user where user_role_id = 1")
    List<User> getAllNormalUser();

    @Select("Select * from user where user_role_id = 2")
    List<User> getAllVideoUser();

    @Select("Select * from user where user_role_id = 3")
    List<User> getAllLiveUser();

    @Select("Select * from user where user_role_id = 4")
    List<User> getAllAdmin();

    @Select("Select * from user where user_role_id = 5")
    List<User> getAllModerator();


    @Select("select * from user where user_name = #{userName}")
    User getUserByName(String userName);

    @Select("select * from user where user_email = #{userEmail}")
    User getUserByEmail(String userEmail);

    @Select("select * from user where user_token = #{userToken}")
    User getUserByToken(String userToken);




    @Insert("insert into user( user_name, user_password, user_gender, user_email, user_role_id, user_token, user_signature, user_avatar_url, user_status) values (#{userName},#{userPassword},#{userGender},#{userEmail},#{userRoleId},#{userToken},#{userSignature},#{userAvatarUrl},#{userStatus})")
    boolean insertUser(User user);

    @Update("UPDATE user SET user_name = #{userName} , user_password = #{userPassword} , user_email = #{userEmail} , user_gender = #{userGender} , user_avatar_url = #{userAvatarUrl} , user_signature = #{userSignature} , user_role_id = #{userRoleId} , user_status = #{userStatus} , user_token = #{userToken} WHERE user_id = #{userId}")
    boolean updateUserByUserId(User user);
    @Update("UPDATE user set user_password =#{userPassword} where user_email = #{userEmail}")
    boolean updatePasswordByEmail(User user);
    @Update("update user set user_name = #{userName},user_gender = #{userGender},user_signature = #{userSignature} where user_token = #{userToken}")
    boolean updateUserInfoByToken(User user);
    @Update("update user set user_avatar_url = #{userAvatarUrl} where user_token = #{userToken}")
    boolean updateAvatarByToken(User user);

    @Select("<script>" +
            "select * from user" +
            "<where>" +
            "<if test='userName!=null'>user_name=#{userName}</if>" +
            "<if test='userGender!=null'>and user_gender=#{userGender}</if>" +
            "<if test='userEmail!=null'>and user_email=#{userEmail}</if>" +
            "<if test='userRoleId!=null'>and user_role_id=#{userRoleId}</if>" +
            "<if test='userStatus!=null'>and user_status=#{userStatus}</if>" +
            "</where>" +
            "</script>")
    List<User> getUserMultipleConditions(User user);


    @Select("select * from user where user_name like concat('%',#{userName},'%')")
    List<User> getUserByNameLike(@Param("userName") String userName);
    @Update("update user set user_signature = null where user_id = #{userId}")
    boolean updateUserSignatureByUserId(@Param("userId") Integer userId);
    @Update("update user set user_avatar_url = 'http://localhost/pic/avatar/avatar_default.png' " +
            "where user_id = #{userId}")
    boolean resetUserAvatarUrlByUserId(@Param("userId") Integer userId);

    @Update("update user set user_status = 0 where user_id = #{userId}")
    boolean banUserByUserId(@Param("userId") Integer userId);

    @Update("update user set user_status = 1 where user_id = #{userId}")
    boolean unBanUserByUserId(@Param("userId") Integer userId);

    @Update("update user set user_role_id = #{userRoleId} where user_id = #{userId}")
    boolean updateUserRoleByUserId(@Param("userId") Integer userId,@Param("userRoleId") Integer userRoleId);


    @Select("select * from user")
    List<User> getAllUsers();
}
