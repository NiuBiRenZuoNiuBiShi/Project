<template>
    <div class="food-item-container">
        <div class="food-pic">
            <img :src="foodPic" :alt="foodName" />
        </div>
        <div class="food-name"> <span>{{ foodName }}</span>
        </div>

        <div class="price-and-controls">
            <div class="food-price">
                <span>¥{{ foodPrice.toFixed(2) }}</span> </div>
            <div class="change-wrapper">
                <button class="food-del" @click="emit('update-count', -1)" :disabled="foodCount <= 0">-</button> <span class="food-count">{{ foodCount }}</span>
                <button class="food-add" @click="emit('update-count', 1)">+</button>
            </div>
        </div>
    </div>
</template>

<script setup>
const { foodPic, foodName, foodPrice, foodCount } = defineProps(['foodPic', 'foodName', 'foodPrice', 'foodCount']);

const emit = defineEmits(['update-count']);

</script>

<style lang="scss" scoped>
.food-item-container {
    background-color: #fff;
    border-radius: 12px; 
    overflow: hidden;
    display: flex; 
    flex-direction: column; 
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    transition: transform 0.3s ease, box-shadow 0.3s ease; 
    padding-bottom: 1rem; 

    &:hover {
        transform: translateY(-5px); 
        box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
    }
}

.food-pic {
    width: 100%;
    height: 200px;
    overflow: hidden;
    margin-bottom: 0.5rem; 

    img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        transition: transform 0.3s ease;

        &:hover {
            transform: scale(1.1);
        }
    }
}

.food-name {
    padding: 0 1rem;
    margin-bottom: 1rem; 
    span {
        font-size: 1.4rem;
        font-weight: 700;
        color: #333; 
    }
}

.price-and-controls {
    display: flex; 
    justify-content: space-between;
    align-items: center;
    padding: 0 1rem;
    margin-top: auto;
}

.food-price {
     span {
        font-size: 1.2rem; 
        font-weight: 700;
        color: #2980b9;
    }
}

.change-wrapper {
    display: flex; 
    align-items: center; 
    gap: 0.8rem; 
}

.change-wrapper button {
    width: 30px;
    height: 30px;
    border: none;
    border-radius: 50%;
    background-color: #3498db; 
    color: #fff; 
    font-size: 1.2rem;
    font-weight: 700;
    cursor: pointer;
    transition: background-color 0.3s ease;
    flex-shrink: 0; 

    &:hover {
        background-color: #2980b9; 
    }

    &:disabled {
        background-color: #bdc3c7; 
        cursor: not-allowed;
    }
}

.change-wrapper .food-count {
    font-size: 1.2rem;
    font-weight: 600;
    color: #333; 
    min-width: 25px;
    text-align: center;
    flex-shrink: 0;
}
</style>