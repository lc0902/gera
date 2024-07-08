package com.alee.gera.mapper;

import com.alee.gera.entity.Poststar;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PoststarMapper {
    @Insert("insert into poststar (poststar_user_id, poststar_post_id) values (#{poststarUserId},#{poststarPostId})")
    boolean insertPoststar(@Param("poststarUserId") Integer poststarUserId, @Param("poststarPostId") Integer poststarPostId);
    @Delete("delete from poststar where poststar_user_id = #{poststarUserId} and poststar_post_id = #{poststarPostId}")
    boolean deletePoststar(@Param("poststarUserId") Integer poststarUserId, @Param("poststarPostId") Integer poststarPostId);
    @Select("select * from poststar where poststar_user_id = #{poststarUserId} and poststar_post_id = #{poststarPostId} limit 1")
    Poststar getPoststar(@Param("poststarUserId") Integer poststarUserId, @Param("poststarPostId") Integer poststarPostId);
    @Select("select count(*) from poststar where poststar_post_id = #{poststarPostId}")
    Integer getPoststarNum(Integer poststarPostId);
    @Select("select * from poststar where poststar_user_id = #{poststarUserId}")
    List<Poststar> getPoststarListByUserId(Integer poststarUserId);

    @Delete("delete from poststar where poststar_post_id = #{poststarPostId}")
    boolean deletePoststarByPostId(@Param("poststarPostId") Integer poststarPostId);

}
