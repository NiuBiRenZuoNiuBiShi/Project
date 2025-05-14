<template>
  <div class="search-input">
    <label for="input" class="label">{{ label }}</label>
    <div class="input-container">
      <div class="icon-left">
        <i class="fas fa-map-marker-alt"></i>
      </div>
      <input 
        type="text" 
        class="input" 
        id="input" 
        :placeholder="placeholder" 
        v-model="searchValue" 
        @input="handleSearch"
        @focus="showSearchResult = true"
        @blur="handleBlur"
      />
      <div class="input-icon" v-if="searchValue">
        <i class="fas fa-times-circle" @click.stop="clearSearch"></i>
      </div>
    </div>
    <div class="search-results" v-if="showSearchResult && searchResults.length > 0">
      <div 
        class="search-result-item" 
        v-for="(item, index) in searchResults" 
        :key="index" 
        @mousedown.prevent="selectItem(item)"
      >
        <i class="fas fa-map-marker-alt"></i>
        <span>{{ item.cityName }}</span>
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
    default: '请输入城市'
  }
});

const searchValue = defineModel();
const showSearchResult = ref(false);
const searchResults = ref([]);
const isLoading = ref(false);
const searchTimeout = ref(null);

// 搜索城市
const handleSearch = async () => {
  // 清除之前的延时
  if (searchTimeout.value) {
    clearTimeout(searchTimeout.value);
  }
  
  // 如果输入为空，清空结果
  if (!searchValue.value || searchValue.value.trim() === '') {
    searchResults.value = [];
    return;
  }
  
  // 设置延时，防止频繁请求
  searchTimeout.value = setTimeout(async () => {
    try {
      isLoading.value = true;
      const response = await axios.get('/api/city/search', {
        params: {
          keyword: searchValue.value
        }
      });
      searchResults.value = response.data;
      showSearchResult.value = true;
    } catch (error) {
      console.error('获取城市列表失败:', error);
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
  searchValue.value = '';
  searchResults.value = [];
  showSearchResult.value = false;
};

// 选择城市项
const selectItem = (item) => {
  searchValue.value = item.cityName;
  showSearchResult.value = false;
};

// 初始加载一些热门城市
const loadInitialCities = async () => {
  try {
    const response = await axios.get('/api/city/search');
    searchResults.value = response.data;
  } catch (error) {
    console.error('获取热门城市失败:', error);
  }
};

// 组件挂载时加载初始城市列表
loadInitialCities();
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
      transform: translateY(-60%);
      color: #3498db;
      z-index: 1;
      font-size: 3rem;
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

      &::placeholder {
        color: #aaa;
        font-size: 1.5rem;
        font-weight: 400;
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
    max-height: 200px;
    overflow-y: auto;
    background-color: white;
    border: 1px solid #e0e0e0;
    border-radius: 0 0 10px 10px;
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
    z-index: 100;
    margin-top: 2px;

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