<template>
    <div class="shopping-cart-summary">
        <div class="summary-header">
            <div class="header-left">
                <span class="cart-icon">🛒</span>
                <span class="title">购物车</span>
            </div>
            <div class="header-right">
                <button @click="emit('clear-cart')" class="clear-cart-button">清空</button>
                <button @click="emit('close-summary')" class="close-button">&times;</button>
            </div>
        </div>

        <div class="delivery-info">
            <div class="delivery-icon">🚄</div>
            <div class="delivery-text">
                <span>预计送达时间</span>
                <strong>{{ deliveryInfo.estimatedTime }}</strong>
            </div>
        </div>

        <div class="cart-divider"></div>

        <div class="summary-items">
            <div v-for="(item, index) in items" :key="item.id || index" class="summary-item">
                <div class="item-details">
                    <span class="item-name">{{ item.foodName }}</span>
                    <span class="item-price">¥{{ (Number(item.price) * item.count).toFixed(2) }}</span>
                </div>
                <div class="item-controls">
                    <button class="control-btn dec" @click="updateItem(item, -1)">-</button>
                    <span class="item-count">{{ item.count }}</span>
                    <button class="control-btn inc" @click="updateItem(item, 1)">+</button>
                </div>
            </div>
        </div>

        <div class="cart-divider"></div>

        <div class="summary-calculation">
            <div class="calc-row">
                <span>商品金额</span>
                <span>¥{{ totalAmount.toFixed(2) }}</span>
            </div>
            <div class="calc-row">
                <span>配送费</span>
                <span>¥{{ deliveryInfo.fee.toFixed(2) }}</span>
            </div>
        </div>

        <div class="summary-total">
            <span class="total-label">合计</span>
            <span class="total-price">¥{{ finalTotal.toFixed(2) }}</span>
        </div>

        <div class="summary-actions">
            <button class="checkout-button" @click="handleCheckout">
                <span>去结算</span>
                <span class="arrow">→</span>
            </button>
        </div>
    </div>
</template>

<script setup>
import { computed } from 'vue';

// 接收父组件传递的props
const props = defineProps({
    items: {
        type: Array,
        default: () => []
    },
    deliveryInfo: {
        type: Object,
        default: () => ({
            estimatedTime: '15:30-15:45',
            fee: 5.00
        })
    }
});

// 定义可以触发的事件
const emit = defineEmits(['clear-cart', 'close-summary', 'update-item', 'checkout']);

// 计算商品总计金额
const totalAmount = computed(() => {
    return props.items.reduce((sum, item) => {
        return sum + (Number(item.price) * item.count);
    }, 0);
});

// 计算最终总价（包含配送费）
const finalTotal = computed(() => {
    return totalAmount.value + props.deliveryInfo.fee;
});

// 更新购物车中的商品数量
const updateItem = (item, change) => {
    emit('update-item', item, change);
};

// 处理结算按钮点击
const handleCheckout = () => {
    emit('checkout');
};
</script>

<style lang="scss" scoped>
// 变量定义
$primary: #4a8eff;
$primary-light: #6ba3ff;
$primary-dark: #3270e9;
$accent: #ff6b6b;
$accent-dark: #e74c3c;
$text: #2c3e50;
$text-light: #5d7290;
$bg-light: #f8faff;
$border: #e6eaf0;
$shadow: rgba(50, 112, 233, 0.1);

// 添加媒体查询断点
$breakpoint-md: 992px;
$breakpoint-sm: 768px;
$breakpoint-xs: 480px;

.shopping-cart-summary {
    position: fixed;
    bottom: 2rem;
    right: 2rem;
    width: 38rem;
    background-color: #fff;
    border-radius: 20px;
    box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
    padding: 2rem;
    z-index: 100;
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
    max-height: 80vh;
    overflow-y: auto;

    @media (max-width: $breakpoint-md) {
        right: 1.5rem;
        width: 35rem;
    }

    @media (max-width: $breakpoint-sm) {
        right: 1rem;
        bottom: 7rem;
        width: 32rem;
        padding: 1.5rem;
        gap: 1.2rem;
        max-height: 70vh;
    }

    @media (max-width: $breakpoint-xs) {
        left: 50%;
        right: auto;
        transform: translateX(-50%);
        width: 90%;
        max-width: 32rem;
        padding: 1.5rem;
        border-radius: 16px;
    }
}

.summary-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .header-left {
        display: flex;
        align-items: center;
        gap: 1rem;

        .cart-icon {
            font-size: 2.2rem;

            @media (max-width: $breakpoint-xs) {
                font-size: 2rem;
            }
        }

        .title {
            font-size: 2rem;
            font-weight: 700;
            color: $text;

            @media (max-width: $breakpoint-xs) {
                font-size: 1.8rem;
            }
        }
    }

    .header-right {
        display: flex;
        align-items: center;
        gap: 1rem;
    }

    .clear-cart-button {
        background-color: #f1f5f9;
        border: none;
        color: $text-light;
        cursor: pointer;
        font-size: 1.4rem;
        padding: 0.5rem 1.2rem;
        border-radius: 8px;
        font-weight: 600;
        transition: all 0.2s ease;

        @media (max-width: $breakpoint-xs) {
            font-size: 1.3rem;
            padding: 0.4rem 1rem;
        }

        &:hover {
            background-color: #e2e8f0;
            color: $text;
        }
    }

    .close-button {
        background: none;
        border: none;
        color: $text-light;
        font-size: 2.4rem;
        cursor: pointer;
        line-height: 1;

        &:hover {
            color: $text;
        }
    }
}

