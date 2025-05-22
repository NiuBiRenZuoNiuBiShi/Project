<template>
  <div class="register-modal">
    <div class="register-container">
      <!-- 关闭按钮 -->
      <button class="close-btn" @click="closeModal">
        <i class="fas fa-times"></i>
      </button>

      <div class="form">
        <h2 class="title">Create Account</h2>

        <div class="input_container">
          <!-- 用户名输入 -->
          <div class="input_field_wrapper">
            <input type="text" class="username" :class="{ invalid: username_invalid }"
                   placeholder="Username" v-model="username" @blur="validateUsername">
            <transition name="fade">
              <p v-if="username_invalid && username_touched" class="error_message">
                Username must be 4-16 characters
              </p>
            </transition>
          </div>

          <!-- 邮箱输入 -->
          <div class="input_field_wrapper">
            <input type="email" class="email" :class="{ invalid: email_invalid }"
                   placeholder="Email" v-model="email" @blur="validateEmail">
            <transition name="fade">
              <p v-if="email_invalid && email_touched" class="error_message">
                Please enter a valid email
              </p>
            </transition>
          </div>

          <!-- 密码输入 -->
          <div class="input_field_wrapper">
            <input type="password" class="password" :class="{ invalid: password_invalid }"
                   placeholder="Password" v-model="password" @blur="validatePassword">
            <transition name="fade">
              <p v-if="password_invalid && password_touched" class="error_message">
                Password must be 8-20 characters with at least one number
              </p>
            </transition>
          </div>

          <!-- 确认密码 -->
          <div class="input_field_wrapper">
            <input type="password" class="confirm-password" :class="{ invalid: confirm_password_invalid }"
                   placeholder="Confirm Password" v-model="confirm_password" @blur="validateConfirmPassword">
            <transition name="fade">
              <p v-if="confirm_password_invalid && confirm_password_touched" class="error_message">
                Passwords do not match
              </p>
            </transition>
          </div>

          <!-- 验证码 -->
          <div class="input_field_wrapper verification-code-wrapper">
            <input type="text" class="verification-code" :class="{ invalid: verificationCode_invalid }"
                   placeholder="Verification Code" v-model="verificationCode" @blur="validateVerificationCode"
                   maxlength="6">
            <button class="send-code-btn" @click="sendVerificationCode" :disabled="sendCodeDisabled">
              {{ sendCodeBtnText }}
            </button>
            <transition name="fade">
              <p v-if="verificationCode_invalid && verificationCode_touched" class="error_message">
                Please enter 6-digit code
              </p>
            </transition>
          </div>
        </div>

        <!-- 条款同意 -->
        <div class="terms_container">
          <input type="checkbox" id="terms" v-model="terms_accepted" class="terms_checkbox">
          <label for="terms">I agree to the <a href="#" class="terms_link">Terms and Conditions</a></label>
          <transition name="fade">
            <p v-if="terms_invalid" class="error_message terms_error">You must accept the terms</p>
          </transition>
        </div>

        <!-- 注册按钮 -->
        <div class="button_container">
          <button class="register_button" @click="handleRegister">Register</button>
          <div class="login_redirect">
            Already have an account? <a href="#" @click.prevent="switchToLogin" class="login_link">Login</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { userRegister } from '@/api/UserApi.js';

const router = useRouter();
const emit = defineEmits(['close', 'switch-to-login']);

// 表单数据
const username = ref('');
const email = ref('');
const password = ref('');
const confirm_password = ref('');
const verificationCode = ref('');
const terms_accepted = ref(false);

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
const sendCodeDisabled = computed(() => countdown.value > 0 || email_invalid.value);
const sendCodeBtnText = computed(() => countdown.value > 0 ? `${countdown.value}s` : 'Send Code');

// 关闭弹窗
const closeModal = () => {
  emit('close');
};

// 切换到登录
const switchToLogin = () => {
  closeModal();
  emit('switch-to-login');
};

// 验证方法
const validateUsername = () => {
  username_touched.value = true;
  username_invalid.value = username.value.length < 4 || username.value.length > 16;
};

const validateEmail = () => {
  email_touched.value = true;
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  email_invalid.value = !emailRegex.test(email.value);
};

const validatePassword = () => {
  password_touched.value = true;
  const hasNumber = /\d/.test(password.value);
  password_invalid.value = password.value.length < 8 ||
      password.value.length > 20 ||
      !hasNumber;
};

const validateConfirmPassword = () => {
  confirm_password_touched.value = true;
  confirm_password_invalid.value = password.value !== confirm_password.value;
};

const validateVerificationCode = () => {
  verificationCode_touched.value = true;
  verificationCode_invalid.value = verificationCode.value.length !== 6;
};

