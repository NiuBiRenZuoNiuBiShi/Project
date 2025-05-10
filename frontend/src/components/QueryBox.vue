<script setup>
import { ref } from 'vue';
import QueryTrainTicketForm from '@/components/QueryTrainTicketFrom.vue'
const currentForm = ref('Train');
</script>

<template>
  <div class="query-box">
    <div class="select-container">
      <a @click="currentForm = 'Train'" href="#" :class="{ active: currentForm === 'Train' }">
        <i class="fas fa-train"></i> 火车票
      </a>
      <a @click="currentForm = 'Hotel'" href="#" :class="{ active: currentForm === 'Hotel' }">
        <i class="fas fa-hotel"></i> 酒店预订
      </a>
    </div>
    <div class="select-form">
      <div v-if="currentForm === 'Train'" class="form-wrapper">
        <QueryTrainTicketForm />
      </div>
      <div v-else-if="currentForm === 'Hotel'" class="form-wrapper">
        <h2>酒店预订</h2>
        <p>酒店预订表单内容将在这里显示。</p>
      </div>
    </div>
  </div>
</template>

<style lang='scss' scoped>
// 颜色变量
$primary-color: #3498db;
$primary-dark: #2980b9;
$primary-light: #ebf5fb;
$secondary-color: #f1c40f;
$text-dark: #2c3e50;
$text-light: #ecf0f1;
$background-light: #ffffff;
$border-color: #e0e0e0;
$box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);

.query-box {
  display: flex;
  flex-direction: column;
  position: relative;
  width: 90%;
  max-width: 1200px;
  margin: 3rem auto;
  background-color: $background-light;
  border-radius: 16px;
  box-shadow: $box-shadow;
  overflow: visible;
  padding: 3rem 2rem 2rem;

  .select-container {
    position: absolute;
    left: 50%;
    top: 0;
    transform: translateX(-50%) translateY(-50%);
    display: flex;
    gap: 1rem;
    padding: 0.5rem;
    border-radius: 12px;
    background-color: rgba($text-dark, 0.8);
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
    z-index: 10;

    a {
      font-size: 1.1rem;
      font-weight: 600;
      color: $text-light;
      text-decoration: none;
      display: flex;
      align-items: center;
      gap: 0.5rem;
      padding: 0.6rem 1.5rem;
      border-radius: 8px;
      transition: all 0.25s ease;

      i {
        font-size: 1rem;
      }

      &:hover {
        background-color: rgba($text-light, 0.15);
        transform: translateY(-2px);
      }

      &.active {
        background-color: $background-light;
        color: $primary-color;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        transform: translateY(-2px);
      }
    }
  }

  .select-form {
    display: flex;
    width: 100%;
    margin-top: 1.5rem;

    .form-wrapper {
      width: 100%;
      padding: 1rem;
      animation: fadeIn 0.3s ease-in-out;
    }
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>