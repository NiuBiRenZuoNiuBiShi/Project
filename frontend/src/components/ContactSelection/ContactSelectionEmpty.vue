<template>
  <div class="empty-contacts">
    <div class="empty-icon">
      <svg viewBox="0 0 24 24" width="48" height="48" fill="currentColor">
        <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
      </svg>
    </div>
    <p class="empty-text">暂无联系人</p>
    <button class="btn btn-primary" @click="$emit('add-contact')">
      添加联系人
    </button>
  </div>
</template>

<script setup>
defineEmits(['add-contact']);
</script>

<style lang="scss" scoped>
.empty-contacts {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px 20px;
  text-align: center;
  
  .empty-icon {
    color: #c0c4cc;
    margin-bottom: 16px;
  }
  
  .empty-text {
    color: #909399;
    margin: 0 0 20px 0;
    font-size: 14px;
  }
  
  .btn {
    padding: 10px 20px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
    transition: all 0.2s;
    
    &.btn-primary {
      background-color: #409eff;
      color: white;
      
      &:hover {
        background-color: #337ecc;
      }
    }
  }
}
</style>