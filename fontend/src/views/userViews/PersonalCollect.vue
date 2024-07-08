<template>
    <h1>
        我的收藏
    </h1>


    <n-tabs type="line" animated>
        <n-tab-pane name="post" tab="帖子">
            <n-input-group>
        <div style="display: flex;align-items: center;">
            <!-- <h1 style="width: 30%;">帖子&nbsp;&nbsp;&nbsp;&nbsp;</h1> -->
            <n-input v-model:value="input1" style=" width: 80%; ;"></n-input>
            <n-button style=" width: 20%;" @click="search1()">搜索</n-button>
        </div>
    </n-input-group>
    <br>
    <br>
    <PostCard v-for="post in posts" :gameCoverUrl="post.gameCoverUrl" :gameName="post.gameName"
        :userAvatarUrl="post.userAvatarUrl" :postTitle="post.postTitle" :postText="post.postText"
        :postStarNum="post.postStarNum" :postTime="post.postTime" :userName="post.userName" :postId="post.postId" />

    <div><n-pagination v-model:page="page1" :page-count="pageCount1" /></div>
    <br>
    <br>
        </n-tab-pane>
        <n-tab-pane name="video" tab="视频">
            <n-input-group>
        <div style="display: flex;align-items: center;">
            <!-- <h1 style="width: 30%;">视频&nbsp;&nbsp;&nbsp;&nbsp;</h1> -->
            <n-input v-model:value="input2" style=" width: 80%; ;"></n-input>
            <n-button style=" width: 20%;" @click="search2()">搜索</n-button>
        </div>
    </n-input-group>
    <br><br>
    <n-flex>
        <VideoCard v-for="i in videoList" :videoId="i.videoId" :videoCoverUrl="i.videoCoverUrl"
            :videoDescription="i.videoDescription" :videoUserName="i.videoUserName" :videoGameName="i.videoGameName" />

    </n-flex>
    <div><n-pagination v-model:page="page2" :page-count="pageCount2" /></div>
        </n-tab-pane>
    </n-tabs>



</template>



<script setup lang="ts">
import axios from 'axios';
import { onMounted, ref, watch } from 'vue';
import VideoCard from '@/components/VideoCard.vue'
import PostCard from '@/components/PostCard.vue';
import { useCookies } from 'vue3-cookies';
const cookies = useCookies().cookies
const page1 = ref(1)
const pageCount1 = ref(12)
const pageSize = ref(10)


class postList {
    gameCoverUrl: String | undefined;
    gameName: String | undefined;
    userAvatarUrl: String | undefined;
    postTitle: String | undefined;
    postText: String | undefined;
    postTime: String | undefined;
    postStarNum: Number | undefined;
    userName: String | undefined;
    postId: Number | undefined;

    constructor(gameCoverUrl: string, gameName: string, postText: string, postTitle: string, userAvatarUrl: string, postTime: string, postStarNum: number, userName: string
        , postId: number) {
        this.gameCoverUrl = gameCoverUrl;
        this.gameName = gameName;
        this.postText = postText;
        this.postTitle = postTitle;
        this.userAvatarUrl = userAvatarUrl;
        this.postTime = postTime;
        this.postStarNum = postStarNum;
        this.userName = userName;
        this.postId = postId
    }
}
const input1 = ref('')
const posts = ref([] as any[])
const postTitle = ref('')
function getPersonalCollectPost() {
    axios.post("http://localhost:8080/getCollectPostListByUserId?pageNum=" + page1.value + "&pageSize=" + pageSize.value + "&userToken=" + cookies.get("userToken") + "&postTitle=" + postTitle.value).then(resp => {
        pageCount1.value = resp.data.data.pageInfo.pages
        console.log(resp)
        posts.value = []
        for (let i = 0; i < resp.data.data.games.length; i++) {
            let p = new postList(resp.data.data.games[i].gameCoverUrl,
                resp.data.data.games[i].gameName,
                (resp.data.data.posts[i].postText).replace(/\!\[.*?\]\((.*?)\)/g, "[图片]"),
                resp.data.data.posts[i].postTitle,
                resp.data.data.users[i].userAvatarUrl,
                resp.data.data.posts[i].postTime,
                resp.data.data.posts[i].postStarNum,
                resp.data.data.users[i].userName,
                resp.data.data.posts[i].postId)
            posts.value.push(p)
        }
    })
}
watch(page1, () => {
    getPersonalCollectPost()
})
function search1() {
    postTitle.value = input1.value
    getPersonalCollectPost()
}



class VideoCardClass {
    videoId: Number | undefined
    videoCoverUrl: String | undefined
    videoDescription: String | undefined
    videoUserName: String | undefined
    videoGameName: String | undefined

    constructor(videoId: number,
        videoCoverUrl: string,
        videoDescription: string,
        videoUserName: string,
        videoGameName: string) {
        this.videoId = videoId
        this.videoCoverUrl = videoCoverUrl
        this.videoDescription = videoDescription
        this.videoUserName = videoUserName
        this.videoGameName = videoGameName

    }
}

const page2 = ref(1)
const pageCount2 = ref(1)
const videoDescription = ref('')
const videoList = ref([] as any[])
const input2 = ref('')
function getCollectVideoListByUserId() {
    axios.post("http://localhost:8080/getCollectVideoListByUserId?pageNum=" + page2.value + "&pageSize=10" + "&userToken=" + cookies.get("userToken") + "&videoDescription=" + videoDescription.value).then(resp => {
        console.log(resp.data)
        videoList.value = []
        console.log(resp.data)
        pageCount2.value = resp.data.data.pageInfo.pages
        for (let i = 0; i < resp.data.data.videos.length; i++) {
            let t = new VideoCardClass(
                resp.data.data.videos[i].videoId,
                resp.data.data.videos[i].videoCoverUrl,
                resp.data.data.videos[i].videoDescription,
                resp.data.data.users[i].userName,
                resp.data.data.games[i].gameName
            )
            videoList.value.push(t)
        }
    })
}

watch(page2, () => {
    getCollectVideoListByUserId()
})
function search2() {
    videoDescription.value = input2.value
    getCollectVideoListByUserId()
}
onMounted(() => {
    getPersonalCollectPost()
    getCollectVideoListByUserId()
})
</script>



<style scoped></style>