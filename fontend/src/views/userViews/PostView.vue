<template>
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

    <n-flex justify="end">

        <StarGreyIcon v-if="!isStar" :width="40" :height="40" @click="collectPost()"></StarGreyIcon>
        <StarPinkIcon v-else :width="40" :height="40" @click="cancleCollect()"></StarPinkIcon>
        <GreyLikeIcon v-if="!isLike" :width="40" :height="40" @click="star()"></GreyLikeIcon>
        <PinkLikeIcon v-else :width="40" :height="40" @click="cancleStar()"></PinkLikeIcon>
        {{ starNum }}

    </n-flex>

    <br>
    <n-input-group>
        <n-input placeholder="评论" @focus="commentFocus = true" v-model:value="postcommentText"></n-input>
        <n-button @click="sendComment()">
            <PaperPlanIcon :width="20" :height="20" />
        </n-button>
    </n-input-group>


    <br>
    <br>
    <br>
    <br>


    <CommentCard v-for="i in postcommentList" :userAvatarUrl="i.userAvatarUrl" :userName="i.userName"
        :postcommentText="i.postcommentText" :postcommentUserId="i.postcommentUserId"
        :postcommentTime="i.postcommentTime" :postcomment-id="i.postcommentId" />
    <div><n-pagination v-model:page="page" :page-count="pageCount" /></div>
    <br>
    <br>
    <br>
    <br>




</template>



<script setup lang="ts">
import GreyLikeIcon from '@/components/icons/GreyLikeIcon.vue'
import PinkLikeIcon from '@/components/icons/PinkLikeIcon.vue'
import StarGreyIcon from '@/components/icons/StarGreyIcon.vue';
import StarPinkIcon from '@/components/icons/StarPinkIcon.vue';
import CommentCard from '@/components/CommentCard.vue';
import PaperPlanIcon from '@/components/icons/PaperPlanIcon.vue'
import axios from 'axios';
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router';
import { useUserStore } from '@/stores/user';
import { useMessage } from 'naive-ui';
import { useCookies } from 'vue3-cookies';
const cookeies = useCookies()
const message = useMessage()
const postText = ref('# Hello world')
const route = useRoute()
const postTitle = ref('')
const userAvatarUrl = ref('')
const userName = ref('')
const postTime = ref('')
const gameCoverUrl = ref('')
const gameName = ref('')
const commentFocus = ref(false)


class PostcommentCardData {
    userName: String | undefined;
    userAvatarUrl: String | undefined;
    postcommentText: String | undefined;
    postcommentTime: String | undefined;
    postcommentUserId: Number | undefined;
    postcommentId: Number | undefined
    constructor(userName: string,
        userAvatarUrl: string,
        postcommentText: string,
        postcommentTime: string,
        postcommentUserId: number,
        postcommentId: number) {
        this.postcommentText = postcommentText
        this.postcommentTime = postcommentTime
        this.userAvatarUrl = userAvatarUrl
        this.userName = userName
        this.postcommentUserId = postcommentUserId
        this.postcommentId = postcommentId
    }
}

const postcommentList = ref([] as any[])

const page = ref(1)
const pageCount = ref(12)


// 是否已点赞
const isLike = ref(false)

// 是否已收藏
const isStar = ref(false)
// 

function collectPost() {
    axios.post("http://localhost:8080/collectPost?userToken=" + cookeies.cookies.get("userToken") + "&postId=" + route.params.postId).then(resp => {
        console.log(resp.data)

        if (resp.data.msg == "成功") {
            isStar.value = true
        }
    })
}
function cancleCollect() {
    axios.post("http://localhost:8080/cancelCollectPost?userToken=" + cookeies.cookies.get("userToken") + "&postId=" + route.params.postId).then(resp => {
        console.log(resp.data)
        if (resp.data.msg == "成功") {
            isStar.value = false
        }
    })

}
// 获取收藏列表
const collectList = ref([])

