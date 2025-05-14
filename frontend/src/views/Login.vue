<template>
    <div class="content">
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
                        <input type="text" class="password" :class="{ invalid: password_invalid }" placeholder="Password"
                            v-model="password">
                        <transition name="fade">
                            <p v-if="password_invalid && password_touched" class="error_message">Invalid password format</p>
                        </transition>
                    </div>
                </div>
                <div class="forget_info" role="button"><span>Forget password?</span></div>
                <div class="button_container">
                    <button class="login_button">Login</button>
                    <button class="register_button">Register</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import { checkPassword, checkUsername } from '@/utils/checkSyntax';

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

// $background-color: #fef9f4;
// $login-color: #fff;
// $button-color: #ff7b54;
// $button-color-hover: #ff5722;


$background-color: #fffdf7;
$login-color: #ffffff;
$button-color: #a3d2ca;
$button-color-hover: #5eaaa8;


// $background-color: #1e1e2f;
// $login-color: #2c2c3e;
// $button-color: #4ecca3;
// $button-color-hover: #3fb68b;


.content {
    background-color: $background-color;
    width: 100%;
    height: 100vh;
    position: relative;
    overflow: hidden;

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

            .tips {
                font-size: 1.2rem;
                color: #555;
                margin-bottom: 8%;
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
                        box-shadow: 0 0 0 2px rgba(94, 170, 168, 0.2); // 温和高亮

                        &::placeholder {
                            opacity: 0.3;
                        }
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
                        border: 2px solid transparent;
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

        &::after {
            content: "";
            width: 60%;
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

// 淡入淡出动画
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
            margin-bottom: 2rem;
        }

        &::after {
            width: 100%;
            height: 40%;
        }
    }
}

@media (max-width: 480px) {
    .content .login_container {
        width: 90%;
        padding: 2rem;

        .title {
            font-size: 2.6rem;
        }

        .button_container button {
            font-size: 1.7rem;
            height: 5rem;
        }
    }
}
</style>