<template>
    <n-card :bordered="false">
        <template #cover>
            <img :src="videoCoverUrl" :height="200" :width="200" @click="viewVideo()">
        </template>
        <n-ellipsis style="max-width: 180px">
            <p>{{ videoDescription }}</p></n-ellipsis>

        <div style="display: flex;align-items: center;color: grey;">
            <VideoIcon :width="20" :height="20" />
            <n-ellipsis style="max-width: 240px">
            <p>
                &nbsp;&nbsp;{{videoGameName}}&nbsp;&nbsp;{{ videoUserName }}

            </p>
            </n-ellipsis>
        </div>
        <n-button @click="deleteVideo()">删除</n-button>
        
    </n-card>

</template>



<script setup lang="ts">
import VideoIcon from './icons/VideoIcon.vue';
import { useRouter } from 'vue-router';
import { ref } from 'vue'
import { useCookies } from 'vue3-cookies';
const cookies = useCookies().cookies
import axios from 'axios';
const router = useRouter()
import { useMessage } from 'naive-ui';
const message = useMessage()
const props = defineProps({
videoId:Number,
videoCoverUrl:String,
videoDescription:String,
videoUserName:String,
videoGameName:String
})

function viewVideo(){
    router.push("/userView/videoView/"+props.videoId)
}

function deleteVideo(){
    axios.post("http://localhost:8080/deletePersonalUploadVideo?userToken="+cookies.get("userToken")+"&videoId="+props.videoId).then(resp=>{
        message.info(resp.data.msg)
        location.reload()
    })
}

</script>



<style scoped>
.n-card {
    max-width: 200px;
}
</style>