<template>
  <div class="register-modal" @click.self="closeModal">
    <div class="register-container" :class="{ 'show': isVisible }">
      <!-- 关闭按钮 -->
      <button class="close-btn" @click="closeModal" aria-label="Close">
        <i class="fas fa-times"></i>
      </button>
      <div class="form">
        <h2 class="title">Create Account</h2>
        
        <div class="input_container">
          <!-- 用户名输入 -->
          <div class="input_field_wrapper" :class="{ 'focused': activeField === 'username' }">
            <input 
              type="text" 
              class="username" 
              :class="{ invalid: username_invalid && username_touched }"
              placeholder="Choose a unique username" 
              v-model="username" 
              @blur="validateUsername"
              @focus="activeField = 'username'"
            >
            <transition name="fade">
              <p v-if="username_invalid && username_touched" class="error_message">
                Username must be 4-16 characters
              </p>
            </transition>
          </div>
          
          <!-- 邮箱输入 -->
          <div class="input_field_wrapper" :class="{ 'focused': activeField === 'email' }">
            <input 
              type="email" 
              class="email" 
              :class="{ invalid: email_invalid && email_touched }"
              placeholder="Your email address" 
              v-model="email" 
              @blur="validateEmail"
              @focus="activeField = 'email'"
            >
            <transition name="fade">
              <p v-if="email_invalid && email_touched" class="error_message">
                Please enter a valid email
              </p>
            </transition>
          </div>
          
          <!-- 密码输入 -->
          <div class="input_field_wrapper" :class="{ 'focused': activeField === 'password' }">
            <div class="password-input-container">
              <input 
                :type="showPassword ? 'text' : 'password'" 
                class="password" 
                :class="{ invalid: password_invalid && password_touched }"
                placeholder="Create a secure password" 
                v-model="password" 
                @blur="validatePassword"
                @focus="activeField = 'password'"
              >
              <button 
                type="button" 
                class="toggle-password" 
                @click="showPassword = !showPassword"
                :aria-label="showPassword ? 'Hide password' : 'Show password'"
              >
                <i :class="showPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
              </button>
            </div>
            <div class="password-strength" v-if="password.length > 0">
              <div class="strength-meter">
                <div class="strength-bar" :style="{ width: passwordStrength + '%', backgroundColor: passwordStrengthColor }"></div>
              </div>
              <span class="strength-text" :style="{ color: passwordStrengthColor }">{{ passwordStrengthText }}</span>
            </div>
            <transition name="fade">
              <p v-if="password_invalid && password_touched" class="error_message">
                Password must be 8-20 characters with at least one number
              </p>
            </transition>
          </div>
          
          <!-- 确认密码 -->
          <div class="input_field_wrapper" :class="{ 'focused': activeField === 'confirm_password' }">
            <div class="password-input-container">
              <input 
                :type="showConfirmPassword ? 'text' : 'password'" 
                class="confirm-password" 
                :class="{ invalid: confirm_password_invalid && confirm_password_touched }"
                placeholder="Confirm your password" 
                v-model="confirm_password" 
                @blur="validateConfirmPassword"
                @focus="activeField = 'confirm_password'"
              >
              <button 
                type="button" 
                class="toggle-password" 
                @click="showConfirmPassword = !showConfirmPassword"
                :aria-label="showConfirmPassword ? 'Hide password' : 'Show password'"
              >
                <i :class="showConfirmPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
              </button>
            </div>
            <transition name="fade">
              <p v-if="confirm_password_invalid && confirm_password_touched" class="error_message">
                Passwords do not match
              </p>
            </transition>
          </div>
          
          <!-- 验证码 -->
          <div class="input_field_wrapper verification-code-wrapper" :class="{ 'focused': activeField === 'verification' }">
            <div class="verification-input-container">
              <input 
                type="text" 
                class="verification-code" 
                :class="{ invalid: verificationCode_invalid && verificationCode_touched }"
                placeholder="6-digit code" 
                v-model="verificationCode" 
                @blur="validateVerificationCode"
                @focus="activeField = 'verification'"
                maxlength="6"
              >
              <button 
                class="send-code-btn" 
                @click="sendVerificationCode" 
                :disabled="sendCodeDisabled"
                :class="{ 'sending': countdown > 0 }"
              >
                <span class="btn-text">{{ sendCodeBtnText }}</span>
                <span class="btn-icon" v-if="isSendingCode">
                  <svg class="spinner" viewBox="0 0 50 50">
                    <circle class="path" cx="25" cy="25" r="20" fill="none" stroke-width="5"></circle>
                  </svg>
                </span>
              </button>
            </div>
            <transition name="fade">
              <p v-if="verificationCode_invalid && verificationCode_touched" class="error_message">
                Please enter 6-digit code
              </p>
            </transition>
          </div>
        </div>
        
        <!-- 条款同意 -->
        <div class="terms_container" :class="{ 'terms-error': terms_invalid }">
          <div class="checkbox-wrapper">
            <input type="checkbox" id="terms" v-model="terms_accepted" class="terms_checkbox">
            <label for="terms" class="checkbox-label"></label>
          </div>
          <label for="terms" class="terms-text">
            I agree to the <a href="#" class="terms_link" @click.prevent="showTerms">Terms and Conditions</a>
          </label>
          <transition name="fade">
            <p v-if="terms_invalid" class="error_message terms_error">You must accept the terms</p>
          </transition>
        </div>
        
        <!-- 注册按钮 -->
        <div class="button_container">
          <button 
            class="register_button" 
            @click="handleRegister" 
            :disabled="isRegistering"
          >
            <span class="button-text">{{ isRegistering ? 'Creating Account...' : 'Create Account' }}</span>
            <span class="button-icon" v-if="isRegistering">
              <svg class="spinner" viewBox="0 0 50 50">
                <circle class="path" cx="25" cy="25" r="20" fill="none" stroke-width="5"></circle>
              </svg>
            </span>
          </button>
          
          <div class="login_redirect">
            Already have an account? <a href="#" @click.prevent="switchToLogin" class="login_link">Login</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { userRegister } from '@/api/UserApi.js';

