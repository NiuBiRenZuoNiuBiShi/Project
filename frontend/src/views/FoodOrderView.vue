<template>
    <div class="food-view-container">
        <!-- 加载状态 -->
        <div v-if="loading" class="loading-container">
            <div class="loading-spinner"></div>
            <p>正在加载美食...</p>
        </div>

        <!-- 错误状态 -->
        <div v-else-if="error" class="error-container">
            <div class="error-icon">⚠️</div>
            <p>{{ error }}</p>
            <button @click="loadFoodData" class="retry-btn">重新加载</button>
        </div>

        <!-- 主要内容 -->
        <template v-else>
            <div class="page-header">
                <h1>高铁点餐</h1>
                <div class="train-info">
                    <div class="info-tag">
                        <span class="icon">🚄</span>
                        <span>{{ trainInfo.trainNumber }}</span>
                    </div>
                    <div class="info-tag">
                        <span class="icon">🚪</span>  
                        <span>{{ trainInfo.carriageNumber }}号车厢</span>
                    </div>
                    <div class="info-tag">
                        <span class="icon">💺</span>
                        <span>{{ trainInfo.seatNumber }}座</span>
                    </div>
                </div>
            </div>

            <div class="category-tabs">
                <button class="tab-btn" :class="{ active: selectedTab === 'all' }" @click="setSelectedTab('all')">
                    全部菜品 ({{ allFoodItems.length }})
                </button>
                <button class="tab-btn" :class="{ active: selectedTab === 'hot' }" @click="setSelectedTab('hot')">
                    热销推荐 ({{ getItemsByCategory('hot').length }})
                </button>
                <button class="tab-btn" :class="{ active: selectedTab === 'main' }" @click="setSelectedTab('main')">
                    主食 ({{ getItemsByCategory('main').length }})
                </button>
                <button class="tab-btn" :class="{ active: selectedTab === 'snack' }" @click="setSelectedTab('snack')">
                    小吃 ({{ getItemsByCategory('snack').length }})
                </button>
                <button class="tab-btn" :class="{ active: selectedTab === 'drink' }" @click="setSelectedTab('drink')">
                    饮品 ({{ getItemsByCategory('drink').length }})
                </button>
            </div>

            <div v-if="filteredFoodItems.length === 0" class="empty-state">
                <div class="empty-icon">🍽️</div>
                <p>该分类暂无商品</p>
            </div>

            <div v-else class="food-order-view">
                <FoodItem 
                    v-for="(item, index) in filteredFoodItems" 
                    :key="item.id" 
                    :food-pic="item.picUrl || defaultFoodImage"
                    :food-name="item.foodName"
                    :food-price="Number(item.price)"
                    :food-count="item.count || 0"
                    :description="item.description"
                    :tag="getItemTag(item)"
                    @update-count="(change) => handleUpdateCount(item.id, change)" 
                />
            </div>

            <!-- 购物车汇总 -->
            <transition name="cart-fade">
                <ShoppingCartSummary 
                    v-if="showShoppingCartSummary" 
                    :items="itemsInCart" 
                    :delivery-info="deliveryInfo"
                    @clear-cart="clearCart"
                    @close-summary="hideCartSummary" 
                    @update-item="handleCartUpdate"
                    @checkout="handleCheckout"
                />
            </transition>

            <!-- 购物车指示器 -->
            <div class="cart-indicator" v-if="totalItemsCount > 0" @click="showCartSummary">
                <div class="cart-icon">
                    <i class="cart-emoji">🛒</i>
                    <span class="items-count">{{ totalItemsCount }}</span>
                </div>
                <span class="total-price">¥{{ cartTotalPrice.toFixed(2) }}</span>
            </div>

            <!-- 结算确认弹窗 -->
            <transition name="modal-fade">
                <div v-if="showCheckoutModal" class="modal-overlay" @click="closeCheckoutModal">
                    <div class="modal-content" @click.stop>
                        <div class="modal-header">
                            <h3>确认订单</h3>
                            <button @click="closeCheckoutModal" class="modal-close">&times;</button>
                        </div>
                        <div class="modal-body">
                            <div class="order-summary">
                                <div v-for="item in itemsInCart" :key="item.id" class="order-item">
                                    <span>{{ item.foodName }} x{{ item.count }}</span>
                                    <span>¥{{ (item.price * item.count).toFixed(2) }}</span>
                                </div>
                                <div class="order-total">
                                    <strong>总计: ¥{{ (cartTotalPrice + deliveryFee).toFixed(2) }}</strong>
                                </div>
                            </div>
                            <div class="delivery-time">
                                <p>预计送达时间: {{ deliveryInfo.estimatedTime }}</p>
                                <p>送达位置: {{ trainInfo.carriageNumber }}号车厢 {{ trainInfo.seatNumber }}座</p>
                            </div>
                        </div>
                        <div class="modal-actions">
                            <button @click="closeCheckoutModal" class="cancel-btn">取消</button>
                            <button @click="confirmOrder" class="confirm-btn" :disabled="submittingOrder">
                                {{ submittingOrder ? '提交中...' : '确认下单' }}
                            </button>
                        </div>
                    </div>
                </div>
            </transition>
        </template>
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import FoodItem from '@/components/FoodItem.vue';
import ShoppingCartSummary from '@/components/ShoppingCartSummary.vue';
import { getFoodListByTrainId, buyFoodList } from '@/api/FoodApi';

