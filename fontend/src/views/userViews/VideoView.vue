<template>
    <div style="display: flex;align-items: center;">
        <n-avatar :src="gameCoverUrl" :size="60" round />
        <span style="font-size: xx-large;font-weight: bold;">
            {{ videoDesc }}
        </span>
    </div>
    <br>
    <div style="color: grey;display: flex;align-items: center;">
        作者：<n-avatar :src="userAvatarUrl" :size="16" />{{ userName }}&nbsp;&nbsp;
        时间：{{ videoUpTime }}

    </div>
    <br>

    <FlvPlayer :url="videoUrl" :type="'mp4'" :is-live="false" />
    <!-- <video controls width="250">
        <source :src="videoUrl" type="video/mp4" />
    </video> -->
    <br>
    <n-flex justify="end">
        <StarGreyIcon v-if="!isStar" :width="40" :height="40" @click="collectVideo()"></StarGreyIcon>
        <StarPinkIcon v-else :width="40" :height="40" @click="cancelCollectVideo()"></StarPinkIcon>
        <GreyLikeIcon v-if="!isLike" :width="40" :height="40" @click="likeVideo()"></GreyLikeIcon>
        <PinkLikeIcon v-else :width="40" :height="40" @click="cancelLikeVideo()"></PinkLikeIcon>
        {{ starNum }}
    </n-flex>
    <br>
    <n-input-group>
        <n-input placeholder="评论" v-model:value="videocommentText"></n-input>
        <n-button @click="commentVideo()">发送</n-button>
    </n-input-group>
    <br>
    <br>


    <VideocommentCard v-for="e in videoCommentList" :user-name="e.userName" :user-avatar-url="e.userAvatarUrl"
        :videocomment-text="e.videocommentText" :videocomment-time="e.videocommentTime"
        :videocomment-id="e.videocommentId" :videocomment-user-id="e.videocommentUserId"></VideocommentCard>
    <div><n-pagination v-model:page="pageNum" :page-count="pageCount" /></div>

</template>



<script setup lang="ts">
import GreyLikeIcon from '@/components/icons/GreyLikeIcon.vue'
import PinkLikeIcon from '@/components/icons/PinkLikeIcon.vue'
import StarGreyIcon from '@/components/icons/StarGreyIcon.vue';
import StarPinkIcon from '@/components/icons/StarPinkIcon.vue';
import FlvPlayer from '@/components/FlvPlayer.vue'
import { useRoute } from "vue-router"
const route = useRoute()
import { ref, onMounted, onBeforeMount, watch } from 'vue'
import axios from 'axios';
import { useUserStore } from '@/stores/user';
import { useMessage } from 'naive-ui';
import VideocommentCard from '@/components/VideoCommentCard.vue'
import formatDate from '@/util/DateFormate'
const message = useMessage()

const userStore = useUserStore()

const videoUrl = ref('')
const userAvatarUrl = ref('')
const userName = ref('')
const videoDesc = ref('')
const videoUpTime = ref('')
const gameCoverUrl = ref('')
const videocommentText = ref('')
const starNum = ref(0)
onBeforeMount(() => {
    axios.post("http://localhost:8080/viewVideo?videoId=" + route.params.videoId).then(resp => {
        videoUrl.value = resp.data.data.video.videoUrl
        videoDesc.value = resp.data.data.video.videoDescription
        videoUpTime.value = formatDate(resp.data.data.video.videoUpTime)

        userAvatarUrl.value = resp.data.data.user.userAvatarUrl
        userName.value = resp.data.data.user.userName

        gameCoverUrl.value = resp.data.data.game.gameCoverUrl
        starNum.value = resp.data.data.video.videoStarNumber

    })
})

function commentVideo() {
    let formdata = new FormData()
    formdata.append('videocommentVideoId', String(route.params.videoId))
    formdata.append('userToken', userStore.userToken)
    formdata.append('videocommentText', videocommentText.value)

    axios({
        url: 'http://localhost:8080/commentVideo',
        method: 'post',
        data: formdata,
        headers: { 'content-type': 'application/x-www-form-urlencoded' },
    }).then(resp => {
        console.log(resp.data)
        message.info(resp.data.msg)
        videocommentText.value = ''

// 重新获取评论列表
        videoCommentList.value = []
        axios.post("http://localhost:8080/getVideocommentList?videoId=" + route.params.videoId + "&pageNum=" + pageNum.value + "&pageSize=" + pageSize.value).then(resp => {
        for (let i = 0; i < resp.data.data.pageInfo.list.length; i++) {
            let videocomment = new VideoCommentCardClass(
                resp.data.data.users[i].userAvatarUrl,
                resp.data.data.users[i].userName,
                formatDate(resp.data.data.pageInfo.list[i].videocommentTime),
                resp.data.data.pageInfo.list[i].videocommentText,
                resp.data.data.pageInfo.list[i].videocommentUserId,
                resp.data.data.pageInfo.list[i].videocommentId
            )
            videoCommentList.value.push(videocomment)
            pageCount.value = resp.data.data.pageInfo.pages
        }
    })

    })
}