const router = useRouter();
const emit = defineEmits(['close', 'switch-to-login']);

// 动画控制
const isVisible = ref(false);
const activeField = ref(null);

// 表单数据
const username = ref('');
const email = ref('');
const password = ref('');
const confirm_password = ref('');
const verificationCode = ref('');
const terms_accepted = ref(false);

// 密码可见性
const showPassword = ref(false);
const showConfirmPassword = ref(false);

// 验证状态
const username_invalid = ref(false);
const email_invalid = ref(false);
const password_invalid = ref(false);
const confirm_password_invalid = ref(false);
const verificationCode_invalid = ref(false);
const terms_invalid = ref(false);

// 触摸状态
const username_touched = ref(false);
const email_touched = ref(false);
const password_touched = ref(false);
const confirm_password_touched = ref(false);
const verificationCode_touched = ref(false);

// 验证码发送状态
const countdown = ref(0);
const isSendingCode = ref(false);
const isRegistering = ref(false);

const sendCodeDisabled = computed(() => {
  return countdown.value > 0 || email_invalid.value || email.value.length === 0 || isSendingCode.value;
});

const sendCodeBtnText = computed(() => {
  if (isSendingCode.value) return 'Sending...';
  return countdown.value > 0 ? `${countdown.value}s` : 'Send Code';
});

// 密码强度计算
const passwordStrength = computed(() => {
  if (password.value.length === 0) return 0;
  
  let strength = 0;
  // 长度检查
  if (password.value.length >= 8) strength += 25;
  if (password.value.length >= 12) strength += 15;
  
  // 复杂性检查
  if (/[A-Z]/.test(password.value)) strength += 15;
  if (/[a-z]/.test(password.value)) strength += 10;
  if (/[0-9]/.test(password.value)) strength += 15;
  if (/[^A-Za-z0-9]/.test(password.value)) strength += 20;
  
  return Math.min(strength, 100);
});

