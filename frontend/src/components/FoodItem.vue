<template>
    <div class="food-item-container">
        <div class="food-pic">
            <img :src="foodPic" :alt="foodName" />
            <span v-if="tag" class="food-tag">{{ tag }}</span>
        </div>
        <div class="food-content">
            <div class="food-name">
                <span>{{ foodName }}</span>
            </div>
            <div v-if="description" class="food-description">
                {{ description }}
            </div>
            <div class="food-rating">
                <div class="stars">★★★★★</div>
                <span class="rating-text">4.8 (24)</span>
            </div>
            <div class="price-and-controls">
                <div class="food-price">
                    <span>¥{{ foodPrice.toFixed(2) }}</span>
                </div>
                <div class="change-wrapper">
                    <button class="food-del" @click="emit('update-count', -1)" :disabled="foodCount <= 0">-</button>
                    <span class="food-count">{{ foodCount }}</span>
                    <button class="food-add" @click="emit('update-count', 1)">+</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
const props = defineProps({
    foodPic: String, 
    foodName: String, 
    foodPrice: Number, 
    foodCount: Number,
    description: {
        type: String,
        default: ''
    },
    tag: {
        type: String,
        default: ''
    }
});
const emit = defineEmits(['update-count']);
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

.food-item-container {
    background-color: #fff;
    border-radius: 16px;
    overflow: hidden;
    display: flex;
    flex-direction: column;
    box-shadow: 0 10px 25px rgba(0, 0, 0, 0.05);
    transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
    height: 100%;
    
    &:hover {
        transform: translateY(-10px);
        box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
        
        @media (max-width: $breakpoint-sm) {
            transform: translateY(-5px); // 减小移动端悬浮效果
        }
        
        .food-pic img {
            transform: scale(1.08);
        }
    }
    
    @media (max-width: $breakpoint-xs) {
        border-radius: 12px;
    }
}

.food-pic {
    width: 100%;
    height: 200px;
    overflow: hidden;
    position: relative;
    
    @media (max-width: $breakpoint-md) {
        height: 180px;
    }
    
    @media (max-width: $breakpoint-sm) {
        height: 150px;
    }
    
    @media (max-width: $breakpoint-xs) {
        height: 120px;
    }
    
    img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        transition: transform 0.6s ease;
    }
    
    .food-tag {
        position: absolute;
        top: 1rem;
        right: 1rem;
        padding: 0.4rem 1rem;
        background: $accent;
        color: white;
        border-radius: 2rem;
        font-size: 1.2rem;
        font-weight: 600;
        box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        
        @media (max-width: $breakpoint-xs) {
            padding: 0.3rem 0.8rem;
            font-size: 1.1rem;
            top: 0.7rem;
            right: 0.7rem;
        }
    }
}

.food-content {
    padding: 1.5rem;
    display: flex;
    flex-direction: column;
    flex-grow: 1;
    
    @media (max-width: $breakpoint-xs) {
        padding: 1.2rem;
    }
}

.food-name {
    margin-bottom: 0.5rem;
    
    span {
        font-size: 1.8rem;
        font-weight: 700;
        color: $text;
        
        @media (max-width: $breakpoint-sm) {
            font-size: 1.6rem;
        }
    }
}

.food-description {
    font-size: 1.4rem;
    color: $text-light;
    margin-bottom: 1rem;
    line-height: 1.4;
    
    @media (max-width: $breakpoint-sm) {
        font-size: 1.3rem;
        margin-bottom: 0.8rem;
        
        // 在移动端限制文本行数
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
    }
}

.food-rating {
    display: flex;
    align-items: center;
    margin-bottom: 1.5rem;
    gap: 0.5rem;
    
    @media (max-width: $breakpoint-sm) {
        margin-bottom: 1.2rem;
    }
    
    .stars {
        color: #FFD700;
        letter-spacing: 2px;
        font-size: 1.4rem;
        
        @media (max-width: $breakpoint-xs) {
            font-size: 1.2rem;
            letter-spacing: 1px;
        }
    }
    
    .rating-text {
        font-size: 1.2rem;
        color: $text-light;
        
        @media (max-width: $breakpoint-xs) {
            font-size: 1.1rem;
        }
    }
}

.price-and-controls {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: auto;
}

.food-price {
    span {
        font-size: 1.8rem;
        font-weight: 700;
        color: $primary-dark;
        
        @media (max-width: $breakpoint-sm) {
            font-size: 1.6rem;
        }
    }
}

.change-wrapper {
    display: flex;
    align-items: center;
    gap: 1rem;
    
    @media (max-width: $breakpoint-xs) {
        gap: 0.8rem;
    }
    
    button {
        width: 36px;
        height: 36px;
        border: none;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 1.6rem;
        font-weight: 700;
        cursor: pointer;
        transition: all 0.2s ease;
        flex-shrink: 0;
        
        @media (max-width: $breakpoint-sm) {
            width: 32px;
            height: 32px;
            font-size: 1.4rem;
        }
        
        @media (max-width: $breakpoint-xs) {
            width: 28px;
            height: 28px;
            font-size: 1.3rem;
        }
        
        &.food-add {
            background-color: $primary;
            color: white;
            
            &:hover {
                background-color: $primary-dark;
                transform: scale(1.1);
                
                @media (max-width: $breakpoint-sm) {
                    transform: scale(1.05);
                }
            }
        }
        
        &.food-del {
            background-color: #f1f5f9;
            color: $text;
            
            &:hover:not(:disabled) {
                background-color: #e2e8f0;
            }
            
            &:disabled {
                background-color: #f1f5f9;
                color: #cbd5e1;
                cursor: not-allowed;
            }
        }
    }
    
    .food-count {
        font-size: 1.6rem;
        font-weight: 600;
        color: $text;
        min-width: 25px;
        text-align: center;
        
        @media (max-width: $breakpoint-sm) {
            font-size: 1.4rem;
            min-width: 20px;
        }
    }
}
</style>