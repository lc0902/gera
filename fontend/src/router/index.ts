import { createRouter, createWebHistory } from 'vue-router'
// userPage
import UserView from '@/views/UserView.vue'
import UserHomeView from '@/views/userViews/HomeView.vue'
import PersonalCenterView from '@/views/userViews/PersonalCenterView.vue'
import PostListView from '@/views/userViews/PostListView.vue'
import PostEditorView from '@/views/userViews/PostEditorView.vue'
import PostView from '../views/userViews/PostView.vue'
import LiveListView from '@/views/userViews/LiveListView.vue'
import VideoListView from '@/views/userViews/VideoListView.vue'
import VideoEditor from '@/views/userViews/VideoEditor.vue'
import VideoView from '@/views/userViews/VideoView.vue'
import LiveInfoView from '@/views/userViews/LiveInfoView.vue'
import LiveView from '@/views/userViews/LiveView.vue'
import GameView from '@/views/userViews/GameView.vue'
import PersonalCollectView from '@/views/userViews/PersonalCollect.vue'
import PersonalUpload from '@/views/userViews/PersonalUpload.vue'
import UserProtocol from '@/views/userViews/UserProtocol.vue'
import RetrievePassword from '@/views/userViews/RetrievePassword.vue'
import LiveUsage from '@/views/userViews/LiveUsage.vue'
import VoiceCommunicationView from '@/views/userViews/VoiceCommunicationView.vue'
import VoiceRoomView from '@/views/userViews/VoiceRoomView.vue'
// adminPage
import AdminView from '@/views/AdminView.vue'
import userControl from '@/views/adminView/userControl.vue'
// import adminControl from '@/views/adminView/adminControl.vue'
import BoardControl from '@/views/adminView/BoardControl.vue'
import VideoControl from '@/views/adminView/VideoControl.vue'
import PostControl from '@/views/adminView/PostControl.vue'
import CommentControl from '@/views/adminView/CommentControl.vue'
import ApplyControl from '@/views/adminView/ApplyControl.vue'
import LiveControl from '@/views/adminView/LiveControl.vue'





// ResultPage
import FZF from '@/views/result/404.vue'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'root',
      redirect: '/userView/home',
      children: [
        {
          path: 'userView',
          component: UserView,
          redirect: '/userView/home',
          children: [
            {
              path: 'home',
              component: UserHomeView
            },{
              path:'personalCenter',
              component:PersonalCenterView
            },{
              path:'postListView',
              component:PostListView
            },{
              path:'postEditorView',
              component:PostEditorView
            },{
              path:'postView/:postId',
              component:PostView
            },{
              path:'liveListView',
              component:LiveListView
            },{
              path:"videoListView",
              component:VideoListView
            },{
              path:"viedoEditor",
              component:VideoEditor
            },{
              path:'videoView/:videoId',
              component:VideoView
            },{
              path:'liveInfo',
              component:LiveInfoView
            },{
              path:'liveView/:liveId',
              component:LiveView
            },{
              path:'gameView/:gameId',
              component:GameView
            },{
              path:'personalCollectView',
              component:PersonalCollectView
            },{
              path:'personalUploadView',
              component:PersonalUpload
            },{
              path:'retrievePassword',
              component:RetrievePassword
            },{
              path:'liveUsage',
              component:LiveUsage
            },{
              path:'voiceCommunicationView',
              component:VoiceCommunicationView
            },{
              path:'voiceRoomView',
              component:VoiceRoomView
            }
          ]
        },
        {
          path: 'adminView',
          component: AdminView,
          children:[
            {
              path:'userControl',
              component:userControl
            },{
              path:'boardControl',
              component:BoardControl
            },{
              path:'videoControl',
              component:VideoControl
            },{
              path:'postControl',
              component:PostControl
            },{
              path:'commentControl',
              component:CommentControl
            },{
              path:'applyControl',
              component:ApplyControl
            },{
              path:'liveControl',
              component:LiveControl
            }
          ]
        },{
          path:'userProtocol',
          component:UserProtocol
        }
      ]
    },
    {
      path: '/404',
      name: '404',
      component: FZF
    },
    {
      path: '/:pathMatch(.*)',
      redirect: '/404'
    }
  ]
})

export default router
