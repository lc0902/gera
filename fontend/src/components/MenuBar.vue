<template>
  <el-menu  class="el-menu-demo" mode="horizontal" :ellipsis="false" 
    router>
    <el-menu-item index="/">
      <GameControllerIcon :width="40"></GameControllerIcon>&nbsp;&nbsp;&nbsp;GEra
    </el-menu-item>
    <div class="flex-grow"></div>
    <el-menu-item index="/adminView" v-if="userStore.userRoleId==4"> <AdminIcon :width="20" :heigth="20"/>&nbsp;&nbsp;&nbsp;管理员</el-menu-item>
    <el-menu-item index="/adminView" v-if="userStore.userRoleId==5"> <AdminIcon :width="20" :heigth="20"/>&nbsp;&nbsp;&nbsp;管理员</el-menu-item>
    <el-menu-item index="/userView/liveListView"> <LiveIcon :width="20" :heigth="20"/>&nbsp;&nbsp;&nbsp;直播</el-menu-item>
    <el-menu-item index="/userView/videoListView"><VideoIcon :width="20" :heigth="20"/>&nbsp;&nbsp;&nbsp;视频</el-menu-item>
    <el-menu-item index="/userView/voiceCommunicationView"><SpeechIcon :width="20" :heigth="20"/>&nbsp;&nbsp;&nbsp;GEra语音</el-menu-item>
    <el-menu-item index="/userView/postListView">
      <div style="display:flex;align-items: center;">
        <CommunityIcon :width="20" :height="20"/>&nbsp;&nbsp;社区
      </div>
    </el-menu-item>
    <el-sub-menu>
      <template #title>
        <el-avatar round :src=userAvatarUrl  />
      </template>
      <el-menu-item @click="showLoginBox()" v-if="!loginStatus">登录</el-menu-item>
      <el-menu-item index="/userView/personalCenter" v-if="loginStatus">个人中心</el-menu-item>
      <el-menu-item index="/userView/postEditorView" v-if="loginStatus">发帖</el-menu-item>
      <el-menu-item index="/userView/viedoEditor" v-if="loginStatus">上传视频</el-menu-item>
      <el-menu-item index="/userView/personalCollectView" v-if="loginStatus">我的收藏</el-menu-item>
      <el-menu-item index="/userView/personalUploadView" v-if="loginStatus">我的发布</el-menu-item>
      <el-menu-item @click="logOut()" v-if="loginStatus">注销</el-menu-item>

    </el-sub-menu>
  </el-menu>

  <n-modal v-model:show="showModal" style="width: 20%; position:fixed; top: 10%;left: 40%;border-radius: 10px; box-shadow: 0 0 10px gray;">
    <n-card
      title="GERA"
      :bordered="false"
      role="dialog"
      aria-modal="true"
    >
    <n-tabs default-value="signin" size="medium" justify-content="space-evenly" type="line">
      <n-tab-pane name="signin" tab="登录">
        <n-form>
          <n-form-item-row label="用户名">
            <n-input placeholder="邮箱/账号" v-model:value="userName"/>
          </n-form-item-row>
          <n-form-item-row label="密码">
            <n-input placeholder="密码" type="password" show-password-on="mousedown" v-model:value="userPassword"/>
          </n-form-item-row>
        </n-form>
        <n-button type="primary" block secondary strong @click="login">
          登录
        </n-button>
        <br>
        <router-link to="/userView/retrievePassword" target="_blank"><a>忘记密码？</a></router-link>
      </n-tab-pane>
      <n-tab-pane name="signup" tab="注册">
        <n-form>
          <n-form-item-row label="用户名">
            <n-input placeholder="用户名" v-model:value="userName" />
          </n-form-item-row>
          <n-form-item-row label="密码">
            <n-input placeholder="密码" type="password" show-password-on="mousedown" v-model:value="userPassword"/>
          </n-form-item-row>
          <n-form-item-row label="重复密码">
            <n-input placeholder="重复密码"  type="password" show-password-on="mousedown" v-model:value="userPasswordVerify"/>
          </n-form-item-row>
          <n-form-item-row label="性别">
            <n-radio-group v-model:value="gender">
              <n-space>
                <n-radio v-for="gender in genders" :key="gender.value" :value="gender.value">
                {{ gender.label }}
              </n-radio>
              </n-space>
            </n-radio-group>
          </n-form-item-row>
          <n-form-item-row label="邮箱">
            <n-input-group>
              <n-input placeholder="邮箱"  v-model:value="userEmail"/>
              <n-button type="primary" @click="getVCode">获取验证码</n-button>
            </n-input-group>
          </n-form-item-row>
          <n-form-item-row label="验证码">
            <n-input placeholder="验证码" v-model:value="vCode"/>
          </n-form-item-row>
        </n-form>
        <n-input-group>
          <n-switch v-model:value="activeSwitch"/>我同意
          <router-link to="/userProtocol" target="_blank"><a>用户协议</a></router-link>
        </n-input-group>
        <n-button type="primary" block secondary strong @click="enroll" :disabled="!activeSwitch">
          注册
        </n-button>
      </n-tab-pane>
    </n-tabs>
  </n-card>
  </n-modal>
