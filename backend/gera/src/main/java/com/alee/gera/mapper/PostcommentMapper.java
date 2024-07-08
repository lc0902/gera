package com.alee.gera.mapper;

import com.alee.gera.entity.Post;
import com.alee.gera.entity.Postcomment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostcommentMapper {
    @Insert("insert into postcomment (postcomment_id, postcomment_user_id, postcomment_post_id, postcomment_text, postcomment_time) values (#{postcommentId} ,#{postcommentUserId},#{postcommentPostId},#{postcommentText},#{postcommentTime})")
    boolean commentPost(Postcomment postcomment);

    @Select("select * from postcomment where postcomment_text like concat('%',#{postcommentText},'%')")
    List<Postcomment> getPostcommentByText(@Param("postcommentText") String postcommentText);

    @Select("select * from postcomment where postcomment_text like concat('%',#{postcommentText},'%') and postcomment_post_id in " +
            "(select post_id from post where post_game_id = #{postcommentGameId})")
    List<Postcomment> getPostcommentByTextAndGameId(@Param("postcommentText") String postcommentText, @Param("postcommentGameId") Integer postcommentGameId);

    @Select("select * from postcomment where postcomment_post_id = #{postcommentPostId}")
    List<Postcomment> getPostcommentByPostId(Integer postcommentPostId);

    @Select("select * from postcomment where postcomment_user_id = #{postcommentUserId}")
    List<Postcomment> getPostcommentByUserId(Integer postcommentUserId);

    @Select("select * from postcomment where postcomment_id = #{postcommentId}")
    Postcomment getPostcommentById(Integer postcommentId);

    @Delete("delete from postcomment where postcomment_id =  #{postcommentId}")
    boolean deletePostcommentById(Integer postcommentId);

    @Delete("delete from postcomment where postcomment_post_id = #{postcommentPostId}")
    boolean deletePostcommentByPostId(@Param("postcommentPostId")Integer postcommentPostId);
}
