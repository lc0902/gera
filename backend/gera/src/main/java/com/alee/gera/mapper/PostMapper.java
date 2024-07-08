package com.alee.gera.mapper;

import com.alee.gera.entity.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostMapper {
    @Select("select * from post")
    List<Post> getAllPost();

    @Select("select * from post where post_user_id = #{postUserId}")
    List<Post> getPostByUserId(Integer postUserId);

    @Insert("insert into post ( post_user_id, post_game_id, post_title, post_text, post_time, post_star_num) values " +
            "( #{postUserId} , #{postGameId} , #{postTitle} , #{postText} , #{postTime} , #{postStarNum})")
    boolean uploadPost(Post post);

    @Select("select * from post where post_title like concat('%',#{postTitle},'%')")
    List<Post> getPostListByTitle(String postTitle);
    @Select("select * from post where post_game_id = #{postGameId} and post_title like concat('%',#{postTitle},'%')")
    List<Post> getPostListByGameIdAndTitle(@Param("postGameId") Integer postGameId,@Param("postTitle") String postTitle);

    @Select("select * from post where post_id = #{postId}")
    Post getPostById(Integer postId);

    @Update("update post set post_star_num = #{postStarNum} where post_id = #{postId}")
    boolean updatePostStarNum(@Param("postStarNum") Integer postStarNum,@Param("postId") Integer postId);

    @Select("select * from post where post_game_id = #{postGameId} and post_title like concat('%',#{postTitle},'%')")
    List<Post> getPostListByGameId(@Param("postGameId")Integer postGameId,@Param("postTitle")String postTitle);

    @Select("select * from post where post_user_id = #{postUserId} and post_title like concat('%',#{postTitle},'%')")
    List<Post> getPersonalPost(@Param("postUserId")Integer postUserId,@Param("postTitle")String postTitle);

    @Delete("delete from post where post_id = #{postId}")
    boolean deletePost(@Param("postId") Integer postId);

    @Select("select * from post where post_game_id = #{postGameId}")
    List<Post> getPostByGameId(Integer postGameId);
}
