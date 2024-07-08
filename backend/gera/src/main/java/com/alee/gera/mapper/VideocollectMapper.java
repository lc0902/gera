package com.alee.gera.mapper;

import com.alee.gera.entity.Videocollect;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface VideocollectMapper {
    @Insert("insert into videocollect ( videocollect_user_id, videocollect_video_id) values (#{videocollectUserId},#{videocollectVideoId})")
    boolean insertVideocollect(@Param("videocollectUserId") Integer videocollectUserId,@Param("videocollectVideoId") Integer videocollectVideoId);

    @Delete("delete from videocollect where videocollect_user_id = #{videocollectUserId} and videocollect_video_id = #{videocollectVideoId}")
    boolean deleteVideocollect(@Param("videocollectUserId") Integer videocollectUserId,@Param("videocollectVideoId") Integer videocollectVideoId);

    @Select("select * from videocollect where videocollect_user_id = #{videocollectUserId} and videocollect_video_id = #{videocollectVideoId} limit 1")
    Videocollect getVideocollect(@Param("videocollectUserId") Integer videocollectUserId, @Param("videocollectVideoId") Integer videocollectVideoId);

    @Select("select * from videocollect where videocollect_user_id = #{videocollectUserId}")
    List<Videocollect> getVideocollectListByUserId(@Param("videocollectUserId")Integer videocollectUserId);

    @Select("select * from videocollect where videocollect_user_id = #{videocollectUserId} and videocollect_video_id in (select video_id from video where video_description like concat('%',#{videoDescription},'%'))")
    List<Videocollect> getVideocollectListByUserIdWithDesc(@Param("videocollectUserId")Integer videocollectUserId,@Param("videoDescription")String videoDescription);

    @Delete("delete from videocollect where videocollect_video_id = #{videocollectVideoId}")
    boolean deleteVideocollectByVideoId(@Param("videocollectVideoId")Integer videocollectVideoId);
}


