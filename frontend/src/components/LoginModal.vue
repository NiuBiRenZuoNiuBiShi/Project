<template>
  <div class="login-modal">
    <div class="login-container">
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
          <button class="login_button" @click="login">Login</button>
          <button class="register_button" @click="goToRegister">Register</button>
        </div>
      </div>
    </div>
  </div>
  <RegisterModal v-if="showRegisterModal" @close="showRegisterModal = false" />
</template>

<script setup>
import { ref, watch } from 'vue';
import { checkPassword, checkUsername } from '@/utils/checkSyntax';
import { useRouter } from 'vue-router'
import { userLogin } from '@/api/UserApi.js';
import RegisterModal from './RegisterModal.vue';
const router = useRouter()
const emit = defineEmits(['close']);
const showRegisterModal = ref(false);
const closeModal = () => {
  emit('close');
}

const goToRegister = () => {
  showRegisterModal.value = true;
}
const closeRegisterModal = () => {
  showRegisterModal.value = false; // 隐藏注册弹窗
}
const login = async () => {
  username_touched.value = true;
  password_touched.value = true;

  if (username_invalid.value || password_invalid.value) {
    return;
  }

  try {
    const user = { username: username.value, password: password.value };
    const data = await userLogin(user);

    if (data.success) {
      router.push('/home');
      closeModal();
    } else {
      alert('登录失败: ' + data.message);
    }
  } catch (error) {
    console.error('登录失败:', error);
    alert('登录失败，请稍后再试');
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
</script>

<style lang="scss" scoped>
$mask-color: rgba(0, 0, 0, 0.2);
$login-color: #ffffff;
$button-color: #a3d2ca;
$button-color-hover: #5eaaa8;
$close-btn-color: #666;

.login-modal {
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

  .login-container {
    position: relative;
    width: 1000px;
    display: flex;
    justify-content: space-between;
    background-color: $login-color;
    border-radius: 30px;
    padding: 4rem;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
    background-image: linear-gradient(105deg, $mask-color 0%, $mask-color 47%, rgba(248, 249, 250, 0.2) 52%, transparent 55%), url(../assets/pic/micke-lindstrom-emvCxhS7OaQ-unsplash.jpg);
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
      font-size: 2rem;
      color: $close-btn-color;
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

      &:active {
        transform: scale(0.95);
      }
    }

    .form {
      width: 40%;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      background-color: rgba(255, 255, 255, 0.6);
      padding: 3rem;
      border-radius: 20px;
      overflow: hidden;
      box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
      backdrop-filter: blur(5px);

      .title {
        font-weight: 900;
        font-size: 3rem;
        color: #222;
        margin-bottom: 3%;
      }

      .input_container {
        display: flex;
        position: relative;
        flex-direction: column;
        gap: 2rem;
        margin-bottom: 2.5rem;
        width: 100%;

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
          transition: all 0.2s ease-in-out;
          background-color: rgba(255, 255, 255, 0.9);

          &:focus {
            border: 1px solid $button-color-hover;
            box-shadow: 0 0 0 2px rgba(94, 170, 168, 0.2);
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
          }
        }
      }

      .forget_info {
        width: 100%;
        font-weight: 700;
        font-size: 2rem;

        span {
          border: 2px solid transparent;
          padding: 0.4rem 1rem;
          transition: all 0.3s ease-in-out;
          border-radius: 50px;

          &:hover {
            cursor: pointer;
            background-color: $button-color-hover;
            color: #fff;
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

        button {
          width: 90%;
          display: block;
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

          &:hover {
            cursor: pointer;
            transform: translateY(-2px);
            background-color: $button-color-hover;
            box-shadow: 0 6px 15px rgba($button-color-hover, 0.4);
          }

          &:active {
            transform: translateY(1px);
            box-shadow: 0 2px 5px rgba($button-color-hover, 0.4);
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

@media (max-width: 768px) {
  .login-container {
    flex-direction: column;
    .form {
      width: 100%;
    }
  }
}

@media (max-width: 480px) {
  .login-container {
    width: 90%;
    padding: 2rem;

    .close-btn {
      top: 10px;
      right: 10px;
      width: 30px;
      height: 30px;
      font-size: 1.5rem;
    }
  }
}
</style>