<template>
    <div class="shopping-cart-summary">
        <div class="summary-header">
            <span>购物车</span>
            <button @click="emit('clear-cart')" class="clear-cart-button">清空</button>
            <button @click="emit('close-summary')" class="close-button">&times;</button>
        </div>

        <div class="summary-items">
            <div v-for="(item, index) in items" :key="item.id || index" class="summary-item"> <span class="item-name">{{ item.foodName }}</span>
                <span class="item-count">{{ item.count }}</span>
                <span class="item-price">¥{{ (item.foodPrice * item.count).toFixed(2) }}</span>
            </div>
        </div>

        <div class="summary-total">
            <span class="total-label">商品合计</span>
            <span class="total-price">¥{{ totalAmount.toFixed(2) }}</span>
        </div>

        <div class="summary-actions">
            <button class="checkout-button">去结算 ></button>
        </div>
    </div>
</template>

<script setup>
import { computed } from 'vue';

// 接收父组件传递的已添加食物列表
const props = defineProps({
    items: {
        type: Array,
        default: () => []
    }
});

// 定义可以触发的事件 (清空购物车, 关闭汇总)
const emit = defineEmits(['clear-cart', 'close-summary']);

// 计算商品总计金额
const totalAmount = computed(() => {
    return props.items.reduce((sum, item) => {
        return sum + item.foodPrice * item.count;
    }, 0);
});
</script>

<style lang="scss" scoped>
.shopping-cart-summary {
    position: fixed; 
    bottom: 2rem;
    right: 2rem; 
    width: 30rem; 
    background-color: #fff; 
    border-radius: 12px; 
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15); 
    padding: 1.5rem; 
    z-index: 1000; 
    display: flex;
    flex-direction: column;
    gap: 1rem; 
    max-height: 80vh; 
    overflow-y: auto; 
}

.summary-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-bottom: 1rem; 
    border-bottom: 1px solid #eee;
    font-weight: 600;
    color: #333;

    > span { 
        font-size: 1.6rem; 
        font-weight: 700;
        color: #3498db; 
    }

    .clear-cart-button {
        background-color: #ecf0f1;
        border: 1px solid #bdc3c7;
        color: #555; 
        cursor: pointer;
        font-size: 1.4rem; 
        padding: 0.3rem 0.8rem;
        border-radius: 5px;
        text-decoration: none; 
        transition: background-color 0.3s ease, border-color 0.3s ease, color 0.3s ease;
        font-weight: 600;

        &:hover {
            background-color: #bdc3c7;
            border-color: #95a5a6;
            color: #333;
        }
         &:active { 
            background-color: #95a5a6;
        }
    }

     .close-button {
        background: none;
        border: none;
        color: #999; 
        font-size: 1.8rem;
        cursor: pointer;
        padding: 0.2rem;
         &:hover {
            color: #555;
        }
    }
}


.summary-items {
    display: flex;
    flex-direction: column;
    gap: 0.8rem; 
    flex-grow: 1; 
    padding-right: 0.5rem;
}

.summary-item {
    display: flex;
    justify-content: space-between;
    font-size: 1.8rem; 
    font-weight: 600;
    color: #555; 

    .item-name {
        flex-grow: 1; 
        margin-right: 0.5rem;
         overflow: hidden; 
         text-overflow: ellipsis;
         white-space: nowrap; 
    }

    .item-count {
        font-weight: 600;
        color: #333;
        margin-right: 2rem;
        flex-shrink: 0; 
    }

    .item-price {
        font-weight: 600;
        color: #2980b9;
        flex-shrink: 0; 
    }
}

.summary-total {
    display: flex;
    justify-content: space-between;
    padding-top: 1rem;
    border-top: 1px solid #eee; 
    font-size: 1.8rem;
    font-weight: 700;
    color: #333;

    .total-price {
        color: #e67e22;
    }
}

.summary-actions {
    margin-top: 1.2rem;
}

.checkout-button {
    width: 100%; 
    padding: 0.8rem;
    border: none;
    border-radius: 8px; 
    background-color: #3498db;
    color: #fff; 
    font-size: 1.8rem;
    font-weight: 700;
    cursor: pointer;
    transition: background-color 0.3s ease;

    &:hover {
        background-color: #2980b9;
    }
     &:active { 
        background-color: #247bb6;
    }
}
</style>