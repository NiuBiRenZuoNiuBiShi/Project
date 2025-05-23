<template>
  <div class="header">
    <h2>选择联系人</h2>
    <button class="close-button" @click="$emit('close')">
      <svg viewBox="0 0 24 24" width="20" height="20" fill="currentColor">
        <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
      </svg>
    </button>
  </div>
</template>

<script setup>
defineEmits(['close']);
</script>

<style lang="scss" scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  border-bottom: 1px solid #e0e0e0;
  
  h2 {
    margin: 0;
    font-size: 18px;
    font-weight: 600;
    color: #303133;
  }
  
  .close-button {
    background: none;
    border: none;
    cursor: pointer;
    padding: 4px;
    border-radius: 4px;
    color: #909399;
    transition: all 0.2s;
    
    &:hover {
      background-color: #f5f7fa;
      color: #606266;
    }
  }
}
</style>