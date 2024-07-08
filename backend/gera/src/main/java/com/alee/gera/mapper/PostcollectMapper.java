package com.alee.gera.mapper;

import com.alee.gera.entity.Postcollect;
import com.alee.gera.entity.Poststar;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostcollectMapper {
//    @Insert("insert into poststar (poststar_user_id, poststar_post_id) values (#{poststarUserId},#{poststarPostId})")
//    boolean insertPoststar(@Param("poststarUserId") Integer poststarUserId, @Param("poststarPostId") Integer poststarPostId);
//    @Delete("delete from poststar where poststar_user_id = #{poststarUserId} and poststar_post_id = #{poststarPostId}")
//    boolean deletePoststar(@Param("poststarUserId") Integer poststarUserId, @Param("poststarPostId") Integer poststarPostId);
//    @Select("select * from poststar where poststar_user_id = #{poststarUserId} and poststar_post_id = #{poststarPostId} limit 1")
//    Poststar getPoststar(@Param("poststarUserId") Integer poststarUserId, @Param("poststarPostId") Integer poststarPostId);
//    @Select("select count(*) from poststar where poststar_post_id = #{poststarPostId}")
//    Integer getPoststarNum(Integer poststarPostId);
//    @Select("select * from poststar where poststar_user_id = #{poststarUserId}")
//    List<Poststar> getPoststarListByUserId(Integer poststarUserId);

    @Insert("insert into postcollect ( postcollect_user_id, postcollect_post_id) values (#{postcollectUserId},#{postcollectPostId})")
    boolean insertPostcollect(@Param("postcollectUserId") Integer postcollectUserId,@Param("postcollectPostId") Integer postcollectPostId);

    @Delete("delete from postcollect where postcollect_post_id = #{postcollectPostId} and postcollect_user_id = #{postcollectUserId}")
    boolean deletePostcollect(@Param("postcollectUserId") Integer postcollectUserId,@Param("postcollectPostId") Integer postcollectPostId);

    @Select("select * from postcollect where postcollect_post_id = #{postcollectPostId} and postcollect_user_id = #{postcollectUserId} limit 1")
    Postcollect getPostcollect(@Param("postcollectUserId") Integer postcollectUserId,@Param("postcollectPostId") Integer postcollectPostId);

    @Select("select count(*) from postcollect where postcollect_post_id = #{postcollectPostId}")
    Integer getPostCollectNum(@Param("postcollectPostId") Integer postcollectPostId);

    @Select("select * from postcollect where postcollect_user_id = #{postcollectUserId}")
    List<Postcollect> getPostcollectListByUserId(@Param("postcollectUserId") Integer postcollectUserId);

    @Select("select * from postcollect where postcollect_user_id = #{postcollectUserId} and postcollect_post_id in (select post_id from post where post_title like concat('%',#{postTitle},'%'))")
    List<Postcollect> getPostcollectListByUserIdWithTitle(@Param("postcollectUserId") Integer postcollectUserId,@Param("postTitle")String postTitle);

    @Delete("delete from postcollect where postcollect_post_id = #{postcollectPostId}")
    boolean deletePostcollectByPostId(@Param("postcollectPostId")Integer postcollectPostId);
}
