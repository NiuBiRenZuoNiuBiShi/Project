<template>
    <div class="food-view-container">
        <div class="food-order-view">
            <FoodItem
                v-for="(item, index) in foodItems"
                :key="index"
                :foodPic="item.foodPic"
                :foodName="item.foodName"
                :foodPrice="item.foodPrice"
                :foodCount="item.count"
                @update-count="(change) => handleUpdateCount(index, change)"
            />
        </div>

        <ShoppingCartSummary
            v-if="showShoppingCartSummary"
            :items="itemsInCart"
            @clear-cart="clearCart"
            @close-summary="hideSummary = true"
        />
    </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import FoodItem from '@/components/FoodItem.vue';
import ShoppingCartSummary from '@/components/ShoppingCartSummary.vue'; // 导入新的组件
import foodImageUrl from '@/assets/pic/food/anh-nguyen-kcA-c3f_3FE-unsplash.jpg';

// 定义食物数据，包含数量 count
const foodItems = ref([
    { foodPic: foodImageUrl, foodName: 'Salad', foodPrice: 100.00, count: 0 },
    { foodPic: foodImageUrl, foodName: 'Salad', foodPrice: 100.00, count: 0 },
    { foodPic: foodImageUrl, foodName: 'Salad', foodPrice: 100.00, count: 0 },
    { foodPic: foodImageUrl, foodName: 'Salad', foodPrice: 100.00, count: 0 },
]);

// 隐藏汇总组件的状态
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
</script>

<style lang="scss" scoped>
.food-view-container {
    display: flex;
    justify-content: center;
    padding: 2rem 0; 
    background-color: #f8f8f8;
    min-height: 100vh; 
    position: relative; 
}

.food-order-view {
    width: 90%;
    max-width: 120rem;
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); 
    height: fit-content;
    gap: 2rem; 
}

</style>