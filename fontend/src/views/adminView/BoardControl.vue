<template>
    <h1>板块管理</h1>
    <n-button @click="showModalf()">添加板块</n-button>
    <n-modal v-model:show="showModal1">
        <n-card style="width: 600px">
            <n-input placeholder="请输入游戏名" v-model:value="addGameName"></n-input>
            <n-input placeholder="请输入游戏描述" v-model:value="addGameDesc" type="textarea"></n-input>
            <n-upload v-model:file-list="addGameCoverFile" :max="1" list-type="image-card">
                点击上传封面
                </n-upload><br>
            <n-date-picker v-model:value="addGameReleaseTime" type="date" />
            <n-button @click="addGame()">确认添加</n-button>
        </n-card>
    </n-modal>
    
    <br><br>
    <n-input-group>
        <n-input placeholder="请输入游戏名" v-model:value="searchGameName" @keyup.enter="searchGame()"></n-input>
        <n-button @click="searchGame()" >搜索</n-button>
    </n-input-group>
    <br><br>
    <n-table>
        <thead>
            <tr>
                <th>游戏id</th>
                <th>游戏名</th>
                <th>游戏描述</th>
                <th>封面</th>
                <th>发行时间</th>
                <th>评分</th>
                <th>版主</th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="i in gameTDList">
                <td>{{ i.gameId }}</td>
                <td>{{ i.gameName }} <n-button @click="showModal(1, i.gameId)">修改</n-button></td>
                <td>{{ i.gameDesc }} <n-button @click="showModal(2, i.gameId)">修改</n-button></td>
                <td><n-image :src="i.gameCoverUrl" width="100" height="100"></n-image> <n-button
                        @click="showModal(3, i.gameId)">修改</n-button></td>
                <td>{{ i.gameReleaseTime }} <n-button @click="showModal(4, i.gameId)">修改</n-button></td>
                <td>{{ i.gameRate }}</td>
                <td><n-avatar :src="i.gameModeratorAvatarUrl" round :size="100"></n-avatar>{{ i.gameModeratorName }}
                    <n-button @click="showModal(5, i.gameId)">修改</n-button>
                </td>
                <td>
                    <n-button type="error" @click="deleteGame(i.gameId)">删除板块</n-button>
                </td>
            </tr>
        </tbody>
    <n-pagination v-model:page="pageNum" :page-count="pageCount" />

    </n-table>

    <n-modal v-model:show="showModalV">

        <n-card style="width: 600px" v-if="changeCode == 1">
            修改游戏名
            <n-input v-model:value="input1"></n-input>
            <n-button @click="updateGameName()">确认修改</n-button>
        </n-card>
        <n-card style="width: 600px" v-if="changeCode == 2">
            修改游戏描述
            <n-input v-model:value="input2" type="textarea"></n-input>
            <n-button @click="updateGameDesc()">确认修改</n-button>
        </n-card>
        <n-card style="width: 600px" v-if="changeCode == 3">
            修改封面
            <n-upload v-model:file-list="coverFile" :max="1" list-type="image-card">
                点击上传封面
            </n-upload><br>
            <n-button @click="updateGameCover()">确认修改</n-button>
        </n-card>
        <n-card style="width: 600px" v-if="changeCode == 4">
            修改发行时间
            <n-date-picker v-model:value="timestamp" type="date" />
            <n-button @click="updateGameReleaseTime()">确认修改</n-button>
        </n-card>
        <n-card style="width: 600px" v-if="changeCode == 5">
            修改版主
            <n-select :options="options" :render-label="renderLabel" filterable v-model:value="selectedUserId"/>
            <n-button @click="changeGameModerator()">确认修改</n-button>
        </n-card>

    </n-modal>
</template>



<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import { NAvatar, NText, useMessage } from 'naive-ui';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { useCookies } from 'vue3-cookies';
const { cookies } = useCookies();
const message = useMessage();
const router = useRouter();
import { defineComponent, h } from 'vue'
import type { SelectRenderLabel } from 'naive-ui'
const changeCode = ref()


const options = ref([] as any[])

const userList = ref([] as any[])
function getAllUser() {
    userList.value = []
    axios.post("http://localhost:8080/getAllUser?userToken=" + cookies.get('userToken')).then(resp => {
        userList.value = resp.data.data
        console.log(userList)
        options.value = []
        for (let i = 0; i < resp.data.data.length; i++) {
            options.value.push({
                label: userList.value[i].userName,
                value: userList.value[i].userId,
                userAvatarUrl: userList.value[i].userAvatarUrl
            })
        }
    })
}

const selectedUserId = ref()

function changeGameModerator() {
    axios.post("http://localhost:8080/modifyUserRole?userToken=" + cookies.get('userToken') + "&userId=" + selectedUserId.value + "&roleId=4&gameId="+currentGameId.value).then(resp => {
        message.info(resp.data.msg)
        getGameList()
        showModalV.value = false
    })
}

watch(changeCode, () => {
    if (changeCode.value == 5) {
        getAllUser()
    }
})

const renderLabel: SelectRenderLabel = (option) => {
    return h(
        'div',
        {
            style: {
                display: 'flex',
                alignItems: 'center'
            }
        },
        [
            h(NAvatar, {
                src: option.userAvatarUrl as string,
                round: true,
                size: 'small'
            }),
            h(
                'div',
                {
                    style: {
                        marginLeft: '12px',
                        padding: '4px 0'
                    }
                },
                [
                    h('div', null, [option.label as string])

                ]
            )
        ]
    )
}


