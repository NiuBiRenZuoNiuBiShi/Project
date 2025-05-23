<template>
  <div class="view-page">
    <!-- Header Section -->
    <header class="header">
      <div class="logo">MyApp</div>
      <nav class="nav">
        <ul>
          <li><a href="http://localhost:5173/home" class="nav-link"><i class="fas fa-home home-icon"></i> Home</a></li>
          <li><a href="http://localhost:5173/about" class="nav-link"><i class="fa-solid fa-address-card"></i>About</a></li>
          <li><a href="#" @click.prevent="toggleSidebar" class="nav-link"><i class="fa-solid fa-phone-volume"></i>Contact</a></li>
        </ul>
      </nav>
    </header>

    <!-- Sidebar Section -->
    <div class="sidebar" :class="{ show: showSidebar }">
      <div class="sidebar-content">
        <button @click="toggleSidebar" class="close-btn">X</button>
        <p><i class="fa-solid fa-phone-volume"></i>: 123-456-7890</p>
        <p><i class="fa-solid fa-envelope"></i>: contact@myapp.com</p>
      </div>
    </div>

    <!-- Main Content Section -->
    <main class="content">
      <div class="register_container">
        <div class="form_background"></div>
        <div class="form">
          <h2 class="title">Create Account</h2>
          <div class="input_container">
            <div class="input_field_wrapper">
              <input type="text" class="username" :class="{ invalid: username_invalid }"
                     placeholder="Username" v-model="username" @blur="validateUsername">
              <transition name="fade">
                <p v-if="username_invalid && username_touched" class="error_message">
                  Username must be 4-16 characters
                </p>
              </transition>
            </div>

            <div class="input_field_wrapper">
              <input type="email" class="email" :class="{ invalid: email_invalid }"
                     placeholder="Email" v-model="email" @blur="validateEmail">
              <transition name="fade">
                <p v-if="email_invalid && email_touched" class="error_message">
                  Please enter a valid email
                </p>
              </transition>
            </div>

            <div class="input_field_wrapper">
              <input type="password" class="password" :class="{ invalid: password_invalid }"
                     placeholder="Password" v-model="password" @blur="validatePassword">
              <transition name="fade">
                <p v-if="password_invalid && password_touched" class="error_message">
                  Password must be 8-20 characters with at least one number
                </p>
              </transition>
            </div>

            <div class="input_field_wrapper">
              <input type="password" class="confirm-password" :class="{ invalid: confirm_password_invalid }"
                     placeholder="Confirm Password" v-model="confirm_password" @blur="validateConfirmPassword">
              <transition name="fade">
                <p v-if="confirm_password_invalid && confirm_password_touched" class="error_message">
                  Passwords do not match
                </p>
              </transition>
            </div>

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

          <div class="terms_container">
            <input type="checkbox" id="terms" v-model="terms_accepted" class="terms_checkbox">
            <label for="terms">I agree to the <a href="#" class="terms_link">Terms and Conditions</a></label>
            <transition name="fade">
              <p v-if="terms_invalid" class="error_message terms_error">You must accept the terms</p>
            </transition>
          </div>

          <div class="button_container">
            <button class="register_button" @click="handleRegister">Register</button>
            <div class="login_redirect">
              Already have an account? <a href="http://localhost:5173/login" class="login_link">Login</a>
            </div>
          </div>
        </div>
      </div>
    </main>

    <footer class="footer">
      <p>&copy; 2025 MyApp. All rights reserved.</p>
    </footer>
  </div>
</template>

<script setup>
import { ref, watch, computed } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { userRegister } from '@/api/UserApi.js'

const router = useRouter();

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

// 侧边栏控制
const showSidebar = ref(false);
const toggleSidebar = () => {
  showSidebar.value = !showSidebar.value;
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
      router.push('/login');
    } else {
      console.error('Registration failed:', response.message);
      // 可以在这里添加错误提示显示逻辑
    }
  } catch (error) {
    console.error('Registration error:', error);
    // 可以在这里添加错误提示显示逻辑
  }
};

// 实时验证
watch(username, validateUsername);
watch(email, validateEmail);
watch(password, () => {
  validatePassword();
  if (confirm_password_touched.value) {
    validateConfirmPassword();
  }
});
watch(confirm_password, validateConfirmPassword);
watch(verificationCode, validateVerificationCode);
</script>

<style lang="scss" scoped>
/* 变量定义 */
$primary-color: #a3d2ca;
$primary-hover: #5eaaa8;
$error-color: #ff4757;
$text-color: #333;
$light-bg: #fffdf7;
$white: #ffffff;
$gray: #cccccc;
$dark-gray: #555;
@font-face {
  font-family: "TsangerYuYangT_W01_W01"; /* 字体名称 */
  src: url('@/assets/font/站酷仓耳渔阳体/TsangerYuYangT_W03_W03.ttf') format('truetype'); /* 字体文件路径 */
  font-weight: normal;
  font-style: normal;
}
/* 基础样式 */
.view-page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  font-family: 'Arial', sans-serif;
}

