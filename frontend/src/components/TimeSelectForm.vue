<template>
  <div class="container">
    <label for="date-input" class="label">出行日期</label>
    <div class="date-input-container">
      <div class="icon-left">
        <i class="fas fa-calendar-alt"></i>
      </div>
      <input type="date" id="date-input" class="date-input" v-model="selectedDate" />
      <div class="date-icon">
        <i class="fas fa-chevron-down"></i>
      </div>
    </div>
    <div class="date-options">
      <span v-for="(day, index) in nextDays" :key="index" @click="setDate(day.date)"
        :class="{ 'selected': isSelected(day.date) }">
        {{ day.label }}
      </span>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';

const selectedDate = ref(new Date().toISOString().split('T')[0]);

// 生成未来5天的日期选项
const nextDays = computed(() => {
  const days = [];
  const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
  const today = new Date();

  days.push({
    label: '今天',
    date: new Date().toISOString().split('T')[0]
  });

  for (let i = 1; i <= 4; i++) {
    const date = new Date();
    date.setDate(today.getDate() + i);
    const dateString = date.toISOString().split('T')[0];
    const weekday = weekdays[date.getDay()];

    days.push({
      label: i === 1 ? '明天' : weekday,
      date: dateString
    });
  }

  return days;
});

const setDate = (date) => {
  selectedDate.value = date;
};

const isSelected = (date) => {
  return selectedDate.value === date;
};
</script>

<style lang="scss" scoped>
.container {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;

  .label {
    padding-left: 0.5rem;
    font-size: 0.9rem;
    font-weight: 600;
    color: #555;
    transition: color 0.2s ease;
  }

  .date-input-container {
    position: relative;
    height: 60px; // 固定高度，与其他输入框一致

    .icon-left {
      position: absolute;
      left: 15px;
      top: 50%;
      transform: translateY(-60%);
      color: #3498db;
      z-index: 1;
      font-size: 3rem;
    }

    .date-input {
      width: 100%;
      height: 100%;
      padding: 0 40px;
      border: 1px solid #e0e0e0;
      border-radius: 10px;
      font-size: 1.5rem;
      cursor: pointer;
      transition: all 0.3s ease;
      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
      appearance: none; // 移除默认样式
      -webkit-appearance: none;
      -moz-appearance: none;

      &::-webkit-calendar-picker-indicator {
        opacity: 0; // 隐藏默认的日历图标
        width: 100%;
        height: 100%;
        position: absolute;
        top: 0;
        left: 0;
        cursor: pointer;
      }

      &:focus {
        outline: none;
        border-color: #3498db;
        box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.2);
      }
    }

    .date-icon {
      position: absolute;
      right: 15px;
      top: 50%;
      transform: translateY(-60%);
      color: #3498db;
      pointer-events: none;
      font-size: 3rem;
    }
  }

  .date-options {
    display: flex;
    gap: 0.5rem;
    flex-wrap: wrap;
    margin-top: 0.5rem;

    span {
      padding: 0.3rem 0.8rem;
      border-radius: 20px;
      font-size: 1.2rem;
      background-color: #f0f8ff;
      cursor: pointer;
      transition: all 0.2s ease;

      &:hover {
        background-color: #d6ebfc;
      }

      &.selected {
        background-color: #3498db;
        color: white;
      }
    }
  }

  &:focus-within {
    .label {
      color: #3498db;
    }
  }
}
</style>