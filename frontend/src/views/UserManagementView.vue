<template>
  <div class="user-management">
    <!-- 页面头部 -->
    <header class="page-header">
      <h1 class="page-title">My Info</h1>
      <button class="btn refresh-btn" @click="refreshData">
        <span class="icon">⟳</span>
        Refresh Data
      </button>
    </header>

    <!-- 主要内容区域 -->
    <main class="main-content">
      <div class="content-grid">
        <!-- 用户信息卡片 -->
        <section class="card user-info-card">
          <div class="card-header">
            <h2>My Profile</h2>
            <div class="action-buttons">
              <button v-if="!editingUserInfo" class="btn edit-btn" @click="startEditUser">
                <span class="icon">✎</span>
                Edit
              </button>
              <div v-else class="button-group">
                <button class="btn save-btn" @click="saveUserInfo">
                  <span class="icon">✓</span>
                  Save
                </button>
                <button class="btn cancel-btn" @click="cancelEditUser">
                  <span class="icon">✕</span>
                  Cancel
                </button>
              </div>
            </div>
          </div>

          <div v-if="loading.user" class="loading-container">
            <div class="loading-spinner"></div>
            <p>Loading user information...</p>
          </div>

          <div v-else-if="error.user" class="error-container">
            <div class="error-icon">⚠</div>
            <p class="error-message">{{ error.user }}</p>
            <button class="btn retry-btn" @click="fetchUserData">Retry</button>
          </div>

          <form v-else class="user-form" @submit.prevent="saveUserInfo">
            <div class="form-group">
              <label for="username">Username</label>
              <input id="username" type="text" v-model="userForm.username" :disabled="!editingUserInfo"
                :class="{ 'input-error': userFormErrors.username }">
              <p v-if="userFormErrors.username" class="error-text">{{ userFormErrors.username }}</p>
            </div>

            <div class="form-group">
              <label for="email">Email</label>
              <input id="email" type="email" v-model="userForm.email" :disabled="!editingUserInfo"
                :class="{ 'input-error': userFormErrors.email }">
              <p v-if="userFormErrors.email" class="error-text">{{ userFormErrors.email }}</p>
            </div>

            <div class="form-group">
              <label for="phone">Phone Number</label>
              <input id="phone" type="tel" v-model="userForm.phone" :disabled="!editingUserInfo"
                :class="{ 'input-error': userFormErrors.phone }">
              <p v-if="userFormErrors.phone" class="error-text">{{ userFormErrors.phone }}</p>
            </div>

            <div class="form-group">
              <label for="idCard">ID Card Number</label>
              <input id="idCard" type="text" v-model="userForm.idCard" :disabled="!editingUserInfo"
                :class="{ 'input-error': userFormErrors.idCard }">
              <p v-if="userFormErrors.idCard" class="error-text">{{ userFormErrors.idCard }}</p>
            </div>

            <p class="form-note">Account Created At: {{ formatDate(userForm.createdAt) }}</p>
          </form>
        </section>

        <!-- 联系人管理卡片 -->
        <section class="card contacts-card">
          <div class="card-header">
            <h2>My Contacts</h2>
            <button class="btn add-btn" @click="openAddContact">
              <span class="icon">+</span>
              Add Contacts
            </button>
          </div>

          <div v-if="loading.contacts" class="loading-container">
            <div class="loading-spinner"></div>
            <p>Loading contacts...</p>
          </div>

          <div v-else-if="error.contacts" class="error-container">
            <div class="error-icon">⚠</div>
            <p class="error-message">{{ error.contacts }}</p>
            <button class="btn retry-btn" @click="fetchContacts">Retry</button>
          </div>

          <div v-else class="contacts-list">
            <div v-if="contacts.length === 0" class="empty-state">
              <div class="empty-icon">👤</div>
              <p>You have not added any contacts</p>
              <p class="empty-subtext">Adding contacts can help you book tickets faster</p>
            </div>

            <div v-else class="contact-cards">
              <div v-for="contact in contacts" :key="contact.contactId" class="contact-item">
                <div class="contact-details">
                  <div class="contact-name">{{ contact.contactName }}</div>
                  <div class="contact-info">
                    <div class="info-item">
                      <span class="info-label">Phone:</span>
                      <span class="info-value">{{ contact.contactPhone }}</span>
                    </div>
                    <div v-if="contact.contactEmail" class="info-item">
                      <span class="info-label">Email:</span>
                      <span class="info-value">{{ contact.contactEmail }}</span>
                    </div>
                  </div>
                </div>
                <div class="contact-actions">
                  <button class="icon-btn edit-icon" @click="editContact(contact)">✎</button>
                  <button class="icon-btn delete-icon" @click="confirmDeleteContact(contact)">🗑</button>
                </div>
              </div>
            </div>
          </div>
        </section>
      </div>
    </main>

    <!-- 联系人表单模态框 -->
    <div v-if="contactModalVisible" class="modal-overlay" @click="closeContactModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h2>{{ contactForm.contactId ? 'Edit Contact' : 'Add Contact' }}</h2>
          <button class="icon-btn close-btn" @click="closeContactModal">✕</button>
        </div>

        <form class="contact-form" @submit.prevent="saveContact">
          <div class="form-group">
            <label for="contactName">Name</label>
            <input id="contactName" type="text" v-model="contactForm.contactName"
              :class="{ 'input-error': contactFormErrors.contactName }">
            <p v-if="contactFormErrors.contactName" class="error-text">{{ contactFormErrors.contactName }}</p>
          </div>

          <div class="form-group">
            <label for="contactPhone">Phone Number</label>
            <input id="contactPhone" type="tel" v-model="contactForm.contactPhone"
              :class="{ 'input-error': contactFormErrors.contactPhone }">
            <p v-if="contactFormErrors.contactPhone" class="error-text">{{ contactFormErrors.contactPhone }}</p>
          </div>

          <div class="form-group">
            <label for="contactEmail">Email (Optional)</label>
            <input id="contactEmail" type="email" v-model="contactForm.contactEmail"
              :class="{ 'input-error': contactFormErrors.contactEmail }">
            <p v-if="contactFormErrors.contactEmail" class="error-text">{{ contactFormErrors.contactEmail }}</p>
          </div>

          <div class="form-actions">
            <button type="button" class="btn cancel-btn" @click="closeContactModal">Cancel</button>
            <button type="submit" class="btn save-btn">Save</button>
          </div>
        </form>
      </div>
    </div>

    <!-- Confirm Delete Modal -->
    <div v-if="deleteModalVisible" class="modal-overlay" @click="closeDeleteModal">
      <div class="modal-content delete-modal" @click.stop>
        <div class="modal-header">
          <h2>Confirm Delete</h2>
          <button class="icon-btn close-btn" @click="closeDeleteModal">✕</button>
        </div>

        <div class="delete-confirmation">
          <p>Are you sure you want to delete contact <strong>{{ contactToDelete?.contactName }}</strong>?</p>
          <p class="delete-warning">This action cannot be undone!</p>

          <div class="form-actions">
            <button class="btn cancel-btn" @click="closeDeleteModal">Cancel</button>
            <button class="btn delete-btn" @click="onDeleteContact">Delete</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Notification -->
    <div v-if="notification.visible" class="notification" :class="notification.type">
      <div class="notification-content">
        <span class="notification-icon">{{ notification.type === 'success' ? '✓' : '✕' }}</span>
        <span class="notification-message">{{ notification.message }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue';
import { getContacts, addContact, updateContact, deleteContact } from '@/api/ContactApi';
import { getUserInfo, updateUserInfo } from '@/api/UserApi';

// 状态管理
const loading = reactive({
  user: false,
  contacts: false
});

const error = reactive({
  user: '',
  contacts: ''
});

const notification = reactive({
  visible: false,
  message: '',
  type: 'success'
});

// 用户信息相关
const originalUserData = ref({});
const userForm = reactive({
  id: '',
  username: '',
  email: '',
  phone: '',
  idCard: '',
  createdAt: null
});
const userFormErrors = reactive({
  username: '',
  email: '',
  phone: '',
  idCard: ''
});
const editingUserInfo = ref(false);

// 联系人相关
const contacts = ref([]);
const contactForm = reactive({
  contactId: '',
  contactName: '',
  contactPhone: '',
  contactEmail: ''
});
const contactFormErrors = reactive({
  contactName: '',
  contactPhone: '',
  contactEmail: ''
});
const contactModalVisible = ref(false);
const contactToDelete = ref(null);
const deleteModalVisible = ref(false);

// 获取用户数据
const fetchUserData = async () => {
  loading.user = true;
  error.user = '';

  try {
    const response = await getUserInfo();

    if (response && response.data) {
      const userData = response.data;
      Object.assign(userForm, {
        id: userData.id,
        username: userData.username,
        email: userData.email,
        phone: userData.phone,
        idCard: userData.idCard,
        createdAt: userData.createdAt
      });

      // 保存原始数据用于重置
      originalUserData.value = { ...userForm };
    } else {
      throw new Error('获取用户信息失败');
    }
  } catch (err) {
    console.error('获取用户信息失败:', err);
    error.user = 'Unable To Load User Information, please try again later';
  } finally {
    loading.user = false;
  }
};

// 获取联系人列表
const fetchContacts = async () => {
  loading.contacts = true;
  error.contacts = '';

  try {
    const response = await getContacts();


    if (response) {
      contacts.value = response;
    } else {
      throw new Error('获取联系人列表失败');
    }
  } catch (err) {
    console.error('获取联系人失败:', err);
    error.contacts = 'Unable To Load Contacts List, please try again later';
  } finally {
    loading.contacts = false;
  }
};

// 刷新所有数据
const refreshData = () => {
  fetchUserData();
  fetchContacts();
};

// 用户信息编辑相关方法
const startEditUser = () => {
  editingUserInfo.value = true;
};

const cancelEditUser = () => {
  // 重置表单为原始数据
  Object.assign(userForm, originalUserData.value);
  // 清除所有错误
  Object.keys(userFormErrors).forEach(key => userFormErrors[key] = '');
  // 退出编辑模式
  editingUserInfo.value = false;
};

const validateUserForm = () => {
  let isValid = true;

  // 重置所有错误
  Object.keys(userFormErrors).forEach(key => userFormErrors[key] = '');

  // 用户名验证
  if (!userForm.username.trim()) {
    userFormErrors.username = 'Username cannot be empty';
    isValid = false;
  } else if (userForm.username.length < 3 || userForm.username.length > 20) {
    userFormErrors.username = 'Username has to be between 3 and 20 characters';
    isValid = false;
  }

  // 邮箱验证
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!userForm.email.trim()) {
    userFormErrors.email = 'Email cannot be empty';
    isValid = false;
  } else if (!emailRegex.test(userForm.email)) {
    userFormErrors.email = 'Please enter a valid email address';
    isValid = false;
  }

  // 手机号验证
  const phoneRegex = /^1[3-9]\d{9}$/;
  if (!userForm.phone.trim()) {
    userFormErrors.phone = 'Phone number cannot be empty';
    isValid = false;
  } else if (!phoneRegex.test(userForm.phone)) {
    userFormErrors.phone = 'please enter the correct phone number';
    isValid = false;
  }

  // 身份证号验证
  const idCardRegex = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
  if (!userForm.idCard.trim()) {
    userFormErrors.idCard = 'ID Card number cannot be empty';
    isValid = false;
  } else if (!idCardRegex.test(userForm.idCard)) {
    userFormErrors.idCard = 'Please enter a valid ID Card number';
    isValid = false;
  }

  return isValid;
};

