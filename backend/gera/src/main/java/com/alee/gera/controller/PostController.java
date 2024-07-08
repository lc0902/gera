package com.alee.gera.controller;

import com.alee.gera.entity.Post;
import com.alee.gera.entity.R;
import com.alee.gera.entity.User;
import com.alee.gera.mapper.UserMapper;
import com.alee.gera.service.PostListService;
import com.alee.gera.service.PostService;
import com.alee.gera.service.UploadPostService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class PostController {
    @Resource
    UploadPostService uploadPostService;

    @PostMapping("/uploadImg")
    public R uploadImg(@RequestParam MultipartFile file){
        return  uploadPostService.uploadImg(file);
    }

    @Resource
    UserMapper userMapper;
    @PostMapping("uploadPost")
    public  R uploadPost(@RequestParam Map<String,Object> map){
        Post post = buildPostFromMap(map);
        User user = userMapper.getUserByToken((String) map.get("userToken"));
        if (user.getUserStatus()==1){
            return uploadPostService.uploadPost(post);
        }
        else {
            R r = new R();
            r.setCode(9999);
            r.setMsg("账号异常");
            return  r;
        }
    }
    @Resource
    PostListService postListService;

    @PostMapping("/getPostList")
    public R getPostList(@RequestParam Map<Object,String> map){
        return postListService.getPostList(Integer.valueOf((String) map.get("pageNum")),Integer.valueOf((String) map.get("pageSize")),(String) map.get("postTitle"));
    }

    @Resource
    PostService postService;

    @PostMapping("/viewPost")
    public R viewPost(@RequestParam Integer postId){
        return postService.viewPost(postId);
    }

    @PostMapping("/getPersonalUploadPost")
    R getPersonalUploadPost(@RequestParam Map<String,Object> map){
        return postListService.getPersonalPostList(
                Integer.valueOf((String) map.get("pageNum")),
                Integer.valueOf((String) map.get("pageSize")),
                (String) map.get("postTitle"),
                (String) map.get("userToken")
        );
    }
    @PostMapping("/deletePost")
    public R deletePost(@RequestParam Map<String,Object> map){
        return postService.deletePost(
                Integer.valueOf((String) map.get("postId")),
                (String) map.get("userToken")
        );
    }

    private Post buildPostFromMap(Map<String,Object> map){
        Post post = new Post();
        if(map.get("postId")!=null)
            post.setPostId(Integer.valueOf((String) map.get("postId")));
        if(map.get("postUserId")!=null)
            post.setPostUserId(Integer.valueOf((String) map.get("postUserId")));
        if(map.get("postGameId")!=null)
            post.setPostGameId(Integer.valueOf((String) map.get("postGameId")));
        if(map.get("postTitle")!=null)
            post.setPostTitle((String) map.get("postTitle"));
        if(map.get("postText")!=null)
            post.setPostText((String) map.get("postText"));
        return post;
    }
}
