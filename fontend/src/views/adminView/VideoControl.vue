<template>
    <n-modal v-model:show="showPreviewModal">
        <div>
        <flv-player :type="'mp4'" :url="selectedVideoUrl" :isLive="false">

        </flv-player>
        </div>

    </n-modal>
    <h1>
        视频管理
    </h1>
    <br>
    <n-input-group>
        <n-input v-model:value="input1" placeholder="输入标题"></n-input>
        <n-button type="primary" @click="search">搜索</n-button>
    </n-input-group>
    <br>
    <br>
    <n-table>
        <tr>
            <th>视频id</th>
            <th>视频标题</th>
            <th>所属板块</th>
            <th>封面</th>
            <th>上传时间</th>
            <th>所属用户</th>
            <th>点赞数</th>
            <th>操作</th>
        </tr>
        <tbody>
            <tr v-for="i in gameList">
                <td>{{ i.videoId }}</td>
                <td>{{ i.videoTitle }}</td>
                <td>
                    <div style="display: flex;align-items: center;">
                        <n-avatar :src="i.videoGameAvatarUrl" :size="90"></n-avatar>
                        {{ i.videoGameName }}

                    </div>    
                </td>

                    <td><n-image :src="i.videoCoverUrl" width="100" height="100"></n-image></td>

                <td>{{ i.videoTime }}</td>
                <td>
                    <div style="display: flex;align-items: center;">
                        <n-avatar :src="i.videoUserAvatarUrl" :size="50" round></n-avatar>
                        {{ i.videoUserName }}

                    </div>
                </td>
                <td>{{ i.videoStarNum }}</td>
                <td>
                    <n-button @click="previewVideo(i.videoId)">预览</n-button>
                    <n-button type="primary" @click="adminDeleteVideo(i.videoId)">删除视频</n-button>
                    <n-button type="error" @click="banUser(i.videoUserId)">封禁用户</n-button>
                </td>
            </tr>
        </tbody>
    </n-table>
    <br>

    <n-pagination v-model:page="page" :page-count="pageCount" />
</template>

<script setup lang="ts">
import FlvPlayer from '@/components/FlvPlayer.vue';
import axios from 'axios';
import { onMounted,ref, watch } from 'vue';
import { useUserStore } from '@/stores/user';
import { post } from 'node_modules/axios/index.cjs';
const userStore = useUserStore();
const selectedVideoUrl = ref("")
const showPreviewModal = ref(false)
function previewVideo(videoId:number){
    axios.post("http://localhost:8080/viewVideo?videoId="+videoId).then(resp=>{
        console.log(resp.data)
        selectedVideoUrl.value =resp.data.data.video.videoUrl
    })
    showPreviewModal.value = true

}





class GameListClass{
    videoId:number
    videoTitle:string
    videoGameAvatarUrl:string
    videoGameId:number
    videoGameName:string
    videoCoverUrl:string
    videoTime:string
    videoUserAvatarUrl:string
    videoUserId:number
    videoUserName:string
    videoStarNum:number
    constructor(
        videoId:number,
        videoTitle:string,
        videoGameAvatarUrl:string,
        videoGameId:number,
        videoGameName:string,
        videoCoverUrl:string,
        videoTime:string,
        videoUserAvatarUrl:string,
        videoUserId:number,
        videoUserName:string,
        videoStarNum:number
    ){
        this.videoId = videoId
        this.videoTitle = videoTitle
        this.videoGameAvatarUrl = videoGameAvatarUrl
        this.videoGameId = videoGameId
        this.videoGameName = videoGameName
        this.videoCoverUrl = videoCoverUrl
        this.videoTime = videoTime
        this.videoUserAvatarUrl = videoUserAvatarUrl
        this.videoUserId = videoUserId
        this.videoUserName = videoUserName
        this.videoStarNum = videoStarNum
    }
}

const gameList = ref([] as GameListClass[])
const input1  = ref("")
const videoDESC = ref("")
const page = ref(1)
const pageCount = ref(1)
function getVideoList(){
    gameList.value = []
    let formdata = new FormData();
    formdata.append("userToken",userStore.userToken)
    formdata.append("pageNum",page.value.toString())
    formdata.append("pageSize","10")
    formdata.append("videoDesc",videoDESC.value)
    axios.post("http://localhost:8080/adminGetVideoList",formdata).then(resp=>{
        pageCount.value = resp.data.data.pageInfo.pages
        // console.log(resp.data)
        for(var i = 0;i<resp.data.data.games.length;i++){
            let gameListClass = new GameListClass(
                resp.data.data.pageInfo.list[i].videoId,
                resp.data.data.pageInfo.list[i].videoDescription,
                resp.data.data.games[i].gameCoverUrl,
                resp.data.data.games[i].gameId,
                resp.data.data.games[i].gameName,
                resp.data.data.pageInfo.list[i].videoCoverUrl,
                resp.data.data.pageInfo.list[i].videoUpTime,
                resp.data.data.users[i].userAvatarUrl,
                resp.data.data.users[i].userId,
                resp.data.data.users[i].userName,
                resp.data.data.pageInfo.list[i].videoStarNumber
            )
            gameList.value.push(gameListClass)
        }
    })
    
}

function search(){
   videoDESC.value = input1.value
   getVideoList()
}
watch(page,()=>{
    getVideoList()
})

onMounted(() => {
    getVideoList()
})

const selectedVideoId = ref()
function adminDeleteVideo(videoId:number){
    selectedVideoId.value = videoId
    axios.post("http://localhost:8080/adminDeleteVideo?videoId="+selectedVideoId.value+"&userToken="+userStore.userToken).then(resp=>{
        console.log(resp.data)
    })
    getVideoList()
}
function banUser(userId:number){
    axios.post("http://localhost:8080/banUser?userId="+userId+"&userToken="+userStore.userToken).then(resp=>{
        console.log(resp.data)
    })
    
}

</script>