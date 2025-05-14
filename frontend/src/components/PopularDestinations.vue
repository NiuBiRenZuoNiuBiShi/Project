<script setup>
import { ref } from 'vue';

const images = import.meta.glob('../assets/pic/popularCity/*.jpg', {
    eager: true,
    import: 'default'
})

// 热门目的地数据
const popularDestinations = ref([
    { id: 1, name: '北京', image: images['../assets/pic/popularCity/Beijing.jpg'], tag: '热门' },
    { id: 2, name: '上海', image: images['../assets/pic/popularCity/Shanghai.jpg'], tag: '商务' },
    { id: 3, name: '广州', image: images['../assets/pic/popularCity/Guangzhou.jpg'], tag: '美食' },
    { id: 4, name: '深圳', image: images['../assets/pic/popularCity/Shenzhen.jpg'], tag: '科技' },
    { id: 5, name: '杭州', image: images['../assets/pic/popularCity/Hangzhou.jpg'], tag: '风景' },
    { id: 6, name: '成都', image: images['../assets/pic/popularCity/Chengdu.jpg'], tag: '休闲' }
])
</script>

<template>
    <div class="popular-destinations">
        <h2 class="section-title">
            <i class="fas fa-fire"></i> 热门目的地
        </h2>
        <div class="destination-cards">
            <div v-for="destination in popularDestinations" :key="destination.id" class="destination-card">
                <div class="destination-img">
                    <img :src="destination.image" :alt="destination.name">
                    <span class="destination-tag">{{ destination.tag }}</span>
                </div>
                <div class="destination-name">{{ destination.name }}</div>
            </div>
        </div>
    </div>
</template>

<style lang="scss" scoped>
// 颜色变量
$primary: #4a8eff;
$primary-light: #6ba3ff;
$primary-dark: #3270e9;
$text: #2c3e50;
$text-light: #5d7290;
$border: #e6eaf0;
$shadow: rgba(50, 112, 233, 0.08);
$secondary-color: #f1c40f;
$bg-card: #ffffff;

.popular-destinations {
    max-width: 1200px;
    margin: 4rem auto 0;
    animation: fadeIn 0.8s ease-out;

    .section-title {
        font-size: 2.2rem;
        font-weight: 600;
        color: $text;
        margin-bottom: 2rem;
        display: flex;
        align-items: center;
        gap: 1rem;

        i {
            font-size: 2rem;
            color: #ff6b6b;
        }
    }

    .destination-cards {
        display: flex;
        gap: 2rem;
        flex-wrap: wrap;
        justify-content: center;

        .destination-card {
            width: 120px;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 4px 12px $shadow;
            background-color: $bg-card;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            cursor: pointer;

            &:hover {
                transform: translateY(-5px);
                box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
            }

            .destination-img {
                position: relative;
                height: 80px;

                img {
                    width: 100%;
                    height: 100%;
                    object-fit: cover;
                }

                .destination-tag {
                    position: absolute;
                    top: 0.5rem;
                    right: 0.5rem;
                    background-color: rgba($secondary-color, 0.9);
                    color: $text;
                    font-size: 1.2rem;
                    font-weight: 600;
                    padding: 0.2rem 0.6rem;
                    border-radius: 4px;
                }
            }

            .destination-name {
                padding: 1rem;
                text-align: center;
                font-size: 1.4rem;
                font-weight: 500;
            }
        }
    }
}

@keyframes fadeIn {
    from {
        opacity: 0;
    }

    to {
        opacity: 1;
    }
}

// 响应式设计
@media (max-width: 768px) {
    .popular-destinations {
        margin-top: 3rem;

        .section-title {
            font-size: 2rem;
        }

        .destination-cards {
            gap: 1.5rem;

            .destination-card {
                width: calc(33.33% - 1rem);
            }
        }
    }
}

@media (max-width: 480px) {
    .destination-cards {
        .destination-card {
            width: calc(50% - 0.75rem) !important;
        }
    }
}
</style>