const saveUserInfo = async () => {
  // 验证表单
  if (!validateUserForm()) {
    return;
  }

  loading.user = true;

  try {
    const userToUpdate = {
      id: userForm.id,
      username: userForm.username,
      phone: userForm.phone,
      email: userForm.email,
      idCard: userForm.idCard
    };

    const response = await updateUserInfo(userToUpdate);

    if (response && response.success) {
      // 更新成功，保存为新的原始数据
      originalUserData.value = { ...userForm };
      showNotification('User information updated successfully', 'success');
      editingUserInfo.value = false;
    } else {
      throw new Error(response?.message || 'Failed to update user information');
    }
  } catch (err) {
    console.error('Failed to save user information:', err);
    showNotification('Failed to save user information: ' + (err.message || 'Unknown error'), 'error');
  } finally {
    loading.user = false;
  }
};

// 联系人相关方法
const openAddContact = () => {
  // 重置表单
  contactForm.contactId = '';
  contactForm.contactName = '';
  contactForm.contactPhone = '';
  contactForm.contactEmail = '';

  // 清除错误
  Object.keys(contactFormErrors).forEach(key => contactFormErrors[key] = '');

  // 显示模态框
  contactModalVisible.value = true;
};

const editContact = (contact) => {
  // 填充表单
  contactForm.contactId = contact.contactId;
  contactForm.contactName = contact.contactName;
  contactForm.contactPhone = contact.contactPhone;
  contactForm.contactEmail = contact.contactEmail || '';

  // 清除错误
  Object.keys(contactFormErrors).forEach(key => contactFormErrors[key] = '');

  // 显示模态框
  contactModalVisible.value = true;
};