import { useCookies } from 'vue3-cookies';
const cookeies = useCookies()
// 
const isLike = ref(false)
const isStar = ref(false)
const videoLikeList = ref([])
function getVideoLiketList() {
    axios.post("http://localhost:8080/getUserVideostarList?userToken=" + cookeies.cookies.get("userToken")).then(resp => {
        videoLikeList.value = resp.data.data
        for (let i = 0; i < resp.data.data.length; i++) {
            if (route.params.videoId == resp.data.data[i].videostarVideoId) {
                isLike.value = true
                break;
            }
        }
    })
}
function likeVideo() {
    axios.post("http://localhost:8080/insertVideostar?userToken=" + cookeies.cookies.get("userToken") + "&videoId=" + route.params.videoId).then(resp => {
        if (resp.data.msg == "成功") {
            isLike.value = true
            starNum.value++
        }
    })
}
function cancelLikeVideo() {
    axios.post("http://localhost:8080/cancelVideostar?userToken=" + cookeies.cookies.get("userToken") + "&videoId=" + route.params.videoId).then(resp => {
        if (resp.data.msg == "成功") {
            isLike.value = false
            starNum.value--
        }
    })

}
function collectVideo() {
    axios.post("http://localhost:8080/collectVideo?userToken=" + cookeies.cookies.get("userToken") + "&videoId=" + route.params.videoId).then(resp => {
        if (resp.data.msg == "成功") {
            isStar.value = true
        }
    })
}
function cancelCollectVideo() {
    axios.post("http://localhost:8080/cancelCollectVideo?userToken=" + cookeies.cookies.get("userToken") + "&videoId=" + route.params.videoId).then(resp => {
        if (resp.data.msg == "成功") {
            isStar.value = false
        }
    })
}
function getUerCollectVideoList() {
    axios.post("http://localhost:8080/getCollectVideoList?userToken=" + cookeies.cookies.get("userToken")).then(resp => {
        for (let i = 0; i < resp.data.data.length; i++) {
            if (route.params.videoId == resp.data.data[i].videocollectVideoId) {
                isStar.value = true
                break;
            }
        }
    })
}


class VideoCommentCardClass {
    userAvatarUrl: String | undefined
    userName: String | undefined
    videocommentTime: String | undefined
    videocommentText: String | undefined
    videocommentUserId: Number | undefined
    videocommentId: Number | undefined
    constructor(
        userAvatarUrl: String,
        userName: String,
        videocommentTime: String,
        videocommentText: String,
        videocommentUserId: Number,
        videocommentId: Number
    ) {
        this.userAvatarUrl = userAvatarUrl
        this.userName = userName
        this.videocommentId = videocommentId
        this.videocommentTime = videocommentTime
        this.videocommentText = videocommentText
        this.videocommentUserId = videocommentUserId
    }
}
const videoCommentList = ref([] as any[])
const pageSize = ref(10)
const pageNum = ref(1)
const pageCount = ref()
onMounted(() => {
    getVideoLiketList()
    getUerCollectVideoList()
    axios.post("http://localhost:8080/getVideocommentList?videoId=" + route.params.videoId + "&pageNum=" + pageNum.value + "&pageSize=" + pageSize.value).then(resp => {
        for (let i = 0; i < resp.data.data.pageInfo.list.length; i++) {
            let videocomment = new VideoCommentCardClass(
                resp.data.data.users[i].userAvatarUrl,
                resp.data.data.users[i].userName,
                formatDate(resp.data.data.pageInfo.list[i].videocommentTime),
                resp.data.data.pageInfo.list[i].videocommentText,
                resp.data.data.pageInfo.list[i].videocommentUserId,
                resp.data.data.pageInfo.list[i].videocommentId
            )
            videoCommentList.value.push(videocomment)
            pageCount.value = resp.data.data.pageInfo.pages
        }
    })
})
watch(pageNum, () => {
    videoCommentList.value = []
    axios.post("http://localhost:8080/getVideocommentList?videoId=" + route.params.videoId + "&pageNum=" + pageNum.value + "&pageSize=" + pageSize.value).then(resp => {
        for (let i = 0; i < resp.data.data.pageInfo.list.length; i++) {
            let videocomment = new VideoCommentCardClass(
                resp.data.data.users[i].userAvatarUrl,
                resp.data.data.users[i].userName,
                formatDate(resp.data.data.pageInfo.list[i].videocommentTime),
                resp.data.data.pageInfo.list[i].videocommentText,
                resp.data.data.pageInfo.list[i].videocommentUserId,
                resp.data.data.pageInfo.list[i].videocommentId
            )
            videoCommentList.value.push(videocomment)
            pageCount.value = resp.data.data.pageInfo.pages
        }
    })
})

</script>



<style scoped></style>