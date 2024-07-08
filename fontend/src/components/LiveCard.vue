<template>
  <n-card @click="viewLive()">
    <template #cover>
      <img :src="liveCoverUrl" :height="200" :width="200">
    </template>
    <n-ellipsis style="max-width: 100px">
    {{ liveDescription }}
    </n-ellipsis>
    <div style="display: flex;align-items: center; color:grey">
      <LiveIcon :width="20" :height="20" /> {{ liveGameName }}&nbsp;&nbsp;&nbsp;
      <n-avatar :src="liveUserAvatarUrl" :size="16"></n-avatar>&nbsp;{{ liveUserName }}

    </div>

  </n-card>
</template>



<script setup lang="ts">
import LiveIcon from './icons/LiveIcon.vue';
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router';

const router = useRouter()

const props = defineProps({
  liveCoverUrl: String,
  liveDescription: String,
  liveUserAvatarUrl: String,
  liveUserName: String,
  liveGameName: String,
  liveId: Number
})

function viewLive(){
  router.push('/userView/liveView/'+props.liveId)
}

</script>



<style scoped>
.n-card {
  max-width: 200px;
}
</style>