const closeContactModal = () => {
  contactModalVisible.value = false;
};

const validateContactForm = () => {
  let isValid = true;

  // 重置所有错误
  Object.keys(contactFormErrors).forEach(key => contactFormErrors[key] = '');

  // 联系人姓名验证
  if (!contactForm.contactName.trim()) {
    contactFormErrors.contactName = 'Contact name cannot be empty';
    isValid = false;
  } else if (contactForm.contactName.length < 2 || contactForm.contactName.length > 20) {
    contactFormErrors.contactName = 'Name length must be between 2 and 20 characters';
    isValid = false;
  }

  // 手机号验证
  const phoneRegex = /^1[3-9]\d{9}$/;
  if (!contactForm.contactPhone.trim()) {
    contactFormErrors.contactPhone = 'Phone number cannot be empty';
    isValid = false;
  } else if (!phoneRegex.test(contactForm.contactPhone)) {
    contactFormErrors.contactPhone = 'Please enter a valid phone number';
    isValid = false;
  }

  // 邮箱验证 (可选)
  if (contactForm.contactEmail.trim()) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(contactForm.contactEmail)) {
      contactFormErrors.contactEmail = 'Please enter a valid email address';
      isValid = false;
    }
  }

  return isValid;
};

const saveContact = async () => {
  // 验证表单
  if (!validateContactForm()) {
    return;
  }

  try {
    if (contactForm.contactId) {
      // 更新联系人
      await updateContact({
        contactId: contactForm.contactId,
        contactName: contactForm.contactName,
        contactPhone: contactForm.contactPhone,
        contactEmail: contactForm.contactEmail
      });

      showNotification('Contact updated successfully', 'success');
    } else {
      // 添加新联系人
      await addContact({
        contactName: contactForm.contactName,
        contactPhone: contactForm.contactPhone,
        contactEmail: contactForm.contactEmail
      });

      showNotification('Contact added successfully', 'success');
    }

    // 关闭模态框并重新获取联系人列表
    closeContactModal();
    fetchContacts();
  } catch (err) {
    console.error('Failed to save contact:', err);
    showNotification('Failed to save contact: ' + (err.message || 'Unknown error'), 'error');
  }
};

