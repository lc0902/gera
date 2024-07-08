<template>
    <n-card :bordered="false" @click="viewVideo()">
        <template #cover>
            <img :src="videoCoverUrl" :height="200" :width="200">
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

    </n-card>
</template>



<script setup lang="ts">
import VideoIcon from './icons/VideoIcon.vue';
import { useRouter } from 'vue-router';
import { ref } from 'vue'
const router = useRouter()
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

</script>



<style scoped>
.n-card {
    max-width: 200px;
}
</style>