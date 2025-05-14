<template>
  <div class="filter-form-container">
    <div class="filter-section">
      <div class="filter-title">座位类型</div>
      <div class="filter-options">
        <label v-for="seatType in seatTypes" :key="seatType" class="checkbox-label">
          <input type="checkbox" :value="seatType" v-model="selectedSeatTypes">
          {{ seatType }}
        </label>
        </div>
    </div>

    <div class="filter-section">
      <div class="filter-title">出发时间</div>
      <div class="filter-options">
        <label v-for="timeRange in timeRanges" :key="timeRange" class="checkbox-label">
          <input type="checkbox" :value="timeRange" v-model="selectedDepartureTimes">
          {{ timeRange }}
        </label>
      </div>
    </div>

    <div class="filter-section">
      <div class="filter-title">抵达时间</div>
      <div class="filter-options">
        <label v-for="timeRange in timeRanges" :key="timeRange" class="checkbox-label">
          <input type="checkbox" :value="timeRange" v-model="selectedArrivalTimes">
          {{ timeRange }}
        </label>
      </div>
    </div>

    </div>
</template>

<script setup>
import { ref } from 'vue';

const seatTypes = ref(['二等座', '一等座', '商务座', '无座']);
const selectedSeatTypes = defineModel('seatType');


const timeRanges = ref(['00:00-06:00', '06:00-12:00', '12:00-18:00', '18:00-00:00']);
const selectedDepartureTimes = defineModel('departTime');
const selectedArrivalTimes = defineModel('arriveTime');


// 需要在这里watch这些 selectedXXX ref 的变化，
// 然后根据选择的条件去过滤车次列表数据。
// 例如：
import { watch } from 'vue';
watch([selectedSeatTypes, selectedDepartureTimes, selectedArrivalTimes], ([newSeats, newDepartTimes, newArriveTimes]) => {
  console.log('筛选条件变化:', {
    seatTypes: newSeats,
    departTimes: newDepartTimes,
    arriveTimes: newArriveTimes
  });
  // 调用父组件或store的方法来应用筛选
});
</script>

<style lang="scss" scoped>
.filter-form-container {
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 1.5rem; /* 增加各个筛选区域之间的间距 */
  width: 100%; /* 确保占据父容器的全部宽度 */
}

.filter-section {
  border-bottom: 1px solid #f0f0f0; /* 分隔线 */
  padding-bottom: 1.5rem; /* 分隔线和下方内容的间距 */

  &:last-child {
    border-bottom: none; /* 最后一个section没有下边框 */
    padding-bottom: 0;
  }
}

.filter-title {
  font-size: 1.6rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 1rem;
}

.filter-options {
  display: flex;
  flex-direction: column;
  gap: 0.8rem; 
}

.checkbox-label {
  display: flex;
  align-items: center;
  font-size: 1.5rem;
  color: #555;
  cursor: pointer;
  user-select: none;

  input[type="checkbox"] {
    margin-right: 0.5rem;
  }
}

// 
// .show-more {
//   font-size: 0.9rem;
//   color: #6ea8ff;
//   cursor: pointer;
//   margin-top: 0.5rem;
//   &:hover {
//     text-decoration: underline;
//   }
// }

// 
// .filter-actions {
//   margin-top: 1.5rem;
//   .apply-filters-button {
//     width: 100%;
//     padding: 0.8rem;
//     background-color: #6ea8ff;
//     color: white;
//     border: none;
//     border-radius: 4px;
//     font-size: 1rem;
//     cursor: pointer;
//     transition: background-color 0.3s ease;
//     &:hover {
//       background-color: #5a9eff;
//     }
//   }
// }
</style>