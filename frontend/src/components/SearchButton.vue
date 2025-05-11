<template>
  <div class="button-container">
    <button class="search-button" @click="emit('search')">
      <i class="fas fa-search"></i>
      <span class="button-text">查询</span>
    </button>
  </div>
</template>

<script setup>
import { ref } from 'vue';
const emit = defineEmits(['search']);

</script>

<style lang="scss" scoped>
.button-container {
  display: flex;
  justify-content: flex-end;
  align-items: flex-end;
  height: 60px; // 匹配其他输入组件的高度

  .search-button {
    display: flex;
    align-items: center;
    justify-content: flex-start;
    gap: 3rem;
    min-width: 140px;
    height: 100%;
    padding: 0 1.5rem;
    border-radius: 10px;
    background: linear-gradient(to right, #3498db, #2980b9);
    color: white;
    font-size: 1.1rem;
    font-weight: 600;
    border: none;
    cursor: pointer;
    transition: all 0.3s ease;
    box-shadow: 0 4px 10px rgba(52, 152, 219, 0.3);

    i {
      font-size: 1rem;
    }

    &:hover {
      background: linear-gradient(to right, #2980b9, #1c638d);
      transform: translateY(-2px);
      box-shadow: 0 6px 15px rgba(52, 152, 219, 0.4);
      color: #adb5bd;
    }

    &:active {
      transform: translateY(0);
      box-shadow: 0 2px 5px rgba(52, 152, 219, 0.4);
    }

    .button-text {
      font-size: 2rem;
      font-weight: 600;
      color: #f8f9fa;
    }
  }
}
</style>