const passwordStrengthText = computed(() => {
  const strength = passwordStrength.value;
  if (strength < 30) return 'Weak';
  if (strength < 60) return 'Medium';
  if (strength < 80) return 'Strong';
  return 'Very Strong';
});

const passwordStrengthColor = computed(() => {
  const strength = passwordStrength.value;
  if (strength < 30) return '#ff4757';
  if (strength < 60) return '#ffa502';
  if (strength < 80) return '#2ed573';
  return '#1e90ff';
});

// 关闭弹窗
const closeModal = () => {
  isVisible.value = false;
  setTimeout(() => {
    emit('close');
  }, 400); // 匹配过渡动画的持续时间
};

// 切换到登录
const switchToLogin = () => {
  closeModal();
  setTimeout(() => {
    emit('switch-to-login');
  }, 400);
};

// 显示条款
const showTerms = () => {
  // 这里可以实现显示条款的逻辑，例如打开一个新的模态框
  alert('Terms and Conditions will be displayed here');
};

// 验证方法
const validateUsername = () => {
  username_touched.value = true;
  username_invalid.value = username.value.length < 4 || username.value.length > 16;
  activeField.value = null;
};

const validateEmail = () => {
  email_touched.value = true;
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  email_invalid.value = !emailRegex.test(email.value);
  activeField.value = null;
};

const validatePassword = () => {
  password_touched.value = true;
  const hasNumber = /\d/.test(password.value);
  password_invalid.value = password.value.length < 8 ||
      password.value.length > 20 ||
      !hasNumber;
  activeField.value = null;
};

const validateConfirmPassword = () => {
  confirm_password_touched.value = true;
  confirm_password_invalid.value = password.value !== confirm_password.value;
  activeField.value = null;
};

const validateVerificationCode = () => {
  verificationCode_touched.value = true;
  verificationCode_invalid.value = verificationCode.value.length !== 6;
  activeField.value = null;
};

// 发送验证码
const sendVerificationCode = async () => {
  validateEmail();
  if (email_invalid.value) return;
  
  isSendingCode.value = true;
  
  try {
    // 修正API调用，确保发送正确的数据格式
    console.log(email.value);
    
    await axios.post('/api/user/register/sendEmail', { email: email.value });
    
    // 开始倒计时
    countdown.value = 60;
    const timer = setInterval(() => {
      countdown.value--;
      if (countdown.value <= 0) {
        clearInterval(timer);
      }
    }, 1000);
    
  } catch (error) {
    console.error('Failed to send verification code:', error);
    // 显示错误提示
    alert('Failed to send verification code. Please try again.');
  } finally {
    isSendingCode.value = false;
  }
};

// 表单验证
const validateForm = () => {
  validateUsername();
  validateEmail();
  validatePassword();
  validateConfirmPassword();
  validateVerificationCode();
  terms_invalid.value = !terms_accepted.value;
  
  return !username_invalid.value &&
      !email_invalid.value &&
      !password_invalid.value &&
      !confirm_password_invalid.value &&
      !verificationCode_invalid.value &&
      terms_accepted.value;
};

// 注册处理
const handleRegister = async () => {
  if (!validateForm()) return;
  
  isRegistering.value = true;
  
  try {
    // 修正请求格式以匹配后端期望的结构
    const response = await userRegister({
      user: {
        username: username.value,
        email: email.value,
        password: password.value
      },
      verificationCode: verificationCode.value
    });
    
    if (response.success || response.code === 200) {
      // 显示成功消息
      alert('Registration successful! You can now login.');
      
      // 关闭注册模态框，可能跳转到登录
      closeModal();
    } else {
      // 显示错误信息
      alert(`Registration failed: ${response.message || 'Unknown error'}`);
    }
  } catch (error) {
    console.error('Registration error:', error);
    alert('Registration failed. Please try again later.');
  } finally {
    isRegistering.value = false;
  }
};

// 监听密码变化，自动验证确认密码
watch(password, () => {
  if (confirm_password.value && confirm_password_touched.value) {
    validateConfirmPassword();
  }
});

