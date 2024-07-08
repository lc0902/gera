<template>
    <n-flex justify="center">
        <n-input-group >
            <n-input v-model:value="searchText"></n-input>
            <n-button @click="searchVideo()">搜索</n-button>
        </n-input-group>
        <VideoCard v-for="i in videoList" :videoId="i.videoId" :videoCoverUrl="i.videoCoverUrl"
            :videoDescription="i.videoDescription" :videoUserName="i.videoUserName" :videoGameName="i.videoGameName" />
    </n-flex>
    <div><n-pagination v-model:page="page" :page-count="pageCount" /></div>
</template>



<script setup lang="ts">
import VideoCard from '@/components/VideoCard.vue';
import axios from 'axios';
import { onMounted, watch, ref } from 'vue';

const page = ref(1)
const pageCount = ref(1)
const videoSearchDescription = ref('')
const searchText = ref('')

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

const videoList = ref([] as any[])
onMounted(() => {
    axios.post("http://localhost:8080/getVideoList?pageSize=12&pageNum=" + page.value+"&videoDescription="+videoSearchDescription.value).then(resp => {
        videoList.value = []
        console.log(resp.data)
        pageCount.value = resp.data.data.pageInfo.pages
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
})

watch(page,()=>{
    axios.post("http://localhost:8080/getVideoList?pageSize=12&pageNum=" + page.value+"&videoDescription="+videoSearchDescription.value).then(resp => {
        videoList.value = []
        console.log(resp.data)
        pageCount.value = resp.data.data.pageInfo.pages
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
})

function searchVideo(){
    videoSearchDescription.value = searchText.value
    axios.post("http://localhost:8080/getVideoList?pageSize=12&pageNum=" + page.value+"&videoDescription="+videoSearchDescription.value).then(resp => {
        videoList.value = []
        console.log(resp.data)
        pageCount.value = resp.data.data.pageInfo.pages
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

</script>



<style scoped></style>