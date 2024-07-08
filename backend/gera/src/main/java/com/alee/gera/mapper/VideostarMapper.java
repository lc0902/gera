package com.alee.gera.mapper;

import com.alee.gera.entity.Videostar;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface VideostarMapper {
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

    @Insert("insert into videostar ( videostar_user_id, videostar_video_id) values (#{videostarUserId},#{videostarVideoId})")
    boolean insertVideostar(@Param("videostarUserId") Integer videostarUserId,@Param("videostarVideoId") Integer videostarVideoId);
    @Delete("delete from videostar where videostar_user_id = #{videostarUserId} and videostar_video_id = #{videostarVideoId}")
    boolean deleteVideostar(@Param("videostarUserId") Integer videostarUserId,@Param("videostarVideoId") Integer videostarVideoId);
    @Select("select * from videostar where videostar_user_id = #{videostarUserId} and videostar_video_id = #{videostarVideoId} limit 1")
    Videostar getVideostar(@Param("videostarUserId") Integer videostarUserId,@Param("videostarVideoId") Integer videostarVideoId);
    @Select("select count(*) from videostar where videostar_video_id = #{videostarVideoId}")
    Integer getVideostarNum(@Param("videostarVideoId")Integer videostarVideoId);
    @Select("select * from videostar where videostar_user_id = #{videostarUserId}")
    List<Videostar> getVideostarListByUserId(@Param("videostarUserId")Integer videostarUserId);

    @Delete("delete from videostar where videostar_video_id = #{videostarVideoId}")
    boolean deleteVideostarByVideoId(@Param("videostarVideoId")Integer videostarVideoId);
}
