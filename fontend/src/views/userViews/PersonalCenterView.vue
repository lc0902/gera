<template>
  <h1 style="color:#ff69b4;">个人信息</h1>
  <n-form ref="formRef" :model="formValue" :rules="rules">
    <n-form-item label="用户名" path="userName">
      <n-input :disabled="disable" v-model:value="formValue.userName" />
    </n-form-item>
    <n-form-item label="个性签名">
      <n-input :disabled="disable" v-model:value="formValue.userSignature"></n-input>
    </n-form-item>
    <n-form-item label="性别">
      <n-radio-group v-model:value="formValue.userGender" name="gender">
        <n-space>
          <n-radio :disabled="disable" v-for="gender in genders" :key="gender.value" :value="gender.value">
            {{ gender.label }}
          </n-radio>
        </n-space>
      </n-radio-group>
    </n-form-item>
    <n-form-item label="邮箱">
      <n-input :disabled=true v-model:value="formValue.userEmail"></n-input>
    </n-form-item>
    <n-button @click="disable = false" v-if="disable == true" type="primary">修改信息</n-button>
    <n-button v-if="disable == false" @click="modifyPersonalInfo()" type="primary">保存</n-button>
    <br>
    <br>
    <n-form-item label="头像">
      <n-image width="100" :src="userStore.userAvatarUrl" :previewed-img-props="{ style: { width: '1200px' } }" />
    </n-form-item>
  </n-form>
  <n-upload action="http://localhost:8080/changeAvatar" :data="{
    'userToken': userStore.userToken
  }" @finish="handleFinish">
    <n-button type="primary">修改头像</n-button>
  </n-upload>
  <br>

  原密码<n-input placeholder="原密码" type="password" v-model:value="oldPassword"></n-input><br>
  新密码<n-input placeholder="新密码" type="password" v-model:value="newPassword"></n-input><br>
  再次确认密码<n-input placeholder="新密码" type="password" v-model:value="newPassword2"></n-input><br>
  <n-button @click="modifyPassword()" type="primary">修改密码</n-button>






  <br>
  <br>
  <span>
    用户身份:{{ formValue.userRoleId }}
    <n-button v-if="userStore.userRoleId == 1" @click="showModalFunction(1)">申请上传视频权限</n-button>
    <n-button v-if="userStore.userRoleId == 2" @click="showModalFunction(2)">申请直播权限</n-button>
  </span>
  <br>
  <br>
  账号状态:{{ formValue.userStatus }}
  <n-button v-if="userStore.userStatus == 0" @click="showModalFunction(3)">申请解禁</n-button>

  <n-modal v-model:show="showModal">
    <div style="width: 600px;">
      申请类型：{{ applyIdText }}
      <n-input placeholder="申请理由" type="textarea" v-model:value="reason"></n-input>
      <br><br>
      <n-button @click="apply()"> 申请</n-button>
    </div>
  </n-modal>



</template>



<script setup lang="ts">
import axios from 'axios';
import { defineComponent, onMounted, ref, watch } from 'vue'
import type { FormInst } from 'naive-ui'
import { useUserStore } from '@/stores/user';
import { elementDark, useMessage } from 'naive-ui';
import type { UploadInst, UploadFileInfo } from 'naive-ui';
import { useCookies } from 'vue3-cookies';
const cookies = useCookies().cookies
const message = useMessage()
const userStore = useUserStore()
const formRef = ref<FormInst | null>(null)
const formValue = ref({
  userName: userStore.userName,
  userAvatarUrl: userStore.userAvatarUrl,
  userGender: userStore.userGender,
  userEmail: userStore.userEmail,
  userRoleId: userRoleIdToString(),
  userSignature: userStore.userSignature,
  userStatus: userStatusToString()

})
const disable = ref(true)
const rules = {
  userName: {
    required: true,
    message: '用户名不能为空',
    trigger: ['input', 'blur']
  }
}
const genders = ref([
  {
    value: 'boy',
    label: '男'
  }, {
    value: 'girl',
    label: '女'
  }, {
    value: 'alien',
    label: '外星人'
  }
])



