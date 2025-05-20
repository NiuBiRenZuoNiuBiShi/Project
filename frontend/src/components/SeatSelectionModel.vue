<template>
    <div v-if="isVisible" class="seat-selection-modal">
        <div class="modal-overlay" @click="closeModal"></div>
        <div v-if="isTransferTicket" class="transfer-segment-selector">
            <div class="segment-tabs">
                <div class="segment-tab" :class="{ 'active': !isSecondSegment }" @click="selectSegment(false)">
                    第一段：{{ ticket.departStation }} → {{ ticket.midStation || ticket.midStation1 }}
                </div>
                <div class="segment-tab" :class="{ 'active': isSecondSegment }" @click="selectSegment(true)">
                    第二段：{{ ticket.midStation2 }} → {{ ticket.arriveStation }}
                </div>
            </div>
        </div>
        <div class="modal-container">
            <div class="modal-header">
                <h2>选择座位</h2>
                <button class="close-button" @click="closeModal">×</button>
            </div>

            <div class="train-info">
                <div class="train-route">
                    <span class="station">{{ currentDepartStation }}</span>
                    <div class="arrow-container">
                        <div class="arrow"></div>
                    </div>
                    <span class="station">{{ currentArriveStation }}</span>
                </div>
                <div class="train-number" :class="getTrainClass(currentTrainNumber)">
                    {{ currentTrainNumber }}
                </div>
                <div class="train-time">
                    {{ currentDepartTime }} - {{ currentArriveTime }}
                </div>
            </div>

            <div class="seat-selection-tabs">
                <div v-for="(type, index) in availableSeatTypes" :key="index" class="seat-tab"
                    :class="{ 'active-tab': selectedSeatType === type.type }" @click="selectSeatType(type.type)">
                    <span class="seat-type-name">{{ type.name }}</span>
                    <span class="seat-type-price" v-if="!type.soldout">¥{{ type.price }}</span>
                    <span class="seat-type-price" v-else>SoldOut</span>
                </div>
            </div>

            <div class="seat-map-content">
                <div v-if="loading" class="loading-container">
                    <div class="loading-spinner"></div>
                    <div class="loading-text">正在加载座位信息...</div>
                </div>

                <div v-else class="seat-map-container">
                    <div v-if="errorMessage" class="error-message">
                        {{ errorMessage }}
                    </div>
                    <div v-else-if="currentTypeCoachSeats.length === 0" class="no-seats">
                        暂无可选座位
                    </div>
                    <template v-else>
                        <div class="coach-selector">
                            <button v-for="coach in availableCoachesByType" :key="coach"
                                :class="{ 'active-coach': selectedCoach === coach }" @click="selectCoach(coach)">
                                {{ coach }}车厢
                            </button>
                        </div>

                        <div v-if="selectedSeatType === 'NoSeat'" class="no-seat-info">
                            <div class="no-seat-icon">
                                <i class="el-icon-info"></i>
                            </div>
                            <div class="no-seat-message">
                                无座票不提供固定座位，您可以在车厢内寻找空位或站立乘车。
                            </div>
                            <div class="no-seat-action">
                                <button class="no-seat-select-btn" @click="selectNoSeat">选择无座票</button>
                            </div>
                        </div>
                        <div v-else class="seat-map">
                            <div class="seat-map-header">
                                <div class="seat-row-label"></div>
                                <div v-for="col in uniqueColumns" :key="col" class="seat-column-label">{{ col }}</div>
                            </div>

                            <div v-for="row in uniqueRows" :key="row" class="seat-row">
                                <div class="seat-row-label">{{ row }}</div>
                                <div v-for="col in uniqueColumns" :key="`${row}${col}`" class="seat" :class="{
                                    'seat-occupied': !isSeatAvailable(`${row}${col}`),
                                    'seat-selected': isSelectedSeat(`${row}${col}`)
                                }" @click="toggleSeat(`${row}${col}`)">
                                    {{ !isSeatAvailable(`${row}${col}`) ? '✕' : `${row}${col}` }}
                                </div>
                            </div>
                        </div>

                        <div class="seat-legend">
                            <div class="legend-item">
                                <div class="legend-seat legend-available"></div>
                                <span>可选</span>
                            </div>
                            <div class="legend-item">
                                <div class="legend-seat legend-occupied"></div>
                                <span>已占</span>
                            </div>
                            <div class="legend-item">
                                <div class="legend-seat legend-selected"></div>
                                <span>已选</span>
                            </div>
                        </div>
                    </template>
                </div>
            </div>

            <div class="selected-seats-info" v-if="selectedSeats.length > 0">
                <div class="selected-seats-list">
                    <div class="selected-seat-item" v-for="(seat, index) in selectedSeats" :key="index">
                        {{ seat.coach }}车厢 {{ seat.place }}
                        <button class="remove-seat" @click="removeSeat(index)">×</button>
                    </div>
                </div>
                <div class="selected-seats-total">
                    总计: <span class="total-price">¥{{ totalPrice }}</span>
                </div>
            </div>

            <div class="modal-actions">
                <button class="cancel-button" @click="closeModal">取消</button>
                <button class="confirm-button" @click="confirmSelection" :disabled="selectedSeats.length === 0">
                    确认选择 {{ selectedSeats.length > 0 ? `(${selectedSeats.length})` : '' }}
                </button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue';