function getCollectList() {
    axios.post("http://localhost:8080/getCollectPostList?userToken=" + cookeies.cookies.get("userToken")).then(resp => {
        console.log(resp.data)
        collectList.value = resp.data.data
        for (let i = 0; i < resp.data.data.length; i++) {
            if (route.params.postId == resp.data.data[i].postcollectPostId) {
                isStar.value = true
                break;
            }
        }
    })
}


// 获取用户点赞列表
const poststarList = ref([])
function getUserPoststarList() {
    axios.post("http://localhost:8080/getUserPoststarList?userToken=" + cookeies.cookies.get("userToken")).then(resp => {
        poststarList.value = resp.data.data
        for (let i = 0; i < resp.data.data.length; i++) {
            if (route.params.postId == resp.data.data[i].poststarPostId) {
                isLike.value = true
                break;
            }
        }
    })
}

function cancleStar() {
    axios.post("http://localhost:8080/cancelPoststar?userToken=" + userStroe.userToken + "&postId=" + route.params.postId).then(resp => {
        // console.log(resp.data)
        if (resp.data.msg == "成功") {
            isLike.value = false
            starNum.value--
        }

    })
}

const starNum = ref(0)


onMounted(() => {
    getUserPoststarList()
    getCollectList()
    // 获取文章
    axios.post("http://localhost:8080/viewPost?postId=" + route.params.postId).then(resp => {
        postTitle.value = resp.data.data.post.postTitle
        postText.value = resp.data.data.post.postText
        postTime.value = resp.data.data.post.postTime
        userAvatarUrl.value = resp.data.data.user.userAvatarUrl
        userName.value = resp.data.data.user.userName
        gameCoverUrl.value = resp.data.data.game.gameCoverUrl
        gameName.value = resp.data.data.game.gameName
        starNum.value = resp.data.data.post.postStarNum
    })
    // 获取评论
    postcommentList.value = []
    axios.post("http://localhost:8080/getPostcommentList?postcommentPostId=" + route.params.postId + "&pageNum=" + page.value + "&pageSize=12")
        .then(resp => {
            for (let i = 0; i < resp.data.data.users.length; i++) {
                let t = new PostcommentCardData(resp.data.data.users[i].userName, resp.data.data.users[i].userAvatarUrl, resp.data.data.pageInfo.list[i].postcommentText, resp.data.data.pageInfo.list[i].postcommentTime, resp.data.data.users[i].userId, resp.data.data.pageInfo.list[i].postcommentId)
                postcommentList.value.push(t)
            }
            page.value = resp.data.data.pageInfo.pageNum
            pageCount.value = resp.data.data.pageInfo.pages
        })
})


function getPostcommentList() {
    // 获取评论
    postcommentList.value = []
    axios.post("http://localhost:8080/getPostcommentList?postcommentPostId=" + route.params.postId + "&pageNum=" + page.value + "&pageSize=12")
        .then(resp => {
            console.log(resp.data)
            for (let i = 0; i < resp.data.data.users.length; i++) {
                let t = new PostcommentCardData(resp.data.data.users[i].userName, resp.data.data.users[i].userAvatarUrl, resp.data.data.pageInfo.list[i].postcommentText, resp.data.data.pageInfo.list[i].postcommentTime, resp.data.data.users[i].userId, resp.data.data.pageInfo.list[i].postcommentId)
                postcommentList.value.push(t)
            }
            page.value = resp.data.data.pageInfo.pageNum
            pageCount.value = resp.data.data.pageInfo.pages
        })
}

watch(page, () => {
    getPostcommentList()
})


const postcommentText = ref('')
const userStroe = useUserStore()
function sendComment() {
    axios.post("http://localhost:8080/commentPost?postcommentText=" + postcommentText.value + "&postcommentPostId=" + route.params.postId + "&userToken=" + userStroe.userToken)
        .then(resp => {
            message.info(resp.data.msg)
            getPostcommentList()
            postcommentText.value = ''
        })
}


function star() {
    axios.post("http://localhost:8080/insertPoststar?userToken=" + userStroe.userToken + "&postId=" + route.params.postId).then(resp => {
        if (resp.data.msg == "成功") {
            isLike.value = true
            starNum.value++
        }
    })
}
</script>



<style scoped></style>