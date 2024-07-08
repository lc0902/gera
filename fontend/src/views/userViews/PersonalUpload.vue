<template>
    <h1>个人发布</h1>

    <n-tabs type="line" animated>
        <n-tab-pane name="video" tab="视频">
            <div style="display: flex;align-items: center;">
                    <n-input v-model:value="input2"></n-input>
                    <n-button @click="search2()">搜索</n-button>
                </div>
            <n-flex justify="center">
                <VideoCardP v-for="i in videoList" :videoId="i.videoId" :videoCoverUrl="i.videoCoverUrl"
            :videoDescription="i.videoDescription" :videoUserName="i.videoUserName" :videoGameName="i.videoGameName" />
            </n-flex>
            <n-pagination v-model:page="page2" :page-count="pageCount2" />
        </n-tab-pane>
        <n-tab-pane name="post" tab="帖子">
            <n-input-group>
                <div style="display: flex;align-items: center;">
                    <!-- <h1 style="width: 30%;">帖子&nbsp;&nbsp;&nbsp;&nbsp;</h1> -->
                    <n-input v-model:value="input1" ></n-input>
                    <n-button  @click="search1()">搜索</n-button>
                </div>
            </n-input-group>
            <div v-for="post in posts">
                <n-button quaternary @click="deletePost(post.postId)">删除</n-button>
                <PostCard :gameCoverUrl="post.gameCoverUrl" :gameName="post.gameName"
                    :userAvatarUrl="post.userAvatarUrl" :postTitle="post.postTitle" :postText="post.postText"
                    :postStarNum="post.postStarNum" :postTime="post.postTime" :userName="post.userName"
                    :postId="post.postId" />
            </div>

            <n-pagination v-model:page="page1" :page-count="pageCount1" />
        </n-tab-pane>
    </n-tabs>


</template>



<script setup lang="ts">
import VideoCardP from '@/components/VideoCardP.vue';
import PostCard from '@/components/PostCard.vue';
import axios from 'axios';
import { ref, onMounted, watch } from 'vue'
import { useCookies } from 'vue3-cookies';
const cookies = useCookies().cookies
import { useMessage } from 'naive-ui';
const message = useMessage()





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
const posts = ref([] as any[])
const postTitle = ref('')
const pageCount1 = ref(1)
const page1 = ref(1)
const input1 = ref('')
function getPersonalPost() {
    axios.post("http://localhost:8080/getPersonalUploadPost?pageNum=" + page1.value + "&pageSize=10&postTitle=" + postTitle.value + "&userToken=" + cookies.get("userToken")).then(resp => {
        pageCount1.value = resp.data.data.pageInfo.pages
        posts.value = []
        for (let i = 0; i < resp.data.data.games.length; i++) {
            let p = new postList(resp.data.data.games[i].gameCoverUrl, resp.data.data.games[i].gameName, (resp.data.data.pageInfo.list[i].postText).replace(/\!\[.*?\]\((.*?)\)/g, "[图片]")
                , resp.data.data.pageInfo.list[i].postTitle, resp.data.data.users[i].userAvatarUrl, resp.data.data.pageInfo.list[i].postTime
                , resp.data.data.pageInfo.list[i].postStarNum, resp.data.data.users[i].userName, resp.data.data.pageInfo.list[i].postId)
            posts.value.push(p)
        }
        // console.log(posts.value)
    })
}
function search1() {
    postTitle.value = input1.value
    getPersonalPost()
}
watch(page1, () => {
    getPersonalPost()
})
function deletePost(postId: any) {
    console.log(postId)
    axios.post("http://localhost:8080/deletePost?userToken=" + cookies.get("userToken") + "&postId=" + postId).then(resp => {
        message.info(resp.data.msg)
        axios.post("http://localhost:8080/getPersonalUploadPost?pageNum=" + page1.value + "&pageSize=10&postTitle=" + "&userToken=" + cookies.get("userToken")).then(resp => {
            pageCount1.value = resp.data.data.pageInfo.pages
            posts.value = []
            for (let i = 0; i < resp.data.data.games.length; i++) {
                let p = new postList(resp.data.data.games[i].gameCoverUrl, resp.data.data.games[i].gameName, (resp.data.data.pageInfo.list[i].postText).replace(/\!\[.*?\]\((.*?)\)/g, "[图片]")
                    , resp.data.data.pageInfo.list[i].postTitle, resp.data.data.users[i].userAvatarUrl, resp.data.data.pageInfo.list[i].postTime
                    , resp.data.data.pageInfo.list[i].postStarNum, resp.data.data.users[i].userName, resp.data.data.pageInfo.list[i].postId)
                posts.value.push(p)
            }
            // console.log(posts.value)
        })
    })

}

// TODO
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
const videoDesc = ref('')
const input2 = ref('')
const videoList = ref([] as any[])

function getPersonalUploadVideo() {
    axios.post("http://localhost:8080/getPersonalUploadVideo?pageNum=" + page2.value + "&pageSize=12&videoDescription=" + videoDesc.value + "&userToken=" + cookies.get("userToken")).then(resp => {
        videoList.value = []
        console.log(resp.data)
        pageCount2.value = resp.data.data.pageInfo.pages
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
    })
}
watch(page2,()=>{
    getPersonalUploadVideo()
})

function search2(){
    videoDesc.value = input2.value
    getPersonalUploadVideo()
}

onMounted(() => {
    getPersonalPost()
    getPersonalUploadVideo()
})

</script>



<style scoped></style>