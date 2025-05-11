<template>
  <div class="search-input" @click="toggleSearchResult">
    <label for="input" class="label">{{ label }}</label>
    <div class="input-container">
      <div class="icon-left">
        <i class="fas fa-map-marker-alt"></i>
      </div>
      <input type="text" class="input" id="input" :placeholder="placeholder" v-model="searchValue" />
      <div class="input-icon" v-if="searchValue">
        <i class="fas fa-times-circle" @click.stop="clearSearch"></i>
      </div>
    </div>
    <div class="search-results" v-if="showSearchResult">
      <div class="search-result-item" v-for="(item, index) in mockResults" :key="index" @click.stop="selectItem(item)">
        <i class="fas fa-map-marker-alt"></i>
        <span>{{ item }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

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

// 模拟搜索结果
const mockResults = ref(['北京', '上海', '广州', '深圳', '杭州']);

const toggleSearchResult = () => {
  showSearchResult.value = !showSearchResult.value;
};

const clearSearch = () => {
  searchValue.value = '';
  showSearchResult.value = false;
};

const selectItem = (item) => {
  searchValue.value = item;
  showSearchResult.value = false;
};
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
