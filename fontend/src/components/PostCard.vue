<template>
  <div>
    <n-thing content-indented @click="viewPost(postId)" >
      <template #avatar>
        <n-avatar :src="gameCoverUrl" :size="110">
        </n-avatar>
      </template>
      <template #header>
        <span style="max-width: 1200px;font-weight: bold;font-size: x-large;">
          {{ postTitle }}
        </span>
      </template>
      <template #header-extra>
        <n-avatar :src="userAvatarUrl" :size="40"></n-avatar>
      </template>

      <n-ellipsis style="max-width: 1200px;font-weight: bold;font-size: small;" :tooltip=false>{{ postText
        }}</n-ellipsis>

      <template #footer>
        <span style="color: grey;">
          时间:{{ postTime }} &nbsp;&nbsp;&nbsp;点赞数:{{ postStarNum }} &nbsp;&nbsp;&nbsp;作者: {{ userName }}
          &nbsp;&nbsp;&nbsp;游戏:{{ gameName }}
        </span>
      </template>
    </n-thing>
    <hr>
  </div>

</template>



<script setup lang="ts">
import { useRouter } from 'vue-router';
const router = useRouter()
const props = defineProps({
  gameCoverUrl: String,
  gameName: String,
  postTitle: String,
  postText: String,
  userAvatarUrl: String,
  postTime: String,
  postStarNum: Number,
  userName: String,
  postId: Number
})


function viewPost(id:number|undefined){
  // console.log(id)
  router.push("/userView/postView/"+id)

}
</script>



<style scoped></style>