// 响应式数据
const loading = ref(true);
const error = ref('');
const allFoodItems = ref([]);
const selectedTab = ref('all');
const hideSummary = ref(false);
const showCheckoutModal = ref(false);
const submittingOrder = ref(false);

// 默认食物图片
const defaultFoodImage = '/api/placeholder/300/200';

// 列车信息
const trainInfo = ref({
    trainNumber: 'G1234',
    carriageNumber: '12',
    seatNumber: '05A',
    trainId: 'train_g1234' // 实际应该从路由参数或用户状态获取
});

// 配送信息
const deliveryInfo = ref({
    estimatedTime: '15:30-15:45',
    fee: 5.00
});

const deliveryFee = computed(() => deliveryInfo.value.fee);

// 计算属性
const itemsInCart = computed(() => {
    return allFoodItems.value.filter(item => (item.count || 0) > 0);
});

const showShoppingCartSummary = computed(() => {
    return itemsInCart.value.length > 0 && !hideSummary.value;
});

const totalItemsCount = computed(() => {
    return allFoodItems.value.reduce((sum, item) => sum + (item.count || 0), 0);
});

const cartTotalPrice = computed(() => {
    return allFoodItems.value.reduce((sum, item) => sum + (Number(item.price) * (item.count || 0)), 0);
});

const filteredFoodItems = computed(() => {
    if (selectedTab.value === 'all') {
        return allFoodItems.value;
    }
    return getItemsByCategory(selectedTab.value);
});

// 方法
const loadFoodData = async () => {
    try {
        loading.value = true;
        error.value = '';
        
        const response = await getFoodListByTrainId(trainInfo.value.trainId);
        
        // 处理后端返回的数据，添加前端需要的字段
        allFoodItems.value = response.map(item => ({
            ...item,
            count: 0, // 初始化购买数量
            description: generateDescription(item), // 生成商品描述
        }));
        
    } catch (err) {
        console.error('Failed to load food data:', err);
        error.value = err.message || '加载数据失败，请稍后重试';
    } finally {
        loading.value = false;
    }
};

const generateDescription = (item) => {
    // 根据食物类型生成描述
    const descriptions = {
        'main': '精心制作的主食，营养丰富',
        'snack': '美味小食，解馋佳品',
        'drink': '精选饮品，清爽解腻',
        'hot': '人气推荐，不容错过'
    };
    return descriptions[item.foodType] || '高铁特供美食';
};

const getItemTag = (item) => {
    // 根据不同条件返回标签
    if (item.foodType === 'hot') return '热销';
    if (item.lunch && item.dinner) return '全时段';
    if (item.lunch) return '午餐';
    if (item.dinner) return '晚餐';
    return '';
};