// 发送验证码
const sendVerificationCode = async () => {
  validateEmail();
  if (email_invalid.value) return;

  try {
    // 替换为实际的API调用
    await axios.post('/api/send-verification-code', { email: email.value });
    countdown.value = 60;
    const timer = setInterval(() => {
      countdown.value--;
      if (countdown.value <= 0) {
        clearInterval(timer);
      }
    }, 1000);
  } catch (error) {
    console.error('Failed to send verification code:', error);
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

  try {
    const response = await userRegister({
      user: {
        username: username.value,
        email: email.value,
        password: password.value
      },
      verificationCode: verificationCode.value
    });

    if (response.success) {
      // 注册成功后可以自动登录或跳转到登录页面
      router.push('/login');
      closeModal();
    } else {
      console.error('Registration failed:', response.message);
      // 可以在这里添加错误提示显示逻辑
    }
  } catch (error) {
    console.error('Registration error:', error);
    // 可以在这里添加错误提示显示逻辑
  }
};
</script>

<style lang="scss" scoped>
$primary-color: #a3d2ca;
$primary-hover: #5eaaa8;
$error-color: #ff4757;
$text-color: #333;
$white: #ffffff;
$gray: #cccccc;
$dark-gray: #555;

.register-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1000;

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
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
    background-image: linear-gradient(105deg, rgba(0, 0, 0, 0.1) 0%, rgba(0, 0, 0, 0.1) 47%, rgba(248, 249, 250, 0.1) 52%, transparent 55%), url('@/assets/pic/micke-lindstrom-emvCxhS7OaQ-unsplash.jpg');
    background-size: cover;
    background-position: center;
    background-blend-mode: multiply;

    .close-btn {
      position: absolute;
      top: 20px;
      right: 20px;
      width: 40px;
      height: 40px;
      background: transparent;
      border: none;
      cursor: pointer;
      font-size: 1.5rem;
      color: $gray;
      transition: all 0.3s ease;
      display: flex;
      justify-content: center;
      align-items: center;
      border-radius: 50%;
      z-index: 10;

      &:hover {
        color: #ff0000;
        background-color: rgba(255, 255, 255, 0.3);
        transform: scale(1.1);
      }
    }

    .form {
      position: relative;
      background: rgba(255, 255, 255, 0.85);
      backdrop-filter: blur(5px);
      border-radius: 20px;
      padding: 3rem;
      width: 40%;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);

      .title {
        font-size: 3rem;
        font-weight: 900;
        color: #222;
        margin-bottom: 3%;
      }

      .input_container {
        display: flex;
        flex-direction: column;
        gap: 1.2rem;
        margin-bottom: 1.5rem;

        .input_field_wrapper {
          position: relative;
          width: 100%;
          margin-bottom: 1rem;

          input {
            width: 100%;
            height: 5.5rem;
            outline: none;
            border: 1px solid rgb(231 231 231);
            border-radius: 12px;
            padding: 0 2rem;
            font-size: 1.5rem;
            transition: all 0.2s ease-in-out;
            background-color: rgba(255, 255, 255, 0.9);

            &:focus {
              border-color: $primary-color;
              box-shadow: 0 0 0 2px rgba($primary-color, 0.2);
              outline: none;
            }

            &.invalid {
              border-color: $error-color;
              box-shadow: 0 0 0 2px rgba($error-color, 0.2);
            }
          }

          &.verification-code-wrapper {
            input {
              padding-right: 110px;
            }

            .send-code-btn {
              position: absolute;
              right: 5px;
              top: 50%;
              transform: translateY(-50%);
              padding: 0.5rem 0.8rem;
              background: $primary-color;
              color: $white;
              border: none;
              border-radius: 6px;
              font-size: 0.9rem;
              cursor: pointer;
              transition: all 0.3s;

              &:hover {
                background: $primary-hover;
              }

              &:disabled {
                background: $gray;
                cursor: not-allowed;
              }
            }
          }

          .error_message {
            position: absolute;
            left: 0;
            bottom: -1.3rem;
            color: $error-color;
            font-size: 0.8rem;
          }
        }
      }

      .terms_container {
        display: flex;
        align-items: center;
        margin-bottom: 1.5rem;
        font-size: 0.9rem;
        position: relative;

        .terms_checkbox {
          margin-right: 0.5rem;
          accent-color: $primary-color;
        }

        .terms_link {
          color: $primary-color;
          text-decoration: none;
          margin-left: 0.3rem;

          &:hover {
            text-decoration: underline;
          }
        }

        .terms_error {
          position: absolute;
          bottom: -1.3rem;
          left: 0;
        }
      }

      .button_container {
        display: flex;
        flex-direction: column;
        gap: 1rem;

        .register_button {
          width: 100%;
          padding: 0.9rem;
          background: $primary-color;
          color: $white;
          border: none;
          border-radius: 8px;
          font-size: 1rem;
          font-weight: 600;
          cursor: pointer;
          transition: all 0.3s;

          &:hover {
            background: $primary-hover;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba($primary-color, 0.3);
          }
        }

        .login_redirect {
          text-align: center;
          font-size: 0.9rem;
          color: $dark-gray;

          .login_link {
            color: $primary-color;
            text-decoration: none;
            font-weight: 600;

            &:hover {
              text-decoration: underline;
              cursor: pointer;
            }
          }
        }
      }
    }
  }
}

/* 过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

@media (max-width: 600px) {
  .register-container {
    padding: 1.5rem;

    .form {
      padding: 1.5rem;
    }
  }
}
</style>