.delivery-info {
    display: flex;
    align-items: center;
    gap: 1.5rem;
    background-color: #f1f9ff;
    padding: 1.2rem;
    border-radius: 12px;

    @media (max-width: $breakpoint-xs) {
        padding: 1rem;
        gap: 1.2rem;
    }

    .delivery-icon {
        font-size: 2.2rem;

        @media (max-width: $breakpoint-xs) {
            font-size: 2rem;
        }
    }

    .delivery-text {
        display: flex;
        flex-direction: column;

        span {
            font-size: 1.3rem;
            color: $text-light;

            @media (max-width: $breakpoint-xs) {
                font-size: 1.2rem;
            }
        }

        strong {
            font-size: 1.6rem;
            color: $text;
            font-weight: 700;

            @media (max-width: $breakpoint-xs) {
                font-size: 1.5rem;
            }
        }
    }
}

.cart-divider {
    height: 1px;
    background-color: $border;
    margin: 0.5rem 0;
}

.summary-items {
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
    max-height: 40vh;
    overflow-y: auto;
    padding-right: 0.5rem;

    @media (max-width: $breakpoint-sm) {
        gap: 1.2rem;
        max-height: 35vh;
    }
}

.summary-item {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .item-details {
        display: flex;
        flex-direction: column;
        gap: 0.5rem;

        .item-name {
            font-size: 1.6rem;
            font-weight: 600;
            color: $text;

            @media (max-width: $breakpoint-xs) {
                font-size: 1.5rem;
            }
        }

        .item-price {
            font-size: 1.5rem;
            font-weight: 700;
            color: $primary-dark;

            @media (max-width: $breakpoint-xs) {
                font-size: 1.4rem;
            }
        }
    }

    .item-controls {
        display: flex;
        align-items: center;
        gap: 1rem;

        @media (max-width: $breakpoint-xs) {
            gap: 0.8rem;
        }

        .control-btn {
            width: 30px;
            height: 30px;
            border: none;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.6rem;
            cursor: pointer;
            transition: all 0.2s ease;

            @media (max-width: $breakpoint-xs) {
                width: 28px;
                height: 28px;
                font-size: 1.4rem;
            }

            &.inc {
                background-color: $primary;
                color: white;

                &:hover {
                    background-color: $primary-dark;
                }
            }

            &.dec {
                background-color: #f1f5f9;
                color: $text;

                &:hover {
                    background-color: #e2e8f0;
                }
            }
        }

        .item-count {
            font-size: 1.6rem;
            font-weight: 600;
            color: $text;
            min-width: 25px;
            text-align: center;

            @media (max-width: $breakpoint-xs) {
                font-size: 1.4rem;
                min-width: 20px;
            }
        }
    }
}

.summary-calculation {
    display: flex;
    flex-direction: column;
    gap: 0.8rem;

    .calc-row {
        display: flex;
        justify-content: space-between;
        font-size: 1.5rem;
        font-weight: 500;
        color: $text-light;

        @media (max-width: $breakpoint-xs) {
            font-size: 1.4rem;
        }
    }
}

.summary-total {
    display: flex;
    justify-content: space-between;
    padding-top: 1rem;
    border-top: 1px solid $border;

    .total-label {
        font-size: 1.8rem;
        font-weight: 700;
        color: $text;

        @media (max-width: $breakpoint-xs) {
            font-size: 1.6rem;
        }
    }

    .total-price {
        font-size: 2.2rem;
        font-weight: 700;
        color: $accent-dark;

        @media (max-width: $breakpoint-xs) {
            font-size: 2rem;
        }
    }
}

.summary-actions {
    margin-top: 1rem;
}

.checkout-button {
    width: 100%;
    padding: 1.2rem;
    border: none;
    border-radius: 12px;
    background: linear-gradient(135deg, $primary, $primary-dark);
    color: white;
    font-size: 1.8rem;
    font-weight: 700;
    cursor: pointer;
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 1rem;
    transition: all 0.3s ease;
    box-shadow: 0 4px 15px $shadow;

    @media (max-width: $breakpoint-xs) {
        padding: 1rem;
        font-size: 1.6rem;
        border-radius: 10px;
    }

    &:hover {
        transform: translateY(-2px);
        box-shadow: 0 8px 20px rgba(50, 112, 233, 0.2);

        @media (max-width: $breakpoint-sm) {
            transform: translateY(-1px);
        }
    }

    &:active {
        transform: translateY(0);
    }

    .arrow {
        transition: transform 0.3s ease;
    }

    &:hover .arrow {
        transform: translateX(5px);
    }
}
</style>