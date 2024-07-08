<template>
    <h1>用户管理</h1>
    <n-input-group>
        <n-input v-model:value="input1" placeholder="请输入用户名"></n-input>
        <n-button type="primary" @click="search">搜索</n-button>
    </n-input-group>

    <br>
    <br>
    <n-table>
        <thead>
            <tr>

                <th>用户ID</th>
                <th>用户名</th>
                <th>性别</th>
                <th>邮箱</th>
                <th>签名</th>
                <th>头像</th>
                <th>身份</th>
                <th>状态</th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="i in userList">
                <td>
                    {{ i.userId }}
                </td>
                <td>
                    {{ i.userName }}
                </td>
                <td>
                    {{ i.userGender }}
                </td>
                <td>
                    {{ i.userEmail }}
                </td>
                <td>
                    {{ i.userSignature }}
                    <n-button @click="resetSignature(i.userId)">清空签名</n-button>
                </td>
                <td>
                    <div style="display: flex;align-items: center;">
                        <n-avatar :src="i.userAvatarUrl"></n-avatar>
                        <n-button @click="resetAvatar(i.userId)">重置头像</n-button>
                    </div>
                </td>
                <td>
                    <span v-if="i.userRoleId == 1" style="color: #009688;">普通用户</span>
                    <span v-if="i.userRoleId == 2" style="color: #009688;">视频用户</span>
                    <span v-if="i.userRoleId == 3" style="color: #009688;">直播用户</span>
                    <span v-if="i.userRoleId == 4" style="color: #009688;">版主</span>
                    <span v-if="i.userRoleId == 5" style="color: #8a2be2;">恶魔</span>

                    <n-button @click="showModalbox(i.userId)">修改身份</n-button>
                </td>
                <td>
                    <span v-if="i.userStatus == 1" style="color: #009688;">正常</span>
                    <span v-if="i.userStatus == 0" style="color: red;">封禁</span>
                    <n-button v-if="i.userStatus == 1" type="error" @click="banUser(i.userId)">封禁</n-button>
                    <n-button v-if="i.userStatus == 0" @click="unbanUser(i.userId)">解封</n-button>
                </td>
            </tr>
        </tbody>
    </n-table>
    <br>
    <n-modal v-model:show="showModal">
        <n-card style="width: 800px;">
            <n-radio value="1" name="userRole" :checked="checkedValue == '1'" @change="handleChange">普通用户</n-radio>
            <n-radio value="2" name="userRole" :checked="checkedValue == '2'" @change="handleChange">视频用户</n-radio>
            <n-radio value="3" name="userRole" :checked="checkedValue == '3'" @change="handleChange">直播用户</n-radio>
            <n-radio value="4" name="userRole" :checked="checkedValue == '4'" @change="handleChange">版主</n-radio>
            <n-radio value="5" name="userRole" :checked="checkedValue == '5'" @change="handleChange">恶魔</n-radio>
            <n-select v-if="checkedValue == '4'" v-model:value="gameId" :options="options" filterable placeholder="选择游戏"  size="large" :style="{width:'20%'}"/>
            <n-button @click="changeRole()">确定</n-button>
        </n-card>
    </n-modal>

    <n-pagination v-model:page="page" :page-count="pageCount" />
</template>



<script setup lang="ts">
import axios from 'axios';
import { ref, onMounted, watch } from 'vue'
import { useCookies } from 'vue3-cookies';
const cookies = useCookies().cookies
import { useMessage } from 'naive-ui';
const message = useMessage();
const page = ref(1)
const pageSize = ref(10)
const pageCount = ref(10)
const input1 = ref('')
const queryParam = ref('')
const userList = ref([] as any[])
function getUserList() {
    userList.value = []
    // 发送请求获取用户列表
    axios.post("http://localhost:8080/getUserList?pageNum=" + page.value + "&pageSize=" + pageSize.value + "&queryParam=" + queryParam.value + "&userToken=" + cookies.get('userToken')).then(resp => {
        userList.value = resp.data.data.list
        pageCount.value = resp.data.data.pages
    })
}
function search() {
    queryParam.value = input1.value
    getUserList()
}
onMounted(() => {
    getUserList()
})
watch(page, () => {
    getUserList()
})


function resetSignature(userId: number) {
    axios.post("http://localhost:8080/resetSignature?userId=" + userId + "&userToken=" + cookies.get('userToken')).then(resp => {
        getUserList()
        message.info(resp.data.msg)

    })
}

function resetAvatar(userId: number) {
    axios.post("http://localhost:8080/resetAvatar?userId=" + userId + "&userToken=" + cookies.get('userToken')).then(resp => {
        getUserList()
        message.info(resp.data.msg)
    })
}

function banUser(userId: number) {
    axios.post("http://localhost:8080/banUser?userId=" + userId + "&userToken=" + cookies.get('userToken')).then(resp => {
        getUserList()
        message.info(resp.data.msg)
    })
}
function unbanUser(userId: number) {
    axios.post("http://localhost:8080/unbanUser?userId=" + userId + "&userToken=" + cookies.get('userToken')).then(resp => {
        getUserList()
        message.info(resp.data.msg)
    })
}


const userId1 = ref()
const showModal = ref(false)
function showModalbox(userId: number) {
    showModal.value = true
    userId1.value = userId
}
const checkedValue = ref<string | null>(null)
function handleChange(e: Event) {
    checkedValue.value = (e.target as HTMLInputElement).value
    // message.info(checkedValue.value)
}

const gameList = ref()
const options = ref([{}])
const gameId = ref(null)
function getGameList(){
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
}
watch(checkedValue,()=>{
    if(checkedValue.value == '4'){
        getGameList()
    }
})

function changeRole(){
    axios.post("http://localhost:8080/modifyUserRole?userId="+userId1.value+"&userToken="+cookies.get('userToken')+"&roleId="+checkedValue.value+"&gameId="+gameId.value).then(resp =>{
        getUserList()
        showModal.value = false
        message.info(resp.data.msg)
    })
}
</script>



<style scoped></style>