// 组件挂载时触发动画
onMounted(() => {
  // 添加小延迟以确保DOM已更新
  setTimeout(() => {
    isVisible.value = true;
  }, 50);
  
  // 添加键盘事件监听器，按ESC关闭模态框
  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') closeModal();
  });
});
</script>

<style lang="scss" scoped>
// 现代配色方案
$primary-color: #4a8eff;
$primary-light: #6ba3ff;
$primary-dark: #3270e9;
$success-color: #2ed573;
$warning-color: #ffa502;
$error-color: #ff4757;
$text-color: #2c3e50;
$text-light: #5d7290;
$bg-light: #fafbff;
$white: #ffffff;
$gray-light: #f1f5f9;
$gray: #e2e8f0;
$gray-dark: #64748b;
$shadow-color: rgba(50, 112, 233, 0.08);
$animation-timing: cubic-bezier(0.22, 1, 0.36, 1);

// 动画关键帧
@keyframes modalFadeIn {
  0% {
    background-color: rgba(0, 0, 0, 0);
    backdrop-filter: blur(0px);
  }
  100% {
    background-color: rgba(0, 0, 0, 0.5);
    backdrop-filter: blur(10px);
  }
}

@keyframes containerSlideIn {
  0% {
    opacity: 0;
    transform: translateY(30px) scale(0.95);
    box-shadow: 0 0 0 rgba(0, 0, 0, 0);
  }
  100% {
    opacity: 1;
    transform: translateY(0) scale(1);
    box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
  100% {
    transform: scale(1);
  }
}

@keyframes shake {
  10%, 90% {
    transform: translateX(-1px);
  }
  20%, 80% {
    transform: translateX(2px);
  }
  30%, 50%, 70% {
    transform: translateX(-4px);
  }
  40%, 60% {
    transform: translateX(4px);
  }
}

@keyframes rotate {
  100% {
    transform: rotate(360deg);
  }
}

@keyframes dash {
  0% {
    stroke-dasharray: 1, 150;
    stroke-dashoffset: 0;
  }
  50% {
    stroke-dasharray: 90, 150;
    stroke-dashoffset: -35;
  }
  100% {
    stroke-dasharray: 90, 150;
    stroke-dashoffset: -124;
  }
}

.register-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  animation: modalFadeIn 0.5s $animation-timing forwards;
  
  .register-container {
    position: relative;
    width: 1000px;
    display: flex;
    justify-content: space-between;
    max-width: 90%;
    max-height: 90vh;
    overflow-y: auto;
    background-color: $white;
    border-radius: 30px;
    padding: 4rem;
    opacity: 0;
    transform: translateY(30px) scale(0.95);
    transition: all 0.5s $animation-timing;
    background-image: linear-gradient(105deg, rgba(0, 0, 0, 0.1) 0%, rgba(0, 0, 0, 0.1) 47%, rgba(248, 249, 250, 0.1) 52%, transparent 55%), url('@/assets/pic/micke-lindstrom-emvCxhS7OaQ-unsplash.jpg');
    background-size: cover;
    background-position: center;
    background-blend-mode: multiply;
    box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
    
    &.show {
      opacity: 1;
      transform: translateY(0) scale(1);
    }
    
    &::-webkit-scrollbar {
      width: 8px;
    }
    
    &::-webkit-scrollbar-track {
      background: transparent;
    }
    
    &::-webkit-scrollbar-thumb {
      background-color: rgba(255, 255, 255, 0.3);
      border-radius: 20px;
    }
    
    .close-btn {
      position: absolute;
      top: 20px;
      right: 20px;
      width: 40px;
      height: 40px;
      background: rgba(255, 255, 255, 0.2);
      border: none;
      cursor: pointer;
      font-size: 1.5rem;
      color: $gray-dark;
      transition: all 0.3s ease;
      display: flex;
      justify-content: center;
      align-items: center;
      border-radius: 50%;
      z-index: 10;
      backdrop-filter: blur(5px);
      
      &:hover {
        color: $error-color;
        background-color: rgba(255, 255, 255, 0.5);
        transform: rotate(90deg);
      }
      
      &:active {
        transform: scale(0.95) rotate(90deg);
      }
    }
    
    .form {
      position: relative;
      background: rgba(255, 255, 255, 0.85);
      backdrop-filter: blur(10px);
      border-radius: 20px;
      padding: 3rem;
      width: 40%;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
      
      .title {
        font-size: 3rem;
        font-weight: 900;
        color: $text-color;
        margin-bottom: 2rem;
        opacity: 0;
        animation: fadeInDown 0.6s $animation-timing 0.3s forwards;
        background: linear-gradient(135deg, $primary-dark, $primary-light);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
      }
      
      .input_container {
        display: flex;
        flex-direction: column;
        gap: 1.8rem;
        margin-bottom: 1.5rem;
        width: 100%;
        
        .input_field_wrapper {
          position: relative;
          width: 100%;
          margin-bottom: 1rem;
          opacity: 0;
          animation: fadeInUp 0.5s $animation-timing forwards;
          animation-delay: calc(0.4s + var(--i, 0) * 0.1s);
          
          &:nth-child(1) { --i: 1; }
          &:nth-child(2) { --i: 2; }
          &:nth-child(3) { --i: 3; }
          &:nth-child(4) { --i: 4; }
          &:nth-child(5) { --i: 5; }
          
          &.focused {
            .field-label {
              color: $primary-color;
              transform: translateY(-25px) scale(0.85);
            }
          }
          
          .field-label {
            position: absolute;
            top: 0;
            left: 0;
            font-size: 1rem;
            color: $text-light;
            pointer-events: none;
            transition: all 0.3s ease;
            transform: translateY(-25px) scale(0.85);
            transform-origin: left;
          }
          
          input {
            width: 100%;
            height: 5rem;
            outline: none;
            border: 1px solid $gray;
            border-radius: 12px;
            padding: 0 1.5rem;
            font-size: 1.5rem;
            transition: all 0.3s ease;
            background-color: rgba(255, 255, 255, 0.9);
            color: $text-color;
            
            &:focus {
              border-color: $primary-color;
              box-shadow: 0 0 0 3px rgba($primary-color, 0.2);
              transform: translateY(-2px);
            }
            
            &::placeholder {
              color: $gray-dark;
              font-style: italic;
              font-weight: 400;
              font-size: 1.3rem;
              opacity: 0.7;
              transition: opacity 0.3s;
            }
            
            &.invalid {
              border-color: $error-color;
              box-shadow: 0 0 0 2px rgba($error-color, 0.2);
              animation: shake 0.5s cubic-bezier(.36,.07,.19,.97) both;
            }
          }
          
          .password-input-container,
          .verification-input-container {
            position: relative;
            width: 100%;
            
            .toggle-password {
              position: absolute;
              right: 15px;
              top: 50%;
              transform: translateY(-50%);
              background: transparent;
              border: none;
              cursor: pointer;
              color: $gray-dark;
              font-size: 1.2rem;
              transition: all 0.3s ease;
              
              &:hover {
                color: $primary-color;
              }
            }
          }
          
          .password-strength {
            margin-top: 8px;
            width: 100%;
            
            .strength-meter {
              height: 4px;
              background-color: $gray;
              border-radius: 2px;
              margin-bottom: 5px;
              overflow: hidden;
              
              .strength-bar {
                height: 100%;
                border-radius: 2px;
                transition: width 0.5s ease, background-color 0.5s ease;
              }
            }
            
            .strength-text {
              font-size: 0.8rem;
              font-weight: 600;
              transition: color 0.5s ease;
            }
          }
          
          &.verification-code-wrapper {
            .verification-input-container {
              input {
                padding-right: 110px;
              }
              
              .send-code-btn {
                position: absolute;
                right: 5px;
                top: 50%;
                transform: translateY(-50%);
                padding: 0.6rem 1rem;
                background: $primary-color;
                color: $white;
                border: none;
                border-radius: 8px;
                font-size: 0.9rem;
                font-weight: 600;
                cursor: pointer;
                transition: all 0.3s ease;
                display: flex;
                align-items: center;
                justify-content: center;
                
                &:hover:not(:disabled) {
                  background: $primary-dark;
                  transform: translateY(-50%) scale(1.02);
                }
                
                &:disabled {
                  background: $gray;
                  color: $gray-dark;
                  cursor: not-allowed;
                }
                
                &.sending {
                  background: $primary-light;
                }
                
                .btn-icon {
                  margin-left: 8px;
                }
                
                .spinner {
                  animation: rotate 2s linear infinite;
                  width: 16px;
                  height: 16px;
                  
                  .path {
                    stroke: $white;
                    stroke-linecap: round;
                    animation: dash 1.5s ease-in-out infinite;
                  }
                }
              }
            }
          }
        }
      }
      
      .terms_container {
        display: flex;
        align-items: flex-start;
        margin: 1rem 0 2rem;
        position: relative;
        width: 100%;
        opacity: 0;
        animation: fadeInUp 0.5s $animation-timing 0.9s forwards;
        
        &.terms-error {
          .checkbox-wrapper .checkbox-label {
            border-color: $error-color;
            box-shadow: 0 0 0 2px rgba($error-color, 0.2);
            animation: shake 0.5s cubic-bezier(.36,.07,.19,.97) both;
          }
        }
        
        .checkbox-wrapper {
          position: relative;
          margin-right: 10px;
          
          .terms_checkbox {
            position: absolute;
            opacity: 0;
            cursor: pointer;
            height: 0;
            width: 0;
            
            &:checked + .checkbox-label:after {
              opacity: 1;
              transform: scale(1);
            }
          }
          
          .checkbox-label {
            position: relative;
            display: inline-block;
            width: 22px;
            height: 22px;
            background-color: $white;
            border: 2px solid $gray;
            border-radius: 4px;
            cursor: pointer;
            transition: all 0.3s ease;
            
            &:hover {
              border-color: $primary-color;
            }
            
            &:after {
              content: '';
              position: absolute;
              left: 7px;
              top: 3px;
              width: 5px;
              height: 10px;
              border: solid $primary-color;
              border-width: 0 2px 2px 0;
              transform: rotate(45deg) scale(0);
              opacity: 0;
              transition: all 0.2s cubic-bezier(0.175, 0.885, 0.32, 1.275);
            }
          }
        }
        
        .terms-text {
          font-size: 0.95rem;
          color: $text-light;
          cursor: pointer;
          line-height: 1.4;
          padding-top: 2px;
        }
        
        .terms_link {
          color: $primary-color;
          text-decoration: none;
          font-weight: 600;
          transition: all 0.3s ease;
          position: relative;
          
          &:after {
            content: '';
            position: absolute;
            width: 100%;
            height: 1px;
            bottom: -1px;
            left: 0;
            background-color: $primary-color;
            transform: scaleX(0);
            transform-origin: bottom right;
            transition: transform 0.3s ease;
          }
          
          &:hover {
            color: $primary-dark;
            
            &:after {
              transform: scaleX(1);
              transform-origin: bottom left;
            }
          }
        }
        
        .terms_error {
          position: absolute;
          bottom: -1.5rem;
          left: 0;
          color: $error-color;
          font-size: 0.8rem;
          font-weight: 600;
        }
      }
      
      .button_container {
        width: 100%;
        display: flex;
        flex-direction: column;
        gap: 1.5rem;
        opacity: 0;
        animation: fadeInUp 0.5s $animation-timing 1.1s forwards;
        
        .register_button {
          width: 100%;
          height: 5rem;
          display: flex;
          align-items: center;
          justify-content: center;
          background: linear-gradient(135deg, $primary-color, $primary-dark);
          color: $white;
          font-size: 1.5rem;
          font-weight: 700;
          border: none;
          border-radius: 12px;
          cursor: pointer;
          transition: all 0.3s ease;
          position: relative;
          overflow: hidden;
          box-shadow: 0 4px 15px rgba($primary-color, 0.3);
          
          &::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
            transition: all 0.6s;
          }
          
          &:hover:not(:disabled) {
            transform: translateY(-3px);
            box-shadow: 0 6px 20px rgba($primary-color, 0.4);
            
            &::before {
              left: 100%;
            }
          }
          
          &:active:not(:disabled) {
            transform: translateY(1px);
            box-shadow: 0 2px 10px rgba($primary-color, 0.3);
          }
          
          &:disabled {
            opacity: 0.7;
            cursor: not-allowed;
            background: linear-gradient(135deg, $gray, $gray-dark);
          }
          
          .button-icon {
            margin-left: 10px;
          }
          
          .spinner {
            animation: rotate 2s linear infinite;
            width: 20px;
            height: 20px;
            
            .path {
              stroke: $white;
              stroke-linecap: round;
              animation: dash 1.5s ease-in-out infinite;
            }
          }
        }
        
        .login_redirect {
          text-align: center;
          font-size: 1rem;
          color: $text-light;
          margin-top: 0.5rem;
          
          .login_link {
            color: $primary-color;
            text-decoration: none;
            font-weight: 700;
            margin-left: 5px;
            transition: all 0.3s ease;
            position: relative;
            
            &:after {
              content: '';
              position: absolute;
              width: 100%;
              height: 2px;
              bottom: -2px;
              left: 0;
              background-color: $primary-color;
              transform: scaleX(0);
              transform-origin: bottom right;
              transition: transform 0.3s ease;
            }
            
            &:hover {
              color: $primary-dark;
              
              &:after {
                transform: scaleX(1);
                transform-origin: bottom left;
              }
            }
          }
        }
      }
    }
  }
}

