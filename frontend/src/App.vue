<template>
    <div class="app-container" :class="{ 'sidebar-collapsed': isSidebarCollapsed }">
        <SideBar @sidebar-toggle="handleSidebarToggle" />
        <main class="content-area">
            <router-view />
        </main>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import SideBar from '@/components/SideBar.vue';

const isSidebarCollapsed = ref(window.innerWidth <= 768);

const handleSidebarToggle = (collapsed) => {
    isSidebarCollapsed.value = collapsed;
};
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

.app-container {
    display: flex;
    width: 100%;
    min-height: 100vh;
    background-color: $bg-light;
    position: relative;
    
    &.sidebar-collapsed {
        .content-area {
            margin-left: 70px; /* Width of collapsed sidebar */
        }
    }
}

.content-area {
    flex: 1;
    min-height: 100vh;
    margin-left: 240px; /* Width of expanded sidebar */
    transition: margin-left 0.3s ease;
    width: calc(100% - 240px);
    background-color: $bg-light;
    overflow-x: hidden;
}

// Mobile styles
@media (max-width: $breakpoint-sm) {
    .content-area {
        margin-left: 70px; /* Always use collapsed sidebar width on mobile */
        width: calc(100% - 70px);
    }
    
    .app-container.sidebar-collapsed .content-area {
        margin-left: 70px;
    }
}
</style>