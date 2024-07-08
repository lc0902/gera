<template>
    <h1>
        评论管理

    </h1>
    <n-tabs type="line" animated>
        <n-tab-pane name="videoComment" tab="视频">
            <n-input-group>
                <n-input placeholder="请输入评论内容（模糊查询）" v-model:value="input1" @keyup.enter="search1()"></n-input>
                <n-button @click="search1()">搜索</n-button>
            </n-input-group>
            <n-table>
                <tr>
                    <th>
                        id
                    </th>
                    <th>
                        用户
                    </th>
                    <th>
                        评论内容
                    </th>
                    <th>
                        所属视频
                    </th>
                    <th>
                        时间
                    </th>
                    <th>
                        操作
                    </th>
                </tr>
                <tbody>
                    <tr v-for="i in videoCommentListRef">
                        <td>
                            {{ i.videoCommentId }}
                        </td>
                        <td>
                            <n-avatar :src="i.userAvatarUrl" />
                            {{ i.userName }}
                        </td>
                        <td>
                            {{ i.commentText }}
                        </td>
                        <td>
                            <n-avatar :src="i.videoCoverUrl" :size="80" />
                        </td>
                        <td>
                            {{ i.commentTime }}
                        </td>
                        <td>
                            <n-button @click="adminDeleteVideoComment(i.videoCommentId)">删除</n-button>
                            <n-button @click="banUser(i.userId)">封禁用户</n-button>
                        </td>
                    </tr>
                </tbody>
            </n-table>

            <n-pagination v-model:page="pageNum1" :page-count="pageCount1" />

        </n-tab-pane>
        <n-tab-pane name="postComment" tab="社区">
            <n-input-group>
                <n-input placeholder="请输入评论内容（模糊查询）" v-model:value="input2" @keyup.enter="search2()"></n-input>
                <n-button @click="search2()">搜索</n-button>
            </n-input-group>
            <n-table>
                <tr>
                    <th>
                        id
                    </th>
                    <th>
                        用户
                    </th>
                    <th>
                        评论内容
                    </th>
                    <th>
                        所属文章
                    </th>
                    <th>
                        时间
                    </th>
                    <th>
                        操作
                    </th>
                </tr>
                <tbody>
                    <tr v-for="i in postCommentListRef">
                        <td>
                            {{ i.postCommentId }}
                        </td>
                        <td>
                            <n-avatar :src="i.userAvatarUrl" />
                            {{ i.userName }}
                        </td>
                        <td>
                            {{ i.commentText }}
                        </td>
                        <td>
                            {{ i.postTitle }}
                        </td>
                        <td>
                            {{ i.commentTime }}
                        </td>
                        <td>
                            <n-button @click="adminDeletePostComment(i.postCommentId)">删除</n-button>
                            <n-button @click="banUser(i.userId)">封禁用户</n-button>
                        </td>
                    </tr>
                </tbody>
            </n-table>
            <n-pagination v-model:page="pageNum2" :page-count="pageCount2" />

        </n-tab-pane>
    </n-tabs>
</template>

<script setup lang="ts">
import axios from 'axios';
import { useCookies } from 'vue3-cookies';
const cookies = useCookies().cookies
import { onMounted, ref, watch } from 'vue'
import { useMessage } from 'naive-ui';
const message = useMessage()