</template>

<script lang="ts" setup>
import AdminIcon from './icons/AdminIcon.vue'
import GameControllerIcon from './icons/GameControllerIcon.vue'
import VideoIcon from './icons/VideoIcon.vue'
import SpeechIcon from './icons/SpeechIcon.vue'
import LiveIcon from './icons/LiveIcon.vue'
import CommunityIcon from './icons/CommunityIcon.vue'
import { ref,watch,onMounted } from 'vue'
import HomeIcon from '@/components/icons/HomeIcon.vue'
import { useMessage } from 'naive-ui'
import axios from 'axios'
import {useUserStore} from '@/stores/user'
import {useCookies} from 'vue3-cookies'
const {cookies} = useCookies()
const userStore = useUserStore()
const message = useMessage()
const uploadVideoStatus = ref(false)
const activeSwitch = ref(false)

const userName = ref('')
const userPassword = ref('')
const userPasswordVerify = ref('')
const userEmail = ref('')
const vCode = ref('')

const loginStatus = ref(userStore.loginStatus)
const userAvatarUrl = ref(userStore.userAvatarUrl)

const gender = ref('alien')
const genders = ref([
  {
    value:'boy',
    label:'男'
  },{
    value:'girl',
    label:'女'
  },{
    value:'alien',
    label:'外星人'
  }
])


const showModal = ref(false)
function showLoginBox(){
  showModal.value=true
}

import {useRouter} from 'vue-router'
const router = useRouter()




function login(){
  axios.post('http://localhost:8080/login?userName='+userName.value+'&userPassword='+userPassword.value)
  .then(function (response) {
    if(response.data.code==1210){
      showModal.value=false
      message.success("登录成功")
      userStore.loginStatus = true
      cookies.set("userToken",response.data.data.userToken,"7D")
      router.push("/")
      location.reload()

    }else{
      message.error(response.data.msg)
    }
  })
  .catch(function (error) {
  });
}
function logOut(){
  userStore.loginStatus = false
  cookies.remove("userToken")
  message.success("已退出登录")
  location.reload()
}
function getVCode(){
  if(userName.value==null){
    message.error("请输入用户名")
  }else if(userPassword.value==null){
    message.error("请输入用户密码")
  }else if(userPassword.value!=userPasswordVerify.value){
    message.error("两次密码不匹配")
  }else if(gender.value==null){
    message.error("未选择性别")
  }else if(userEmail.value==null){
    message.error("请输入邮箱")
  }else{
    axios.post("http://localhost:8080/enrollCode?userEmail="+userEmail.value).then(resp => {
      if(resp.data.code==1010){
        message.success(resp.data.msg)
      }else{
        message.error(resp.data.msg)
      }
    }).catch(error => {
    })
  }
}
function enroll(){
  if(userName.value==null){
    message.error("请输入用户名")
  }else if(userPassword.value==null){
    message.error("请输入用户密码")
  }else if(userPassword.value!=userPasswordVerify.value){
    message.error("两次密码不匹配")
  }else if(gender.value==null){
    message.error("未选择性别")
  }else if(userEmail.value==null){
    message.error("请输入邮箱")
  }else{
    axios.post("http://localhost:8080/enroll?"+"userName="+userName.value+"&userPassword="+userPassword.value+"&userGender="+gender.value+"&userEmail="+userEmail.value+"&userAvatarUrl=http://localhost/pic/avatar/avatar_"+gender.value+".png&vCode="+vCode.value).then(resp => {
      if(resp.data.code==1110){
        message.success(resp.data.msg)
        login()
      }else{
        message.error(resp.data.msg)
      }
    }).catch(error => {
    })
  }
}



watch(() => userStore.loginStatus, (newValue, oldValue) => {
    loginStatus.value=newValue
})
watch(() => userStore.userAvatarUrl,(newValue,oldValue) => {
  userAvatarUrl.value=newValue
})
</script>

<style>
.flex-grow {
  flex-grow: 1;
}
</style> 