// 删除联系人相关方法
const confirmDeleteContact = (contact) => {
  contactToDelete.value = contact;
  deleteModalVisible.value = true;
};

const closeDeleteModal = () => {
  deleteModalVisible.value = false;
  contactToDelete.value = null;
};

const onDeleteContact = async () => {
  if (!contactToDelete.value) return;

  try {
    await deleteContact(contactToDelete.value);

    showNotification('Contact deleted successfully', 'success');

    // 关闭模态框并重新获取联系人列表
    closeDeleteModal();
    fetchContacts();
  } catch (err) {
    console.error('Failed to delete contact:', err);
    showNotification('Failed to delete contact: ' + (err.message || 'Unknown error'), 'error');
  }
};

// 通知相关方法
const showNotification = (message, type = 'success') => {
  notification.message = message;
  notification.type = type;
  notification.visible = true;

  // 3秒后自动关闭通知
  setTimeout(() => {
    notification.visible = false;
  }, 3000);
};

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '';

  const date = new Date(dateString);
  return new Intl.DateTimeFormat('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  }).format(date);
};

// 组件挂载时获取数据
onMounted(() => {
  fetchUserData();
  fetchContacts();
});
</script>

<style lang="scss" scoped>
// 主题颜色变量
$primary: #4361ee;
$primary-light: #4cc9f0;
$primary-dark: #3a0ca3;
$accent: #f72585;
$accent-light1: #ffdae3;
$accent-light: #ff85a1;
$accent-secondary: #7209b7;
$gradient-start: #4cc9f0;
$gradient-mid: #4361ee;
$gradient-end: #3a0ca3;
$text: #2b2d42;
$text-light: #8d99ae;
$border: #edf2f4;
$shadow: rgba(67, 97, 238, 0.15);
$glass-bg: rgba(255, 255, 255, 0.6);

