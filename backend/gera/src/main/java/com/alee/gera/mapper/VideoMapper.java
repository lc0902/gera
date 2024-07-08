package com.alee.gera.mapper;

import com.alee.gera.entity.Video;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface VideoMapper {
    @Select("select * from video where video_id = #{videoId}")
    Video getVideoById(Integer videoId);

    @Select("select * from video")
    List<Video> getAllVideo();

    @Insert("insert into video(video_user_id, video_game_id, video_url, video_cover_url, video_description, video_up_time, video_star_number) values (#{videoUserId},#{videoGameId},#{videoUrl},#{videoCoverUrl},#{videoDescription},#{videoUpTime},#{videoStarNumber})")
    boolean insertVideo(Video video);

    @Select("select * from video where video_description like concat('%',#{videoDescription},'%')")
    List<Video> getVideoWithDesc(@Param("videoDescription") String videoDescription);

    @Update("update video set video_star_number = #{videoStarNumber} where video_id = #{videoId}")
    boolean updateVideoStarNumById(@Param("videoStarNumber")Integer videoStarNumber,@Param("videoId") Integer videoId);

    @Select("select * from video where video_game_id = #{videoGameId} and video_description like concat('%',#{videoDescription},'%')")
    List<Video> getVideoByGameId(@Param("videoGameId") Integer videoGameId,@Param("videoDescription") String videoDescription);

    @Select("select * from video where video_user_id = #{videoUserId} and video_description like concat('%',#{videoDescription},'%')")
    List<Video> getVideoByUserId(@Param("videoUserId")Integer videoUserId,@Param("videoDescription")String videoDescription);

    @Delete("delete from video where video_id = #{videoId}")
    boolean deleteVideoById(@Param("videoId")Integer videoId);

    @Select("select * from video where video_game_id = #{videoGameId}")
    List<Video> getVideoByGameId2(@Param("videoGameId") Integer videoGameId);
}
