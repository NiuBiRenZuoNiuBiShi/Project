<template>
    <div class="app-container" :class="{ 'sidebar-collapsed': isSidebarCollapsed }">
        <SideBar @sidebar-toggle="handleSidebarToggle" @showLogin="showLoginModal = true" />
        <main class="content-area">
            <router-view />
        </main>
        <Transition name="fade">
            <LoginModal v-if="showLoginModal" @close="showLoginModal = false" class="login-modal" />
        </Transition>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import SideBar from '@/components/SideBar.vue';
import LoginModal from './components/LoginModal.vue';
import { useAuthStore } from '@/store/auth.js';

const authStore = useAuthStore();
const isSidebarCollapsed = ref(window.innerWidth <= 768);
const showLoginModal = ref(false);

const handleSidebarToggle = (collapsed) => {
    isSidebarCollapsed.value = collapsed;
};

// 应用启动时初始化用户认证状态
onMounted(async () => {
    await authStore.initializeAuth();
});
</script>

<style lang="scss" scoped>
// Modern Color Scheme
$primary: #4a8eff;
$primary-light: #6ba3ff;
$primary-dark: #3270e9;
$text: #2c3e50;
$text-light: #5d7290;
$bg-light: #fafbff;
$border: #e6eaf0;
$shadow: rgba(50, 112, 233, 0.08);

// Media query breakpoints
$breakpoint-sm: 768px;
$breakpoint-xs: 480px;

.login-modal {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.25);
  backdrop-filter: blur(20px) saturate(160%);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.app-container {
    display: flex;
    width: 100%;
    min-height: 100vh;
    background-color: $bg-light;
    position: relative;

    &.sidebar-collapsed {
        .content-area {
            margin-left: 70px;
            /* Width of collapsed sidebar */
        }
    }
}

.content-area {
    flex: 1;
    min-height: 100vh;
    margin-left: 240px;
    /* Width of expanded sidebar */
    transition: margin-left 0.3s ease;
    width: calc(100% - 240px);
    background-color: $bg-light;
    overflow-x: hidden;
}

.fade-enter-from,
.fade-leave-to {
    opacity: 0;
    transform: scale(0.85) translateY(30px);
    filter: blur(4px);
}

.fade-enter-to,
.fade-leave-from {
    opacity: 1;
    transform: scale(1) translateY(0);
    filter: blur(0);
}

.fade-enter-active,
.fade-leave-active {
    transition: all 0.3s cubic-bezier(0.22, 1, 0.36, 1); // 弹性效果
}

// Mobile styles
@media (max-width: $breakpoint-sm) {
    .content-area {
        margin-left: 70px;
        /* Always use collapsed sidebar width on mobile */
        width: calc(100% - 70px);
    }

    .app-container.sidebar-collapsed .content-area {
        margin-left: 70px;
    }
}
</style>