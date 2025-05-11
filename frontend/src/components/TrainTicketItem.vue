<template>
    <div class="ticket-item">
        <div class="ticket-main-info">
            <div class="ticket-time-container">
                <div class="departure">
                    <div class="time">{{ ticket.departTime }}</div>
                    <div class="station">{{ ticket.departStation }}</div>
                </div>

                <div class="duration-line">
                    <div class="arrow-line"></div>
                </div>

                <div class="arrival">
                    <div class="time">{{ ticket.arriveTime }}</div>
                    <div class="station">{{ ticket.arriveStation }}</div>
                </div>
            </div>

            <div class="ticket-train-info">
                <div class="train-number">{{ ticket.trainNumber }}</div>
                <div class="duration">{{ ticket.duration }}</div>
            </div>

            <div class="ticket-price-container">
                <div class="price-label">起</div>
                <div class="price"><i class="fa-solid fa-coins" style="margin-right: 5px;"></i>{{ ticket.price }}</div>
                <button class="select-button" @click="emit('BuyTicket', ticket)">
                    选择
                    <span class="arrow-down">▼</span>
                </button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { defineProps } from 'vue';

const emit = defineEmits(['BuyTicket']);

const props = defineProps({
    ticket: {
        type: Object,
        required: true,
        default: () => ({}), // Provide a default empty object for safety
    },
});
</script>

<style lang="scss" scoped>
.ticket-item {
    background-color: #ffffff;
    border-radius: 12px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    padding: 1.5rem;
    transition: all 0.25s ease;
    border: 1px solid #f0f0f0;

    &:hover {
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
        transform: translateY(-2px);
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
        color: #333;
        /* 主要时间使用深色 */
        margin-bottom: 0.3rem;
    }

    .station {
        font-size: 1.2rem;
        color: #333;
    }
}

.duration-line {
    flex: 1;
    margin: 0 1rem;
    position: relative;
    height: 2px;

    .arrow-line {
        position: absolute;
        width: 100%;
        height: 2px;
        background: linear-gradient(to right, #e0e0e0 0%, #e0e0e0 100%);
        background-size: 8px 2px;
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
            border-left: 4px solid #e0e0e0;
        }
    }
}

.ticket-train-info {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    min-width: 100px;
    text-align: right;

    .train-number {
        font-size: 1.6rem;
        font-weight: 600;
        color: #333;
        margin-bottom: 0.3rem;
    }

    .duration {
        font-size: 1.2rem;
        color: #333;
    }
}

.ticket-price-container {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    gap: 0.5rem;
    min-width: 120px;

    @media (max-width: 768px) {
        flex-direction: row;
        justify-content: space-between;
        align-items: center;
        width: 100%;

        .price {
            margin-right: auto;
            margin-left: 0.5rem;
        }
    }

    .price-label {
        font-size: 1rem;
        color: #333;
    }

    .price {
        font-size: 1.5rem;
        font-weight: 700;
        color: #333;
    }

    .select-button {
        background-color: #6ea8ff;
        color: white;
        border: none;
        border-radius: 8px;
        padding: 0.7rem 1.5rem;
        font-size: 1.2rem;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s ease;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 0.3rem;

        &:hover {
            background-color: #5a9eff;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(110, 168, 255, 0.25);
        }

        &:active {
            transform: translateY(0);
        }

        .arrow-down {
            font-size: 0.7rem;
        }
    }
}

/* Responsive styles specific to the ticket item */
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

        .price {
            margin-right: auto;
            margin-left: 0.5rem;
        }
    }
}

@media (max-width: 480px) {

    .departure .time,
    .arrival .time {
        font-size: 1.3rem;
    }
}
</style>