// 辅助颜色
$success: #10b981;
$warning: #f59e0b;
$error: #ef4444;
$info: #3b82f6;

// 基础样式
.user-management {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
  color: $text;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
  position: relative;
}

// 页面头部
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid rgba($border, 0.8);

  .page-title {
    font-size: 2rem;
    font-weight: 700;
    color: $primary-dark;
    margin: 0;
    background: linear-gradient(to right, $gradient-start, $gradient-end);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
  }
}

// 内容区域
.main-content {
  width: 100%;
}

.content-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
}

// 卡片组件
.card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 6px 20px $shadow;
  overflow: hidden;
  transition: all 0.3s ease;

  &:hover {
    box-shadow: 0 8px 30px rgba($shadow, 0.3);
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 1.5rem;
    border-bottom: 1px solid rgba($border, 0.8);

    h2 {
      margin: 0;
      font-size: 1.8rem;
      font-weight: 600;
      color: $primary-dark;
    }

    .action-buttons {
      display: flex;
      gap: 0.5rem;
    }
  }
}

// 按钮样式
.btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  padding: 0.6rem 1.2rem;
  border-radius: 8px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  border: none;
  outline: none;
  font-size: 1.2rem;

  .icon {
    font-size: 1.4rem;
  }
}

.refresh-btn {
  background: linear-gradient(to right, $primary-light, $primary);
  color: white;

  &:hover {
    background: linear-gradient(to right, $primary, $primary-dark);
    box-shadow: 0 4px 12px rgba($primary, 0.3);
    transform: translateY(-2px);
  }

  &:active {
    transform: translateY(0);
  }
}

.edit-btn {
  background-color: $primary;
  color: white;

  &:hover {
    background-color: darken($primary, 5%);
    box-shadow: 0 4px 12px rgba($primary, 0.3);
  }
}

