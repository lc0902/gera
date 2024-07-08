<template>

    <n-layout has-sider>
      <n-layout-sider content-style="padding: 24px;">
        <n-menu :options="menuOptions"  />
        
      </n-layout-sider>
      <n-layout>
        <!-- <n-layout-header>颐和园路</n-layout-header> -->
        <n-layout-content content-style="padding-right: 24px;">
          <router-view></router-view>
        </n-layout-content >
        <!-- <n-layout-footer>成府路</n-layout-footer> -->
      </n-layout>
    </n-layout>
</template>



<script setup lang="ts">
import LiveIcon2 from '@/components/icons/LiveIcon2.vue'
import ApplyIcon from '@/components/icons/ApplyIcon.vue'
import CommentIcon from '@/components/icons/CommentIcon.vue'
import VideoIcon2 from '@/components/icons/VideoIcon2.vue'
import PostIcon from '@/components/icons/PostIcon.vue'
import VideoIcon from '@/components/icons/VideoIcon.vue'
import BoardIcon from '@/components/icons/BoardIcon.vue'
import UserIcon from '@/components/icons/UserIcon.vue'
import { defineComponent, h } from 'vue'
import type { Component } from 'vue'
import { NIcon, useMessage } from 'naive-ui'
import type { MenuOption } from 'naive-ui'
import { RouterLink } from 'vue-router'
import HomeIcon from '@/components/icons/HomeIcon.vue'
const message = useMessage()
import axios from 'axios'

// function handleUpdateValue(key: string, item: MenuOption) {
//     message.info('[onUpdate:value]: ' + JSON.stringify(key))
//     message.info('[onUpdate:value]: ' + JSON.stringify(item))
// }

function renderIcon(icon: Component) {
    return () => h(NIcon, null, { default: () => h(icon) })
}
const menuOptions: MenuOption[] = [
    {
        label: () =>
            h(
                RouterLink,
                {
                    to: '/'
                },
                { default: () => '返回用户界面' }
            ),
        key: 'go-back-user-view',
        icon:renderIcon(HomeIcon)
    },{
        label: ()=>
        h(
            RouterLink,
            {
                to: '/adminView/userControl'
            },
            { default: () => '用户管理(Demon)' }),
        key: 'user-manage',
        icon:renderIcon(UserIcon)
    },{
        label: ()=>
        h(
            RouterLink,
            {
                to: '/adminView/boardControl'
            },
            { default: () => '板块管理(Demon)' }),
        key: 'board-manage',
        icon:renderIcon(BoardIcon)
    },{
        label: ()=>
        h(
            RouterLink,
            {
                to: '/adminView/applyControl'
            },
            { default: () => '申请处理(Demon)' }),
        key: 'apply-manage',
        icon:renderIcon(ApplyIcon)
    },{
        label: ()=>
        h(
            RouterLink,
            {
                to: '/adminView/videoControl'
            },
            { default: () => '视频管理' }),
        key: 'video-manage',
        icon:renderIcon(VideoIcon2)
    },{
        label: ()=>
        h(
            RouterLink,
            {
                to: '/adminView/postControl'
            },
            { default: () => '发帖管理' }),
        key: 'post-manage',
        icon:renderIcon(PostIcon)
    },{
        label: ()=>
        h(
            RouterLink,
            {
                to: '/adminView/commentControl'
            },
            { default: () => '评论管理' }),
        key: 'comment-manage',
        icon:renderIcon(CommentIcon)
    },{
        label: ()=>
        h(
            RouterLink,
            {
                to: '/adminView/liveControl'
            },
            { default: () => '直播管理' }),
        key: 'live-manage',
        icon:renderIcon(LiveIcon2)
    }
    
]




</script>



<style scoped></style>