// store/auth.js
import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { getUserInfo, userLogout } from '@/api/UserApi.js'

export const useAuthStore = defineStore('auth', () => {
    // 状态
    const user = ref(null)
    const token = ref(null) // 注意：这里我们将 token 的初始值设为 null，让持久化插件去加载
    const isLoading = ref(false)

    // 计算属性
    const isLoggedIn = computed(() => !!token.value && !!user.value)

    // 初始化用户信息
    const initializeAuth = async () => {
        // initializeAuth 仍然是必要的，即使 token 被持久化了。
        // 因为 user 信息可能不是持久化的，或者需要根据 token 重新验证。
        if (token.value) {
            try {
                isLoading.value = true
                const response = await getUserInfo()
                if (response.success) {
                    user.value = response.data
                } else {
                    // Token无效,清除本地存储
                    clearAuth()
                }
            } catch (error) {
                console.error('Failed to fetch user info:', error)
                clearAuth()
            } finally {
                isLoading.value = false
            }
        }
    }

    // 登录
    const login = (userData, authToken) => {
        user.value = userData
        token.value = authToken
        // localStorage.setItem('token', authToken) // 这一行可以移除，因为插件会自动处理
    }

    // 登出
    const logout = async () => {
        try {
            if (token.value) {
                await userLogout()
            }
        } catch (error) {
            console.error('Logout API call failed:', error)
        } finally {
            clearAuth()
        }
    }

    // 清除认证信息
    const clearAuth = () => {
        user.value = null
        token.value = null
        // localStorage.removeItem('token') // 这一行可以移除，因为插件会自动处理
    }

    // 更新用户信息
    const updateUser = (userData) => {
        user.value = { ...user.value, ...userData }
    }

    return {
        // 状态
        user,
        token,
        isLoading,

        // 计算属性
        isLoggedIn,

        // 方法
        initializeAuth,
        login,
        logout,
        clearAuth,
        updateUser
    }
}, {
    // === 添加这个配置对象来启用持久化 ===
    persist: {
        key: 'my-auth-store', // 可选，存储在 localStorage/sessionStorage 中的键名，默认是 store 的 id
        paths: ['token'], // 指定哪些状态需要被持久化。这里我们只持久化 'token'
        // storage: localStorage, // 默认就是 localStorage，也可以设置为 sessionStorage
        // serializer: { ... }, // 更多高级选项，例如自定义序列化和反序列化
    }
})