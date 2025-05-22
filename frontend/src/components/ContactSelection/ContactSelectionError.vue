<template>
  <div class="error">
    <div class="error-icon">
      <svg viewBox="0 0 24 24" width="24" height="24" fill="currentColor">
        <path d="M1 21h22L12 2 1 21zm12-3h-2v-2h2v2zm0-4h-2v-4h2v4z"/>
      </svg>
    </div>
    <span>{{ error }}</span>
    <button class="btn btn-primary" @click="$emit('retry')">
      重试
    </button>
  </div>
</template>

<script setup>
defineProps({
  error: String
});

defineEmits(['retry']);
</script>

<style lang="scss" scoped>
.error {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
  gap: 16px;
  color: #f56c6c;
  text-align: center;
  
  .error-icon {
    color: #f56c6c;
  }
  
  .btn {
    padding: 8px 16px;
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