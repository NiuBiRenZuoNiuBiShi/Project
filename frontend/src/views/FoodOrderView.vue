<template>
    <div class="food-view-container">
        <div class="page-header">
            <h1>高铁点餐</h1>
            <div class="train-info">
                <div class="info-tag">
                    <span class="icon">🚄</span>
                    <span>G1234</span>
                </div>
                <div class="info-tag">
                    <span class="icon">🚪</span>
                    <span>12号车厢</span>
                </div>
                <div class="info-tag">
                    <span class="icon">💺</span>
                    <span>05A座</span>
                </div>
            </div>
        </div>

        <div class="category-tabs">
            <button class="tab-btn" :class="{ active: selectTab === 'all' }" @click="selectTab = 'all'">全部菜品</button>
            <button class="tab-btn" :class="{ active: selectTab === 'support' }"
                @click="selectTab = 'support'">热销推荐</button>
            <button class="tab-btn" :class="{ active: selectTab === 'main' }" @click="selectTab = 'main'">主食</button>
            <button class="tab-btn" :class="{ active: selectTab === 'snack' }" @click="selectTab = 'snack'">小吃</button>
            <button class="tab-btn" :class="{ active: selectTab === 'drink' }" @click="selectTab = 'drink'">饮品</button>
        </div>

        <div class="food-order-view">
            <FoodItem v-for="(item, index) in foodItems" :key="index" :foodPic="item.foodPic" :foodName="item.foodName"
                :foodPrice="item.foodPrice" :foodCount="item.count"
                @update-count="(change) => handleUpdateCount(index, change)" />
        </div>

        <transition name="cart-fade">
            <ShoppingCartSummary v-if="showShoppingCartSummary" :items="itemsInCart" @clear-cart="clearCart"
                @close-summary="hideSummary = true" @update-item="handleCartUpdate"/>
        </transition>

        <div class="cart-indicator" v-if="totalItemsCount > 0" @click="hideSummary = false">
            <div class="cart-icon">
                <i class="cart-emoji">🛒</i>
                <span class="items-count">{{ totalItemsCount }}</span>
            </div>
            <span class="total-price">¥{{ cartTotalPrice.toFixed(2) }}</span>
        </div>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import FoodItem from '@/components/FoodItem.vue';
import ShoppingCartSummary from '@/components/ShoppingCartSummary.vue';
import foodImageUrl from '@/assets/pic/food/FoodItem.webp'; // 假设图片路径

const foodItems = ref([
    { foodPic: foodImageUrl, foodName: '精选沙拉', foodPrice: 38.00, count: 0, description: '新鲜蔬菜配以特制酱料', tag: '健康' },
    { foodPic: foodImageUrl, foodName: '牛肉三明治', foodPrice: 45.00, count: 0, description: '高铁特供美味三明治', tag: '热销' },
    { foodPic: foodImageUrl, foodName: '意大利面', foodPrice: 52.00, count: 0, description: '经典意面配特制酱料', tag: '主食' },
    { foodPic: foodImageUrl, foodName: '水果拼盘', foodPrice: 35.00, count: 0, description: '时令水果精选拼盘', tag: '健康' },
    { foodPic: foodImageUrl, foodName: '矿泉水', foodPrice: 5.00, count: 0, description: '纯净水源', tag: '饮品' },
    { foodPic: foodImageUrl, foodName: '咖啡', foodPrice: 22.00, count: 0, description: '醇香浓郁', tag: '饮品' },
]);

const hideSummary = ref(false);

// 计算属性：获取购物车中数量大于0的商品
const itemsInCart = computed(() => {
    return foodItems.value.filter(item => item.count > 0);
});

// 计算属性：判断是否显示购物车汇总组件
const showShoppingCartSummary = computed(() => {
    // 只有当购物车中有商品且未被手动关闭时显示
    return itemsInCart.value.length > 0 && !hideSummary.value;
});

// 处理 FoodItem 触发的更新数量事件
const handleUpdateCount = (index, change) => {
    const currentCount = foodItems.value[index].count;
    // 确保数量不会小于0
    if (currentCount + change >= 0) {
        foodItems.value[index].count += change;
         // 当数量变化时，重置 hideSummary 状态，使汇总组件重新显示
        hideSummary.value = false;
    }
};

// 清空购物车的方法
const clearCart = () => {
    foodItems.value.forEach(item => {
        item.count = 0;
    });
    // 清空后也可以选择隐藏汇总组件
    hideSummary.value = true;
};

const totalItemsCount = computed(() => {
    return foodItems.value.reduce((sum, item) => sum + item.count, 0);
});

const cartTotalPrice = computed(() => {
    return foodItems.value.reduce((sum, item) => sum + (item.foodPrice * item.count), 0);
});

const handleCartUpdate = (item, change) => {
    const index = foodItems.value.findIndex(i => i.foodName === item.foodName);
    if (index !== -1) {
        foodItems.value[index].count += change;
        // 确保数量不会小于0
        if (foodItems.value[index].count < 0) {
            foodItems.value[index].count = 0;
        }
    }
};

const selectTab = ref('all');

</script>

<style lang="scss" scoped>
// 变量定义
$primary: #4a8eff;
$primary-light: #6ba3ff;
$primary-dark: #3270e9;
$text: #2c3e50;
$text-light: #5d7290;
$bg-light: #f8faff;
$bg-dark: #f0f4fc;
$border: #e6eaf0;
$shadow: rgba(50, 112, 233, 0.08);
$accent-color: #36d6e7;
$accent: #ff6b6b;

// 添加媒体查询断点
$breakpoint-lg: 1200px;
$breakpoint-md: 992px;
$breakpoint-sm: 768px;
$breakpoint-xs: 480px;

