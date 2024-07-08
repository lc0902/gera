import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(createPinia())
app.use(router)

// naive-ui
import naive from 'naive-ui'
app.use(naive)
// element-plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
app.use(ElementPlus)
// vue-cookies
import VueCookies from 'vue3-cookies'
app.use(VueCookies)
// mavon-editor
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css' // 样式记得导入喔
app.use(mavonEditor)

app.mount('#app')
