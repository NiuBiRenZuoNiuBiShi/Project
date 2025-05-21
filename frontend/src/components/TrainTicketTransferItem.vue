<template>
    <div class="transfer-ticket-item">
        <div class="ticket-main-info">
            <!-- Departure and arrival times with transfer information -->
            <div class="ticket-time-container">
                <div class="time-segment">
                    <div class="departure">
                        <div class="time">{{ ticket.departTime }}</div>
                        <div class="station">{{ ticket.departStation }}</div>
                    </div>

                    <div class="duration-line">
                        <div class="duration">第一程</div>
                        <div class="arrow-line"></div>
                    </div>

                    <div class="arrival">
                        <div class="time">{{ ticket.midArriveTime }}</div>
                        <div class="station">{{ ticket.midStation1 }}</div>
                    </div>
                </div>

                <div class="transfer-info">
                    <div class="transfer-icon">
                        <div class="transfer-dot"></div>
                        <div class="transfer-line"></div>
                        <div class="transfer-dot"></div>
                    </div>
                    <div class="transfer-details">
                        <div class="transfer-city">{{ ticket.transferCity }}</div>
                        <div class="transfer-time">中转{{ ticket.transferTime }}</div>
                        <div v-if="ticket.message" class="transfer-message">{{ ticket.message }}</div>
                    </div>
                </div>

                <div class="time-segment">
                    <div class="departure">
                        <div class="time">{{ ticket.midDepartTime }}</div>
                        <div class="station">{{ ticket.midStation2 }}</div>
                    </div>

                    <div class="duration-line">
                        <div class="duration">第二程</div>
                        <div class="arrow-line"></div>
                    </div>

                    <div class="arrival">
                        <div class="time">{{ ticket.arriveTime }}</div>
                        <div class="station">{{ ticket.arriveStation }}</div>
                    </div>
                </div>
            </div>

            <!-- Train type information -->
            <div class="ticket-train-info">
                <div class="train-route">
                    <div class="train-number" :class="getTrainClass(ticket.type1)">
                        {{ ticket.type1 }}
                    </div>
                    <div class="transfer-indicator">
                        <span>→</span>
                    </div>
                    <div class="train-number" :class="getTrainClass(ticket.type2)">
                        {{ ticket.type2 }}
                    </div>
                </div>
                <div class="duration-info">
                    <span class="total-duration">总用时: {{ ticket.duration }}</span>
                </div>
            </div>

            <!-- Price information and selection button -->
            <div class="ticket-price-container">
                <div class="prices">
                    <div v-if="hasSecondClassSeat" class="price-item">
                        <span class="seat-type">二等座</span>
                        <span class="price">¥{{ totalSecondPrice }}</span>
                    </div>
                    <div v-if="hasFirstClassSeat" class="price-item">
                        <span class="seat-type">一等座</span>
                        <span class="price">¥{{ totalFirstPrice }}</span>
                    </div>
                    <div v-if="hasBusinessClassSeat" class="price-item">
                        <span class="seat-type">商务座</span>
                        <span class="price">¥{{ totalBusinessPrice }}</span>
                    </div>
                </div>
                <button class="select-button" @click="handleBuyTicket">
                    选择
                    <i class="arrow-icon"></i>
                </button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue';

const emit = defineEmits(['buy-ticket']);

const props = defineProps({
    ticket: {
        type: Object,
        required: true,
        default: () => ({}),
    },
});

const handleBuyTicket = () => {
    emit('buy-ticket', props.ticket);
};

// 根据车次类型获取对应的列车类型样式
const getTrainClass = (trainType) => {
    if (!trainType) return '';
    
    switch(trainType) {
        case 'G':
            return 'train-high-speed';
        case 'D':
            return 'train-electric';
        case 'C':
            return 'train-intercity';
        case 'Z':
            return 'train-direct';
        case 'K':
            return 'train-fast';
        default:
            return 'train-regular';
    }
};

// 计算总价格
const totalSecondPrice = computed(() => {
    const price1 = Number(props.ticket.secondPrice1) || 0;
    const price2 = Number(props.ticket.secondPrice2) || 0;
    return price1 + price2;
});