import { getSeatsInfoApi } from '@/api/TrainTicketApi';
import { ElMessage } from 'element-plus';

const props = defineProps({
    ticket: {
        type: Object,
        default: () => ({})
    },
    isTransferTicket: {
        type: Boolean,
        default: false
    }
});

const isVisible = defineModel("isVisible", Boolean);
const loading = ref(true);
const errorMessage = ref('');
const seatsData = ref([]);
const selectedSeatType = ref('');
const selectedCoach = ref('');
const selectedSeats = ref([]);
const isSecondSegment = ref(false);

// Fetch seat data when component mounts
onMounted(() => {
    if (isVisible.value) {
        fetchSeats();
    }
});

const currentTrainNumber = computed(() => {
    if (props.isTransferTicket) {
        return isSecondSegment.value
            ? (props.ticket.trainNumber2 || `${props.ticket.type2}`)
            : (props.ticket.trainNumber || props.ticket.trainNumber1 || `${props.ticket.type1}`);
    } else {
        return props.ticket.trainNumber || `${props.ticket.type || ''}`;
    }
});

const currentTrainNumberId = computed(() => {
    if (props.isTransferTicket) {
        return isSecondSegment.value ? props.ticket.trainNumberId2 : props.ticket.trainNumberId1;
    } else {
        return props.ticket.trainNumberId;
    }
});

const currentDepartStation = computed(() => {
    if (props.isTransferTicket) {
        return isSecondSegment.value ? props.ticket.midStation2 : props.ticket.departStation;
    } else {
        return props.ticket.departStation;
    }
});

const currentArriveStation = computed(() => {
    if (props.isTransferTicket) {
        return isSecondSegment.value ? props.ticket.arriveStation : (props.ticket.midStation || props.ticket.midStation1);
    } else {
        return props.ticket.arriveStation;
    }
});

const currentDepartTime = computed(() => {
    if (props.isTransferTicket) {
        return isSecondSegment.value ? props.ticket.midDepartTime : props.ticket.departTime;
    } else {
        return props.ticket.departTime;
    }
});

const currentArriveTime = computed(() => {
    if (props.isTransferTicket) {
        return isSecondSegment.value ? props.ticket.arriveTime : props.ticket.midArriveTime;
    } else {
        return props.ticket.arriveTime;
    }
});