.save-btn {
  background-color: $success;
  color: white;

  &:hover {
    background-color: darken($success, 5%);
    box-shadow: 0 4px 12px rgba($success, 0.3);
  }
}

.cancel-btn {
  background-color: $text-light;
  color: white;

  &:hover {
    background-color: darken($text-light, 5%);
    box-shadow: 0 4px 12px rgba($text-light, 0.3);
  }
}

.add-btn {
  background: linear-gradient(to right, $accent, $accent-secondary);
  color: white;

  &:hover {
    background: linear-gradient(to right, darken($accent, 5%), darken($accent-secondary, 5%));
    box-shadow: 0 4px 12px rgba($accent, 0.3);
  }
}

.retry-btn {
  background-color: $info;
  color: white;

  &:hover {
    background-color: darken($info, 5%);
    box-shadow: 0 4px 12px rgba($info, 0.3);
  }
}

.delete-btn {
  background-color: $error;
  color: white;

  &:hover {
    background-color: darken($error, 5%);
    box-shadow: 0 4px 12px rgba($error, 0.3);
  }
}

.icon-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: none;
  outline: none;
  cursor: pointer;
  transition: all 0.2s ease;
  background: rgba($text-light, 0.1);
  color: $text;
  font-size: 1.1rem;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba($shadow, 0.2);
  }

  &:active {
    transform: translateY(0);
  }
}

.edit-icon {
  &:hover {
    background: rgba($primary, 0.1);
    color: $primary;
  }
}

.delete-icon {
  &:hover {
    background: rgba($error, 0.1);
    color: $error;
  }
}

.close-btn {
  font-size: 1.2rem;

  &:hover {
    background: rgba($text-light, 0.2);
  }
}

// 表单样式
.user-form,
.contact-form {
  padding: 1.5rem;

  .form-group {
    margin-bottom: 1.5rem;

    label {
      display: block;
      margin-bottom: 1.4rem;
      font-weight: 500;
      font-size: 1.2rem;
      color: $text;
    }

    input {
      width: 100%;
      padding: 0.8rem 1rem;
      border-radius: 8px;
      border: 1px solid rgba($text-light, 0.3);
      font-size: 1.4rem;
      transition: all 0.2s ease;
      outline: none;

      &:focus {
        border-color: $primary;
        box-shadow: 0 0 0 3px rgba($primary, 0.1);
      }

      &:disabled {
        background-color: rgba($border, 0.5);
        color: $text-light;
        cursor: not-allowed;
      }

      &.input-error {
        border-color: $error;

        &:focus {
          box-shadow: 0 0 0 3px rgba($error, 0.1);
        }
      }
    }

    .error-text {
      margin-top: 0.5rem;
      color: $error;
      font-size: 0.9rem;
    }
  }

  .form-actions {
    display: flex;
    justify-content: flex-end;
    gap: 1rem;
    margin-top: 2rem;
  }

  .form-note {
    margin-top: 1rem;
    font-size: 1.1rem;
    color: $text-light;
    font-style: italic;
  }
}

// 加载和错误状态
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem 1rem;
  gap: 1rem;

  .loading-spinner {
    width: 40px;
    height: 40px;
    border: 4px solid rgba($primary, 0.1);
    border-radius: 50%;
    border-top-color: $primary;
    animation: spin 1s linear infinite;
  }

  @keyframes spin {
    0% {
      transform: rotate(0deg);
    }

    100% {
      transform: rotate(360deg);
    }
  }

  p {
    color: $text-light;
    font-size: 1rem;
  }
}

.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem 1rem;
  gap: 1rem;

  .error-icon {
    font-size: 2.5rem;
    color: $error;
  }

  .error-message {
    color: $text;
    font-size: 1.5rem;
    text-align: center;
    max-width: 80%;
  }
}

