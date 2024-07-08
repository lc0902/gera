<template>


    <n-modal v-model:show="showModal">
        <n-card style="width: 600px" title="创建房间" :bordered="false" size="huge" role="dialog" aria-modal="true">
            <n-input placeholder="请输入房间名" v-model:value="roomName"></n-input>
            <br>
            <br>
            <n-select v-model:value="gameId" :options="options" filterable placeholder="选择游戏" size="large"
                :style="{ width: '20%' }" />
            <br>
            <n-input type="password" placeholder="请输入密码" v-model:value="roomPassword"></n-input>
            <br>
            <br>
            <n-button @click="createRoom">创建</n-button>
        </n-card>
    </n-modal>
    <h1>GEra语音</h1>
    <n-button @click="showModal = true">开启房间</n-button>
    <br>
    <br>
    <n-flex vertical>
        <n-card v-for="i in roomListClass">
            <n-flex justify="space-between">
                <div style="display: flex;align-items: center;">
                    <n-image :src="i.gameCoverUrl" width="100" height="100" preview-disabled></n-image>
                    <span style="color: #ff69b4;font-size: xx-large;">
                        <n-ellipsis style="max-width: 500px">
                            {{ i.roomName }}

                        </n-ellipsis>
                    </span>
                </div>
                <div style="display: flex;align-items: center;">
                    <LockIcon v-if="i.hasPassword" width="20" height="20"></LockIcon>
                    <n-button @click="showModal2Box(i.roomId)">加入</n-button>
                </div>
            </n-flex>

        </n-card>
    </n-flex>
    <n-modal v-model:show="showModal2">
        <n-card style="width: 600px" title="加入房间" :bordered="false" size="huge" role="dialog" aria-modal="true">
            <n-input type="password" placeholder="请输入密码" v-model:value="inputPwd"></n-input>
            <br>
            <br>
            <n-button @click="checkPwd()">加入</n-button>
        </n-card>
    </n-modal>
</template>



<script setup lang="ts">
import LockIcon from '@/components/icons/LockIcon.vue';
import axios from 'axios';
import { onMounted, ref } from 'vue';
import { useUserStore } from '@/stores/user';
const userStore = useUserStore()
import { useMessage } from 'naive-ui';
const message = useMessage()
import { useCookies } from 'vue3-cookies';
const cookies = useCookies().cookies
import { useRouter } from 'vue-router';
import { pa } from 'element-plus/es/locales.mjs';
const router = useRouter()


const showModal2 = ref(false)
const inputPwd = ref("")
const selectedRoom = ref()

function showModal2Box(roomId: string) {
    selectedRoom.value = roomId

    for (let i = 0; i < roomListClass.value.length; i++) {
        if (roomListClass.value[i].roomId == roomId) {
            if (roomListClass.value[i].hasPassword == true) {
                showModal2.value = true
            } else {
                // 无密码直接进入
                // 此处注释代码为无需执行
                // let formdata = new FormData()
                // formdata.append("userToken",userStore.userToken)
                // formdata.append("roomId",selectedRoom.value)
                // axios.post("http://localhost:8080/checkRoomPwd",formdata).then(resp=>{
                //     console.log(resp)
                // })
                // 此处将cookie设置，并跳转到路由
                cookies.set("currentRoomId", selectedRoom.value)
                router.push({ path: "/userView/voiceRoomView" })
            }
        }
    }

}

function checkPwd() {
    for (let i = 0; i < roomListClass.value.length; i++) {
        if (roomListClass.value[i].roomId == selectedRoom.value) {
            if (roomListClass.value[i].hasPassword == false) {

            } else {
                // 有密码
                let formdata = new FormData()
                formdata.append("userToken", userStore.userToken)
                formdata.append("roomId", selectedRoom.value)
                formdata.append("roomPwd", inputPwd.value)
                axios.post("http://localhost:8080/checkRoomPwd", formdata).then(resp => {
                    if (resp.data.msg == "密码正确") {
                        message.info(resp.data.msg)
                        // 此处将cookie设置，并跳转到路由
                        cookies.set("currentRoomId", selectedRoom.value)
                        router.push({ path: "/userView/voiceRoomView" })
                    }
                })
            }
        }
    }
}


const roomName = ref("")
const roomPassword = ref("")
const gameId = ref()
const showModal = ref(false)




function createRoom() {
    let formdata = new FormData();
    formdata.append("roomName", roomName.value)
    if (roomPassword.value == "") {

    } else {
        formdata.append("password", roomPassword.value)
    }
    formdata.append("gameId", gameId.value)
    formdata.append("userToken", userStore.userToken)

    axios.post("http://localhost:8080/createRoom", formdata).then(resp => {
        console.log(resp)
        if (resp.data.msg == "房间创建成功") {
            message.info(resp.data.msg)
            showModal.value = false
            getRoomList()
        }

    })
}

class RoomListClass {
    roomName: string
    roomId: string
    hasPassword: boolean
    password: string
    gameName: string
    gameId: string
    gameCoverUrl: string
    constructor(roomName: string, roomId: string, hasPassword: boolean, password: string, gameName: string, gameId: string, gameCoverUrl: string) {
        this.roomName = roomName
        this.roomId = roomId
        this.hasPassword = hasPassword
        this.password = password
        this.gameName = gameName
        this.gameId = gameId
        this.gameCoverUrl = gameCoverUrl
    }
}

const roomList = ref([] as any[])
const gameList = ref()
const options = ref([{}])
const roomListClass = ref([] as RoomListClass[])

function getRoomList() {
    axios.post("http://localhost:8080/getVoiceRoom").then(resp => {
        roomList.value = resp.data.data
        roomListClass.value = []
        for (let i = 0; i < roomList.value.length; i++) {
            axios.post("http://localhost:8080/getGameInfo?gameId=" + resp.data.data[i].gameId).then(resp1 => {
                roomListClass.value.push(new RoomListClass(
                    resp.data.data[i].roomName,
                    resp.data.data[i].roomId,
                    resp.data.data[i].hasPassword,
                    resp.data.data[i].password,
                    resp1.data.data.gameName,
                    resp1.data.data.gameId,
                    resp1.data.data.gameCoverUrl
                ))
            })
        }
    })

}
onMounted(() => {
    getRoomList()

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

</script>



<style scoped></style>