const totalFirstPrice = computed(() => {
    const price1 = Number(props.ticket.firstPrice1) || 0;
    const price2 = Number(props.ticket.firstPrice2) || 0;
    return price1 + price2;
});

const totalBusinessPrice = computed(() => {
    const price1 = Number(props.ticket.businessPrice1) || 0;
    const price2 = Number(props.ticket.businessPrice2) || 0;
    return price1 + price2;
});

// 检查是否有各类型座位
const hasSecondClassSeat = computed(() => {
    return props.ticket.secondPrice1 && props.ticket.secondPrice2;
});

const hasFirstClassSeat = computed(() => {
    return props.ticket.firstPrice1 && props.ticket.firstPrice2;
});

const hasBusinessClassSeat = computed(() => {
    return props.ticket.businessPrice1 && props.ticket.businessPrice2;
});
</script>

<style lang="scss" scoped>
// 引入主题颜色变量
$primary: #4361ee;
$primary-light: #4cc9f0;
$primary-dark: #3a0ca3;
$accent: #f72585;
$accent-light1: #ffdae3;
$accent-light: #ff85a1;
$accent-secondary: #7209b7;
$gradient-start: #4cc9f0;
$gradient-mid: #4361ee;
$gradient-end: #3a0ca3;
$text: #2b2d42;
$text-light: #8d99ae;
$border: #edf2f4;
$shadow: rgba(67, 97, 238, 0.15);
$glass-bg: rgba(255, 255, 255, 0.6);

.transfer-ticket-item {
    background-color: $glass-bg;
    border-radius: 16px;
    box-shadow: 0 8px 20px $shadow;
    padding: 1.5rem;
    transition: all 0.25s ease;
    border: 1px solid $border;
    backdrop-filter: blur(10px);

    &:hover {
        box-shadow: 0 12px 24px rgba($primary, 0.2);
        transform: translateY(-4px);
    }
}

.ticket-main-info {
    display: grid;
    grid-template-columns: 1.8fr 0.8fr 1fr;
    align-items: center;
    gap: 1rem;

    @media (max-width: 768px) {
        display: flex;
        flex-direction: column;
        align-items: stretch;
    }
}

.ticket-time-container {
    display: flex;
    flex-direction: column;
    gap: 1rem;
}

.time-segment {
    display: flex;
    align-items: center;
    justify-content: space-between;
    min-width: 220px;
}

.departure, .arrival {
    text-align: center;

    .time {
        font-size: 1.4rem;
        font-weight: 700;
        color: $text;
        margin-bottom: 0.3rem;
        letter-spacing: -0.5px;
    }

    .station {
        font-size: 0.85rem;
        color: $text-light;
        max-width: 100px;
        margin: 0 auto;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }
}

.duration-line {
    flex: 1;
    margin: 0 1rem;
    position: relative;
    min-height: 30px;
    display: flex;
    flex-direction: column;
    align-items: center;

    .duration {
        font-size: 0.75rem;
        color: $text-light;
        background-color: rgba($primary-light, 0.1);
        padding: 0.2rem 0.6rem;
        border-radius: 12px;
        position: absolute;
        top: -16px;
        z-index: 1;
    }

    .arrow-line {
        position: absolute;
        width: 100%;
        height: 2px;
        background: linear-gradient(to right, $primary-light, $primary);
        top: 50%;
        transform: translateY(-50%);

        &::after {
            content: '';
            position: absolute;
            right: -4px;
            top: 50%;
            transform: translateY(-50%);
            width: 0;
            height: 0;
            border-top: 4px solid transparent;
            border-bottom: 4px solid transparent;
            border-left: 6px solid $primary;
        }
    }
}

.transfer-info {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 0.8rem 0;
    position: relative;
    
    .transfer-icon {
        display: flex;
        flex-direction: column;
        align-items: center;
        margin-right: 1rem;
        
        .transfer-dot {
            width: 10px;
            height: 10px;
            border-radius: 50%;
            background-color: $accent;
        }
        
        .transfer-line {
            width: 2px;
            height: 20px;
            background-color: $accent;
            margin: 3px 0;
        }
    }
    
    .transfer-details {
        display: flex;
        flex-direction: column;
        
        .transfer-city {
            font-size: 0.9rem;
            font-weight: 600;
            color: $accent;
        }
        
        .transfer-time {
            font-size: 0.8rem;
            color: $text-light;
        }
        
        .transfer-message {
            font-size: 0.75rem;
            color: $accent-light;
            margin-top: 0.3rem;
        }
    }
}

