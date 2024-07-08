<template>
    <n-message-provider>
      <router-view>
      </router-view>
    </n-message-provider>

</template>

<script setup lang="ts">
import MenuBar from '@/components/MenuBar.vue';
import { useCookies } from 'vue3-cookies';
const { cookies } = useCookies()
import axios from 'axios';

import { useUserStore } from '@/stores/user';
const userStore = useUserStore()
import { onMounted } from 'vue'
onMounted(
    () => {
        // 读取cookie判断用户登录状况
        if (cookies.get("userToken") != null) {
            axios.post("http://localhost:8080/login?userToken=" + cookies.get("userToken")).then(resp => {
                // message.info(resp.data.msg)
                // 登陆成功
                if (resp.data.code == 1210) {
                    // 设置store
                    userStore.userName = resp.data.data.userName
                    userStore.userAvatarUrl = resp.data.data.userAvatarUrl
                    userStore.userEmail = resp.data.data.userEmail
                    userStore.userGender = resp.data.data.userGender
                    userStore.userId = resp.data.data.userId
                    userStore.userRoleId = resp.data.data.userRoleId
                    userStore.userSignature = resp.data.data.userSignature
                    userStore.userStatus = resp.data.data.userStatus
                    userStore.userToken = resp.data.data.userToken
                    userStore.loginStatus = true
                    // message.success(resp.data.msg)
                } else {

                    cookies.remove("userToken")
                }
            })
        }else{
            // message.info("无token")
        }
    }
)

</script>