// 联系人列表样式
.contacts-list {
  padding: 1.5rem;

  .empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 2rem;
    gap: 1rem;

    .empty-icon {
      margin-top: 4rem;
      font-size: 4rem;
      color: rgba($text-light, 0.5);
    }

    p {
      margin: 0;
      font-size: 2rem;
      color: $text;
      font-weight: 500;
    }

    .empty-subtext {
      font-size: 1.2rem;
      color: $text-light;
    }
  }

  .contact-cards {
    display: flex;
    flex-direction: column;
    gap: 1rem;

    .contact-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 1rem;
      border-radius: 8px;
      border: 1px solid rgba($border, 0.8);
      transition: all 0.2s ease;

      &:hover {
        border-color: $primary;
        background-color: rgba($primary, 0.02);
        box-shadow: 0 4px 12px rgba($shadow, 0.1);
      }

      .contact-details {
        flex: 1;

        .contact-name {
          font-size: 1.1rem;
          font-weight: 500;
          color: $text;
          margin-bottom: 0.5rem;
        }

        .contact-info {
          display: flex;
          flex-direction: column;
          gap: 0.3rem;

          .info-item {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            font-size: 0.95rem;

            .info-label {
              color: $text-light;
            }

            .info-value {
              color: $text;
            }
          }
        }
      }

      .contact-actions {
        display: flex;
        gap: 0.5rem;
      }
    }
  }
}

// 模态框样式
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 100;
  display: flex;
  justify-content: center;
  align-items: center;
  animation: fadeIn 0.2s ease-in-out;

  @keyframes fadeIn {
    from {
      opacity: 0;
    }

    to {
      opacity: 1;
    }
  }
}

.modal-content {
  width: 90%;
  max-width: 500px;
  background-color: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15);
  animation: slideUp 0.3s ease-in-out;

  @keyframes slideUp {
    from {
      transform: translateY(20px);
      opacity: 0;
    }

    to {
      transform: translateY(0);
      opacity: 1;
    }
  }

  .modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 1.5rem;
    border-bottom: 1px solid rgba($border, 0.8);

    h2 {
      margin: 0;
      font-size: 1.4rem;
      font-weight: 600;
      color: $primary-dark;
    }
  }
}

.delete-modal {
  max-width: 400px;

  .delete-confirmation {
    padding: 1.5rem;
    text-align: center;

    p {
      margin: 0 0 1rem;
      font-size: 1.1rem;

      strong {
        font-weight: 600;
      }
    }

    .delete-warning {
      color: $error;
      font-weight: 500;
    }

    .form-actions {
      margin-top: 1.5rem;
      display: flex;
      justify-content: center;
      gap: 1rem;
    }
  }
}

// 通知提示样式
.notification {
  position: fixed;
  bottom: 2rem;
  right: 2rem;
  padding: 1rem 1.5rem;
  border-radius: 12px;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  animation: slideIn 0.3s ease-in-out;

  @keyframes slideIn {
    from {
      transform: translateX(100%);
      opacity: 0;
    }

    to {
      transform: translateX(0);
      opacity: 1;
    }
  }

  &.success {
    background-color: lighten($success, 48%);
    border-left: 4px solid $success;
  }

  &.error {
    background-color: lighten($error, 40%);
    border-left: 4px solid $error;
  }

  .notification-content {
    display: flex;
    align-items: center;
    gap: 0.8rem;

    .notification-icon {
      font-size: 1.2rem;

      .success & {
        color: $success;
      }

      .error & {
        color: $error;
      }
    }

    .notification-message {
      font-size: 0.95rem;
      color: $text;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .user-management {
    padding: 1.5rem;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;

    .refresh-btn {
      align-self: flex-start;
    }
  }

  .content-grid {
    grid-template-columns: 1fr;
  }

  .contact-item {
    flex-direction: column;
    align-items: flex-start;

    .contact-actions {
      margin-top: 1rem;
      align-self: flex-end;
    }
  }

  .button-group {
    display: flex;
    gap: 0.5rem;
  }
}
</style>