const fetchSeats = async () => {
    try {
        loading.value = true;
        errorMessage.value = '';
        seatsData.value = [];
        selectedSeats.value = [];

        const trainId = currentTrainNumberId.value;
        if (!trainId) {
            errorMessage.value = '无效的车次信息';
            return;
        }

        const response = await getSeatsInfoApi(trainId);
        seatsData.value = response;

        // Initialize with first available seat type
        if (availableSeatTypes.value.length > 0) {
            selectedSeatType.value = availableSeatTypes.value[0].type;
            // Then set the first coach for that type
            if (availableCoachesByType.value.length > 0) {
                selectedCoach.value = availableCoachesByType.value[0];
            }
        }
    } catch (error) {
        console.error('Error fetching seat data:', error);
        errorMessage.value = '获取座位信息失败，请稍后再试';
    } finally {
        loading.value = false;
    }
};


// 监听当前车段变化时刷新座位数据
watch([isSecondSegment, () => props.ticket], () => {
    if (isVisible.value) {
        fetchSeats();
    }
}, { immediate: false });

// Fetch seat data when component mounts or becomes visible
watch(isVisible, (newValue) => {
    if (newValue) {
        fetchSeats();
    }
});

// Group seats by type
const seatsByType = computed(() => {
    const grouped = {};
    seatsData.value.forEach(seat => {
        if (!grouped[seat.seatType]) {
            grouped[seat.seatType] = [];
        }
        grouped[seat.seatType].push(seat);
    });

    return grouped;
});

// Group seats by coach for the selected type
const seatsByCoach = computed(() => {
    const grouped = {};
    if (!selectedSeatType.value) return grouped;
    const typeSeats = seatsByType.value[selectedSeatType.value] || [];
    typeSeats.forEach(seat => {
        if (!grouped[seat.coach]) {
            grouped[seat.coach] = [];
        }
        grouped[seat.coach].push(seat);
    });
    return grouped;
});

const availableSeatTypes = computed(() => {
    const types = [];
    Object.keys(seatsByType.value).forEach(type => {
        const typeSeats = seatsByType.value[type];
        // Only include seat types that have at least one seat
        if (typeSeats && typeSeats.length > 0) {
            types.push({
                type: type,
                name: getTypeName(type),
                price: getPriceForType(type),
                soldout: false
            });
        }
    });
    return types;
});

// 根据座位类型和车段获取价格
// 根据座位类型和车段获取价格
const getPriceForType = (type) => {
    if (props.isTransferTicket) {
        if (isSecondSegment.value) {
            // 第二段车票价格
            switch (type) {
                case 'Business': return props.ticket.businessPrice2 || 0;
                case 'First': return props.ticket.firstPrice2 || 0;
                case 'Second': return props.ticket.secondPrice2 || 0;
                case 'NoSeat': return props.ticket.noSeatPrice2 || 0;
                default: return 0;
            }
        } else {
            // 第一段车票价格
            switch (type) {
                case 'Business': return props.ticket.businessPrice1 || 0;
                case 'First': return props.ticket.firstPrice1 || 0;
                case 'Second': return props.ticket.secondPrice1 || 0;
                case 'NoSeat': return props.ticket.noSeatPrice1 || 0;
                default: return 0;
            }
        }
    } else {
        // 直达车票价格
        switch (type) {
            case 'Business': return props.ticket.businessPrice || 0;
            case 'First': return props.ticket.firstPrice || 0;
            case 'Second': return props.ticket.secondPrice || 0;
            case 'NoSeat': return props.ticket.noSeatPrice || 0;
            default: return 0;
        }
    }
};

// Available coaches for the selected seat type
const availableCoachesByType = computed(() => {
    return Object.keys(seatsByCoach.value).sort((a, b) => parseInt(a) - parseInt(b));
});

// Current seats for selected coach and type
const currentTypeCoachSeats = computed(() => {
    if (!selectedSeatType.value || !selectedCoach.value) return [];
    return seatsByCoach.value[selectedCoach.value] || [];
});

// Get unique row numbers for the selected coach
const uniqueRows = computed(() => {
    const rows = new Set();
    currentTypeCoachSeats.value.forEach(seat => {
        if (seat.row) rows.add(seat.row);
    });
    return Array.from(rows).sort((a, b) => parseInt(a) - parseInt(b));
});

