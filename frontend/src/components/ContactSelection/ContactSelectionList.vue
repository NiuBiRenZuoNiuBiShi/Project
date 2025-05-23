<template>
  <div class="contact-list">
    <div class="contact-item" v-for="contact in contacts" :key="contact.contactId">
      <label class="contact-radio">
        <input 
          type="radio" 
          :value="contact.contactId" 
          :checked="selectedId === contact.contactId"
          @change="$emit('update:selectedId', contact.contactId)"
        />
        <span class="radio-mark"></span>
        <div class="contact-info">
          <span class="name">{{ contact.contactName }}</span>
          <span class="phone">{{ contact.contactPhone }}</span>
          <span class="email" v-if="contact.contactEmail">{{ contact.contactEmail }}</span>
        </div>
      </label>
      
      <div class="actions">
        <button class="action-btn edit-btn" @click="$emit('edit', contact)" title="编辑">
          <svg viewBox="0 0 24 24" width="16" height="16" fill="currentColor">
            <path d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z"/>
          </svg>
        </button>
        <button class="action-btn delete-btn" @click="$emit('delete', contact)" title="删除">
          <svg viewBox="0 0 24 24" width="16" height="16" fill="currentColor">
            <path d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z"/>
          </svg>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  contacts: Array,
  selectedId: String
});

defineEmits(['update:selectedId', 'edit', 'delete']);
</script>

<style lang="scss" scoped>
.contact-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  
  .contact-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px;
    border: 1px solid #e4e7ed;
    border-radius: 6px;
    transition: all 0.2s;
    
    &:hover {
      background-color: #f5f7fa;
      border-color: #c6e2ff;
    }
    
    .contact-radio {
      display: flex;
      align-items: center;
      gap: 12px;
      cursor: pointer;
      flex: 1;
      
      input[type="radio"] {
        display: none;
      }
      
      .radio-mark {
        width: 16px;
        height: 16px;
        border: 2px solid #dcdfe6;
        border-radius: 50%;
        position: relative;
        flex-shrink: 0;
        transition: all 0.2s;
        
        &::after {
          content: '';
          width: 8px;
          height: 8px;
          background-color: #409eff;
          border-radius: 50%;
          position: absolute;
          top: 50%;
          left: 50%;
          transform: translate(-50%, -50%) scale(0);
          transition: transform 0.2s;
        }
      }
      
      input:checked + .radio-mark {
        border-color: #409eff;
        
        &::after {
          transform: translate(-50%, -50%) scale(1);
        }
      }
      
      .contact-info {
        display: flex;
        flex-direction: column;
        gap: 4px;
        
        .name {
          font-size: 16px;
          font-weight: 500;
          color: #303133;
        }
        
        .phone, .email {
          font-size: 14px;
          color: #606266;
        }
      }
    }
    
    .actions {
      display: flex;
      gap: 8px;
      
      .action-btn {
        background: none;
        border: none;
        cursor: pointer;
        padding: 6px;
        border-radius: 4px;
        transition: all 0.2s;
        
        &:hover {
          background-color: #f0f0f0;
        }
        
        &.edit-btn {
          color: #409eff;
          
          &:hover {
            background-color: #ecf5ff;
          }
        }
        
        &.delete-btn {
          color: #f56c6c;
          
          &:hover {
            background-color: #fef0f0;
          }
        }
      }
    }
  }
}
</style>