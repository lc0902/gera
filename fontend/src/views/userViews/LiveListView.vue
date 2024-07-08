<template>
    <div v-if="userStore.userRoleId==3">
            <router-link to="/userView/liveInfo">
                <n-button>
                    我的直播
                </n-button>
            </router-link>
    </div>
    <div v-if="nothing">
        <n-empty description="暂无直播">
        </n-empty>
    </div>
    <n-flex justify="center" v-else>
        <LiveCard v-for="i in liveList" 
        :liveId="i.liveId"
        :live-cover-url="i.liveCoverUrl"
        :live-description="i.liveDescription"
        :live-user-avatar-url="i.liveUserAvatarUrl"
        :live-user-name="i.liveUserName"
        :live-game-name="i.liveGameName"
        />
    </n-flex>
    <div v-if="!nothing"> <n-pagination v-model:page="page" :page-count="pageCount" /></div>

</template>



<script setup lang="ts">
import LiveCard from '@/components/LiveCard.vue';
import { useRoute } from "vue-router"
const route = useRoute()
import { ref, onMounted, onBeforeMount, watch } from 'vue'
import axios from 'axios';
import { useUserStore } from '@/stores/user';
const userStore = useUserStore()
import { useMessage } from 'naive-ui';
import VideocommentCard from '@/components/VideoCommentCard.vue'
import formatDate from '@/util/DateFormate'
const message = useMessage()

const page = ref(1)
const pageCount = ref(1)
const liveDescription = ref('')
const nothing = ref(false)


class LiveCardClass {
    liveCoverUrl: String | undefined
    liveDescription: String | undefined
    liveUserAvatarUrl: String | undefined
    liveUserName: String | undefined
    liveGameName: String | undefined
    liveId:Number | undefined
    constructor(
        liveCoverUrl: string,
        liveDescription: string,
        liveUserAvatarUrl: string,
        liveUserName: string,
        liveGameName: string,
        liveId:number
    ) {
        this.liveCoverUrl = liveCoverUrl
        this.liveDescription = liveDescription
        this.liveUserAvatarUrl = liveUserAvatarUrl
        this.liveUserName = liveUserName
        this.liveGameName = liveGameName
        this.liveId = liveId
    }
}

const liveList = ref([] as any[])

function getLiveList() {
    axios.post("http://localhost:8080/getLiveList?pageNum=" + page.value + "&pageSize=12&liveDescription=" + liveDescription.value).then(resp => {
        liveList.value = []
        if (resp.data.data.pageInfo.list.length == 0) {
            nothing.value = true
        } else {
            nothing.value = false
            for(let i =0;i<resp.data.data.pageInfo.list.length;i++){
                liveList.value.push(new LiveCardClass(
                    resp.data.data.pageInfo.list[i].liveCoverUrl,
                    resp.data.data.pageInfo.list[i].liveDescription,
                    resp.data.data.users[i].userAvatarUrl,
                    resp.data.data.users[i].userName,
                    resp.data.data.games[i].gameName,
                    resp.data.data.pageInfo.list[i].liveId,
                ))
            }

        }
    })
}

onMounted(() => {
    getLiveList()
})

watch(page, () => {
    getLiveList()
})

</script>



<style scoped></style>