// Get unique column letters for the selected coach
const uniqueColumns = computed(() => {
    const columns = new Set();
    currentTypeCoachSeats.value.forEach(seat => {
        if (seat.column) columns.add(seat.column);
    });
    return Array.from(columns).sort();
});

// Function to select seat type
const selectSeatType = (type) => {
    selectedSeatType.value = type;
    // Reset coach selection when changing seat type
    if (availableCoachesByType.value.length > 0) {
        selectedCoach.value = availableCoachesByType.value[0];
    } else {
        selectedCoach.value = '';
    }
    // Clear selected seats when changing type
    selectedSeats.value = [];
};

// Function to select coach
const selectCoach = (coach) => {
    selectedCoach.value = coach;
};

// Function to select segment (if transfer ticket)
const selectSegment = (isSecond) => {
    if (isSecondSegment.value !== isSecond) {
        isSecondSegment.value = isSecond;
        // Clear selections when changing segments
        selectedSeats.value = [];
        selectedSeatType.value = '';
        selectedCoach.value = '';
        // Refetch data for the new segment
        fetchSeats();
    }
};

// Check if a seat is available

const containsFlags = (flagsArray, checkFlagsArray) => {
    console.log('flagsArray:', flagsArray);
    console.log('checkFlagsArray:', checkFlagsArray);


    if (flagsArray.length !== checkFlagsArray.length) return false;
    for (let i = 0; i < flagsArray.length; i++) {
        if ((flagsArray[i] & checkFlagsArray[i]) !== checkFlagsArray[i]) {
            return false;
        }
    }
    return true;
}

const isSeatAvailable = (place) => {
    const seat = currentTypeCoachSeats.value.find(s => s.place === place);
    if (props.ticket.isTransferTicket) {
        if (isSecondSegment.value) {
            return seat && containsFlags(seat.flags, props.ticket.flags2);
        } else {
            return seat && containsFlags(seat.flags, props.ticket.flags1);
        }
    } else {
        return seat && containsFlags(seat.flags, props.ticket.flags);
    }
};

// Check if a seat is selected
const isSelectedSeat = (place) => {
    return selectedSeats.value.some(s => s.place === place && s.coach === selectedCoach.value);
};

// Toggle seat selection
const toggleSeat = (place) => {
    if (!isSeatAvailable(place)) return; // Can't select unavailable seats
    const seatIndex = selectedSeats.value.findIndex(s =>
        s.place === place && s.coach === selectedCoach.value);
    if (seatIndex !== -1) {
        // Remove seat if already selected
        selectedSeats.value.splice(seatIndex, 1);
    } else {
        // Add seat if not already selected
        const seat = currentTypeCoachSeats.value.find(s => s.place === place);
        if (seat) {
            selectedSeats.value.push({
                id: seat.id,
                type: selectedSeatType.value,
                place: seat.place,
                coach: selectedCoach.value,
                price: getPriceForType(selectedSeatType.value)
            });
        }
    }
};

// 添加无座票选择功能
const selectNoSeat = () => {
    // 清除之前可能已选的无座票
    selectedSeats.value = selectedSeats.value.filter(seat => seat.type !== 'NoSeat');

    // 添加一张无座票
    selectedSeats.value.push({
        id: `noSeat-${Date.now()}`,
        type: 'NoSeat',
        place: '无座',
        coach: '无固定车厢',
        price: getPriceForType('NoSeat')
    });
};

// Remove a selected seat by index
const removeSeat = (index) => {
    selectedSeats.value.splice(index, 1);
};

// Calculate total price
const totalPrice = computed(() => {
    return selectedSeats.value.reduce((total, seat) => {
        return total + (seat.price || 0);
    }, 0);
});

// Function to get train class (implement based on your needs)
const getTrainClass = (trainNumber) => {
    // Implement based on your train classification logic
    if (trainNumber?.startsWith('G')) return 'high-speed';
    if (trainNumber?.startsWith('D')) return 'bullet';
    return 'regular';
};

