import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', () => {
    const userId = ref<Number | null>(null)
    const userName = ref('')
    const userEmail = ref('')
    const userGender = ref('')
    const userSignature = ref('')
    const userAvatarUrl = ref('http://localhost/pic/avatar/avatar_default.png')
    const userToken = ref('')
    const userRoleId = ref<Number | null>(null)
    const userStatus = ref<Number | null>(null)
    //客户登陆状态
    const loginStatus = ref(false)


    return {
        userId,
        userName,
        userEmail,
        userGender,
        userSignature,
        userAvatarUrl,
        userToken,
        userRoleId,
        userStatus,
        loginStatus
    }
})
