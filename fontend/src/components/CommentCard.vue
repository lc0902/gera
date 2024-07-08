<template>
    <div style="display: flex;align-items: center;">
        <n-avatar :src="userAvatarUrl" round></n-avatar>&nbsp;{{ userName }}
    </div>
    <br>
    {{ postcommentText }}
    <br>

    <span style="color: grey;">{{ postcommentTime }}</span>
    <br>
    <n-button @click="deletePostcomment()" text v-if="postcommentUserId==userStore.userId">删除</n-button>
    <br><br><br>
</template>



<script setup lang="ts">
import {ref} from 'vue'
import { useUserStore } from '@/stores/user';
import axios from 'axios';
import { useMessage } from 'naive-ui';
const message = useMessage()
const userStore = useUserStore()

const props = defineProps({
    userAvatarUrl:String,
    userName:String,
    postcommentTime:String,
    postcommentText:String,
    postcommentUserId:Number,
    postcommentId:Number

})

function deletePostcomment(){
    axios.post("http://localhost:8080/deletePostcomment?postcommentId="+props.postcommentId+"&userToken="+userStore.userToken).then(resp=>{
        console.log(resp.data)
        if(resp.data.code==3610){
            message.info(resp.data.msg)
            location.reload()
        }
    })
}



</script>



<style scoped>
    
</style>