.error_message {
  position: absolute;
  left: 0.5rem;
  bottom: -1.5rem;
  color: $error-color;
  font-size: 0.8rem;
  font-weight: 600;
  transition: all 0.3s ease;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .register-container {
    .form {
      width: 50% !important;
    }
  }
}

@media (max-width: 992px) {
  .register-container {
    .form {
      width: 60% !important;
    }
  }
}

@media (max-width: 768px) {
  .register-container {
    padding: 3rem !important;
    
    .form {
      width: 100% !important;
      padding: 2.5rem !important;
      
      .title {
        font-size: 2.5rem;
      }
    }
  }
}

@media (max-width: 576px) {
  .register-container {
    padding: 2rem !important;
    max-width: 95% !important;
    
    .close-btn {
      top: 15px;
      right: 15px;
      width: 35px;
      height: 35px;
      font-size: 1.2rem;
    }
    
    .form {
      padding: 2rem !important;
      
      .title {
        font-size: 2.2rem;
        margin-bottom: 1.5rem;
      }
      
      .input_container {
        gap: 1.5rem;
        
        .input_field_wrapper {
          input {
            height: 4.5rem;
            font-size: 1.3rem;
            padding: 0 1.2rem;
          }
          
          &.verification-code-wrapper {
            .verification-input-container {
              .send-code-btn {
                padding: 0.5rem 0.8rem;
                font-size: 0.8rem;
              }
            }
          }
        }
      }
      
      .button_container {
        .register_button {
          height: 4.5rem;
          font-size: 1.3rem;
        }
      }
    }
  }
}

@media (max-width: 400px) {
  .register-container {
    .form {
      padding: 1.5rem !important;
      
      .title {
        font-size: 2rem;
      }
      
      .input_container {
        .input_field_wrapper {
          input {
            height: 4rem;
            font-size: 1.1rem;
          }
          
          .field-label {
            font-size: 0.9rem;
          }
        }
      }
      
      .terms_container {
        .terms-text {
          font-size: 0.85rem;
        }
      }
      
      .button_container {
        .register_button {
          height: 4rem;
          font-size: 1.2rem;
        }
        
        .login_redirect {
          font-size: 0.9rem;
        }
      }
    }
  }
}
</style>