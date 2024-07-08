<template>

    <n-modal v-model:show="showModal">
        <div style="background-color: white;width: 1000px; padding: 20px;">

            <div style="display: flex;align-items: center;">
                <n-avatar :src="gameCoverUrl" :size="70" round></n-avatar>&nbsp;&nbsp;&nbsp;&nbsp;
                <p style="font-size: xx-large; font-weight: bold;color: #8a2be2;">{{ gameName }}</p>
            </div>

            <h1>{{ postTitle }}</h1>
            <div style="color: grey;display: flex;align-items: center;">
                作者：
                <n-avatar :src="userAvatarUrl" :size="16"></n-avatar>&nbsp;&nbsp;{{ userName }}
                &nbsp;&nbsp;&nbsp;
                时间： {{ postTime }}

            </div>



            <br>
            <mavon-editor :toolbarsFlag="false" :subfield="false" :defaultOpen="'preview'" v-model="postText" />
            <br>

        </div>
    </n-modal>
    <h1>发帖管理</h1>

    <n-input-group>
        <n-input v-model:value="input1" placeholder="输入标题"></n-input>
        <n-button type="primary" @click="search">搜索</n-button>
    </n-input-group>
    <br>
    <n-table>
        <tr>
            <th>Id</th>
            <th>标题</th>
            <th>发布时间</th>
            <th>所属板块</th>
            <th>所属用户</th>
            <th>操作</th>
        </tr>
        <tbody>
            <tr v-for="i in postListRef">
                <td>{{ i.postId }}</td>
                <td>{{ i.postTitle }}</td>
                <td>{{ i.postTime }}</td>
                <td>
                    <div style="display: flex; align-items: center;">
                        <n-avatar :src="i.postGameCoverUrl" :size="80"></n-avatar>
                        {{ i.postGameName }}
                    </div>
                </td>
                <td>
                    <div style="display: flex; align-items: center;">
                        <n-avatar :src="i.postUserAvatarUrl" :size="80" round></n-avatar>
                        {{ i.postUserName }}
                    </div>
                </td>
                <td>
                    <n-button @click="previewPost(i.postId)">预览</n-button>
                    <n-button @click="adminDeletePost(i.postId)">删除</n-button>
                    <n-button @click="banUser(i.postUserId)">封禁用户</n-button>
                </td>
            </tr>
        </tbody>
    </n-table>
    <br>
    <n-pagination v-model:page="pageNum" :page-count="pageCount" />

</template>



<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import { useUserStore } from '@/stores/user';
import { formDark } from 'naive-ui';
import axios from 'axios';
const userStore = useUserStore()







class PostListClass {
    postId: number
    postTitle: string
    postUserId: number
    postUserName: string
    postUserAvatarUrl: string
    postGameId: number
    postGameName: string
    postGameCoverUrl: string
    postTime: string
    constructor(postId: number, postTitle: string, postUserId: number, postUserName: string, postUserAvatarUrl: string, postGameId: number, postGameName: string, postGameCoverUrl: string, postTime: string) {
        this.postId = postId
        this.postTitle = postTitle
        this.postUserId = postUserId
        this.postUserName = postUserName
        this.postUserAvatarUrl = postUserAvatarUrl
        this.postGameId = postGameId
        this.postGameName = postGameName
        this.postGameCoverUrl = postGameCoverUrl
        this.postTime = postTime
    }
}
const postListRef = ref([] as any[])
const pageNum = ref(1)
const postTitle = ref("")
const pageCount = ref()
function getPostList() {
    postListRef.value = []
    let formdata = new FormData()
    formdata.append("userToken", userStore.userToken)
    formdata.append("pageNum", pageNum.value.toString())
    formdata.append("pageSize", "10")
    formdata.append("postTitle", postTitle.value)
    axios.post("http://localhost:8080/adminGetPostList", formdata).then(resp => {
        // console.log(resp);
        pageCount.value = resp.data.data.pageInfo.pages
        const postList = ref()
        postList.value = resp.data.data
        // console.log(postList);

        for (let i = 0; i < postList.value.pageInfo.list.length; i++) {
            let postListClass = new PostListClass(
                postList.value.pageInfo.list[i].postId,
                postList.value.pageInfo.list[i].postTitle,
                postList.value.users[i].userId,
                postList.value.users[i].userName,
                postList.value.users[i].userAvatarUrl,
                postList.value.games[i].gameId,
                postList.value.games[i].gameName,
                postList.value.games[i].gameCoverUrl,
                postList.value.pageInfo.list[i].postTime
            )
            postListRef.value.push(postListClass)
        }

    })
    // console.log(postListRef.value);
}
const input1 = ref("")
function search() {
    postTitle.value = input1.value
    getPostList()
}

watch(pageNum, () => {
    getPostList()
})

onMounted(() => {
    getPostList()
}
)


function adminDeletePost(postId: number) {
    axios.post("http://localhost:8080/adminDeletePost?postId=" + postId.toString() + "&userToken=" + userStore.userToken).then(resp => {
        console.log(resp);
        getPostList()
    })

}

const showModal = ref(false)
const postText = ref("")
const postTime = ref("")
const userAvatarUrl = ref("")
const userName = ref("")
const gameCoverUrl = ref("")
const gameName = ref("")
const starNum = ref(0)
function previewPost(postId: number) {
    showModal.value = true
    axios.post("http://localhost:8080/viewPost?postId=" + postId.toString()).then(resp => {
        postTitle.value = resp.data.data.post.postTitle
        postText.value = resp.data.data.post.postText
        postTime.value = resp.data.data.post.postTime
        userAvatarUrl.value = resp.data.data.user.userAvatarUrl
        userName.value = resp.data.data.user.userName
        gameCoverUrl.value = resp.data.data.game.gameCoverUrl
        gameName.value = resp.data.data.game.gameName
        starNum.value = resp.data.data.post.postStarNum
    })
}
function banUser(userId:number){
    axios.post("http://localhost:8080/banUser?userId="+userId+"&userToken="+userStore.userToken).then(resp=>{
        console.log(resp.data)
    })
    
}

</script>



<style scoped></style>