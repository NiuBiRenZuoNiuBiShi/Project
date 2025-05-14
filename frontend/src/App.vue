<template>
    <div class="app-container">
        <SideBar />
        <router-view />
    </div>
</template>

<script setup>
import SideBar from '@/components/SideBar.vue';
// App.vue 只负责整体布局和 SideBar 的引入
</script>

<style lang="scss" scoped>
// Modern Color Scheme (matching sidebar)
$primary: #4a8eff;
$primary-light: #6ba3ff;
$primary-dark: #3270e9;
$text: #2c3e50;
$text-light: #5d7290;
$bg-light: #fafbff; // App.vue 控制整体背景色
$border: #e6eaf0;
$shadow: rgba(50, 112, 233, 0.08);

.app-container {
    display: grid;
    grid-template-columns: auto 1fr; /* 侧边栏宽度自适应，内容区域填充剩余空间 */
    min-height: 100vh;
    background-color: $bg-light;
    // 这里不再包含 content-area 的具体样式
}


@media (max-width: 768px) {
    .app-container {
        grid-template-columns: 1fr; /* 小屏幕下内容区域独占一行 */

        // 当侧边栏收起时，给内容区域一个左 padding，避免被侧边栏遮挡
        // 注意：这里需要根据侧边栏的收起宽度来调整 padding
        // 这部分逻辑可能需要根据 SideBar 的状态动态添加样式类
         > :deep(.content-area) { // 使用 :deep() 穿透作用域样式
             padding-left: 90px; // 假设收起后侧边栏宽度为 70px + 一些间距
         }
    }
}
</style>