.food-view-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 2rem 1.5rem 8rem 1.5rem; // 添加左右内边距
    background-color: $bg-light;
    min-height: 100vh;
    position: relative;
    width: 100%; // 确保容器不会超出视口宽度
    overflow-x: hidden; // 防止横向滚动
    
    @media (max-width: $breakpoint-sm) {
        padding: 1.5rem 1rem 8rem 1rem;
    }
}

.page-header {
    width: 100%; // 改为100%
    max-width: 120rem;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 2.5rem;
    padding-bottom: 1.5rem;
    border-bottom: 1px solid $border;
    
    @media (max-width: $breakpoint-sm) {
        flex-direction: column;
        align-items: flex-start;
        gap: 1.5rem;
        margin-bottom: 2rem;
    }
    
    h1 {
        font-size: 2.4rem;
        font-weight: 700;
        color: $text;
        
        @media (max-width: $breakpoint-xs) {
            font-size: 2.2rem;
        }
    }
    
    .train-info {
        display: flex;
        gap: 1.2rem;
        flex-wrap: wrap; // 允许在小屏幕上换行
        
        @media (max-width: $breakpoint-xs) {
            width: 100%;
            justify-content: space-between;
            gap: 0.8rem;
        }
    }
    
    .info-tag {
        display: flex;
        align-items: center;
        gap: 0.5rem;
        padding: 0.6rem 1.2rem;
        background-color: white;
        border-radius: 8px;
        box-shadow: 0 2px 8px $shadow;
        font-size: 1.6rem; // 调整字体大小
        font-weight: 600;
        color: $text;
        
        @media (max-width: $breakpoint-xs) {
            padding: 0.4rem 0.8rem;
            font-size: 1.4rem;
        }
        
        .icon {
            font-size: 1.8rem; // 调整图标大小
            
            @media (max-width: $breakpoint-xs) {
                font-size: 1.6rem;
            }
        }
    }
}

.category-tabs {
    width: 100%; // 改为100%
    max-width: 120rem;
    display: flex;
    gap: 1rem;
    margin-bottom: 2.5rem;
    overflow-x: auto;
    scrollbar-width: none;
    -webkit-overflow-scrolling: touch; // 增强移动端滚动体验
    padding-bottom: 0.5rem; // 增加底部空间滚动指示
    
    &::-webkit-scrollbar {
        display: none;
    }
    
    @media (max-width: $breakpoint-sm) {
        margin-bottom: 2rem;
        gap: 0.8rem;
    }
    
    .tab-btn {
        padding: 0.8rem 1.5rem;
        background: white;
        border: 1px solid $border;
        border-radius: 8px;
        font-size: 1.4rem;
        font-weight: 600;
        color: $text;
        cursor: pointer;
        white-space: nowrap;
        transition: all 0.2s ease;
        flex-shrink: 0; // 防止按钮被压缩
        
        @media (max-width: $breakpoint-xs) {
            padding: 0.7rem 1.2rem;
            font-size: 1.3rem;
        }
        
        &:hover {
            border-color: $primary-light;
            color: $primary;
        }
        
        &.active {
            background: $primary;
            color: white;
            border-color: $primary;
        }
    }
}

.food-order-view {
    width: 100%; // 改为100%
    max-width: 120rem;
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
    height: fit-content;
    gap: 2.5rem;
    
    @media (max-width: $breakpoint-md) {
        grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
        gap: 2rem;
    }
    
    @media (max-width: $breakpoint-sm) {
        grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
        gap: 1.5rem;
    }
    
    @media (max-width: $breakpoint-xs) {
        grid-template-columns: repeat(2, 1fr); // 确保在小屏幕上只有两列
        gap: 1.2rem;
    }
    
    @media (max-width: 360px) {
        grid-template-columns: 1fr; // 在极小屏幕上只有一列
    }
}

.cart-indicator {
    position: fixed;
    bottom: 2rem;
    left: 50%;
    transform: translateX(-50%);
    background: $primary;
    color: white;
    border-radius: 3rem;
    padding: 1rem 2rem;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 2rem;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
    cursor: pointer;
    z-index: 99; // 降低z-index，确保不会覆盖侧边栏
    transition: all 0.3s ease;
    max-width: 90%; // 确保不会超出屏幕
    
    @media (max-width: $breakpoint-xs) {
        width: 90%;
        padding: 0.8rem 1.5rem;
        gap: 1.5rem;
    }
    
    &:hover {
        background: $primary-dark;
        transform: translateX(-50%) translateY(-5px);
        
        @media (max-width: $breakpoint-sm) {
            transform: translateX(-50%) translateY(-3px); // 移动端减小悬浮效果
        }
    }
    
    .cart-icon {
        position: relative;
        
        .cart-emoji {
            font-size: 2.2rem;
            
            @media (max-width: $breakpoint-xs) {
                font-size: 2rem;
            }
        }
        
        .items-count {
            position: absolute;
            top: -8px;
            right: -8px;
            background: $accent;
            color: white;
            border-radius: 50%;
            width: 2.2rem;
            height: 2.2rem;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.2rem;
            font-weight: 700;
            
            @media (max-width: $breakpoint-xs) {
                width: 2rem;
                height: 2rem;
                font-size: 1.1rem;
            }
        }
    }
    
    .total-price {
        font-size: 1.8rem;
        font-weight: 700;
    }
}

// 购物车动画
.cart-fade-enter-active,
.cart-fade-leave-active {
    transition: all 0.3s ease;
}
.cart-fade-enter-from,
.cart-fade-leave-to {
    opacity: 0;
    transform: translateY(20px);
}
</style>