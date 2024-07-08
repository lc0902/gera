<template>
    <h1>申请管理</h1>
    <n-table>
        <tr>
            <th>
                id
            </th>
            <th>
                用户
            </th>
            <th>
                申请类型
            </th>
            <th>
                申请理由
            </th>
            <th>
                操作
            </th>
            <th>状态</th>
            
 
        </tr>
        <tbody>
            <tr v-for="i in applyListRef">
                <td>
                    {{ i.applyId }}
                </td>
                <td>
                    <div style="display:flex;align-items: center;">
                        <n-avatar :src="i.userAvatarUrl" round :size="50"></n-avatar>
                        {{ i.userName }}
                    </div>
                </td>
                <td>
                    <span v-if="i.applyType==1">申请视频上传权限</span>
                    <span v-if="i.applyType==2">申请直播权限</span>
                    <span v-if="i.applyType==3">申诉</span>
                </td>
                <td>
                    {{ i.applyText }}
                </td>
                <td>
                    <n-button @click="dealApply(i.applyId,'1')">同意</n-button>
                    <n-button @click="dealApply(i.applyId,'0')">拒绝</n-button>

                </td>
                <td>
                    <span v-if="i.applyStatus==0">待处理</span>
                    <span v-if="i.applyStatus==1">已处理</span>
                </td>
            </tr>
        </tbody>
    </n-table>
    <n-pagination v-model:page="pageNum" :page-count="pageCount" />

</template>

<script setup lang="ts">
import axios from 'axios';
import { onMounted, ref, watch } from 'vue'
import { useCookies } from 'vue3-cookies';
const cookies = useCookies().cookies
import { useMessage } from 'naive-ui';
const message = useMessage()














class ApplyClass{
    applyId:number
    userId:number
    userName:string
    userAvatarUrl:string
    applyType:number
    applyText:number
    applyStatus:number
    constructor(applyId:number,userId:number,userName:string,userAvatarUrl:string,applyType:number,applyText:number,applyStatus:number){
        this.applyId = applyId
        this.userId = userId
        this.userName = userName
        this.userAvatarUrl = userAvatarUrl
        this.applyType = applyType
        this.applyText = applyText
        this.applyStatus = applyStatus
    }
}
const applyListRef = ref([] as any[])
const pageNum=ref(1)
const pageCount = ref(1)
function adminGetApplyList(){
    applyListRef.value = []
    let formdata  =new FormData()
    formdata.append("pageNum",pageNum.value.toString())
    formdata.append("pageSize","10")
    formdata.append("userToken",cookies.get("userToken"))
    axios.post("http://localhost:8080/adminGetApplyList",formdata).then(resp=>{
        for(let i = 0 ; i < resp.data.data.pageInfo.list.length ; i++){
            let applyClass = new ApplyClass(
                resp.data.data.pageInfo.list[i].applicationId,
                resp.data.data.users[i].userId,
                resp.data.data.users[i].userName,
                resp.data.data.users[i].userAvatarUrl,
                resp.data.data.pageInfo.list[i].applicationType,
                resp.data.data.pageInfo.list[i].applicationText,
                resp.data.data.pageInfo.list[i].applicationStatus
            )
            applyListRef.value.push(applyClass)
            pageCount.value = resp.data.data.pageInfo.pages
        }
        
    })
}
watch(pageNum,()=>{
    adminGetApplyList()
})
function dealApply(applyId:number,dealCode:string){
    axios.post("http://localhost:8080/adminDealApply?userToken="+cookies.get("userToken")+"&applyId="+applyId+"&dealCode="+dealCode).then(resp=>{
        message.info(resp.data.msg)
        adminGetApplyList()
    })
}
onMounted(()=>{
    adminGetApplyList()
}
)

</script>
