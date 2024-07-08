<template>
    <div>
        <n-select v-model:value="gameId" :options="options" filterable placeholder="选择游戏" size="large"
            :style="{ width: '20%' }" />
        <br>
        <n-input type="text" placeholder="请输入标题" v-model:value="liveDescription"></n-input>
        <br>
        <br>
        <n-upload v-model:file-list="coverFile" :max="1" list-type="image-card">
            点击上传封面
        </n-upload><br>

        <n-button @click="uploadLiveInfo()">
            上传
        </n-button>
        <br><br>

        <n-button @click="copyLiveUrl()">
            复制推流码
        </n-button>
        <br>
        <a href="https://obsproject.com/">直播工具获取？</a>
        <br>
        <router-link to="/userView/liveUsage"> 
        <a >使用教程</a>

        </router-link>
        <br><br><br>
        preview
        <FlvPlayer :is-live="true" :url="liveUrl" :type="'flv'"></FlvPlayer>


        <br>
        <br>
        <br>
        <n-button v-if="!living" @click="startLive()">
            开启直播
        </n-button>
        <n-button v-if="living" @click="closeLive()">
            关闭直播
        </n-button>

        <!-- <n-popover
      placement="bottom"
      trigger="click"
      @update:show="handleUpdateShow"
    >
      <template #trigger>
        <n-button>推流码</n-button>
      </template>
<span>推流码</span>
</n-popover> -->
    </div>

</template>



<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios';
import clipboard from 'clipboard'
import { useCookies } from 'vue3-cookies';
import FlvPlayer from '@/components/FlvPlayer.vue'
import { useMessage } from 'naive-ui';
const message = useMessage()
const coockies = useCookies().cookies


const handleUpdateShow = ref(false)
const coverFile = ref()
const gameId = ref()
const gameList = ref()
const options = ref([{}])
const liveUrl = ref('')
const pushUrl = ref()
const liveDescription = ref("")
const living=ref(false)

function startLive(){
    axios.post("http://localhost:8080/startLive?userToken="+coockies.get("userToken")).then(resp=>{
        message.info(resp.data.msg)
        if(resp.data.msg=="成功"){
            living.value = true
        }
    })
}

function closeLive(){
    axios.post("http://localhost:8080/closeLive?userToken="+coockies.get("userToken")).then(resp=>{
        message.info(resp.data.msg)
        if(resp.data.msg=="成功"){
            living.value = false
        }
    })
}
function uploadLiveInfo() {
    let formdata = new FormData()
    formdata.append("userToken", coockies.get("userToken"))
    formdata.append("liveGameId", gameId.value)
    formdata.append("liveDescription", liveDescription.value)
    formdata.append("liveCover", coverFile.value[0].file)
    axios({
        url: 'http://localhost:8080/uploadLiveInfo',
        method: 'post',
        data: formdata,
        headers: { 'Content-Type': 'multipart/form-data' },
    }).then(resp => {
        console.log(resp.data)
    })
}

function copyLiveUrl() {
    clipboard.copy(pushUrl.value)
    message.success("复制成功，请不要向其他人泄露您的推流码")

}
const liveStatus = ref()
function getLiveStatus() {
    axios.post("http://localhost:8080/getLiveStatus?userToken=" + coockies.get("userToken")).then(resp => {
        liveStatus.value = resp.data.data
        console.log(liveStatus.value)
        coverFile.value = [{
            status: 'finished',
            url: liveStatus.value.liveCoverUrl
        }]
        liveDescription.value = resp.data.data.liveDescription
        gameId.value = resp.data.data.liveGameId
        if(resp.data.data.liveStatus==1){
            living.value=true
        }else{
            living.value = false
        }


    })

}
onMounted(() => {
    getLiveStatus()
    axios.post("http://localhost:8080/getLiveUrl?userToken=" + coockies.get("userToken")).then(resp => {
        liveUrl.value = resp.data.data.liveUrl
        pushUrl.value = "rtmp://localhost:7933/myapp/" + liveUrl.value.substring(49, liveUrl.value.length)
        console.log(pushUrl.value)
    })
    axios.post("http://localhost:8080/getAllGame").then(resp => {
        gameList.value = resp.data.data
        gameList.value.forEach((element: { gameName: any; gameId: any; }) => {
            options.value.push({
                label: element.gameName,
                value: element.gameId
            })
        });
    })
})

</script>



<style scoped></style>