const getItemsByCategory = (category) => {
    switch (category) {
        case 'hot':
            return allFoodItems.value.filter(item => item.foodType === 'hot' || item.isHot);
        case 'main':
            return allFoodItems.value.filter(item => item.foodType === 'main');
        case 'snack':
            return allFoodItems.value.filter(item => item.foodType === 'snack');
        case 'drink':
            return allFoodItems.value.filter(item => item.foodType === 'drink');
        default:
            return allFoodItems.value;
    }
};

const setSelectedTab = (tab) => {
    selectedTab.value = tab;
};

const handleUpdateCount = (itemId, change) => {
    const item = allFoodItems.value.find(item => item.id === itemId);
    if (item) {
        const newCount = (item.count || 0) + change;
        if (newCount >= 0) {
            item.count = newCount;
            // 当数量变化时，重新显示购物车汇总
            if (change > 0) {
                hideSummary.value = false;
            }
        }
    }
};

const handleCartUpdate = (item, change) => {
    const foundItem = allFoodItems.value.find(i => i.id === item.id);
    if (foundItem) {
        const newCount = foundItem.count + change;
        foundItem.count = Math.max(0, newCount);
    }
};

const clearCart = () => {
    allFoodItems.value.forEach(item => {
        item.count = 0;
    });
    hideSummary.value = true;
};

const showCartSummary = () => {
    hideSummary.value = false;
};

const hideCartSummary = () => {
    hideSummary.value = true;
};

const handleCheckout = () => {
    showCheckoutModal.value = true;
};

const closeCheckoutModal = () => {
    showCheckoutModal.value = false;
};

const confirmOrder = async () => {
    try {
        submittingOrder.value = true;
        
        // 准备订单数据
        const orderItems = itemsInCart.value.map(item => ({
            id: item.id,
            trainsId: item.trainsId,
            foodName: item.foodName,
            foodType: item.foodType,
            price: item.price,
            lunch: item.lunch,
            dinner: item.dinner,
            picUrl: item.picUrl,
            del: false,
            number: item.count
        }));
        
        // 提交订单
        await buyFoodList(orderItems);
        
        // 订单提交成功
        showCheckoutModal.value = false;
        clearCart();
        
        // 显示成功消息（这里可以用toast组件）
        alert('订单提交成功！预计' + deliveryInfo.value.estimatedTime + '送达');
        
    } catch (err) {
        console.error('Failed to submit order:', err);
        alert('订单提交失败：' + (err.message || '网络错误，请稍后重试'));
    } finally {
        submittingOrder.value = false;
    }
};

// 生命周期
onMounted(() => {
    loadFoodData();
});
</script>

<style lang="scss" scoped>
// 原有样式保持不变，添加新样式
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

$breakpoint-lg: 1200px;
$breakpoint-md: 992px;
$breakpoint-sm: 768px;
$breakpoint-xs: 480px;

.food-view-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 2rem 1.5rem 8rem 1.5rem;
    background-color: $bg-light;
    min-height: 100vh;
    position: relative;
    width: 100%;
    overflow-x: hidden;
    
    @media (max-width: $breakpoint-sm) {
        padding: 1.5rem 1rem 8rem 1rem;
    }
}

// 加载状态样式
.loading-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 50vh;
    gap: 2rem;
    
    .loading-spinner {
        width: 50px;
        height: 50px;
        border: 4px solid $border;
        border-top: 4px solid $primary;
        border-radius: 50%;
        animation: spin 1s linear infinite;
    }
    
    p {
        font-size: 1.6rem;
        color: $text-light;
    }
}

@keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
}

// 错误状态样式
.error-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 50vh;
    gap: 2rem;
    
    .error-icon {
        font-size: 4rem;
    }
    
    p {
        font-size: 1.6rem;
        color: $text-light;
        text-align: center;
    }
    
    .retry-btn {
        padding: 1rem 2rem;
        background: $primary;
        color: white;
        border: none;
        border-radius: 8px;
        font-size: 1.4rem;
        cursor: pointer;
        transition: background 0.2s ease;
        
        &:hover {
            background: $primary-dark;
        }
    }
}

