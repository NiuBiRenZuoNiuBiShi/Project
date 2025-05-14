<script setup>
// 品牌宣传区域组件
</script>

<template>
  <div class="branding-section">
    <h1 class="brand-title">探索世界，从这里开始</h1>
    <p class="brand-slogan">找到完美的旅程，创造美好的回忆</p>
  </div>
</template>

<style lang="scss" scoped>
// 颜色变量
$primary: #4a8eff;
$primary-light: #6ba3ff;
$primary-dark: #3270e9;
$text: #2c3e50;
$text-light: #5d7290;

.branding-section {
  text-align: center;
  padding: 4rem 0 2rem;
  animation: fadeInUp 0.8s ease-out;

  .brand-title {
    font-size: 3.6rem;
    font-weight: 700;
    color: $text;
    margin-bottom: 1rem;
    background: linear-gradient(135deg, $primary-dark, $primary-light);
    background-clip: text;
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
  }

  .brand-slogan {
    font-size: 1.8rem;
    color: $text-light;
    max-width: 60rem;
    margin: 0 auto;
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// 响应式设计
@media (max-width: 768px) {
  .branding-section {
    padding: 3rem 0 1.5rem;
    
    .brand-title {
      font-size: 2.8rem;
    }
    
    .brand-slogan {
      font-size: 1.6rem;
    }
  }
}
</style>