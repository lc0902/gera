<template>
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <n-result  title="您已到达世界的尽头，只有传说中的恶魔才能穿越这些边界" description="或许你该找点时间陪陪家人朋友" size="huge">
    <template #icon>
      <DeamonIcon></DeamonIcon>
    </template>
    <template #footer>
      <n-button @click="joy()">来局昆特牌？</n-button>
    </template>
  </n-result>
</template>

<script lang="ts" setup>
import { useMessage } from 'naive-ui'
import { useRouter, useRoute } from 'vue-router'
import DeamonIcon from '@/components/icons/DeamonIcon.vue'
const router = useRouter()
const message = useMessage()
function joy() {
  router.push({path:'/userView/home'})
  message.error("啊，我讨厌传送门")
}
</script>