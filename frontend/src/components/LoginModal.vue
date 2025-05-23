<template>
  <div class="login-modal" @click.self="closeModal">
    <div class="login-container" :class="{ 'show': isVisible }">
      <!-- 关闭按钮 -->
      <button class="close-btn" @click="closeModal">
        <i class="fas fa-times"></i>
      </button>
      <div class="form">
        <h2 class="title">User Login</h2>
        <div class="input_container">
          <div class="input_field_wrapper">
            <input type="text" class="username" :class="{ invalid: username_invalid }" placeholder="Username"
                   v-model="username">
            <transition name="fade">
              <p v-if="username_invalid && username_touched" class="error_message">Invalid username format</p>
            </transition>
          </div>
          <div class="input_field_wrapper">
            <input type="password" class="password" :class="{ invalid: password_invalid }" placeholder="Password"
                   v-model="password">
            <transition name="fade">
              <p v-if="password_invalid && password_touched" class="error_message">Invalid password format</p>
            </transition>
          </div>
        </div>
        <div class="forget_info" role="button"><span>Forget password?</span></div>
        <div class="button_container">
          <button class="login_button" @click="login" :disabled="isLogging">
            <span class="button-text">{{ isLogging ? 'Logging in...' : 'Login' }}</span>
            <span class="button-icon" v-if="isLogging">
              <svg class="spinner" viewBox="0 0 50 50">
                <circle class="path" cx="25" cy="25" r="20" fill="none" stroke-width="5"></circle>
              </svg>
            </span>
          </button>
          <button class="register_button" @click="goToRegister">Register</button>
        </div>
      </div>
    </div>
  </div>
  <RegisterModal v-if="showRegisterModal" @close="showRegisterModal = false" />
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import { checkPassword, checkUsername } from '@/utils/checkSyntax';
import { useRouter } from 'vue-router'
import { userLogin } from '@/api/UserApi.js';
import { useAuthStore } from '@/store/auth.js';
import RegisterModal from './RegisterModal.vue';

const router = useRouter()
const authStore = useAuthStore();
const emit = defineEmits(['close']);
const showRegisterModal = ref(false);
const isLogging = ref(false);
const isVisible = ref(false);

const closeModal = () => {
  isVisible.value = false;
  setTimeout(() => {
    emit('close');
  }, 400); // Match the transition duration
}

const goToRegister = () => {
  showRegisterModal.value = true;
}

const login = async () => {
  username_touched.value = true;
  password_touched.value = true;
  if (username_invalid.value || password_invalid.value) {
    return;
  }
  isLogging.value = true;
  try {
    const user = { username: username.value, password: password.value };
    const result = await userLogin(user);
    if (result.success) {
      // 存储用户信息和token到状态管理
      authStore.login(result.data.user, result.data.token);
      // localStorage.set('token', result.data);
      // 关闭登录模态框
      closeModal();
      
      // 跳转到首页或用户之前访问的页面
      router.push('/home');
    } else {
      alert('登录失败: ' + result.message);
    }
  } catch (error) {
    console.error('登录失败:', error);
    alert('登录失败，请稍后再试');
  } finally {
    isLogging.value = false;
  }
};

const username = ref('');
const password = ref('');
const username_invalid = ref(false);
const password_invalid = ref(false);
const username_touched = ref(false);
const password_touched = ref(false);

watch(username, (val) => {
  if (!username_touched.value) {
    username_touched.value = true;
    return;
  }
  username_invalid.value = !checkUsername(val);
});

watch(password, (val) => {
  if (!password_touched.value) {
    password_touched.value = true;
    return;
  }
  password_invalid.value = !checkPassword(val);
});

