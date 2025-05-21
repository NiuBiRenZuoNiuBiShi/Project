<template>
  <div class="contact-selection-overlay">
    <div class="contact-selection-container">
      <div class="header">
        <h2>选择联系人</h2>
        <el-button type="text" @click="close" class="close-button">
          <el-icon><Close /></el-icon>
        </el-button>
      </div>
      
      <div class="content">
        <div v-if="loading" class="loading">
          <el-icon class="loading-icon"><Loading /></el-icon>
          <span>加载联系人中...</span>
        </div>
        
        <div v-else-if="error" class="error">
          <el-icon><WarningFilled /></el-icon>
          <span>{{ error }}</span>
          <el-button type="primary" @click="fetchContacts">重试</el-button>
        </div>
        
        <div v-else>
          <div v-if="contacts.length === 0" class="empty-contacts">
            <el-empty description="暂无联系人" />
            <el-button type="primary" @click="showAddContactForm = true">添加联系人</el-button>
          </div>
          
          <div v-else class="contact-list">
            <div class="contact-item" v-for="contact in contacts" :key="contact.contactId">
              <el-radio v-model="selectedContactId" :label="contact.contactId">
                <div class="contact-info">
                  <span class="name">{{ contact.contactName }}</span>
                  <span class="phone">{{ contact.contactPhone }}</span>
                  <span class="email" v-if="contact.contactEmail">{{ contact.contactEmail }}</span>
                </div>
              </el-radio>
              <div class="actions">
                <el-button type="text" @click="editContact(contact)">
                  <el-icon><Edit /></el-icon>
                </el-button>
                <el-button type="text" @click="deleteContactConfirm(contact)">
                  <el-icon><Delete /></el-icon>
                </el-button>
              </div>
            </div>
            
            <div class="add-contact-button">
              <el-button type="primary" @click="showAddContactForm = true">添加联系人</el-button>
            </div>
          </div>
          
          <div class="submit-area">
            <el-button :disabled="!selectedContactId" type="primary" @click="confirmSelection">确认选择</el-button>
            <el-button @click="close">取消</el-button>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 添加/编辑联系人表单 -->
    <el-dialog
      :title="editingContact ? '编辑联系人' : '添加联系人'"
      v-model="showAddContactForm"
      width="500px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form :model="contactForm" :rules="rules" ref="contactFormRef" label-width="90px">
        <el-form-item label="姓名" prop="contactName">
          <el-input v-model="contactForm.contactName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="contactPhone">
          <el-input v-model="contactForm.contactPhone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱" prop="contactEmail">
          <el-input v-model="contactForm.contactEmail" placeholder="请输入邮箱（可选）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showAddContactForm = false">取消</el-button>
          <el-button type="primary" @click="submitContactForm">确认</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 确认删除联系人 -->
    <el-dialog
      title="确认删除"
      v-model="showDeleteConfirm"
      width="400px"
    >
      <div class="delete-confirm">
        <p>确定要删除联系人 <strong>{{ contactToDelete?.contactName }}</strong> 吗？</p>
        <p class="warning">此操作不可撤销！</p>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showDeleteConfirm = false">取消</el-button>
          <el-button type="danger" @click="confirmDelete">确认删除</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import { Close, Loading, WarningFilled, Edit, Delete } from '@element-plus/icons-vue';
import { getContacts, addContact, updateContact, deleteContact } from '@/api/ContactApi';

const props = defineProps({
  ticket: Object,
  isVisible: Boolean
});

const emit = defineEmits(['update:isVisible', 'confirm']);

const contacts = ref([]);
const loading = ref(false);
const error = ref('');
const selectedContactId = ref('');

const showAddContactForm = ref(false);
const showDeleteConfirm = ref(false);
const contactToDelete = ref(null);
const editingContact = ref(null);

const contactForm = reactive({
  contactName: '',
  contactPhone: '',
  contactEmail: ''
});

