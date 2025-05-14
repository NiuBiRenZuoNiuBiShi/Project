<script setup>
import { ref } from 'vue';
import QueryBox from '@/components/QueryBox.vue';
import BrandingSection from '@/components/BrandingSection.vue';
import PopularDestinations from '@/components/PopularDestinations.vue';
import ServiceFeatures from '@/components/ServiceFeatures.vue';
import FooterSection from '@/components/FooterSection.vue';
</script>

<template>
  <div class="search-view">
    <!-- 背景元素 -->
    <div class="background-elements">
      <div class="bg-circle bg-circle-1"></div>
      <div class="bg-circle bg-circle-2"></div>
    </div>

    <!-- 内容区域 -->
    <div class="content-area">
      <!-- 品牌宣传区域 -->
      <BrandingSection />

      <!-- 搜索框区域 -->
      <div class="query-box-container">
        <QueryBox />
      </div>

      <!-- 热门目的地区域 -->
      <PopularDestinations />

      <!-- 服务特点区域 -->
      <ServiceFeatures />

      <!-- 页脚区域 -->
      <FooterSection />
    </div>
  </div>
</template>

<style scoped lang="scss">
// 颜色变量 (调整为匹配侧边栏的配色)
$primary: #4a8eff;
$primary-light: #6ba3ff;
$primary-dark: #3270e9;
$text: #2c3e50;
$text-light: #5d7290;
$bg-light: #fafbff;
$border: #e6eaf0;
$shadow: rgba(50, 112, 233, 0.08);

.search-view {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  position: relative;
  overflow-x: hidden;
}

// 背景元素
.background-elements {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
  overflow: hidden;

  .bg-circle {
    position: absolute;
    border-radius: 50%;
    opacity: 0.05;
  }

  .bg-circle-1 {
    top: -10%;
    right: -5%;
    width: 50rem;
    height: 50rem;
    background-color: $primary-light;
  }

  .bg-circle-2 {
    bottom: 10%;
    left: -10%;
    width: 40rem;
    height: 40rem;
    background-color: $primary;
  }
}

// 内容区域
.content-area {
  flex: 1;
  padding: 2rem 3rem;
  display: flex;
  flex-direction: column;
}

// 搜索框区域
.query-box-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 2rem;
  width: 100%;
}

// 响应式设计
@media (max-width: 768px) {
  .content-area {
    padding: 2rem 1.5rem;
  }
}
</style>