class GameTDClass {
    gameId: number;
    gameName: string;
    gameDesc: string;
    gameCoverUrl: string;
    gameReleaseTime: string;
    gameRate: number;
    gameModeratorName: string
    gameModeratorAvatarUrl: string
    constructor(gameId: number, gameName: string, gameDesc: string, gameCoverUrl: string, gameReleaseTime: string, gameRate: number, gameModeratorName: string, gameModeratorAvatarUrl: string
    ) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.gameDesc = gameDesc;
        this.gameCoverUrl = gameCoverUrl;
        this.gameReleaseTime = gameReleaseTime;
        this.gameRate = gameRate;
        this.gameModeratorName = gameModeratorName;
        this.gameModeratorAvatarUrl = gameModeratorAvatarUrl;
    }
}


// TODO
const gameTDList = ref<GameTDClass[]>([]);
const searchGameName = ref('')
const searchParm = ref('')
const pageNum = ref(1)
const pageSize = ref(8)
const pageCount = ref(1)
function searchGame(){
    searchParm.value = searchGameName.value
    getGameList()
}
function getGameList() {
    gameTDList.value = [];
    axios.post("http://localhost:8080/adminGetGameList?userToken=" + cookies.get('userToken') + "&pageNum="+pageNum.value+"&pageSize="+pageSize.value+"&gameName="+searchParm.value).then(resp => {
        console.log(resp.data);
        pageCount.value = resp.data.data.pageInfo.pages

        for (let i = 0; i < resp.data.data.games.length; i++) {
            let gametdUserName = '无'
            let gametdUserAvatarUrl = ''
            if (resp.data.data.users[i] != null) {
                gametdUserName = resp.data.data.users[i].userName
                gametdUserAvatarUrl = resp.data.data.users[i].userAvatarUrl
            }
            let gametd = new GameTDClass(
                resp.data.data.games[i].gameId,
                resp.data.data.games[i].gameName,
                resp.data.data.games[i].gameDescription,
                resp.data.data.games[i].gameCoverUrl,
                resp.data.data.games[i].gameReleaseTime,
                resp.data.data.games[i].gameRate,
                gametdUserName, gametdUserAvatarUrl

            )
            gameTDList.value.push(gametd)
        }
        // console.log(gameTDList.value)
    })

}
watch(pageNum, () => {
    getGameList()
})

import { useUserStore } from '@/stores/user';
const userStore = useUserStore();
onMounted(() => {
    if(userStore.userRoleId!=5){
        message.error("无权限")
    }
    getGameList();
})
const currentGameId = ref()
const showModalV = ref(false)
function showModal(id: number, changeGameId: number) {
    showModalV.value = true
    changeCode.value = id
    currentGameId.value = changeGameId
}
const input1 = ref()
function updateGameName() {
    axios.post("http://localhost:8080/updateGameName?userToken=" + cookies.get('userToken') + "&gameId=" + currentGameId.value + "&gameName=" + input1.value).then(resp => {
        message.info(resp.data.msg)
        showModalV.value = false
        getGameList();
    })
}

const input2 = ref('')
function updateGameDesc() {
    axios.post("http://localhost:8080/updateGameDesc?userToken=" + cookies.get('userToken') + "&gameId=" + currentGameId.value + "&gameDesc=" + input2.value).then(resp => {
        message.info(resp.data.msg)
        showModalV.value = false
        getGameList();
    })
}
const coverFile = ref()
function updateGameCover() {
    let formData = new FormData()
    formData.append('coverFile', coverFile.value[0].file)
    formData.append('userToken', cookies.get('userToken'))
    formData.append('gameId', currentGameId.value)
    axios.post("http://localhost:8080/updateGameCover", formData).then(resp => {
        message.info(resp.data.msg)
        showModalV.value = false
        getGameList();
    })

}
const timestamp = ref()
function updateGameReleaseTime() {
    axios.post("http://localhost:8080/updateGameReleaseTime?userToken=" + cookies.get('userToken') + "&gameId=" + currentGameId.value + "&releaseTime=" + JSON.stringify(timestamp.value)).then(resp => {
        message.info(resp.data.msg)
        showModalV.value = false
        getGameList();
    })
}



// 危险操作！！！

function deleteGame(deleteGameId:number){
    axios.post("http://localhost:8080/deleteGame?userToken=" + cookies.get('userToken') + "&gameId=" + deleteGameId).then(resp => {
        message.info(resp.data.msg)
        showModalV.value = false
        getGameList();
    })
}
const showModal1 = ref(false)
const addGameName = ref('')
const addGameDesc = ref('')
const addGameCoverFile = ref()
const addGameReleaseTime = ref()
function showModalf(){
    showModal1.value = true
    console.log("error")
}
function addGame() {
    let formData = new FormData()
    formData.append('gameName', addGameName.value)
    formData.append('gameDescription', addGameDesc.value)
    formData.append('coverFile', addGameCoverFile.value[0].file)
    formData.append('gameReleaseTime', JSON.stringify(addGameReleaseTime.value))
    formData.append('userToken', cookies.get('userToken'))
    axios.post("http://localhost:8080/addGame", formData).then(resp => {
        message.info(resp.data.msg)
        showModal1.value = false
        getGameList();
    })
}

</script>



<style scoped></style>