class VideoCommentClass {
    videoCommentId: number
    userId: number
    userName: string
    userAvatarUrl: string
    commentText: string
    videoId: number
    videoCoverUrl: string
    commentTime: string
    constructor(videoCommentId: number, userId: number, userName: string, userAvatarUrl: string, commentText: string, videoId: number, videoCoverUrl: string, commentTime: string) {
        this.videoCommentId = videoCommentId
        this.userId = userId
        this.userName = userName
        this.userAvatarUrl = userAvatarUrl
        this.commentText = commentText
        this.videoId = videoId
        this.videoCoverUrl = videoCoverUrl
        this.commentTime = commentTime
    }
}
const videoCommentListRef = ref([] as any[])
const pageNum1 = ref(1)
const pageCount1 = ref(1)
const content = ref("")
function adminGetVideoCommentList() {
    videoCommentListRef.value = []
    let formdata = new FormData()
    formdata.append("userToken", cookies.get("userToken"))
    formdata.append("pageNum", pageNum1.value.toString())
    formdata.append("pageSize", "10")
    formdata.append("content", content.value)
    axios.post("http://localhost:8080/adminGetVideoCommentList", formdata).then(res => {
        pageCount1.value = res.data.data.pageInfo.pages
        for (let i = 0; i < res.data.data.pageInfo.list.length; i++) {
            let videoCommentClass = new VideoCommentClass(
                res.data.data.pageInfo.list[i].videocommentId,
                res.data.data.users[i].userId,
                res.data.data.users[i].userName,
                res.data.data.users[i].userAvatarUrl,
                res.data.data.pageInfo.list[i].videocommentText,
                res.data.data.videos[i].videoId,
                res.data.data.videos[i].videoCoverUrl,
                res.data.data.pageInfo.list[i].videocommentTime
            )
            videoCommentListRef.value.push(videoCommentClass)
        }
    })
}
const input1 = ref("")
function search1() {
    content.value = input1.value
    adminGetVideoCommentList()
}
watch(pageNum1, () => {
    adminGetVideoCommentList()
})

function adminDeleteVideoComment(videoCommentID: number) {
    axios.post("http://localhost:8080/adminDeleteVideoComment?userToken=" + cookies.get("userToken") + "&commentId=" + videoCommentID).then(res => {
        message.info(res.data.msg)
        adminGetVideoCommentList()
    })
}
function banUser(userId: number) {
    axios.post("http://localhost:8080/banUser?userId=" + userId + "&userToken=" + cookies.get("userToken")).then(resp => {
        message.info(resp.data.msg)
    })

}

class PostCommentClass {
    postCommentId: number
    userId: number
    userName: string
    userAvatarUrl: string
    commentText: string
    postId: number
    postTitle: string
    commentTime: string
    constructor(postCommentId: number, userId: number, userName: string, userAvatarUrl: string, commentText: string, postId: number, postTitle: string, commentTime: string) {
        this.postCommentId = postCommentId
        this.userId = userId
        this.userName = userName
        this.userAvatarUrl = userAvatarUrl
        this.commentText = commentText
        this.postId = postId
        this.postTitle = postTitle
        this.commentTime = commentTime
    }
}

const postCommentListRef = ref([] as any[])
const pageNum2 = ref(1)
const pageCount2 = ref(1)
const content2 = ref("")
function adminGetPostCommentList() {
    postCommentListRef.value = []
    let formdata = new FormData()
    formdata.append("userToken", cookies.get("userToken"))
    formdata.append("pageNum", pageNum2.value.toString())
    formdata.append("pageSize", "10")
    formdata.append("content", content2.value)
    axios.post("http://localhost:8080/adminGetPostCommentList", formdata).then(resp => {
        pageCount2.value = resp.data.data.pageInfo.pages
        for (let i = 0; i < resp.data.data.pageInfo.list.length; i++) {
            let postCommentClass = new PostCommentClass(
                resp.data.data.pageInfo.list[i].postcommentId,
                resp.data.data.users[i].userId,
                resp.data.data.users[i].userName,
                resp.data.data.users[i].userAvatarUrl,
                resp.data.data.pageInfo.list[i].postcommentText,
                resp.data.data.posts[i].postId,
                resp.data.data.posts[i].postTitle,
                resp.data.data.pageInfo.list[i].postcommentTime
            )
            postCommentListRef.value.push(postCommentClass)
        }
    })
}

watch(pageNum2, () => {
    adminGetPostCommentList()
})
const input2 = ref("")
function search2() {
    content2.value = input2.value
    adminGetPostCommentList()
}

function adminDeletePostComment(postCommentID: number) {
    axios.post("http://localhost:8080/adminDeletePostComment?userToken=" + cookies.get("userToken") + "&commentId=" + postCommentID).then(resp => {
        message.info(resp.data.msg)
        adminGetPostCommentList()
    })
}

onMounted(() => {
    adminGetVideoCommentList()
    adminGetPostCommentList()
})
</script>
