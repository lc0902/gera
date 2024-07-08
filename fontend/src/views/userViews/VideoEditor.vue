<template>
    <br>
    <n-select v-model:value="gameId" :options="options" filterable placeholder="选择游戏" size="large" :style="{ width: '50%' }" />
<br><br>
    <n-input v-model:value="videoDescription" placeholder="请输入描述" size="large" :style="{ width: '100%' }"></n-input>
<br><br>
    <n-upload :max="1" v-model:file-list="videoFile" >
        <n-upload-dragger>
            <div style="margin-bottom: 12px">
                <n-icon size="48" :depth="3">
                    <archive-icon />
                </n-icon>
            </div>
            <n-text style="font-size: 16px">
                点击或者拖动视频文件到该区域来上传
            </n-text>
            <n-p depth="3" style="margin: 8px 0 0 0">
                请不要上传敏感数据，比如你的银行卡号和密码，信用卡号有效期和安全码
            </n-p>
        </n-upload-dragger>
    </n-upload>
    <n-upload v-model:file-list="coverFile" :max="1" list-type="image-card">
        点击上传封面
    </n-upload><br><br>
    <n-button @click="uploadVideo()" v-if="userStore.userRoleId==2">提交</n-button>
    <n-button @click="uploadVideo()" v-else-if="userStore.userRoleId==3">提交</n-button>
    <n-button @click="uploadVideo()" v-else-if="userStore.userRoleId==4">提交</n-button>
    <n-button @click="uploadVideo()" v-else-if="userStore.userRoleId==5">提交</n-button>
    <n-button v-else disabled>无权限</n-button>
</template>



<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios';
import { useUserStore } from '@/stores/user';

const userStore = useUserStore()
import { useMessage } from 'naive-ui';
const message = useMessage()
const videoFile = ref()
const coverFile = ref()
const gameId = ref()
const videoDescription = ref('')
const gameList = ref()
const options = ref([{}])
const videoSrc =ref('')
const imgSrc = ref('')
onMounted(() => {
    axios.post("http://localhost:8080/getAllGame").then(resp => {
        gameList.value = resp.data.data
        options.value = []
        gameList.value.forEach((element: { gameName: any; gameId: any; }) => {
            options.value.push({
                label: element.gameName,
                value: element.gameId
            })
        });
    })
})

import { useRouter } from 'vue-router';
const router = useRouter();
function uploadVideo(){
    message.info("正在上传，请不要关闭页面")
    let formdata = new FormData()
    formdata.append("videoFile",videoFile.value[0].file)
    formdata.append("coverFile",coverFile.value[0].file)
    formdata.append("userToken",userStore.userToken)
    formdata.append("videoGameId",gameId.value)
    formdata.append("videoDescription",videoDescription.value)
    axios({
        url: 'http://localhost:8080/uploadVideo',
               method: 'post',
               data: formdata,
               headers: { 'Content-Type': 'multipart/form-data' },
    }).then(resp=>{
        message.info(resp.data.msg)
        router.push('/userView/personalUploadView')
    })
}
// function onUpdateHandle(){
//     try{
//         videoSrc.value = URL.createObjectURL(videoFile.value[0].file);
//         var video = document.getElementById('video')!;
//         video.addEventListener('play', function() {
//         var canvas = document.createElement('canvas');
//         canvas.width = video.videoWidth;
//         canvas.height = video.videoHeight;
//         var context = canvas.getContext('2d')!;
//         context.drawImage(video, 0, 0, canvas.width, canvas.height);
//         var imageData = canvas.toDataURL('image/png');
//         var coverEle = document.getElementById("coverEle")
//         coverEle?.setAttribute("file-list",canvas)
//         imgSrc.value=imageData
//     // 在这里可以使用imageData，例如将其设置为其他元素的src属性
//   });

//     }catch{

//     }
//     console.log("Gello")
// }
</script>



<style scoped></style>