.ticket-train-info {
    display: flex;
    flex-direction: column;
    align-items: center;
    min-width: 100px;
    text-align: center;
    padding: 0 0.5rem;
    
    .train-route {
        display: flex;
        align-items: center;
        margin-bottom: 0.8rem;
        
        .train-number {
            font-size: 1.2rem;
            font-weight: 600;
            padding: 0.3rem 0.8rem;
            border-radius: 8px;
        }
        
        .transfer-indicator {
            margin: 0 0.5rem;
            color: $text-light;
            font-size: 1rem;
        }
    }
    
    .duration-info {
        font-size: 0.85rem;
        background-color: rgba($primary, 0.1);
        padding: 0.3rem 0.8rem;
        border-radius: 12px;
        color: $primary;
        
        .total-duration {
            font-weight: 500;
        }
    }
}

.train-high-speed {
    background-color: rgba($primary, 0.1);
    color: $primary;
    border: 1px solid rgba($primary, 0.2);
}

.train-electric {
    background-color: rgba($primary-light, 0.1);
    color: $primary-light;
    border: 1px solid rgba($primary-light, 0.2);
}

.train-intercity {
    background-color: rgba($primary-dark, 0.1);
    color: $primary-dark;
    border: 1px solid rgba($primary-dark, 0.2);
}

.train-direct {
    background-color: rgba($accent-secondary, 0.1);
    color: $accent-secondary;
    border: 1px solid rgba($accent-secondary, 0.2);
}

.train-fast, .train-regular {
    background-color: rgba($text-light, 0.1);
    color: $text-light;
    border: 1px solid rgba($text-light, 0.2);
}

.ticket-price-container {
    display: flex;
    flex-direction: column;
    align-items: stretch;
    gap: 0.8rem;
    min-width: 120px;

    @media (max-width: 768px) {
        flex-direction: row;
        justify-content: space-between;
        align-items: center;
        width: 100%;
    }

    .prices {
        display: flex;
        flex-direction: column;
        gap: 0.3rem;
        
        @media (max-width: 768px) {
            flex-direction: row;
            gap: 1rem;
        }
    }

    .price-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        
        .seat-type {
            font-size: 0.8rem;
            color: $text-light;
        }
        
        .price {
            font-size: 1.1rem;
            font-weight: 600;
            color: $accent;
        }
    }

    .select-button {
        background: linear-gradient(145deg, $primary, $primary-dark);
        color: white;
        border: none;
        border-radius: 12px;
        padding: 0.8rem 1.2rem;
        font-size: 1rem;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s ease;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 0.5rem;
        width: 100%;
        box-shadow: 0 4px 12px rgba($primary, 0.3);

        &:hover {
            background: linear-gradient(145deg, lighten($primary, 5%), $primary);
            transform: translateY(-2px);
            box-shadow: 0 6px 16px rgba($primary, 0.4);
        }

        &:active {
            transform: translateY(0);
        }

        .arrow-icon {
            display: inline-block;
            width: 0;
            height: 0;
            border-left: 5px solid transparent;
            border-right: 5px solid transparent;
            border-top: 5px solid white;
            margin-left: 5px;
        }
    }
}

/* Responsive styles */
@media (max-width: 768px) {
    .ticket-main-info {
        grid-template-columns: 1fr;
        gap: 1.5rem;
    }

    .ticket-price-container {
        align-items: center;
        flex-direction: row;
        justify-content: space-between;
        width: 100%;
    }
    
    .select-button {
        width: auto;
    }
}

@media (max-width: 480px) {
    .departure .time,
    .arrival .time {
        font-size: 1.2rem;
    }
    
    .transfer-ticket-item {
        padding: 1rem;
    }
    
    .prices {
        flex-direction: column !important;
        gap: 0.3rem !important;
    }
    
    .train-route {
        flex-direction: column;
        
        .transfer-indicator {
            transform: rotate(90deg);
            margin: 0.5rem 0;
        }
    }
}
</style>