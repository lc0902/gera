package com.alee.gera.mapper;

import com.alee.gera.entity.R;
import com.alee.gera.entity.Rate;
import org.apache.ibatis.annotations.*;

import java.net.Inet4Address;

@Mapper
public interface RateMapper {
    @Select("select * from rate where rate_user_id = #{rateUserId} and rate_game_id = #{rateGameId}")
    Rate getRateByUserIdAndGameId(@Param("rateUserId")Integer rateUserId,@Param("rateGameId")Integer rateGameId);

    @Insert("insert into rate ( rate_user_id, rate_game_id, rate_rate) values (#{rateUserId},#{rateGameId},#{rateRate})")
    boolean insertRate(@Param("rateUserId")Integer rateUserId,@Param("rateGameId")Integer rateGameId,@Param("rateRate")Float rateRate);

    @Select("select AVG(rate_rate) from rate where rate_game_id = #{rateGameId}")
    Float getAvgRate(@Param("rateGameId")Integer rateGameId);

    @Delete("delete from rate where rate_game_id = #{rateGameId}")
    boolean deleteRateByGameId(@Param("rateGameId")Integer rateGameId);
}
