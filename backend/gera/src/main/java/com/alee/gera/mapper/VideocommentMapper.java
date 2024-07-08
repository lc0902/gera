package com.alee.gera.mapper;

import com.alee.gera.entity.Videocomment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface VideocommentMapper {
    @Select("select * from videocomment where videocomment_id = #{videocommentId}")
    Videocomment getVideocommentById(Integer videocommentId);

    @Select("select * from videocomment where videocomment_text like concat('%',#{videocommentText},'%')")
    List<Videocomment> getVideocommentListByText(@Param("videocommentText") String videocommentText);

    @Select("select * from videocomment where videocomment_text like concat('%',#{videocommentText},'%') and videocomment_video_id in" +
            "(select video_id from video where video_game_id = #{videoGameId})")
    List<Videocomment> getVideocommentListByTextAndGameId(@Param("videocommentText") String videocommentText, @Param("videoGameId") Integer videoGameId);

    @Insert("insert into videocomment( videocomment_user_id, videocomment_video_id, videocomment_text, videocomment_time) VALUES (#{videocommentUserId},#{videocommentVideoId},#{videocommentText},#{videocommentTime})")
    boolean insertVideocomment(Videocomment videocomment);

    @Select("select * from videocomment where videocomment_video_id = #{videocommentVideoId}")
    List<Videocomment> getVideocommentListByVideoId(Integer videocommentVideoId);

    @Delete("delete from videocomment where videocomment_id = #{videocommentId}")
    boolean deleteVideocommentById(Integer videocommentId);

    @Delete("delete from videocomment where videocomment_video_id = #{videocommentVideoId}")
    boolean deleteVideocommentByVideoId(@Param("videocommentVideoId") Integer videocommentVideoId);
}