// Function to get type name (implement based on your needs)
const getTypeName = (type) => {
    // 直接返回座位类型，因为后端已经提供了中文名称
    return type || '未知座位';
};

const emit = defineEmits(['confirm']);

const closeModal = () => {
    isVisible.value = false;
};

const confirmSelection = () => {
    if (selectedSeats.value.length === 0) {
        ElMessage.error('请至少选择一个座位');
        return;
    }

    const selectionData = {
        ticket: props.ticket,
        selectedSeats: selectedSeats.value,
        totalPrice: totalPrice.value,
        transferSegment: props.isTransferTicket ? (isSecondSegment.value ? 2 : 1) : null
    };

    emit('confirm', selectionData);
    closeModal();
};
</script>

<style lang="scss" scoped>
// 引入主题颜色变量
$primary: #4361ee;
$primary-light: #4cc9f0;
$primary-dark: #3a0ca3;
$accent: #f72585;
$accent-light: #ff85a1;
$accent-light1: #ffdae3;
$accent-secondary: #7209b7;
$gradient-start: #4cc9f0;
$gradient-mid: #4361ee;
$gradient-end: #3a0ca3;
$text: #2b2d42;
$text-light: #8d99ae;
$border: #edf2f4;
$shadow: rgba(67, 97, 238, 0.15);
$glass-bg: rgba(255, 255, 255, 0.8);

.seat-map-content {
    flex: 1;
    overflow-y: auto;
    position: relative;
    min-height: 100px;
    max-height: 50vh;
}

.transfer-segment-selector {
    margin-bottom: 1rem;

    .segment-tabs {
        display: flex;
        border-radius: 12px;
        overflow: hidden;
        border: 1px solid $border;

        .segment-tab {
            flex: 1;
            padding: 0.8rem 1rem;
            text-align: center;
            cursor: pointer;
            background-color: #f8f9fa;
            transition: all 0.3s;

            &:first-child {
                border-right: 1px solid $border;
            }

            &:hover {
                background-color: rgba($primary-light, 0.1);
            }

            &.active {
                background-color: $primary;
                color: white;
            }
        }
    }
}

.seat-selection-modal {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 100;

    .modal-overlay {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.5);
        backdrop-filter: blur(5px);
    }

    .modal-container {
        position: relative;
        width: 90%;
        max-width: 700px;
        max-height: 90vh;
        overflow-y: auto;
        background-color: white;
        border-radius: 16px;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
        padding: 1.5rem;
        display: flex;
        flex-direction: column;
        gap: 1.5rem;
        z-index: 101;

        &::-webkit-scrollbar {
            width: 8px;
        }

        &::-webkit-scrollbar-track {
            background: #f1f1f1;
            border-radius: 10px;
        }

        &::-webkit-scrollbar-thumb {
            background: #c1c1c1;
            border-radius: 10px;
        }

        &::-webkit-scrollbar-thumb:hover {
            background: #a8a8a8;
        }
    }

    .modal-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-bottom: 1px solid $border;
        padding-bottom: 1rem;

        h2 {
            font-size: 1.5rem;
            font-weight: 700;
            color: $text;
            margin: 0;
        }

        .close-button {
            background: none;
            border: none;
            font-size: 1.8rem;
            cursor: pointer;
            color: $text-light;
            transition: color 0.3s;

            &:hover {
                color: $accent;
            }
        }
    }
}

