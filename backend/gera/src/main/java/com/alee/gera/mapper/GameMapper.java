package com.alee.gera.mapper;

import com.alee.gera.entity.Game;
import com.alee.gera.entity.User;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface GameMapper {
    @Select("select * from game")
    public List<Game> getAllGame();
    @Select("select * from game where game_name like concat('%',#{gameName},'%')")
    List<Game> getGameByName(@Param("gameName") String gameName);

    @Insert("insert into game ( game_name, game_cover_url, game_release_time, game_description) values (#{gameName}," +
            "#{gameCoverUrl} , #{gameReleaseTime} , #{gameDescription} );")
    boolean addGame(Game game);

    @Select("select * from game where game_id = #{gameId}")
    Game getGameById(Integer gameId);

    @Select("select * from game where game_id = #{gameId}")
    Game getGameByGameId(Integer gameId);

    @Update("update game set game_rate = #{gameRate} where game_id = #{gameId}")
    boolean updateGameRate(@Param("gameRate")Float gameRate,@Param("gameId")Integer gameId);

    @Update("update game set game_name = #{gameName} where game_id = #{gameId}")
    boolean updateGameName(@Param("gameName")String gameName,@Param("gameId")Integer gameId);
    @Update("update game set game_description = #{gameDescription} where game_id = #{gameId}")
    boolean updateGameDescription(@Param("gameDescription")String gameDescription,@Param("gameId")Integer gameId);

    @Update("update game set game_cover_url = #{gameCoverUrl} where game_id = #{gameId}")
    boolean updateGameCoverUrl(@Param("gameCoverUrl")String gameCoverUrl,@Param("gameId")Integer gameId);

    @Update("update game set game_release_time = #{gameReleaseTime} where game_id = #{gameId}")
    boolean updateGameReleaseTime(@Param("gameReleaseTime") Timestamp gameReleaseTime, @Param("gameId")Integer gameId);

    @Delete("delete from game where game_id = #{gameId}")
    boolean deleteGameById(Integer gameId);
}
