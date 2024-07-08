<template>
    <div style="display: flex;align-items: center;">
        <n-avatar :src="gameCoverUrl" :size="80" round />
        <h1>{{ desc }}</h1>
    </div>
    <br>
    <div style="display: flex;align-items: center;color: grey;">
        <n-avatar :src="userAvatarUrl" :size="20"></n-avatar>&nbsp;&nbsp;{{ userName }}&nbsp;&nbsp;{{ gameName }}
    </div>
    <br>
    <FlvPlayer :is-live="true" :url="liveUrl" :type="'flv'" />
</template>



<script setup lang="ts">
import FlvPlayer from '@/components/FlvPlayer.vue';
import axios from 'axios';
import { ref, onMounted, onBeforeMount, onUnmounted } from 'vue'
import { useCookies } from 'vue3-cookies';
const cookies = useCookies().cookies
import { useRoute } from 'vue-router';
import { useRouter } from 'vue-router';
const router = useRouter()
const route = useRoute()
import { useMessage } from 'naive-ui';
const message = useMessage()


const liveUrl = ref()
const userName = ref()
const userAvatarUrl = ref()
const gameCoverUrl = ref()
const desc = ref()
const gameName = ref()
onBeforeMount(() => {
    axios.post("http://localhost:8080/viewLive?userToken=" + cookies.get("userToken") + "&liveId=" + route.params.liveId).then(resp => {
        console.log(resp.data)
        liveUrl.value = resp.data.data.live.liveUrl
        userName.value = resp.data.data.user.userName
        userAvatarUrl.value = resp.data.data.user.userAvatarUrl
        gameCoverUrl.value = resp.data.data.game.gameCoverUrl
        desc.value = resp.data.data.live.liveDescription
        gameName.value = resp.data.data.game.gameName
    })
})
const liveRoomWs = ref()
function initLiveRoomWs(){
    liveRoomWs.value = new WebSocket("ws://localhost:8080/liveRoom/"+route.params.liveId.toString()+"/"+cookies.get("userToken"))
    liveRoomWs.value.onopen = ()=>{
        console.log("liveRoomWs 连接成功");
    }
    liveRoomWs.value.onmessage = (event:any)=>{
        if(event.data=="close"){
            router.push("/")
            message.info("直播内容违规，已被管理员结束直播")
        }
    }
}
onMounted(() => {
    initLiveRoomWs()
})
onUnmounted(() => {
    liveRoomWs.value.close()
}
)
</script>



<style scoped></style>