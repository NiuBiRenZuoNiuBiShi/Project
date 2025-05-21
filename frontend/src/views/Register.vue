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
      <div class="register_container">
        <div class="form">
          <h2 class="title">Create Account</h2>
          <div class="input_container">
            <div class="input_field_wrapper">
              <input type="text" class="username" :class="{ invalid: username_invalid }" placeholder="Username"
                     v-model="username" @blur="validateUsername">
              <transition name="fade">
                <p v-if="username_invalid && username_touched" class="error_message">Username must be 4-16 characters</p>
              </transition>
            </div>

            <div class="input_field_wrapper">
              <input type="email" class="email" :class="{ invalid: email_invalid }" placeholder="Email"
                     v-model="email" @blur="validateEmail">
              <transition name="fade">
                <p v-if="email_invalid && email_touched" class="error_message">Please enter a valid email</p>
              </transition>
            </div>

            <div class="input_field_wrapper">
              <input type="password" class="password" :class="{ invalid: password_invalid }" placeholder="Password"
                     v-model="password" @blur="validatePassword">
              <transition name="fade">
                <p v-if="password_invalid && password_touched" class="error_message">Password must be 8-20 characters with at least one number</p>
              </transition>
            </div>

            <div class="input_field_wrapper">
              <input type="password" class="confirm-password" :class="{ invalid: confirm_password_invalid }"
                     placeholder="Confirm Password" v-model="confirm_password" @blur="validateConfirmPassword">
              <transition name="fade">
                <p v-if="confirm_password_invalid && confirm_password_touched" class="error_message">Passwords do not match</p>
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

    <!-- Footer Section -->
    <footer class="footer">
      <p>&copy; 2025 MyApp. All rights reserved.</p>
    </footer>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import { checkPassword, checkUsername } from '@/utils/checkSyntax';
import { useRouter } from 'vue-router';

const router = useRouter();

const username = ref('');
const email = ref('');
const password = ref('');
const confirm_password = ref('');
const terms_accepted = ref(false);

const username_invalid = ref(false);
const email_invalid = ref(false);
const password_invalid = ref(false);
const confirm_password_invalid = ref(false);
const terms_invalid = ref(false);

const username_touched = ref(false);
const email_touched = ref(false);
const password_touched = ref(false);
const confirm_password_touched = ref(false);

const showSidebar = ref(false);
const toggleSidebar = () => {
  showSidebar.value = !showSidebar.value;
};

const validateUsername = () => {
  username_touched.value = true;
  username_invalid.value = !checkUsername(username.value);
};

const validateEmail = () => {
  email_touched.value = true;
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  email_invalid.value = !emailRegex.test(email.value);
};

const validatePassword = () => {
  password_touched.value = true;
  password_invalid.value = !checkPassword(password.value);
};

const validateConfirmPassword = () => {
  confirm_password_touched.value = true;
  confirm_password_invalid.value = password.value !== confirm_password.value;
};

const validateForm = () => {
  validateUsername();
  validateEmail();
  validatePassword();
  validateConfirmPassword();

  terms_invalid.value = !terms_accepted.value;

  return !username_invalid.value &&
      !email_invalid.value &&
      !password_invalid.value &&
      !confirm_password_invalid.value &&
      terms_accepted.value;
};

const handleRegister = () => {
  if (validateForm()) {
    // Here you would typically make an API call to register the user
    console.log('Registration data:', {
      username: username.value,
      email: email.value,
      password: password.value
    });

    // Redirect to login page after successful registration
    router.push('/login');
  }
};

// Watch for changes to validate in real-time
watch(username, validateUsername);
watch(email, validateEmail);
watch(password, () => {
  validatePassword();
  if (confirm_password_touched.value) {
    validateConfirmPassword();
  }
});
watch(confirm_password, validateConfirmPassword);
</script>

<style lang="scss" scoped>
$mask-color: rgba(0, 0, 0, 0.2);
$background-color: #fffdf7;
$login-color: #ffffff;
$button-color: #a3d2ca;
$button-color-hover: #5eaaa8;
$error-color: #ff4757;
$terms-color: #555;

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

    .register_container {
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
              border: 2px solid $error-color;
              box-shadow: 0 0 5px rgba($error-color, 0.5);
            }
          }
        }

        .terms_container {
          width: 100%;
          display: flex;
          align-items: center;
          margin-bottom: 2rem;
          font-size: 1.4rem;
          color: $terms-color;

          .terms_checkbox {
            width: 2rem;
            height: 2rem;
            margin-right: 1rem;
            accent-color: $button-color-hover;
          }

          .terms_link {
            color: $button-color-hover;
            text-decoration: none;
            font-weight: 600;
            margin-left: 0.3rem;

            &:hover {
              text-decoration: underline;
            }
          }

          .terms_error {
            position: absolute;
            bottom: -2.5rem;
            left: 0;
          }
        }

        .button_container {
          width: 100%;
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          gap: 2rem;

          .register_button {
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

          .login_redirect {
            font-size: 1.5rem;
            color: $terms-color;

            .login_link {
              color: $button-color-hover;
              text-decoration: none;
              font-weight: 600;
              margin-left: 0.5rem;

              &:hover {
                text-decoration: underline;
              }
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
    background-color: rgba(0, 123, 255, 0.9);
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
    color: $error-color;
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
    .content .register_container {
      flex-direction: column;
      .form {
        width: 100%;
      }
    }
  }

  @media (max-width: 480px) {
    .content .register_container {
      width: 90%;
      padding: 2rem;
    }
  }
}
</style>