<template>
    <n-form>
        <n-form-item-row label="邮箱">
            <n-input placeholder="邮箱" v-model:value="email" />
        </n-form-item-row>
        <n-form-item-row label="新密码">
            <n-input placeholder="密码" type="password" show-password-on="mousedown" v-model:value="userPassword" />
        </n-form-item-row>
        <n-form-item-row label="验证码">
            <n-input-group>
                <n-input placeholder="验证码" v-model:value="VCode" />
                <n-button type="primary" @click="sendVCode()">发送验证码</n-button>
            </n-input-group>
        </n-form-item-row>
    </n-form>
    <n-button @click="resetPassword">重置</n-button>

</template>



<script setup lang="ts">
import axios from 'axios';
import { ref } from 'vue'
import { useMessage } from 'naive-ui';
const message = useMessage()

const email = ref('')
const userPassword = ref('')
const VCode = ref('')

function sendVCode() {
    if (email.value == '') {
        message.error("邮箱不能为空")
    }
    // 发送验证码
    else {
        axios.post("http://localhost:8080/retrieveCode?email="+email.value).then(res => {
            message.info(res.data.msg)
        })
    }
}

function resetPassword() {
    // 重置密码
    axios.post("http://localhost:8080/retrievePassword?userEmail="+email.value+"&userPassword="+userPassword.value+"&vCode="+VCode.value).then(res => {
        message.info(res.data.msg)
    })
}

</script>



<style scoped></style>