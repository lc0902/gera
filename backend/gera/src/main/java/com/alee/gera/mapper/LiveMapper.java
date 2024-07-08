package com.alee.gera.mapper;

import com.alee.gera.entity.Live;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LiveMapper {
    @Select("select * from live where live_user_id = #{liveUserId}")
    Live getLiveByUserId(@Param("liveUserId")Integer liveUserId);
    @Insert("insert into live (live_user_id,live_status,live_url) VALUES (#{liveUserId},0,#{liveUrl})")
    boolean initLiveByUserId(Live live);

    @Update("update live set live_game_id = #{liveGameId},live_cover_url = #{liveCoverUrl},live_description = #{liveDescription}" +
            " where live_user_id = #{liveUserId}")
    boolean updateLiveInfo(Live live);

    @Update("update live set live_status = 1 where live_user_id = #{liveUserId}")
    boolean startLive(@Param("liveUserId") Integer liveUserId);

    @Update("update live set live_status = 0 where live_user_id = #{liveUserId}")
    boolean closeLive(@Param("liveUserId") Integer liveUserId);

    @Update("update live set live_status = 0 where live_id = #{liveId}")
    boolean closeLiveByLiveId(@Param("liveId") Integer liveId);


    @Select("select * from live where live_status = 1 and live_description like concat('%',#{liveDescription},'%')")
    List<Live> getLivingList(@Param("liveDescription") String liveDescription);

    @Select("select * from live where live_id = #{liveId}")
    Live getLiveById(@Param("liveId")Integer liveId);

    @Select("select * from live where live_game_id = #{liveGameId} and live_description like concat('%',#{liveDescription},'%') and live_status = 1")
    List<Live> getLiveListByGameId(@Param("liveGameId")Integer liveGameId,@Param("liveDescription")String liveDescription);
    @Update("update live set live_game_id = NULL where live_game_id = #{liveGameId}")
    boolean resetLiveByGameId(@Param("liveGameId")Integer liveGameId);
}
