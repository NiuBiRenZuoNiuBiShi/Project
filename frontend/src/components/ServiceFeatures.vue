<script setup>
import { ref } from 'vue';

// 服务特点数据
const serviceFeatures = ref([
  { 
    icon: 'fa-shield-alt', 
    title: '安全保障', 
    description: '全程保障您的出行安全' 
  },
  { 
    icon: 'fa-tags', 
    title: '优惠折扣', 
    description: '独家优惠，特价票源' 
  },
  { 
    icon: 'fa-clock', 
    title: '24小时服务', 
    description: '全天候客服在线支持' 
  },
  { 
    icon: 'fa-gem', 
    title: '品质服务', 
    description: '高品质的旅行体验' 
  }
]);
</script>

<template>
  <div class="service-features">
    <div class="features-container">
      <div v-for="(feature, index) in serviceFeatures" :key="index" class="feature-item">
        <div class="feature-icon">
          <i :class="['fas', feature.icon]"></i>
        </div>
        <div class="feature-content">
          <h3 class="feature-title">{{ feature.title }}</h3>
          <p class="feature-description">{{ feature.description }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
// 颜色变量
$primary: #4a8eff;
$primary-light: #6ba3ff;
$primary-dark: #3270e9;
$text: #2c3e50;
$text-light: #5d7290;
$bg-light: #fafbff;
$border: #e6eaf0;
$shadow: rgba(50, 112, 233, 0.08);
$bg-card: #ffffff;
$primary-gradient: linear-gradient(135deg, $primary-light, $primary-dark);

.service-features {
  background-color: $bg-card;
  padding: 4rem 0;
  margin-top: 5rem;
  box-shadow: 0 -4px 20px $shadow;
  border-radius: 20px 20px 0 0;
  animation: slideUp 0.8s ease-out;

  .features-container {
    max-width: 1200px;
    margin: 0 auto;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-around;
    gap: 3rem;

    .feature-item {
      display: flex;
      align-items: flex-start;
      gap: 1.5rem;
      width: 250px;
      transition: transform 0.3s ease;

      &:hover {
        transform: translateY(-5px);
        
        .feature-icon {
          background: $primary-gradient;
          
          i {
            color: white;
          }
        }
      }

      .feature-icon {
        width: 5rem;
        height: 5rem;
        display: flex;
        justify-content: center;
        align-items: center;
        background-color: rgba($primary-light, 0.15);
        border-radius: 50%;
        flex-shrink: 0;
        transition: all 0.3s ease;

        i {
          font-size: 2rem;
          color: $primary;
          transition: color 0.3s ease;
        }
      }

      .feature-content {
        .feature-title {
          font-size: 1.8rem;
          font-weight: 600;
          color: $text;
          margin-bottom: 0.5rem;
        }

        .feature-description {
          font-size: 1.4rem;
          color: $text-light;
        }
      }
    }
  }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// 响应式设计
@media (max-width: 768px) {
  .service-features {
    padding: 3rem 1.5rem;
    margin-top: 4rem;
    
    .features-container {
      gap: 2rem;
      
      .feature-item {
        width: calc(50% - 1rem);
      }
    }
  }
}

@media (max-width: 480px) {
  .service-features {
    .features-container {
      .feature-item {
        width: 100%;
      }
    }
  }
}
</style>