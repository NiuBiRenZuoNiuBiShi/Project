<template>
  <div class="modal-overlay">
    <div class="modal-container">
      <div class="modal-header">
        <h3>确认删除</h3>
      </div>
      
      <div class="modal-body">
        <div class="delete-confirm">
          <div class="warning-icon">
            <svg viewBox="0 0 24 24" width="48" height="48" fill="currentColor">
              <path d="M1 21h22L12 2 1 21zm12-3h-2v-2h2v2zm0-4h-2v-4h2v4z"/>
            </svg>
          </div>
          <p class="confirm-text">
            确定要删除联系人 <strong>{{ contact?.contactName }}</strong> 吗？
          </p>
          <p class="warning-text">此操作不可撤销！</p>
        </div>
        
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="$emit('close')">
            取消
          </button>
          <button class="btn btn-danger" @click="$emit('confirm')">
            确认删除
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  contact: Object
});

defineEmits(['close', 'confirm']);
</script>

<style lang="scss" scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1001;
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-container {
  width: 400px;
  max-width: 90vw;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.modal-header {
  padding: 16px 24px;
  border-bottom: 1px solid #e4e7ed;
  
  h3 {
    margin: 0;
    font-size: 16px;
    font-weight: 600;
    color: #303133;
  }
}

.modal-body {
  padding: 24px;
}

.delete-confirm {
  text-align: center;
  margin-bottom: 24px;
  
  .warning-icon {
    color: #e6a23c;
    margin-bottom: 16px;
  }
  
  .confirm-text {
    margin: 0 0 8px 0;
    color: #303133;
    font-size: 14px;
    line-height: 1.5;
    
    strong {
      color: #f56c6c;
    }
  }
  
  .warning-text {
    margin: 0;
    color: #f56c6c;
    font-size: 12px;
    font-weight: 500;
  }
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  
  .btn {
    padding: 8px 16px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
    transition: all 0.2s;
    
    &.btn-secondary {
      background-color: #f5f7fa;
      color: #606266;
      border: 1px solid #dcdfe6;
      
      &:hover {
        background-color: #ecf5ff;
        border-color: #b3d8ff;
      }
    }
    
    &.btn-danger {
      background-color: #f56c6c;
      color: white;
      
      &:hover {
        background-color: #f04444;
      }
    }
  }
}
</style>