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
            <div v-for="(item, index) in visibleMenuItems" :key="item.text"
                :class="['sidebar-item', { active: activeItem === index }]" @click.prevent="activateItem(index)">
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
import { ref, onMounted, onBeforeUnmount, watch, nextTick, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAuthStore } from '@/store/auth.js';

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();

const isExpanded = ref(window.innerWidth > 768);
const activeItem = ref(null); // Start with null to avoid initial flashing

const emit = defineEmits(['sidebar-toggle', 'showLogin']);

watch(isExpanded, (newValue) => {
    emit('sidebar-toggle', !newValue);
});

const toggleSidebar = () => {
    isExpanded.value = !isExpanded.value;
};

const expandSidebar = () => {
    if (!isExpanded.value) isExpanded.value = true;
};

const activateItem = async (index) => {
    const item = visibleMenuItems.value[index];

    if (item.action) {
        // 执行特殊操作（如登出、登录）
        await item.action();
        return;
    }

    if (item.href && item.href !== '#') {
        // Immediately set active item for smoother transition
        activeItem.value = index;

        // Use router.push instead of link navigation when possible
        if (item.href.startsWith('/')) {
            // Prevent default navigation and use router instead
            router.push(item.href);
        }
    } else {
        activeItem.value = index;
    }

    if (!isExpanded.value) isExpanded.value = true;
};

// 处理登出
const handleLogout = async () => {
    try {
        await authStore.logout();
        router.push('/home');
    } catch (error) {
        console.error('Logout failed:', error);
    }
};

// 处理登录点击
const handleLoginClick = () => {
    emit('showLogin');
};

// 所有菜单项
const allMenuItems = [
    { icon: 'fa-solid fa-house', text: 'Home', href: '/home', requireAuth: false },
    { icon: 'fa-solid fa-train', text: 'Travel', href: '/search?form=Train', requireAuth: false },
    { icon: 'fa-solid fa-hotel', text: 'Hotel', href: '/search?form=Hotel', requireAuth: false },
    { icon: 'fa-solid fa-user', text: 'Profile', href: '/user', requireAuth: true },
    { icon: 'fa-solid fa-circle-info', text: 'Support', href: '/home', requireAuth: false },
    { icon: 'fa-solid fa-utensils', text: 'Order Food', href: '/orderFood', requireAuth: true },
    { icon: 'fa-solid fa-right-from-bracket', text: 'Logout', href: '#', requireAuth: true, action: handleLogout },
    { icon: 'fa-solid fa-right-to-bracket', text: 'Login', href: '#', requireAuth: false, loginOnly: true, action: handleLoginClick },
];

// 根据登录状态过滤菜单项
const visibleMenuItems = computed(() => {
    return allMenuItems.filter(item => {
        if (item.loginOnly) {
            // 只有未登录时显示登录按钮
            return !authStore.isLoggedIn;
        }
        if (item.requireAuth) {
            // 需要登录的菜单项只在登录时显示
            return authStore.isLoggedIn;
        }
        // 不需要登录的菜单项总是显示
        return true;
    });
});

// Enhanced mapping from URL patterns to menu indices
const urlToMenuItemMap = {
    // Exact paths
    'home': 0,
    'Home': 0,
    'search': {
        default: 1,
        params: {
            'form=Train': 1,
            'form=Hotel': 2
        }
    },
    // Named routes
    'trains': 1,
    'orderFood': 1,
    // Path prefixes (without leading slash)
    'train': 1,
    'trainfood': 1
};

// Update active item based on current route
const updateActiveItemFromRoute = () => {
    const currentRouteName = route.name;
    const currentPath = route.path.toLowerCase(); // Normalize path to lowercase
    const currentQuery = route.query;
    let matched = false;

    // First check for exact route name matches
    if (currentRouteName && urlToMenuItemMap[currentRouteName]) {
        const mapping = urlToMenuItemMap[currentRouteName];

        // If it's an object with params mapping
        if (typeof mapping === 'object' && mapping.params) {
            // Check for query parameters match
            for (const [paramPattern, menuIndex] of Object.entries(mapping.params)) {
                const [paramKey, paramValue] = paramPattern.split('=');
                if (currentQuery[paramKey] === paramValue) {
                    activeItem.value = menuIndex;
                    matched = true;
                    break;
                }
            }
            // If no specific parameter match but has a default
            if (!matched && mapping.default !== undefined) {
                activeItem.value = mapping.default;
                matched = true;
            }
        } else {
            // Direct numeric index
            activeItem.value = mapping;
            matched = true;
        }
    }

    // If no match by route name, check path prefixes
    if (!matched) {
        // Sort path prefixes by length (descending) for more specific matches first
        const pathPrefixes = Object.keys(urlToMenuItemMap)
            .filter(key => typeof urlToMenuItemMap[key] === 'number')
            .sort((a, b) => b.length - a.length);

        for (const prefix of pathPrefixes) {
            // Skip entries that are likely route names (e.g., 'home', 'search')
            if (prefix === 'home' || prefix === 'Home' || prefix === 'search') continue;

            // Check if the current path contains this prefix
            if (currentPath.includes(prefix.toLowerCase())) {
                activeItem.value = urlToMenuItemMap[prefix];
                matched = true;
                break;
            }
        }
    }

    // Default to Home if no match is found
    if (!matched) {
        activeItem.value = 0;
    }
};

// Immediate route matching on component creation
updateActiveItemFromRoute();

// Watch for route changes to update active item
// Use { immediate: true } to run it immediately
watch(() => route.fullPath, () => {
    nextTick(() => {
        updateActiveItemFromRoute();
    });
}, { immediate: true });

const handleResize = () => {
    if (window.innerWidth <= 768) isExpanded.value = false;
    else isExpanded.value = true;
};

onMounted(() => {
    // Update active item on page load
    updateActiveItemFromRoute();

    window.addEventListener('resize', handleResize);
});

onBeforeUnmount(() => {
    window.removeEventListener('resize', handleResize);
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
    height: 100%;
    min-height: 100vh;
    background-color: $bg;
    color: $text;
    box-shadow: 2px 0 15px $shadow;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    overflow-x: hidden;
    display: flex;
    flex-direction: column;
    border-right: 1px solid $border;
    position: fixed;
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

            &:nth-last-child(2) {
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
        z-index: 10;
    }
}
</style>