<template>
    <n-flex justify="center">
        <n-image width="1000" height="200" :src="gameCoverUrl" object-fit="none" preview-disabled />
    </n-flex>
    <h1 style="color: #8a2be2;">{{ gaemName }}</h1>
    <div style="display: flex; align-items: center;">
        评分:{{ gameRate }} &nbsp;&nbsp;<n-rate v-model:value="gameRate" :readonly="isRate" @click="rateGame()"
            allow-half></n-rate>
            &nbsp;&nbsp;
            <span v-if="isRate" style="color: green;">已评分</span>
    </div>
    发行时间:
    {{ gameReleaseTime }}

    <br>
    {{ gameDesc }}
    <br>

    <br>

    <br>

    <n-tabs type="line" animated>
        <n-tab-pane name="video" tab="视频">
            <n-input-group>
                <div style="display: flex;align-items: center;">
                    <!-- <h1 style="width: 30%;">视频&nbsp;&nbsp;&nbsp;&nbsp;</h1> -->
                    <n-input v-model:value="input1" style="width: 80%"></n-input>
                    <n-button style="width: 20%;" @click="search1()">搜索</n-button>
                </div>
            </n-input-group>
            <br>
            <br>
            <n-flex>
                <VideoCard v-for="i in videoList" :videoId="i.videoId" :videoCoverUrl="i.videoCoverUrl"
                    :videoDescription="i.videoDescription" :videoUserName="i.videoUserName"
                    :videoGameName="i.videoGameName" />
            </n-flex>
            <br>
            <br>
            <n-pagination v-model:page="page1" :page-count="pageCount1" v-if="!empty1" />
            <n-empty v-if="empty1"></n-empty>
        </n-tab-pane>
        <n-tab-pane name="live" tab="直播">
            <n-input-group>
                <div style="display: flex;align-items: center;">
                    <!-- <h1 style="width: 30%;">直播&nbsp;&nbsp;&nbsp;&nbsp;</h1> -->
                    <n-input style="width: 80%;" v-model:value="input2"></n-input>
                    <n-button style="width: 20%;" @click="search2()">搜索</n-button>
                </div>
            </n-input-group>
            <br>
            <br>
            <n-flex>
                <LiveCard v-for="i in liveList" :liveId="i.liveId" :live-cover-url="i.liveCoverUrl"
                    :live-description="i.liveDescription" :live-user-avatar-url="i.liveUserAvatarUrl"
                    :live-user-name="i.liveUserName" :live-game-name="i.liveGameName" />
            </n-flex>
            <br>
            <br>
            <n-pagination v-model:page="page2" :page-count="pageCount2" v-if="!empty2" />
            <n-empty v-if="empty2"></n-empty>

        </n-tab-pane>
        <n-tab-pane name="post" tab="社区">
            <n-input-group>
                <div style="display: flex;align-items: center;">
                    <!-- <h1 style="width: 30%;">社区&nbsp;&nbsp;&nbsp;&nbsp;</h1> -->
                    <n-input style="width: 80%;" v-model:value="input3"></n-input>
                    <n-button style="width: 20%;" @click="search3()">搜索</n-button>
                </div>
            </n-input-group>
            <br>
            <br>
            <PostCard v-for="post in posts" :gameCoverUrl="post.gameCoverUrl" :gameName="post.gameName"
                :userAvatarUrl="post.userAvatarUrl" :postTitle="post.postTitle" :postText="post.postText"
                :postStarNum="post.postStarNum" :postTime="post.postTime" :userName="post.userName"
                :postId="post.postId" />
            <br>
            <br>
            <n-pagination v-model:page="page3" :page-count="pageCount3" v-if="!empty3" />
            <n-empty v-if="empty3"></n-empty>
        </n-tab-pane>
    </n-tabs>







</template>



<script setup lang="ts">
import PostCard from '@/components/PostCard.vue';
import LiveCard from '@/components/LiveCard.vue';
import VideoCard from '@/components/VideoCard.vue';
import axios from 'axios';
import { onBeforeMount, ref, watch } from 'vue';
import { useRoute } from 'vue-router'
const route = useRoute()
import { useCookies } from 'vue3-cookies';
const cookies = useCookies().cookies
import formateDate from '@/util/DateFormate'




const gameCoverUrl = ref()
const gameDesc = ref()
const gaemName = ref()
const gameRate = ref()
const gameReleaseTime = ref()
function getGameInfo() {
    axios.post("http://localhost:8080/getGameInfo?gameId=" + route.params.gameId).then(resp => {
        gameCoverUrl.value = resp.data.data.gameCoverUrl
        gameDesc.value = resp.data.data.gameDescription
        gaemName.value = resp.data.data.gameName
        gameRate.value = resp.data.data.gameRate
        gameReleaseTime.value = formateDate(resp.data.data.gameReleaseTime)
    })
}

