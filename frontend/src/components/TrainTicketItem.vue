<template>
    <div class="ticket-item">
        <div class="ticket-main-info">
            <div class="ticket-time-container">
                <div class="departure">
                    <div class="time">{{ ticket.departTime }}</div>
                    <div class="station">{{ ticket.departStation }}</div>
                </div>

                <div class="duration-line">
                    <div class="duration">{{ ticket.duration }}</div>
                    <div class="arrow-line"></div>
                </div>

                <div class="arrival">
                    <div class="time">{{ ticket.arriveTime }}</div>
                    <div class="station">{{ ticket.arriveStation }}</div>
                </div>
            </div>

            <div class="ticket-train-info">
                <div class="train-number" :class="getTrainClass(ticket.trainNumber || ticket.type)">
                    {{ ticket.trainNumber }}
                </div>
                <div class="seat-info">
                    <span v-if="ticket.secondNumber > 0" class="seat-available">二等座: {{ ticket.secondNumber }}张</span>
                    <span v-else class="seat-unavailable">二等座: 无票</span>
                </div>
            </div>

            <div class="ticket-price-container">
                <div class="prices">
                    <div v-if="ticket.secondPrice" class="price-item">
                        <span class="seat-type">二等座</span>
                        <span class="price">¥{{ ticket.secondPrice }}</span>
                    </div>
                    <div v-if="ticket.firstPrice" class="price-item">
                        <span class="seat-type">一等座</span>
                        <span class="price">¥{{ ticket.firstPrice }}</span>
                    </div>
                    <div v-if="ticket.businessPrice" class="price-item">
                        <span class="seat-type">商务座</span>
                        <span class="price">¥{{ ticket.businessPrice }}</span>
                    </div>
                    <div v-if="ticket.noSeatPrice" class="price-item">
                        <span class="seat-type">无座</span>
                        <span class="price">¥{{ ticket.noSeatPrice }}</span>
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

// 根据车次号获取对应的列车类型样式
const getTrainClass = (trainNumber) => {
    if (!trainNumber) return '';
    
    const firstChar = typeof trainNumber === 'string' ? trainNumber.charAt(0) : trainNumber;
    
    switch(firstChar) {
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

.ticket-item {
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
    grid-template-columns: 1.8fr 1fr 1.2fr;
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
    align-items: center;
    justify-content: space-between;
    min-width: 220px;
}

.departure,
.arrival {
    text-align: center;

    .time {
        font-size: 2rem;
        font-weight: 700;
        color: $text;
        margin-bottom: 0.3rem;
        letter-spacing: -0.5px;
    }

    .station {
        font-size: 1.1rem;
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
        font-size: 1.2rem;
        color: $text-light;
        background-color: rgba($primary-light, 0.2);
        padding: 0.2rem 0.6rem;
        border-radius: 10px;
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

.ticket-train-info {
    display: flex;
    flex-direction: column;
    align-items: center;
    min-width: 100px;
    text-align: center;
    padding: 0 0.5rem;

    .train-number {
        font-size: 1.4rem;
        font-weight: 600;
        color: $text;
        margin-bottom: 0.5rem;
        padding: 0.3rem 0.8rem;
        border-radius: 8px;
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

    .seat-info {
        font-size: 1.2rem;
        
        .seat-available {
            color: $primary;
        }
        
        .seat-unavailable {
            color: $text-light;
        }
    }
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
            font-size: 1.2rem;
            color: $text-light;
        }
        
        .price {
            font-size: 1.2rem;
            font-weight: 600;
            color: $accent;
        }
    }
    
    .price-item + .price-item {
        border-top: 1px solid #ccc;
    }

    .select-button {
        background: linear-gradient(145deg, $primary, $primary-dark);
        color: white;
        border: none;
        border-radius: 12px;
        padding: 0.8rem 1.2rem;
        font-size: 1.2rem;
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
        font-size: 1.3rem;
    }
    
    .ticket-item {
        padding: 1rem;
    }
    
    .prices {
        flex-direction: column !important;
        gap: 0.3rem !important;
    }
}
</style>