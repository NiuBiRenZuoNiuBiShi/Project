<template>
  <div class="view-page">
    <!-- Header Section -->
    <header class="header">
      <div class="logo">MyApp</div>
      <nav class="nav">
        <ul>
          <li><a href="http://localhost:5173/home"><i class="fas fa-home home-icon"></i> Home</a></li>
          <li><a href="http://localhost:5173/about"><i class="fa-solid fa-address-card"></i>About</a></li>
          <li><a href="#" @click.prevent="toggleSidebar"><i class="fa-solid fa-phone-volume"></i>Contact</a></li>
        </ul>
      </nav>
    </header>

    <!-- Sidebar Section (Phone & Email) -->
    <div class="sidebar" :class="{ show: showSidebar }">
      <div class="sidebar-content">
        <button @click="toggleSidebar" class="close-btn">X</button>
        <p><i class="fa-solid fa-phone-volume"></i>: 123-456-7890</p>
        <p><i class="fa-solid fa-envelope"></i>: contact@myapp.com</p>
      </div>
    </div>

    <!-- Main Content Section -->
    <main class="content">
      <div class="login_container">
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
    </main>

    <!-- Footer Section -->
    <footer class="footer">
      <p>&copy; 2025 MyApp. All rights reserved.</p>
    </footer>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import { checkPassword, checkUsername } from '@/utils/checkSyntax';
import { useRouter } from 'vue-router'
import { userLogin } from '@/api/UserApi.js';
const router = useRouter()

const goToRegister = () => {
  // 两种写法都可以（使用路径或路由名称）
  router.push('/register') // 方式一：直接使用路径
  // 或
  // router.push({ name: 'register' }) // 方式二：使用你在路由配置中定义的名称
}
const login = async () => {
  // 进行表单验证
  username_touched.value = true;
  password_touched.value = true;

  if (username_invalid.value || password_invalid.value) {
    return; // 如果用户名或密码格式不正确，直接返回
  }

  try {
    // 调用登录接口
    const user = { username: username.value, password: password.value };
    const data = await userLogin(user);

    if (data.success) {
      // 登录成功后处理，比如跳转到主页
      router.push('/home');
    } else {
      // 登录失败，显示错误信息
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

const showSidebar = ref(false);
const toggleSidebar = () => {
  showSidebar.value = !showSidebar.value;
};

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
$background-color: #fffdf7;
$login-color: #ffffff;
$button-color: #a3d2ca;
$button-color-hover: #5eaaa8;
@font-face {
  font-family: "TsangerYuYangT_W01_W01"; /* 字体名称 */
  src: url('@/assets/font/站酷仓耳渔阳体/TsangerYuYangT_W03_W03.ttf') format('truetype'); /* 字体文件路径 */
  font-weight: normal;
  font-style: normal;
}

.home-icon {
  color: #a3d2ca; /* 设置房子图标颜色 */
}
.view-page {
  display: flex;
  flex-direction: column;
  height: 100vh;

  .header {
    background-color: #222;
    color: white;
    padding: 1rem 2rem;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .logo {
      font-size: 2rem;
      font-weight: 700;
    }

    .nav ul {
      list-style-type: none;
      padding-left: 0;
      display: flex;
      gap: 2rem;

      li a {
        color: white;
        text-decoration: none;
        font-size: 1.2rem;
        transition: color 0.3s;

        i {
          margin-right: 4px;
          color: inherit;
          transition: color 0.3s;
        }
        &:hover {
          color: $button-color;
        }
        &:hover i {
          color: $button-color;
        }
      }
    }
  }

  .content {
    flex: 1;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: $background-color;

    .login_container {
      width: 1000px;
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
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

  .footer {
    background-color: #222;
    color: white;
    text-align: center;
    padding: 1rem;
  }

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
      gap: 1rem;

      p {
        font-size: 1.5rem;
        color: #333;
      }

      .close-btn {
        align-self: flex-end;
        font-size: 2rem;
        background: none;
        border: none;
        color: #333;
        cursor: pointer;
        transition: color 0.3s ease;

        &:hover {
          color: #a3d2ca;
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
    .content .login_container {
      flex-direction: column;
      .form {
        width: 100%;
      }
    }
  }

  @media (max-width: 480px) {
    .content .login_container {
      width: 90%;
      padding: 2rem;
    }
  }
}
</style>