// 视频列表
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
const page1 = ref(1)
const pageCount1 = ref(1)
const videoSearchDescription = ref('')
const input1 = ref('')
const videoList = ref([] as any[])
const empty1 = ref(false)
function getVideoListByGameId() {
    axios.post("http://localhost:8080/getVideoListByGameId?pageSize=12&pageNum=" + page1.value + "&videoDescription=" + videoSearchDescription.value + "&gameId=" + route.params.gameId).then(resp => {
        videoList.value = []
        // console.log(resp.data)
        pageCount1.value = resp.data.data.pageInfo.pages
        for (let i = 0; i < resp.data.data.pageInfo.list.length; i++) {
            let t = new VideoCardClass(
                resp.data.data.pageInfo.list[i].videoId,
                resp.data.data.pageInfo.list[i].videoCoverUrl,
                resp.data.data.pageInfo.list[i].videoDescription,
                resp.data.data.users[i].userName,
                resp.data.data.games[i].gameName
            )
            videoList.value.push(t)
        }
        if (resp.data.data.pageInfo.list.length == 0) {
            empty1.value = true
        } else {
            empty1.value = false

        }
    })
}
watch(page1, () => {
    getVideoListByGameId()
})
function search1() {
    videoSearchDescription.value = input1.value
    getVideoListByGameId()
}

// 直播列表 todo
class LiveCardClass {
    liveCoverUrl: String | undefined
    liveDescription: String | undefined
    liveUserAvatarUrl: String | undefined
    liveUserName: String | undefined
    liveGameName: String | undefined
    liveId: Number | undefined
    constructor(
        liveCoverUrl: string,
        liveDescription: string,
        liveUserAvatarUrl: string,
        liveUserName: string,
        liveGameName: string,
        liveId: number
    ) {
        this.liveCoverUrl = liveCoverUrl
        this.liveDescription = liveDescription
        this.liveUserAvatarUrl = liveUserAvatarUrl
        this.liveUserName = liveUserName
        this.liveGameName = liveGameName
        this.liveId = liveId
    }
}
const liveList = ref([] as any[])
const page2 = ref(1)
const liveDescription = ref('')
const pageCount2 = ref(1)
const input2 = ref('')
const empty2 = ref(false)
function getLiveListByGameId() {
    axios.post("http://localhost:8080/getLiveListByGameId?pageNum=" + page2.value + "&pageSize=12&liveDescription=" + liveDescription.value + "&gameId=" + route.params.gameId).then(resp => {
        liveList.value = []
        console.log(resp.data)
        pageCount2.value = resp.data.data.pageInfo.pages
        for (let i = 0; i < resp.data.data.pageInfo.list.length; i++) {
            liveList.value.push(new LiveCardClass(
                resp.data.data.pageInfo.list[i].liveCoverUrl,
                resp.data.data.pageInfo.list[i].liveDescription,
                resp.data.data.users[i].userAvatarUrl,
                resp.data.data.users[i].userName,
                resp.data.data.games[i].gameName,
                resp.data.data.pageInfo.list[i].liveId,
            ))
        }
        if (resp.data.data.pageInfo.list.length == 0) {
            empty2.value = true
        } else {
            empty2.value = false

        }
    })
}
watch(page2, () => {
    getLiveListByGameId()
})
function search2() {
    liveDescription.value = input2.value
    getLiveListByGameId()
}

// 帖子列表 todo
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
const input3 = ref('')
const postTitle = ref('')
const posts = ref([] as any[])
const pageCount3 = ref(1)
const page3 = ref(1)
const empty3 = ref(false)
function getPostListByGameId() {
    axios.post("http://localhost:8080/getPostListByGameId?pageNum=" + page3.value + "&pageSize=10" + "&postTitle=" + postTitle.value + "&gameId=" + route.params.gameId).then(resp => {
        posts.value = []
        pageCount3.value = resp.data.data.pageInfo.pages

        for (let i = 0; i < resp.data.data.games.length; i++) {
            let p = new postList(resp.data.data.games[i].gameCoverUrl, resp.data.data.games[i].gameName, (resp.data.data.pageInfo.list[i].postText).replace(/\!\[.*?\]\((.*?)\)/g, "[图片]")
                , resp.data.data.pageInfo.list[i].postTitle, resp.data.data.users[i].userAvatarUrl, resp.data.data.pageInfo.list[i].postTime
                , resp.data.data.pageInfo.list[i].postStarNum, resp.data.data.users[i].userName, resp.data.data.pageInfo.list[i].postId)
            posts.value.push(p)
        }
        if (resp.data.data.games.length == 0) {
            empty3.value = true
        } else {
            empty3.value = false
        }
    })
}
function search3() {
    postTitle.value = input3.value
    getPostListByGameId()
}
watch(page3, () => {
    getPostListByGameId()
})
// init
const isRate = ref(false)
function getRateStatus() {
    axios.post("http://localhost:8080/getRateStatus?userToken=" + cookies.get("userToken") + "&gameId=" + route.params.gameId).then(resp => {
        if (resp.data.msg == "存在") {
            isRate.value = true
        } else {
            isRate.value = false
        }
    })
}


function init() {
    getRateStatus()
    getGameInfo()
    getVideoListByGameId()
    getLiveListByGameId()
    getPostListByGameId()
}
onBeforeMount(() => {
    init()

})





function rateGame() {
    axios.post("http://localhost:8080/rateGame?userToken=" + cookies.get("userToken") + "&gameId=" + route.params.gameId + "&rate=" + gameRate.value).then(resp => {
        console.log(resp.data)
    })
}
</script>



<style scoped></style>