// 空状态样式
.empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 4rem 2rem;
    
    .empty-icon {
        font-size: 4rem;
        margin-bottom: 1rem;
    }
    
    p {
        font-size: 1.6rem;
        color: $text-light;
    }
}

// 原有样式保持不变
.page-header {
    width: 100%;
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
        flex-wrap: wrap;
        
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
        font-size: 1.6rem;
        font-weight: 600;
        color: $text;
        
        @media (max-width: $breakpoint-xs) {
            padding: 0.4rem 0.8rem;
            font-size: 1.4rem;
        }
        
        .icon {
            font-size: 1.8rem;
            
            @media (max-width: $breakpoint-xs) {
                font-size: 1.6rem;
            }
        }
    }
}

.category-tabs {
    width: 100%;
    max-width: 120rem;
    display: flex;
    gap: 1rem;
    margin-bottom: 2.5rem;
    overflow-x: auto;
    scrollbar-width: none;
    -webkit-overflow-scrolling: touch;
    padding-bottom: 0.5rem;
    
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
        flex-shrink: 0;
        
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
    width: 100%;
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
        grid-template-columns: repeat(2, 1fr);
        gap: 1.2rem;
    }
    
    @media (max-width: 360px) {
        grid-template-columns: 1fr;
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
    z-index: 99;
    transition: all 0.3s ease;
    max-width: 90%;
    
    @media (max-width: $breakpoint-xs) {
        width: 90%;
        padding: 0.8rem 1.5rem;
        gap: 1.5rem;
    }
    
    &:hover {
        background: $primary-dark;
        transform: translateX(-50%) translateY(-5px);
        
        @media (max-width: $breakpoint-sm) {
            transform: translateX(-50%) translateY(-3px);
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

// 模态框样式
.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1000;
    padding: 2rem;
}

.modal-content {
    background: white;
    border-radius: 16px;
    width: 100%;
    max-width: 500px;
    max-height: 80vh;
    overflow-y: auto;
    
    .modal-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 2rem 2rem 1rem;
        border-bottom: 1px solid $border;
        
        h3 {
            font-size: 2rem;
            font-weight: 700;
            color: $text;
        }
        
        .modal-close {
            background: none;
            border: none;
            font-size: 2.4rem;
            color: $text-light;
            cursor: pointer;
            
            &:hover {
                color: $text;
            }
        }
    }
    
    .modal-body {
        padding: 2rem;
        
        .order-summary {
            margin-bottom: 2rem;
            
            .order-item {
                display: flex;
                justify-content: space-between;
                padding: 0.8rem 0;
                border-bottom: 1px solid #f0f0f0;
            }
            
            .order-total {
                padding: 1rem 0;
                text-align: right;
                font-size: 1.8rem;
            }
        }
        
        .delivery-time {
            background: $bg-light;
            padding: 1.5rem;
            border-radius: 8px;
            
            p {
                margin: 0.5rem 0;
                color: $text-light;
            }
        }
    }
    
    .modal-actions {
        display: flex;
        gap: 1rem;
        padding: 1rem 2rem 2rem;
        
        button {
            flex: 1;
            padding: 1.2rem;
            border: none;
            border-radius: 8px;
            font-size: 1.6rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.2s ease;
            
            &.cancel-btn {
                background: #f1f5f9;
                color: $text;
                
                &:hover {
                    background: #e2e8f0;
                }
            }
            
            &.confirm-btn {
                background: $primary;
                color: white;
                
                &:hover:not(:disabled) {
                    background: $primary-dark;
                }
                
                &:disabled {
                    background: #cbd5e1;
                    cursor: not-allowed;
                }
            }
        }
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

// 模态框动画
.modal-fade-enter-active,
.modal-fade-leave-active {
    transition: all 0.3s ease;
}
.modal-fade-enter-from,
.modal-fade-leave-to {
    opacity: 0;
}
.modal-fade-enter-from .modal-content,
.modal-fade-leave-to .modal-content {
    transform: scale(0.8) translateY(-50px);
}
</style>