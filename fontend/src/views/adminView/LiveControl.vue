<template>
        <n-modal v-model:show="showPreviewModal">
        <div>
        <flv-player :type="'flv'" :url="selectedLiveUrl" :isLive="true">

        </flv-player>
        </div>

    </n-modal>

    <h1>直播管理</h1>
    <n-input-group>
        <n-input placeholder="直播间名称" v-model:value="input" ></n-input>
        <n-button >搜索</n-button>
    </n-input-group>
    <n-table>
        <tr>
            <th>id</th>
            <th>直播间名称</th>
            <th>封面</th>
            <th>所属游戏</th>
            <th>用户</th>
            <th>状态</th>
            <th>操作</th>
        </tr>
        <tbody>
            <tr v-for="i in liveListRef">
                <td>
                    {{ i.liveId }}
                </td>
                <td>
                    {{ i.liveName }}
                </td>
                <td>
                    <n-image :src="i.liveCoverUrl" :width="100" :height="100"/>
                </td>
                <td>
                    <div style="display: flex;align-items: center;">
                        <n-image :src="i.gameCoverUrl"  :width="100" :height="100"/>
                        {{ i.gameName }}
                    </div>
                </td>
                <td>
                    <div style="display: flex;align-items: center;">
                        <n-image :src="i.userAvatarUrl"  :width="100" :height="100"/>
                        {{ i.userName }}
                    </div>
                </td>
                <td>
                    <span v-if="i.liveStatus==1">直播中</span>
                    <span v-if="i.liveStatus==0">暂未开播</span>
                </td>
                <td>
                    <n-flex vertical>
                        <n-button type="info" @click="previewLive(i.liveUrl)">预览</n-button>
                    <n-button type="error" @click="closeLiveRoom(i.liveId)">关闭直播间</n-button>
                    <n-button type="error" @click="banUser(i.userId,i.liveId)">关闭并封禁账号</n-button>
                    </n-flex>
                </td>
            </tr>
        </tbody>
    </n-table>
    <n-pagination v-model:page="pageNum" :page-count="pageCount" />

</template>

<script setup lang="ts">
import FlvPlayer from '@/components/FlvPlayer.vue';
import axios from 'axios';
import {onMounted, ref, watch} from 'vue'
import { useCookies } from 'vue3-cookies';
const cookies = useCookies().cookies
import { useMessage } from 'naive-ui';
const message = useMessage()





class LiveListClass{
    liveId:number
    liveName:string
    liveStatus:number
    liveUrl:string
    liveCoverUrl:string
    userId:number
    userName:string
    userAvatarUrl:string
    gameId:number
    gameName:string
    gameCoverUrl:string
    constructor(
        liveId:number,
        liveName:string,
        liveStatus:number,
        liveUrl:string,
        liveCoverUrl:string,
        userId:number,
        userName:string,
        userAvatarUrl:string,
        gameId:number,
        gameName:string,
        gameCoverUrl:string
    ){
        this.liveId=liveId
        this.liveName=liveName
        this.liveStatus=liveStatus
        this.liveUrl=liveUrl
        this.liveCoverUrl=liveCoverUrl
        this.userId=userId
        this.userName=userName
        this.userAvatarUrl=userAvatarUrl
        this.gameId=gameId
        this.gameName=gameName
        this.gameCoverUrl=gameCoverUrl
    }

}
const input = ref("")
const pageNum = ref(1)
const pageCount = ref(1)
const liveName = ref("")
const liveListRef = ref([] as any[])
function getLiveList(){
    let formdata = new FormData()
    formdata.append("userToken",cookies.get("userToken"))
    formdata.append("pageNum",pageNum.value.toString())
    formdata.append("pageSize","10")
    formdata.append("liveName",liveName.value)
    axios.post("http://localhost:8080/adminGetLiveList",formdata).then(resp=>{
        liveListRef.value = []
        for(let i  = 0 ; i < resp.data.data.pageInfo.list.length ; i++){
            let liveListClass = new LiveListClass(
                resp.data.data.pageInfo.list[i].liveId,
                resp.data.data.pageInfo.list[i].liveDescription,
                resp.data.data.pageInfo.list[i].liveStatus,
                resp.data.data.pageInfo.list[i].liveUrl,
                resp.data.data.pageInfo.list[i].liveCoverUrl,
                resp.data.data.users[i].userId,
                resp.data.data.users[i].userName,
                resp.data.data.users[i].userAvatarUrl,
                resp.data.data.games[i].gameId,
                resp.data.data.games[i].gameName,
                resp.data.data.games[i].gameCoverUrl
            )
            liveListRef.value.push(liveListClass)
        }
        
    })
}

function searchLive(){
    liveName.value = input.value
    getLiveList()
}

watch(pageNum,()=>{
    getLiveList()
})
onMounted(()=>{
    getLiveList()
})

const showPreviewModal = ref(false)
const selectedLiveUrl = ref("")
function previewLive(liveUrl:string){
    selectedLiveUrl.value = liveUrl
    showPreviewModal.value = true
}


function closeLiveRoom(roomId:number){
    const ws = ref()
    ws.value = new WebSocket("ws://localhost:8080/liveRoom/"+roomId.toString()+"/"+cookies.get("userToken")
    )
    ws.value.onopen = () => {
        ws.value.send("close")
    }
    ws.value.onmessage = (e:any) =>{
        if(e.data == "close"){
            ws.value.close()
            getLiveList()
        }
    }
}

function banUser(userId: number,roomId:number) {
    closeLiveRoom(roomId)
    axios.post("http://localhost:8080/banUser?userId=" + userId + "&userToken=" + cookies.get("userToken")).then(resp => {
        message.info(resp.data.msg)
    })

}
</script>