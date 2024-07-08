package com.alee.gera.controller;

import com.alee.gera.entity.Live;
import com.alee.gera.entity.R;
import com.alee.gera.service.LiveService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class LiveController {
    @Resource
    LiveService liveService;
//    wtf?This system looks so naive,umm.....
    @PostMapping("/getLiveStatus")
    R getLiveStatus(@RequestParam Map<String,Object> map){
        return liveService.getLiveStatus((String) map.get("userToken"));
    }
    @PostMapping("/getLiveUrl")
    R getLiveCode(@RequestParam Map<String,Object> map){
//        done 获取直播推流地址
        return liveService.getLiveUrl((String) map.get("userToken"));
    }
    @PostMapping("/uploadLiveInfo")
    R uploadLiveInfo(@RequestParam Map<String,Object> map,@RequestParam MultipartFile liveCover){
//        done 上传直播相关信息
        return liveService.uploadLiveInfo(buildLiveByMap(map),(String) map.get("userToken"),liveCover);
    }
    @PostMapping("/startLive")
    R starLive(@RequestParam Map<String,Object> map){
//        done 开启直播
        return liveService.startLive((String) map.get("userToken"));
    }
    @PostMapping("/closeLive")
    R closeLive(@RequestParam Map<String,Object> map){
//        done 关闭直播
        return liveService.closeLive((String) map.get("userToken"));
    }

    @PostMapping("/getLiveList")
    R getLiveList(@RequestParam Map<String,Object> map){
//        done 获取直播列表
        return liveService.getLiveList((String) map.get("userToken"),(String) map.get("liveDescription"),Integer.valueOf((String) map.get("pageNum")),
                Integer.valueOf((String) map.get("pageSize")));
    }

    @PostMapping("/viewLive")
    R viewLive(@RequestParam Map<String,Object> map){
        return liveService.viewLive((String) map.get("userToken"),Integer.valueOf((String) map.get("liveId")));
    }




    Live buildLiveByMap(Map<String,Object> map){
        Live live = new Live();
        if(map.get("liveId")!=null)
            live.setLiveId(Integer.valueOf((String) map.get("liveId")));
        if(map.get("liveUserId")!=null)
            live.setLiveUserId(Integer.valueOf((String) map.get("liveUserId")));
        if(map.get("liveGameId")!=null)
            live.setLiveGameId(Integer.valueOf((String) map.get("liveGameId")));
        if(map.get("liveUrl")!=null)
            live.setLiveUrl((String) map.get("liveUrl"));
        if(map.get("liveStatus")!=null)
            live.setLiveStatus(Integer.valueOf((String) map.get("liveStatus")));
        if (map.get("liveDescription")!=null)
            live.setLiveDescription((String) map.get("liveDescription"));
        return live;
    }
}