.train-info {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 0.8rem;
    padding: 1rem;
    background: linear-gradient(145deg, rgba($gradient-start, 0.05), rgba($gradient-mid, 0.1));
    border-radius: 12px;
    border: 1px solid $border;

    .train-route {
        display: flex;
        align-items: center;
        gap: 1rem;
        width: 100%;
        justify-content: center;

        .station {
            font-size: 1.2rem;
            font-weight: 600;
            color: $text;
        }

        .arrow-container {
            flex: 1;
            max-width: 100px;
            height: 2px;
            background-color: $primary;
            position: relative;

            .arrow {
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

    .train-number {
        font-size: 1.3rem;
        font-weight: 600;
        padding: 0.3rem 0.8rem;
        border-radius: 8px;
    }

    .train-time {
        font-size: 1.1rem;
        color: $text-light;
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

.train-fast,
.train-regular {
    background-color: rgba($text-light, 0.1);
    color: $text-light;
    border: 1px solid rgba($text-light, 0.2);
}

.seat-selection-tabs {
    display: flex;
    position: sticky;
    top: 0;
    background-color: white;
    z-index: 200;
    padding: 1rem 0;
    border-bottom: 1px solid $border;

    &::-webkit-scrollbar {
        height: 0;
        width: 0;
    }

    .seat-tab {
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 0.8rem 1.2rem;
        border-radius: 12px;
        cursor: pointer;
        transition: all 0.3s ease;
        border: 1px solid $border;
        min-width: 80px;

        .seat-type-name {
            font-size: 1rem;
            font-weight: 500;
            color: $text;
        }

        .seat-type-price {
            font-size: 1.1rem;
            font-weight: 600;
            color: $accent;
            margin-top: 0.3rem;
        }

        &:hover {
            background-color: rgba($primary-light, 0.05);
            transform: translateY(-2px);
        }
    }

    .active-tab {
        background: linear-gradient(145deg, $primary, $primary-dark);
        color: white;
        border: none;
        box-shadow: 0 4px 10px rgba($primary, 0.3);

        .seat-type-name,
        .seat-type-price {
            color: white;
        }

        &:hover {
            background: linear-gradient(145deg, $primary, $primary-dark);
            transform: translateY(-2px);
        }
    }
}

.loading-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 1rem;
    padding: 2rem;

    .loading-spinner {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        border: 3px solid rgba($primary, 0.3);
        border-top-color: $primary;
        animation: spin 1s infinite linear;
    }

    .loading-text {
        color: $text-light;
        font-size: 1rem;
    }

    @keyframes spin {
        to {
            transform: rotate(360deg);
        }
    }
}

.error-message {
    padding: 2rem;
    text-align: center;
    color: $accent;
    font-size: 1.1rem;
}

.no-seats {
    padding: 2rem;
    text-align: center;
    color: $text-light;
    font-size: 1.1rem;
}

.seat-map-container {
    display: flex;
    flex-direction: column;
    gap: 1rem;
}

.coach-selector {
    display: flex;
    gap: 0.5rem;
    overflow-x: auto;
    padding: 0.5rem 0;

    &::-webkit-scrollbar {
        height: 4px;
    }

    &::-webkit-scrollbar-track {
        background: #f1f1f1;
        border-radius: 10px;
    }

    &::-webkit-scrollbar-thumb {
        background: #c1c1c1;
        border-radius: 10px;
    }

    button {
        padding: 0.5rem 1rem;
        border: 1px solid $border;
        border-radius: 8px;
        background: white;
        cursor: pointer;
        transition: all 0.3s;
        white-space: nowrap;

        &:hover {
            background-color: rgba($primary-light, 0.1);
        }

        &.active-coach {
            background-color: $primary;
            color: white;
            border-color: $primary;
        }
    }
}

.no-seat-info {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 1.5rem;
    padding: 2rem;
    border: 1px solid $border;
    border-radius: 12px;
    background-color: rgba($primary-light, 0.05);
    text-align: center;

    .no-seat-icon {
        font-size: 2.5rem;
        color: $primary;
    }

    .no-seat-message {
        color: $text;
        font-size: 1.1rem;
        max-width: 80%;
        line-height: 1.6;
    }

    .no-seat-action {
        margin-top: 1rem;

        .no-seat-select-btn {
            padding: 0.8rem 1.5rem;
            border-radius: 10px;
            font-size: 1rem;
            font-weight: 500;
            cursor: pointer;
            transition: all 0.3s;
            background: linear-gradient(145deg, $primary, $primary-dark);
            color: white;
            border: none;
            box-shadow: 0 4px 10px rgba($primary, 0.3);

            &:hover {
                transform: translateY(-2px);
                box-shadow: 0 6px 15px rgba($primary, 0.4);
            }
        }
    }
}

.seat-map {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
    padding: 1rem;
    border: 1px solid $border;
    border-radius: 12px;
    background-color: rgba($primary-light, 0.05);

    .seat-map-header {
        display: flex;
        align-items: center;
        margin-bottom: 0.5rem;

        .seat-row-label {
            width: 2rem;
            text-align: center;
        }

        .seat-column-label {
            flex: 1;
            text-align: center;
            font-weight: 600;
            color: $text-light;
        }
    }

    .seat-row {
        display: flex;
        align-items: center;
        gap: 0.5rem;

        .seat-row-label {
            width: 2rem;
            text-align: center;
            font-weight: 600;
            color: $text-light;
        }

        .seat {
            flex: 1;
            height: 3rem;
            display: flex;
            align-items: center;
            justify-content: center;
            background-color: rgba(255, 255, 255, 0.9);
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
            cursor: pointer;
            transition: all 0.2s;

            &:hover:not(.seat-occupied) {
                transform: translateY(-2px);
                box-shadow: 0 4px 8px rgba($primary, 0.2);
            }

            &.seat-occupied {
                background-color: #f1f1f1;
                color: $text-light;
                cursor: not-allowed;
            }

            &.seat-selected {
                background-color: $primary;
                color: white;
                font-weight: 600;
            }
        }
    }
}

.seat-legend {
    display: flex;
    gap: 1rem;
    padding: 0.5rem;
    justify-content: center;

    .legend-item {
        display: flex;
        align-items: center;
        gap: 0.3rem;

        .legend-seat {
            width: 1.2rem;
            height: 1.2rem;
            border-radius: 4px;
        }

        .legend-available {
            background-color: white;
            border: 1px solid $border;
        }

        .legend-occupied {
            background-color: #f1f1f1;
            border: 1px solid $text-light;
        }

        .legend-selected {
            background-color: $primary;
        }

        span {
            font-size: 0.9rem;
            color: $text-light;
        }
    }
}

.selected-seats-info {
    border-top: 1px solid $border;
    padding-top: 1rem;
    display: flex;
    flex-direction: column;
    gap: 0.8rem;

    .selected-seats-list {
        display: flex;
        flex-wrap: wrap;
        gap: 0.5rem;

        .selected-seat-item {
            padding: 0.5rem 0.8rem;
            background-color: rgba($primary-light, 0.1);
            border-radius: 8px;
            font-size: 0.9rem;
            color: $primary;
            display: flex;
            align-items: center;
            gap: 0.3rem;

            .remove-seat {
                border: none;
                background: none;
                color: $primary;
                cursor: pointer;
                padding: 0 0.3rem;
                font-size: 1rem;
                transition: color 0.2s;

                &:hover {
                    color: $accent;
                }
            }
        }
    }

    .selected-seats-total {
        font-size: 1.1rem;
        text-align: right;

        .total-price {
            font-weight: 600;
            color: $accent;
            font-size: 1.2rem;
        }
    }
}

.modal-actions {
    display: flex;
    justify-content: flex-end;
    gap: 1rem;
    margin-top: 1rem;

    button {
        padding: 0.8rem 1.5rem;
        border-radius: 10px;
        font-size: 1rem;
        font-weight: 500;
        cursor: pointer;
        transition: all 0.3s;
    }

    .cancel-button {
        background-color: white;
        border: 1px solid $border;
        color: $text;

        &:hover {
            background-color: #f1f1f1;
        }
    }

    .confirm-button {
        background: linear-gradient(145deg, $primary, $primary-dark);
        color: white;
        border: none;
        box-shadow: 0 4px 10px rgba($primary, 0.3);

        &:hover:not(:disabled) {
            transform: translateY(-2px);
            box-shadow: 0 6px 15px rgba($primary, 0.4);
        }

        &:disabled {
            background: #cccccc;
            cursor: not-allowed;
            box-shadow: none;
        }
    }
}
</style>