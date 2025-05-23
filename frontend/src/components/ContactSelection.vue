<template>
  <div class="contact-selection-overlay">
    <div class="contact-selection-container">
      <ContactHeader @close="close" />
      
      <div class="content">
        <LoadingState v-if="loading" />
        
        <ErrorState 
          v-else-if="error" 
          :error="error" 
          @retry="fetchContacts" 
        />
        
        <div v-else>
          <EmptyState 
            v-if="contacts.length === 0" 
            @add-contact="showAddForm = true" 
          />
          
          <div v-else>
            <ContactList 
              :contacts="contacts"
              v-model:selected-id="selectedContactId"
              @edit="editContact"
              @delete="deleteContactConfirm"
            />
            
            <div class="add-contact-section">
              <button 
                class="btn btn-primary"
                @click="showAddForm = true"
              >
                添加联系人
              </button>
            </div>
          </div>
          
          <div class="submit-area">
            <button 
              :disabled="!selectedContactId" 
              class="btn btn-primary"
              @click="confirmSelection"
            >
              确认选择
            </button>
            <button class="btn btn-secondary" @click="close">
              取消
            </button>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 添加/编辑联系人表单 -->
    <ContactFormModal
      v-if="showAddForm"
      :editing-contact="editingContact"
      @close="resetForm"
      @submit="handleFormSubmit"
    />
    
    <!-- 删除确认对话框 -->
    <DeleteConfirmModal
      v-if="showDeleteConfirm"
      :contact="contactToDelete"
      @close="showDeleteConfirm = false"
      @confirm="confirmDelete"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { getContacts, addContact, updateContact, deleteContact } from '@/api/ContactApi';
import ContactHeader from '@/components/ContactSelection/ContactSelectHeader.vue';
import LoadingState from '@/components/ContactSelection/ContactSelectionLoading.vue';
import ErrorState from '@/components/ContactSelection/ContactSelectionError.vue';
import EmptyState from '@/components/ContactSelection/ContactSelectionEmpty.vue';
import ContactList from '@/components/ContactSelection/ContactSelectionList.vue';
import ContactFormModal from '@/components/ContactSelection/ContactSelectionForm.vue';
import DeleteConfirmModal from '@/components/ContactSelection/ContactSelectionDeleteConfirm.vue';

const props = defineProps({
  ticket: Object,
  isVisible: Boolean
});

const emit = defineEmits(['update:isVisible', 'confirm']);

// 状态管理
const contacts = ref([]);
const loading = ref(false);
const error = ref('');
const selectedContactId = ref('');

// 表单相关
const showAddForm = ref(false);
const editingContact = ref(null);

// 删除相关
const showDeleteConfirm = ref(false);
const contactToDelete = ref(null);

// 获取联系人列表
const fetchContacts = async () => {
  loading.value = true;
  error.value = '';
  
  try {
    const data = await getContacts();
    contacts.value = data || [];
    
    if (contacts.value.length > 0 && !selectedContactId.value) {
      selectedContactId.value = contacts.value[0].contactId;
    }
  } catch (err) {
    console.error('获取联系人失败:', err);
    error.value = '获取联系人列表失败，请稍后再试';
  } finally {
    loading.value = false;
  }
};

// 编辑联系人
const editContact = (contact) => {
  editingContact.value = contact;
  showAddForm.value = true;
};

// 删除联系人确认
const deleteContactConfirm = (contact) => {
  contactToDelete.value = contact;
  showDeleteConfirm.value = true;
};

// 确认删除
const confirmDelete = async () => {
  try {
    await deleteContact(contactToDelete.value);
    ElMessage.success('删除联系人成功');
    
    if (selectedContactId.value === contactToDelete.value.contactId) {
      selectedContactId.value = '';
    }
    
    await fetchContacts();
  } catch (err) {
    ElMessage.error('删除联系人失败');
  } finally {
    showDeleteConfirm.value = false;
    contactToDelete.value = null;
  }
};

// 处理表单提交
const handleFormSubmit = async (formData) => {
  try {
    if (editingContact.value) {
      const updatedContact = {
        ...editingContact.value,
        ...formData
      };
      await updateContact(updatedContact);
      ElMessage.success('更新联系人成功');
    } else {
      await addContact(formData);
      ElMessage.success('添加联系人成功');
    }
    
    await fetchContacts();
    resetForm();
  } catch (error) {
    ElMessage.error(editingContact.value ? '更新联系人失败' : '添加联系人失败');
  }
};

// 重置表单
const resetForm = () => {
  editingContact.value = null;
  showAddForm.value = false;
};

// 确认选择
const confirmSelection = () => {
  if (!selectedContactId.value) {
    ElMessage.warning('请选择一位联系人');
    return;
  }
  
  const selectedContact = contacts.value.find(
    contact => contact.contactId === selectedContactId.value
  );
  
  emit('confirm', {
    contact: selectedContact
  });
};

// 关闭弹窗
const close = () => {
  emit('update:isVisible', false);
};

onMounted(() => {
  fetchContacts();
});
</script>

<style lang="scss" scoped>
.contact-selection-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1000;
  display: flex;
  justify-content: center;
  align-items: center;
}

.contact-selection-container {
  width: 600px;
  max-width: 90vw;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.content {
  padding: 24px;
  max-height: 60vh;
  overflow-y: auto;
}

.add-contact-section {
  margin-top: 16px;
  text-align: center;
}

.submit-area {
  margin-top: 24px;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

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
</style>