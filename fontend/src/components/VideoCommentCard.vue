<template>
    <div>
        <div style="display: flex;align-items: center;">
            <n-avatar :src="userAvatarUrl" round></n-avatar>&nbsp;{{ userName }}
        </div>
        <br>
        {{ videocommentText }}
        <br>

        <span style="color: grey;">{{ videocommentTime }}</span>
        <br>
        <n-button @click="deletePostcomment()" text v-if="videocommentUserId == userStore.userId">删除</n-button>
        <br><br><br>

    </div>

</template>



<script setup lang="ts">
import { ref } from 'vue'
import { useUserStore } from '@/stores/user';
import axios from 'axios';
import { useMessage } from 'naive-ui';
const message = useMessage()
const userStore = useUserStore()
const props = defineProps({
    userAvatarUrl: String,
    userName: String,
    videocommentTime: String,
    videocommentText: String,
    videocommentUserId: Number,
    videocommentId: Number

})

function deletePostcomment() {
    axios.post("http://localhost:8080/deleteVideocomment?videocommentId=" + props.videocommentId + "&userToken=" + userStore.userToken).then(resp => {
        console.log(resp.data)
        if (resp.data.msg == "删除成功") {
            message.info(resp.data.msg)
        }
    })
}



</script>



<style scoped></style>