function modifyPersonalInfo() {
  axios({
    method: 'post',
    url: 'http://localhost:8080/modifyPersonalInformation?userName=' + formValue.value.userName + "&userSignature=" + formValue.value.userSignature + "&userGender=" + formValue.value.userGender + "&userToken=" + userStore.userToken
  }).then(resp => {
    if (resp.data.code == 1510) {
      message.success(resp.data.msg)
      disable.value = true
    } else {
      message.error(resp.data.msg)
    }
  }).catch(resp => {
  })
}




const handleFinish = () => {
  location.reload()
}

const reason = ref('')
const applyIdText = ref('')
const showModal = ref(false)

function showModalFunction(applyId: any) {
  if (applyId == 1) {
    applyIdText.value = '申请上传视频权限'
  }
  if (applyId == 2) {
    applyIdText.value = '申请直播权限'
  }
  if (applyId == 3) {
    applyIdText.value = '申请解禁'
  }
  showModal.value = true
}


function apply() {
  if (applyIdText.value == '申请上传视频权限') {
    axios.post("http://localhost:8080/apply?userToken=" + cookies.get("userToken") + "&applicationType=" + 1 + "&applicationText=" + reason.value).then(resp => {
      if (resp.data.data == true) {
        message.success("申请成功，请等待审核")
      } else {
        message.error("请勿重复申请")
      }
    }).catch(resp => {
    })
    showModal.value = false
  } else if (applyIdText.value == '申请直播权限') {
    axios.post("http://localhost:8080/apply?userToken=" + cookies.get("userToken") + "&applicationType=" + 2 + "&applicationText=" + reason.value).then(resp => {
      if (resp.data.data == true) {
        message.success("申请成功，请等待审核")
      } else {
        message.error("请勿重复申请")
      }
    }).catch(resp => {
    })
    showModal.value = false
  } else if (applyIdText.value == '申请解禁') {
    axios.post("http://localhost:8080/apply?userToken=" + cookies.get("userToken") + "&applicationType=" + 3 + "&applicationText=" + reason.value).then(resp => {
      if (resp.data.data == true) {
        message.success("申请成功，请等待审核")
      } else {
        message.error("请勿重复申请")
      }
    }).catch(resp => {

    })
  }
}












watch(() => userStore, () => {
  formValue.value.userName = userStore.userName

})










watch(() => userStore.userName, () => {
  formValue.value.userName = userStore.userName
})
watch(() => userStore.userAvatarUrl, () => {
  formValue.value.userAvatarUrl = userStore.userAvatarUrl
})
watch(() => userStore.userSignature, () => {
  formValue.value.userSignature = userStore.userSignature
})
watch(() => userStore.userGender, () => {
  formValue.value.userGender = userStore.userGender
})
watch(() => userStore.userEmail, () => {
  formValue.value.userEmail = userStore.userEmail
})
watch(() => userStore.userRoleId, () => {
  formValue.value.userRoleId = userRoleIdToString()
})
watch(() => userStore.userStatus, () => {
  formValue.value.userStatus = userStatusToString()
})

function userRoleIdToString() {
  if (userStore.userRoleId == 1) {
    return "普通用户"
  } else if (userStore.userRoleId == 2) {
    return "视频用户"
  } else if (userStore.userRoleId == 3) {
    return "直播用户"
  } else if (userStore.userRoleId == 4) {
    return "版主"
  }
}
function userStatusToString() {
  if (userStore.userStatus == 1) {
    return "正常"
  } else {
    return "封禁"
  }
}
const newPassword = ref('')
const newPassword2 = ref('')

const oldPassword = ref('')
function modifyPassword() {
  if (newPassword.value == newPassword2.value) {
    axios.post("http://localhost:8080/modifyPassword?userToken=" + cookies.get("userToken") + "&oldPassword=" + oldPassword.value + "&newPassword=" + newPassword.value
    ).then(resp => {
      message.info(resp.data.msg)
    })
  } else {
    message.error("两次密码不一致")
  }

}









</script>



<style scoped></style>