onMounted(() => {
  // 在组件挂载后触发动画
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
$mask-color: rgba(0, 0, 0, 0.2);
$login-color: #ffffff;
$button-color: #a3d2ca;
$button-color-hover: #5eaaa8;
$close-btn-color: #666;
$animation-timing: cubic-bezier(0.22, 1, 0.36, 1);

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

.login-modal {
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
  
  .login-container {
    position: relative;
    width: 1000px;
    display: flex;
    justify-content: space-between;
    background-color: $login-color;
    border-radius: 30px;
    padding: 4rem;
    opacity: 0;
    transform: translateY(30px) scale(0.95);
    transition: all 0.5s $animation-timing;
    background-image: linear-gradient(105deg, $mask-color 0%, $mask-color 47%, rgba(248, 249, 250, 0.2) 52%, transparent 55%), url(../assets/pic/micke-lindstrom-emvCxhS7OaQ-unsplash.jpg);
    background-size: cover;
    background-position: center;
    background-blend-mode: multiply;
    
    &.show {
      opacity: 1;
      transform: translateY(0) scale(1);
      box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
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
      font-size: 2rem;
      color: $close-btn-color;
      transition: all 0.3s ease;
      display: flex;
      justify-content: center;
      align-items: center;
      border-radius: 50%;
      z-index: 10;
      backdrop-filter: blur(5px);
      
      &:hover {
        color: #ff0000;
        background-color: rgba(255, 255, 255, 0.5);
        transform: rotate(90deg);
      }
      
      &:active {
        transform: scale(0.95) rotate(90deg);
      }
    }
    
    .form {
      width: 40%;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      background-color: rgba(255, 255, 255, 0.8);
      padding: 3rem;
      border-radius: 20px;
      overflow: hidden;
      box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
      backdrop-filter: blur(10px);
      
      .title {
        font-weight: 900;
        font-size: 3rem;
        color: #222;
        margin-bottom: 3%;
        opacity: 0;
        transform: translateY(-20px);
        animation: fadeInDown 0.6s $animation-timing 0.3s forwards;
      }
      
      .input_container {
        display: flex;
        position: relative;
        flex-direction: column;
        gap: 2rem;
        margin-bottom: 2.5rem;
        width: 100%;
        opacity: 0;
        transform: translateY(20px);
        animation: fadeInUp 0.6s $animation-timing 0.5s forwards;
        
        .input_field_wrapper {
          position: relative;
          width: 100%;
          margin-bottom: 1rem;
        }
        
        input {
          width: 100%;
          height: 5.5rem;
          outline: none;
          border: 1px solid rgb(231 231 231);
          border-radius: 12px;
          padding: 0 2rem;
          font-size: 1.5rem;
          transition: all 0.3s ease-in-out;
          background-color: rgba(255, 255, 255, 0.9);
          
          &:focus {
            border: 1px solid $button-color-hover;
            box-shadow: 0 0 0 3px rgba(94, 170, 168, 0.2);
            transform: translateY(-2px);
          }
          
          &::placeholder {
            color: #aaa;
            font-style: italic;
            font-weight: 500;
            font-size: 1.5rem;
            letter-spacing: 0.5px;
            transition: opacity 0.3s;
          }
          
          &.invalid {
            border: 2px solid red;
            box-shadow: 0 0 5px rgba(255, 0, 0, 0.5);
            animation: shake 0.5s cubic-bezier(.36,.07,.19,.97) both;
          }
        }
      }
      
      .forget_info {
        width: 100%;
        font-weight: 700;
        font-size: 2rem;
        opacity: 0;
        transform: translateY(20px);
        animation: fadeInUp 0.6s $animation-timing 0.7s forwards;
        
        span {
          border: 2px solid transparent;
          padding: 0.4rem 1rem;
          transition: all 0.3s ease-in-out;
          border-radius: 50px;
          
          &:hover {
            cursor: pointer;
            background-color: $button-color-hover;
            color: #fff;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
          }
        }
      }
      
      .button_container {
        width: 100%;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        gap: 2rem;
        margin-top: 2.5rem;
        opacity: 0;
        transform: translateY(20px);
        animation: fadeInUp 0.6s $animation-timing 0.9s forwards;
        
        button {
          width: 90%;
          display: flex;
          justify-content: center;
          align-items: center;
          height: 5.5rem;
          background-color: $button-color;
          color: #fff;
          font-size: 2rem;
          letter-spacing: 1px;
          font-weight: 700;
          border-radius: 12px;
          border: none;
          transition: all 0.3s ease-in-out;
          box-shadow: 0 4px 10px rgba($button-color, 0.3);
          transform: translateY(0);
          position: relative;
          overflow: hidden;
          
          &::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(
              90deg,
              transparent,
              rgba(255, 255, 255, 0.2),
              transparent
            );
            transition: all 0.6s;
          }
          
          &:hover:not(:disabled) {
            cursor: pointer;
            transform: translateY(-2px);
            background-color: $button-color-hover;
            box-shadow: 0 6px 15px rgba($button-color-hover, 0.4);
            
            &::before {
              left: 100%;
            }
          }
          
          &:active:not(:disabled) {
            transform: translateY(1px);
            box-shadow: 0 2px 5px rgba($button-color-hover, 0.4);
          }
          
          &:disabled {
            opacity: 0.8;
            cursor: not-allowed;
            transform: none;
          }
          
          .button-icon {
            margin-left: 10px;
          }
          
          .spinner {
            animation: rotate 2s linear infinite;
            width: 20px;
            height: 20px;
            
            .path {
              stroke: #ffffff;
              stroke-linecap: round;
              animation: dash 1.5s ease-in-out infinite;
            }
          }
        }
        
        .register_button {
          background-color: transparent;
          color: $button-color-hover;
          border: 2px solid $button-color;
          box-shadow: none;
          
          &:hover:not(:disabled) {
            background-color: rgba($button-color, 0.1);
            box-shadow: 0 4px 10px rgba($button-color, 0.2);
          }
        }
      }
    }
  }
}

.error_message {
  position: absolute;
  left: 0.5rem;
  bottom: -2rem;
  color: red;
  font-size: 1.2rem;
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

@media (max-width: 768px) {
  .login-container {
    width: 90% !important;
    flex-direction: column;
    padding: 3rem !important;
    
    .form {
      width: 100% !important;
      padding: 2rem !important;
    }
  }
}

@media (max-width: 480px) {
  .login-container {
    padding: 2rem !important;
    
    .close-btn {
      top: 10px;
      right: 10px;
      width: 30px;
      height: 30px;
      font-size: 1.5rem;
    }
    
    .form {
      padding: 1.5rem !important;
      
      .title {
        font-size: 2.5rem;
      }
      
      .input_container input {
        height: 5rem;
        font-size: 1.3rem;
      }
      
      .forget_info {
        font-size: 1.6rem;
      }
      
      .button_container button {
        height: 5rem;
        font-size: 1.6rem;
      }
    }
  }
}
</style>