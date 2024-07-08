package com.alee.gera.mapper;

import com.alee.gera.entity.Admin;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AdminMapper {
    @Insert("insert into admin(admin_user_id, admin_game_id) values(#{adminUserId}, #{adminGameId})")
    boolean addAdmin(@Param("adminUserId") Integer adminUserId,@Param("adminGameId") Integer adminGameId);

    @Select("select * from admin where admin_user_id = #{adminUserId}")
    Admin isAdmin(@Param("adminUserId") Integer adminUserId);

    @Update("update admin set admin_game_id = #{adminGameId} where admin_user_id = #{adminUserId}")
    boolean updateAdmin(@Param("adminUserId") Integer adminUserId, @Param("adminGameId") Integer adminGameId);

    @Delete("delete from admin where admin_user_id = #{adminUserId}")
    boolean deleteAdmin(@Param("adminUserId") Integer adminUserId);

    @Select("select * from admin where admin_game_id = #{adminGameId}")
    Admin getAdminByGameId(@Param("adminGameId") Integer adminGameId);



}
