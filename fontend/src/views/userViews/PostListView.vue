<template>
    <n-input-group>
        <n-input v-model:value="searchName"></n-input>
        <n-button type="primary" @click="getPostListWithName">搜索</n-button>
    </n-input-group>
    <br>
    <br>
    <PostCard v-for="post in posts" :gameCoverUrl="post.gameCoverUrl" :gameName="post.gameName"
        :userAvatarUrl="post.userAvatarUrl" :postTitle="post.postTitle" :postText="post.postText"
        :postStarNum="post.postStarNum" :postTime="post.postTime" :userName="post.userName" :postId="post.postId" />

    <div><n-pagination v-model:page="page" :page-count="pageCount" /></div>
</template>


<script setup lang="ts">
import axios from 'axios';
import { onMounted, ref, watch } from 'vue';
import PostCard from '@/components/PostCard.vue';
const page = ref(1)
const pageCount = ref(12)
const pageSize = ref(10)
const searchName = ref('')
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
onMounted(() => {
    axios.post("http://localhost:8080/getPostList?pageNum=" + page.value + "&pageSize=" + pageSize.value).then(resp => {
        pageCount.value = resp.data.data.pageInfo.pages
        posts.value = []

        for (let i = 0; i < resp.data.data.games.length; i++) {
            let p = new postList(resp.data.data.games[i].gameCoverUrl, resp.data.data.games[i].gameName, (resp.data.data.pageInfo.list[i].postText).replace(/\!\[.*?\]\((.*?)\)/g, "[图片]")
                , resp.data.data.pageInfo.list[i].postTitle, resp.data.data.users[i].userAvatarUrl, resp.data.data.pageInfo.list[i].postTime
                , resp.data.data.pageInfo.list[i].postStarNum, resp.data.data.users[i].userName, resp.data.data.pageInfo.list[i].postId)
            posts.value.push(p)
        }
    })
})

function getPostList() {
    axios.post("http://localhost:8080/getPostList?pageNum=" + page.value + "&pageSize=" + pageSize.value+"&postTitle="+searchName.value).then(resp => {
        posts.value = []
        pageCount.value = resp.data.data.pageInfo.pages

        for (let i = 0; i < resp.data.data.games.length; i++) {
            let p = new postList(resp.data.data.games[i].gameCoverUrl, resp.data.data.games[i].gameName, (resp.data.data.pageInfo.list[i].postText).replace(/\!\[.*?\]\((.*?)\)/g, "[图片]")
                , resp.data.data.pageInfo.list[i].postTitle, resp.data.data.users[i].userAvatarUrl, resp.data.data.pageInfo.list[i].postTime
                , resp.data.data.pageInfo.list[i].postStarNum, resp.data.data.users[i].userName, resp.data.data.pageInfo.list[i].postId)
            posts.value.push(p)
        }
    })
}

function getPostListWithName(){
    axios.post("http://localhost:8080/getPostList?pageNum=" + page.value + "&pageSize=" + pageSize.value+"&postTitle="+searchName.value).then(resp => {
        posts.value = []
        pageCount.value = resp.data.data.pageInfo.pages

        for (let i = 0; i < resp.data.data.games.length; i++) {
            let p = new postList(resp.data.data.games[i].gameCoverUrl, resp.data.data.games[i].gameName, (resp.data.data.pageInfo.list[i].postText).replace(/\!\[.*?\]\((.*?)\)/g, "[图片]")
                , resp.data.data.pageInfo.list[i].postTitle, resp.data.data.users[i].userAvatarUrl, resp.data.data.pageInfo.list[i].postTime
                , resp.data.data.pageInfo.list[i].postStarNum, resp.data.data.users[i].userName, resp.data.data.pageInfo.list[i].postId)
            posts.value.push(p)
        }
    })
}

watch(page, (newValue, oldValue) => {
    getPostList()
})
</script>



<style scoped></style>