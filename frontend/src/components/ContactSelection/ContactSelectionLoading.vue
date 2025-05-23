<template>
  <div class="loading">
    <div class="loading-spinner"></div>
    <span>加载联系人中...</span>
  </div>
</template>

<style lang="scss" scoped>
.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
  gap: 16px;
  color: #606266;
  
  .loading-spinner {
    width: 32px;
    height: 32px;
    border: 3px solid #f0f0f0;
    border-top: 3px solid #409eff;
    border-radius: 50%;
    animation: spin 1s linear infinite;
  }
  
  @keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
  }
}
</style>