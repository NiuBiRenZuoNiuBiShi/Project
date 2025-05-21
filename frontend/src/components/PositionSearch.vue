<template>
  <div class="search-input">
    <label for="input" class="label">{{ label }}</label>
    <div class="input-container">
      <div class="icon-left">
        <i class="fas fa-map-marker-alt"></i>
      </div>
      <input type="text" class="input"
        :class="{ 'input-right': selectedType && selectedItem, 'input-error': !selectedType && !selectedItem && hasSelected }"
        id="input" :placeholder="placeholder" v-model="inputValue" @input="handleSearch"
        @focus="showSearchResult = true" @blur="handleBlur" />
      <div class="selection-badge" v-if="selectedType">
        <i :class="selectedType === 'city' ? 'fas fa-city' : 'fas fa-train'"></i>
        <span>{{ selectedType === 'city' ? '城市' : '车站' }}</span>
      </div>
      <div class="input-icon" v-if="inputValue">
        <i class="fas fa-times-circle" @click.stop="clearSearch"></i>
      </div>
    </div>
    <div class="search-results" v-if="showSearchResult && (cityResults.length > 0 || stationResults.length > 0)">
      <!-- 城市搜索结果 -->
      <div v-if="cityResults.length > 0" class="result-category">
        <div class="category-title">城市</div>
        <div class="search-result-item" v-for="(item, index) in cityResults" :key="`city-${index}`"
          @mousedown.prevent="selectItem(item, 'city')">
          <i class="fas fa-city"></i>
          <span>{{ item.cityName }}</span>
        </div>
      </div>

      <!-- 车站搜索结果 -->
      <div v-if="stationResults.length > 0" class="result-category">
        <div class="category-title">车站</div>
        <div class="search-result-item" v-for="(item, index) in stationResults" :key="`station-${index}`"
          @mousedown.prevent="selectItem(item, 'station')">
          <i class="fas fa-train"></i>
          <span>{{ item.stationName }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';

const props = defineProps({
  label: {
    type: String,
    default: '出发地'
  },
  placeholder: {
    type: String,
    default: '请输入城市或车站然后在下方选择'
  }
});

// 使用 defineModel
const hasSelected = ref(false);

const inputValue = defineModel('modelValue');
const selectedItem = defineModel('selectedItem', { default: null });
const selectedType = defineModel('selectedType', { default: '' });

// 搜索相关状态
const showSearchResult = ref(false);
const cityResults = ref([]);
const stationResults = ref([]);
const isLoading = ref(false);
const searchTimeout = ref(null);

// 搜索城市和车站
const handleSearch = async () => {
  selectedItem.value = null;
  selectedType.value = '';
  // 清除之前的延时
  if (searchTimeout.value) {
    clearTimeout(searchTimeout.value);
  }

  // 如果输入为空，清空结果
  if (!inputValue.value || inputValue.value.trim() === '') {
    cityResults.value = [];
    stationResults.value = [];
    return;
  }

  // 设置延时，防止频繁请求
  searchTimeout.value = setTimeout(async () => {
    try {
      isLoading.value = true;

      // 并行请求城市和车站数据
      const [cityResponse, stationResponse] = await Promise.all([
        axios.get('/api/city/search', {
          params: {
            input: inputValue.value
          }
        }),
        axios.get('/api/station/search', {
          params: {
            input: inputValue.value
          }
        })
      ]);

      cityResults.value = cityResponse.data.data || [];
      stationResults.value = stationResponse.data.data || [];
      showSearchResult.value = true;
    } catch (error) {
      console.error('搜索失败:', error);
    } finally {
      isLoading.value = false;
    }
  }, 300); // 300ms延迟
};

// 处理失去焦点事件
const handleBlur = () => {
  // 使用延时，允许点击事件先执行
  setTimeout(() => {
    showSearchResult.value = false;
  }, 200);
};

// 清除搜索
const clearSearch = () => {
  inputValue.value = '';
  cityResults.value = [];
  stationResults.value = [];
  showSearchResult.value = false;
  selectedItem.value = null;
  selectedType.value = '';
};

// 选择项目
const selectItem = (item, type) => {
  if (type === 'city') {
    inputValue.value = item.cityName;
    selectedItem.value = item.cityName;
  } else {
    inputValue.value = item.stationName;
    selectedItem.value = item.stationName;
  }
  hasSelected.value = true;
  selectedType.value = type;
  // console.log(selectedType.value); Good

  showSearchResult.value = false;
};

// 初始加载一些热门城市和车站
const loadInitialData = async () => {
  try {
    const [cityResponse, stationResponse] = await Promise.all([
      axios.get('/api/city/search', { params: { input: '' } }),
      axios.get('/api/station/search', { params: { input: '' } })
    ]);

    cityResults.value = cityResponse.data.data || [];
    stationResults.value = stationResponse.data.data || [];
  } catch (error) {
    console.error('获取初始数据失败:', error);
  }
};

// 组件挂载时加载初始数据
loadInitialData();
</script>

<style lang="scss" scoped>
.search-input {
  position: relative;
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;

  .label {
    padding-left: 0.5rem;
    font-size: 1rem;
    font-weight: 600;
    color: #555;
    transition: color 0.2s ease;
  }

  .input-container {
    position: relative;
    height: 60px; // 固定高度

    .icon-left {
      position: absolute;
      left: 15px;
      top: 50%;
      transform: translateY(-50%);
      color: #3498db;
      z-index: 1;
      font-size: 2rem;
    }

    .input {
      width: 100%;
      height: 100%;
      padding: 0 40px;
      border: 1px solid #e0e0e0;
      border-radius: 10px;
      font-size: 1.5rem;
      transition: all 0.3s ease;
      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);

      &:focus {
        outline: none;
        border-color: #3498db;
        box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.2);
      }

      &.input-right {
        border-color: #74b816;
        box-shadow: 0 0 0 3px rgba(116, 184, 22, 0.2);
      }

      &.input-error {
        border-color: #e74c3c;
        box-shadow: 0 0 0 3px rgba(231, 76, 60, 0.2);
      }

      &::placeholder {
        color: #aaa;
        font-size: 1.5rem;
        font-weight: 400;
      }
    }

    .selection-badge {
      position: absolute;
      right: 50px;
      top: 50%;
      transform: translateY(-50%);
      display: flex;
      align-items: center;
      padding: 4px 8px;
      background-color: #e6f7ff;
      border: 1px solid #91d5ff;
      border-radius: 12px;
      font-size: 0.8rem;
      color: #1890ff;

      i {
        margin-right: 4px;
        font-size: 0.9rem;
      }
    }

    .input-icon {
      position: absolute;
      right: 15px;
      top: 50%;
      transform: translateY(-60%);
      color: #aaa;
      cursor: pointer;
      transition: color 0.2s ease;
      font-size: 2rem;

      &:hover {
        color: #3498db;
      }
    }
  }

  &:focus-within {
    .label {
      color: #3498db;
    }
  }

  .search-results {
    position: absolute;
    top: 100%;
    left: 0;
    width: 100%;
    max-height: 300px;
    overflow-y: auto;
    background-color: white;
    border: 1px solid #e0e0e0;
    border-radius: 0 0 10px 10px;
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
    z-index: 100;
    margin-top: 2px;

    .result-category {
      padding: 0.5rem 0;

      .category-title {
        padding: 0.5rem 1rem;
        font-weight: 600;
        font-size: 0.9rem;
        color: #777;
        background-color: #f8f9fa;
      }
    }

    .search-result-item {
      display: flex;
      align-items: center;
      gap: 10px;
      padding: 0.8rem 1rem;
      cursor: pointer;
      transition: background-color 0.2s;

      i {
        color: #3498db;
        font-size: 1.2rem;
      }

      &:hover {
        background-color: #f0f8ff;
      }
    }
  }
}
</style>