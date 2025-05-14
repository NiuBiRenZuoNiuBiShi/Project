<template>
    <div :class="['side-bar-container', { collapsed: !isExpanded }]">
        <div class="sidebar-header">
            <div class="logo-container" @click="expandSidebar">
                <i class="fa-solid fa-dove logo"></i>
                <transition name="fade">
                    <span v-if="isExpanded" class="logo-text">SwiftTravel</span>
                </transition>
            </div>
            <button class="sidebar-toggle-btn" @click="toggleSidebar">
                <i class="fa-solid" :class="isExpanded ? 'fa-chevron-left' : 'fa-chevron-right'"></i>
            </button>
        </div>

        <div class="menu-container">
            <div v-for="(item, index) in menuItems" :key="item.text"
                :class="['sidebar-item', { active: activeItem === index }]" @click="activateItem(index)">
                <a :href="item.href">
                    <div class="item-icon">
                        <i :class="item.icon"></i>
                    </div>
                    <transition name="fade">
                        <span v-if="isExpanded" class="item-text">{{ item.text }}</span>
                    </transition>
                </a>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue';

const isExpanded = ref(window.innerWidth > 768);
const activeItem = ref(0); // 默认值，将在页面加载时更新

const toggleSidebar = () => {
    isExpanded.value = !isExpanded.value;
};

const expandSidebar = () => {
    if (!isExpanded.value) isExpanded.value = true;
};

const activateItem = (index) => {
    activeItem.value = index;
    if (!isExpanded.value) isExpanded.value = true;
};

const menuItems = [
    { icon: 'fa-solid fa-house', text: 'Home', href: '/home' },
    { icon: 'fa-solid fa-train', text: 'Travel', href: '/search?form=Train' },
    { icon: 'fa-solid fa-hotel', text: 'Hotel', href: '/search?form=Hotel' },
    { icon: 'fa-solid fa-user', text: 'Profile', href: '#' },
    { icon: 'fa-solid fa-circle-info', text: 'Support', href: '/home' },
    { icon: 'fa-solid fa-right-from-bracket', text: 'Logout', href: '#' },
];

// 更新当前活动项基于当前URL
const updateActiveItemFromUrl = () => {
    const currentPath = window.location.pathname;
    const currentSearch = window.location.search;
    const fullPath = currentPath + currentSearch;
    
    // 查找匹配当前URL的菜单项
    const index = menuItems.findIndex(item => {
        // 对于 href 只有 # 的项目，跳过
        if (item.href === '#') return false;
        
        // 如果 href 包含参数，尝试完整匹配
        if (item.href.includes('?')) {
            return fullPath === item.href;
        }
        // 否则只匹配路径部分
        return currentPath === item.href;
    });
    
    // 如果找到匹配项，则设置活动项
    if (index !== -1) {
        activeItem.value = index;
    }
};

const handleResize = () => {
    if (window.innerWidth <= 768) isExpanded.value = false;
    else isExpanded.value = true;
};

onMounted(() => {
    // 页面加载时更新活动项
    updateActiveItemFromUrl();
    
    window.addEventListener('resize', handleResize);
    
    // 监听路由变化（如果使用了Vue Router）
    if (window.history && window.history.pushState) {
        window.addEventListener('popstate', updateActiveItemFromUrl);
    }
});

onBeforeUnmount(() => {
    window.removeEventListener('resize', handleResize);
    
    if (window.history && window.history.pushState) {
        window.removeEventListener('popstate', updateActiveItemFromUrl);
    }
});
</script>

<style lang="scss" scoped>
// Modern Color Scheme
$primary: #4a8eff;
$primary-light: #6ba3ff;
$primary-dark: #3270e9;
$primary-gradient: linear-gradient(135deg, $primary-light, $primary-dark);
$bg: #f8faff;
$bg-dark: #f0f4fc;
$text: #2c3e50;
$text-light: #5d7290;
$hover: #e0e9ff;
$active-bg: #e0e9ff;
$border: #e6eaf0;
$shadow: rgba(50, 112, 233, 0.08);

.side-bar-container {
    width: 240px;
    height: 100vh;
    background-color: $bg;
    color: $text;
    box-shadow: 2px 0 15px $shadow;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    overflow-x: hidden;
    display: flex;
    flex-direction: column;
    border-right: 1px solid $border;
    position: sticky;
    top: 0;
    left: 0;

    &.collapsed {
        width: 70px;
    }

    .sidebar-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 1.5rem 1rem;
        border-bottom: 1px solid $border;

        .logo-container {
            display: flex;
            align-items: center;
            cursor: pointer;

            .logo {
                font-size: 1.8rem;
                background: $primary-gradient;
                background-clip: text;
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
                transition: transform 0.3s ease;

                &:hover {
                    transform: scale(1.1);
                }
            }

            .logo-text {
                display: block;
                font-size: 2rem;
                font-weight: 700;
                margin-left: 2rem;
                color: $primary-dark;
                white-space: nowrap;
            }
        }

        .sidebar-toggle-btn {
            background: none;
            border: none;
            cursor: pointer;
            height: 32px;
            width: 32px;
            border-radius: 8px;
            display: flex;
            align-items: center;
            justify-content: center;
            color: $text-light;
            transition: all 0.2s ease;

            &:hover {
                background-color: $hover;
                color: $primary-dark;
            }

            i {
                font-size: 0.9rem;
            }
        }
    }

    .menu-container {
        display: flex;
        flex-direction: column;
        padding: 1rem 0.75rem;
        gap: 1rem;
        overflow-y: auto;
        overflow-x: hidden;
        flex-grow: 1;

        .sidebar-item {
            position: relative;
            border-radius: 10px;
            transition: all 0.2s ease;

            &:hover {
                background-color: $hover;
            }

            &.active {
                background-color: $active-bg;

                a {
                    color: $primary-dark;
                    font-weight: 600;
                }

                &::before {
                    content: '';
                    position: absolute;
                    left: 0;
                    top: 0;
                    width: 4px;
                    height: 100%;
                    background: $primary-gradient;
                    border-radius: 0 4px 4px 0;
                }

                .item-icon i {
                    color: $primary-dark;
                }
            }

            a {
                display: flex;
                align-items: center;
                text-decoration: none;
                color: $text;
                padding: 0.9rem 1rem;
                border-radius: 10px;
                transition: all 0.3s ease;
                white-space: nowrap;

                .item-icon {
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    width: 36px;

                    i {
                        font-size: 24px;
                        transition: color 0.3s ease;
                        color: $text-light;
                    }
                }

                .item-text {
                    padding-left: 2rem;
                    font-size: 1.5rem;
                    font-weight: 500;
                    white-space: nowrap;
                }
            }

            &:nth-last-child(2){
                margin-top: auto;
            }
        }
    }
}

// Improved transitions
.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.25s ease, transform 0.25s ease;
}

.fade-enter-from,
.fade-leave-to {
    opacity: 0;
    transform: translateX(-10px);
}

// Responsive adjustments
@media (max-width: 768px) {
    .side-bar-container {
        position: fixed;
        z-index: 999;
        height: 100vh;
        left: 0;
        top: 0;
    }
}
</style>