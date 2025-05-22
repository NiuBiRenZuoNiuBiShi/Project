<template>
  <div class="modal-overlay">
    <div class="modal-container">
      <div class="modal-header">
        <h3>{{ editingContact ? '编辑联系人' : '添加联系人' }}</h3>
        <button class="close-button" @click="$emit('close')">
          <svg viewBox="0 0 24 24" width="20" height="20" fill="currentColor">
            <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
          </svg>
        </button>
      </div>
      
      <form @submit.prevent="handleSubmit" class="modal-body">
        <div class="form-group">
          <label for="contactName">姓名 *</label>
          <input
            id="contactName"
            v-model="form.contactName"
            type="text"
            placeholder="请输入姓名"
            :class="{ error: errors.contactName }"
            @blur="validateField('contactName')"
          />
          <span v-if="errors.contactName" class="error-message">{{ errors.contactName }}</span>
        </div>
        
        <div class="form-group">
          <label for="contactPhone">手机号 *</label>
          <input
            id="contactPhone"
            v-model="form.contactPhone"
            type="tel"
            placeholder="请输入手机号"
            :class="{ error: errors.contactPhone }"
            @blur="validateField('contactPhone')"
          />
          <span v-if="errors.contactPhone" class="error-message">{{ errors.contactPhone }}</span>
        </div>
        
        <div class="form-group">
          <label for="contactEmail">邮箱</label>
          <input
            id="contactEmail"
            v-model="form.contactEmail"
            type="email"
            placeholder="请输入邮箱（可选）"
            :class="{ error: errors.contactEmail }"
            @blur="validateField('contactEmail')"
          />
          <span v-if="errors.contactEmail" class="error-message">{{ errors.contactEmail }}</span>
        </div>
        
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="$emit('close')">
            取消
          </button>
          <button type="submit" class="btn btn-primary" :disabled="!isFormValid">
            确认
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { reactive, computed, watch } from 'vue';

const props = defineProps({
  editingContact: Object
});

const emit = defineEmits(['close', 'submit']);

const form = reactive({
  contactName: '',
  contactPhone: '',
  contactEmail: ''
});

const errors = reactive({
  contactName: '',
  contactPhone: '',
  contactEmail: ''
});

const rules = {
  contactName: [
    { required: true, message: '请输入联系人姓名' },
    { min: 2, max: 20, message: '姓名长度应为2-20个字符' }
  ],
  contactPhone: [
    { required: true, message: '请输入手机号' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号' }
  ],
  contactEmail: [
    { pattern: /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/, message: '请输入正确的邮箱格式' }
  ]
};

const validateField = (field) => {
  const value = form[field];
  const fieldRules = rules[field] || [];
  
  errors[field] = '';
  
  for (const rule of fieldRules) {
    if (rule.required && (!value || value.trim() === '')) {
      errors[field] = rule.message;
      break;
    }
    
    if (rule.min && value && value.length < rule.min) {
      errors[field] = rule.message;
      break;
    }
    
    if (rule.max && value && value.length > rule.max) {
      errors[field] = rule.message;
      break;
    }
    
    if (rule.pattern && value && !rule.pattern.test(value)) {
      errors[field] = rule.message;
      break;
    }
  }
};

const isFormValid = computed(() => {
  // 检查必填字段
  if (!form.contactName || !form.contactPhone) {
    return false;
  }
  
  // 检查是否有错误
  return !Object.values(errors).some(error => error);
});

const handleSubmit = () => {
  // 验证所有字段
  Object.keys(form).forEach(field => validateField(field));
  
  if (isFormValid.value) {
    emit('submit', { ...form });
  }
};

// 监听编辑联系人数据变化
watch(() => props.editingContact, (contact) => {
  if (contact) {
    form.contactName = contact.contactName || '';
    form.contactPhone = contact.contactPhone || '';
    form.contactEmail = contact.contactEmail || '';
  } else {
    form.contactName = '';
    form.contactPhone = '';
    form.contactEmail = '';
  }
  
  // 清除错误
  Object.keys(errors).forEach(key => {
    errors[key] = '';
  });
}, { immediate: true });
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
  width: 500px;
  max-width: 90vw;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  border-bottom: 1px solid #e4e7ed;
  
  h3 {
    margin: 0;
    font-size: 16px;
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

.modal-body {
  padding: 24px;
  
  .form-group {
    margin-bottom: 20px;
    
    label {
      display: block;
      margin-bottom: 6px;
      font-size: 14px;
      font-weight: 500;
      color: #606266;
    }
    
    input {
      width: 100%;
      padding: 10px 12px;
      border: 1px solid #dcdfe6;
      border-radius: 4px;
      font-size: 14px;
      transition: border-color 0.2s;
      box-sizing: border-box;
      
      &:focus {
        outline: none;
        border-color: #409eff;
      }
      
      &.error {
        border-color: #f56c6c;
      }
      
      &::placeholder {
        color: #c0c4cc;
      }
    }
    
    .error-message {
      display: block;
      margin-top: 4px;
      font-size: 12px;
      color: #f56c6c;
    }
  }
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
  
  .btn {
    padding: 8px 16px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
    transition: all 0.2s;
    
    &:disabled {
      opacity: 0.5;
      cursor: not-allowed;
    }
    
    &.btn-primary {
      background-color: #409eff;
      color: white;
      
      &:hover:not(:disabled) {
        background-color: #337ecc;
      }
    }
    
    &.btn-secondary {
      background-color: #f5f7fa;
      color: #606266;
      border: 1px solid #dcdfe6;
      
      &:hover {
        background-color: #ecf5ff;
        border-color: #b3d8ff;
      }
    }
  }
}
</style>