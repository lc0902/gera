<template>
    <br>
    <n-input-group justify="end">
        <n-input v-model:value="postTitle" placeholder="请输入标题" size="large" :style="{width:'80%'}" ></n-input>
    <n-select v-model:value="gameId" :options="options" filterable placeholder="选择游戏"  size="large" :style="{width:'20%'}"/>
    </n-input-group>
    <br>
    <br>
    <mavon-editor ref="md" v-model="postText" @imgAdd="handleEditorImgAdd"></mavon-editor>
    <n-button @click="uploadPost">发布</n-button>
</template>



<script setup lang="ts">
import { onMounted, ref } from 'vue'
import axios from 'axios';
import { useUserStore } from '@/stores/user';
import { useRouter } from 'vue-router';
const router = useRouter()
const userStore = useUserStore();
const postText = ref('')
const md = ref()
const postTitle = ref('')
const gameId = ref<Number | undefined>(undefined)
import { useMessage } from 'naive-ui';
const message = useMessage()

function handleEditorImgAdd(pos: any, $file: any) {
    // 第一步.将图片上传到服务器.
    let formdata = new FormData();
    formdata.append('file', $file);
    axios({
               url: 'http://localhost:8080/uploadImg',
               method: 'post',
               data: formdata,
               headers: { 'Content-Type': 'multipart/form-data' },
           }).then((resp) => {
               // 第二步.将返回的url替换到文本原位置![...](0) -> ![...](url)
               /**
               * $vm 指为mavonEditor实例，可以通过如下两种方式获取
               * 1. 通过引入对象获取: `import {mavonEditor} from ...` 等方式引入后，`$vm`为`mavonEditor`
               * 2. 通过$refs获取: html声明ref : `<mavon-editor ref=md ></mavon-editor>，`$vm`为 `this.$refs.md`
               */
               md.value.$img2Url(pos, resp.data.data.url);
           })
}
const gameList = ref()
const options = ref([{}])
onMounted(() => {
    axios.post("http://localhost:8080/getAllGame").then(resp =>{
        gameList.value = resp.data.data
        options.value = []
        gameList.value.forEach((element: { gameName: any; gameId: any; }) => {
            options.value.push({
                label:element.gameName,
                value:element.gameId
            })
        });
    })
})


function uploadPost(){
    let formdata1 = new FormData();
    formdata1.append('postUserId',String (userStore.userId))
    formdata1.append('postGameId',String (gameId.value))
    formdata1.append('postTitle',postTitle.value)
    formdata1.append('postText',postText.value)
    formdata1.append('userToken',userStore.userToken)
    axios({
               url: 'http://localhost:8080/uploadPost',
               method: 'post',
               data: formdata1,
               headers: { 'content-type': 'application/x-www-form-urlencoded' },
           }).then((resp) => {
            if(resp.data.code==3110){
                router.push("/userView/personalUploadView")
                message.info(resp.data.msg)
            }
           })
}
</script>



<style scoped></style>