/* Header 样式 */
.header {
  background-color: #222;
  color: $white;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);

  .logo {
    font-size: 2rem;
    font-weight: 700;
    letter-spacing: 1px;
  }

  .nav ul {
    display: flex;
    gap: 2rem;
    list-style: none;
    margin: 0;
    padding: 0;

    li {
      transition: color 0.3s ease; /* 添加平滑过渡 */
    }

    .nav-link {
      display: flex;
      align-items: center;
      gap: 0.5rem;
      color: $white; /* 默认字体颜色 */
      text-decoration: none;
      font-size: 1.2rem;
      transition: color 0.3s ease;
    }

    .nav-link:hover,
    .nav-link:hover i {
      color: #a3d2ca; /* 鼠标悬停时，字体和图标颜色变为红色 */
    }

    .nav-link i {
      font-size: 1.2rem;
      color: $white; /* 默认图标颜色 */
      transition: color 0.3s ease;
    }

    li a {
      color: $white;
      text-decoration: none;
      font-size: 1.1rem;
      transition: color 0.3s;
      display: flex;
      align-items: center;
      gap: 0.5rem;
      i {
        font-size: 1.2rem;
        color: $white;
      }
    }
  }
}

/* 内容区域样式 */
.content {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: $light-bg;
  padding: 2rem;

  .register_container {
    position: relative;
    width: 100%;
    max-width: 1000px;
    height: 600px;
    border-radius: 30px;
    overflow: hidden;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);

    .form_background {
      position: absolute;
      width: 100%;
      height: 100%;
      background-image:
          linear-gradient(
                  105deg,
                  rgba(0, 0, 0, 0.2) 0%,
                  rgba(0, 0, 0, 0.2) 47%,
                  rgba(248, 249, 250, 0.2) 52%,
                  transparent 55%
          ),
          url('@/assets/pic/micke-lindstrom-emvCxhS7OaQ-unsplash.jpg');
      background-size: cover;
      background-position: center;
      z-index: 1;
    }

    .form {
      position: relative;
      width: 50%;
      height: 100%;
      padding: 3rem;
      background: rgba(255, 255, 255, 0.85);
      backdrop-filter: blur(5px);
      z-index: 2;
      display: flex;
      flex-direction: column;
      justify-content: center;

      .title {
        font-size: 2.5rem;
        font-weight: 700;
        color: $text-color;
        margin-bottom: 2rem;
        text-align: center;
      }

      .input_container {
        display: flex;
        flex-direction: column;
        gap: 1.5rem;
        margin-bottom: 2rem;

        .input_field_wrapper {
          position: relative;

          input {
            width: 100%;
            padding: 1rem;
            border: 1px solid #e7e7e7;
            border-radius: 8px;
            font-size: 1rem;
            transition: all 0.3s;
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
              padding-right: 120px;
            }

            .send-code-btn {
              position: absolute;
              right: 5px;
              top: 50%;
              transform: translateY(-50%);
              padding: 0.5rem 1rem;
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
            bottom: -1.5rem;
            color: $error-color;
            font-size: 0.8rem;
            margin-top: 0.3rem;
          }
        }
      }

      .terms_container {
        display: flex;
        align-items: center;
        margin-bottom: 2rem;
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
          bottom: -1.5rem;
          left: 0;
        }
      }

      .button_container {
        display: flex;
        flex-direction: column;
        gap: 1rem;

        .register_button {
          width: 100%;
          padding: 1rem;
          background: $primary-color;
          color: $white;
          border: none;
          border-radius: 8px;
          font-size: 1.1rem;
          font-weight: 600;
          cursor: pointer;
          transition: all 0.3s;

          &:hover {
            background: $primary-hover;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba($primary-color, 0.3);
          }

          &:active {
            transform: translateY(0);
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
            }
          }
        }
      }
    }
  }
}

/* Footer 样式 */
.footer {
  background: #222;
  color: $white;
  text-align: center;
  padding: 1.5rem;
  font-size: 0.9rem;
}

/* Sidebar 样式 */
.sidebar {
  position: fixed;
  top: 0;
  right: 0;
  width: 230px;
  height: auto;
  background-color: white;
  box-shadow: -2px 0 10px rgba(0, 0, 0, 0.3);
  transform: translateX(100%);
  transition: transform 0.3s ease-in-out;
  z-index: 1000;
  border-top-left-radius: 20px;
  border-bottom-left-radius: 20px;
  border-bottom-right-radius: 20px;
  font-family: "TsangerYuYangT_W01_W01", sans-serif;
  &.show {
    transform: translateX(0);
  }

  .sidebar-content {
    padding: 1rem;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    gap: 1.5rem;

    .close-btn {
      align-self: flex-end;
      background: none;
      border: none;
      font-size: 1.2rem;
      cursor: pointer;
      margin-bottom: 1.5rem;
    }

    p {
      font-size: 1.5rem;
      color: #333;

      i {
        color: $primary-color;
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

/* 响应式设计 */
@media (max-width: 768px) {
  .register_container {
    flex-direction: column;
    height: auto;

    .form_background {
      height: 200px;
    }

    .form {
      width: 100%;
      padding: 2rem;
    }
  }

  .header {
    flex-direction: column;
    padding: 1rem;

    .logo {
      margin-bottom: 1rem;
    }

    .nav ul {
      gap: 1rem;
    }
  }
}

@media (max-width: 480px) {
  .content {
    padding: 1rem;
  }

  .register_container {
    border-radius: 15px;

    .form {
      padding: 1.5rem;
    }
  }
}
</style>