const contactFormRef = ref(null);
const rules = {
  contactName: [
    { required: true, message: '请输入联系人姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度应为2-20个字符', trigger: 'blur' }
  ],
  contactPhone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  contactEmail: [
    { pattern: /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/, message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
};

const fetchContacts = async () => {
  loading.value = true;
  error.value = '';
  
  try {
    const data = await getContacts();
    contacts.value = data;
    
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

const editContact = (contact) => {
  editingContact.value = contact;
  contactForm.contactName = contact.contactName;
  contactForm.contactPhone = contact.contactPhone;
  contactForm.contactEmail = contact.contactEmail || '';
  showAddContactForm.value = true;
};

const deleteContactConfirm = (contact) => {
  contactToDelete.value = contact;
  showDeleteConfirm.value = true;
};

const confirmDelete = async () => {
  try {
    await deleteContact(contactToDelete.value);
    ElMessage.success('删除联系人成功');
    
    if (selectedContactId.value === contactToDelete.value.contactId) {
      selectedContactId.value = '';
    }
    
    fetchContacts();
  } catch (err) {
    ElMessage.error('删除联系人失败');
  } finally {
    showDeleteConfirm.value = false;
    contactToDelete.value = null;
  }
};

const submitContactForm = async () => {
  if (!contactFormRef.value) return;
  
  await contactFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (editingContact.value) {
          // 更新联系人
          const updatedContact = {
            ...editingContact.value,
            contactName: contactForm.contactName,
            contactPhone: contactForm.contactPhone,
            contactEmail: contactForm.contactEmail
          };
          await updateContact(updatedContact);
          ElMessage.success('更新联系人成功');
        } else {
          // 添加联系人
          const newContact = {
            contactName: contactForm.contactName,
            contactPhone: contactForm.contactPhone,
            contactEmail: contactForm.contactEmail
          };
          await addContact(newContact);
          ElMessage.success('添加联系人成功');
        }
        
        // 重新获取联系人列表
        fetchContacts();
        resetContactForm();
      } catch (error) {
        ElMessage.error(editingContact.value ? '更新联系人失败' : '添加联系人失败');
      }
    }
  });
};

const resetContactForm = () => {
  contactForm.contactName = '';
  contactForm.contactPhone = '';
  contactForm.contactEmail = '';
  editingContact.value = null;
  showAddContactForm.value = false;
};

const confirmSelection = () => {
  if (!selectedContactId.value) {
    ElMessage.warning('请选择一位联系人');
    return;
  }
  
  const selectedContact = contacts.value.find(contact => contact.contactId === selectedContactId.value);
  
  emit('confirm', {
    contact: selectedContact
  });
};

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
    }
    
    .close-button {
      font-size: 20px;
    }
  }
  
  .content {
    padding: 24px;
    max-height: 60vh;
    overflow-y: auto;
    
    .loading, .error {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 30px;
      gap: 12px;
      
      .loading-icon {
        font-size: 24px;
        animation: spin 1s infinite linear;
      }
      
      @keyframes spin {
        from { transform: rotate(0deg); }
        to { transform: rotate(360deg); }
      }
    }
    
    .empty-contacts {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 20px 0;
    }
    
    .contact-list {
      display: flex;
      flex-direction: column;
      gap: 16px;
      
      .contact-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 12px 16px;
        border: 1px solid #e0e0e0;
        border-radius: 6px;
        
        &:hover {
          background-color: #f5f7fa;
        }
        
        .contact-info {
          display: flex;
          flex-direction: column;
          gap: 4px;
          
          .name {
            font-size: 16px;
            font-weight: 500;
          }
          
          .phone, .email {
            font-size: 14px;
            color: #606266;
          }
        }
        
        .actions {
          display: flex;
          gap: 8px;
        }
      }
      
      .add-contact-button {
        margin-top: 16px;
        text-align: center;
      }
    }
    
    .submit-area {
      margin-top: 24px;
      display: flex;
      justify-content: flex-end;
      gap: 12px;
    }
  }
}

.delete-confirm {
  text-align: center;
  padding: 10px;
  
  .warning {
    color: #ff4949;
    margin-top: 10px;
    font-weight: 500;
  }
}
</style>