<template>
    <div class="outside">
        <div class="train-list-view-container">
            <div class="form-container">
                <QueryTrainTicketFrom v-model:departure="searchForm.departure"
                    v-model:destination="searchForm.destination" v-model:selectedTime="searchForm.selectedTime"
                    v-model:transfer_option="searchForm.transfer_option"
                    v-model:departureType="searchForm.departureType"
                    v-model:destinationType="searchForm.destinationType" @search="search" />
            </div>
            <div class="filter-container">
                <FilterForm v-model:seatType="filterForm.seatType" v-model:departTime="filterForm.departTime"
                    v-model:arriveTime="filterForm.arriveTime" />
            </div>
            <div class="list-container">
                <TrainList @buy-ticket="buyTicket" :filterConditions="filterForm" />
            </div>
        </div>

        <!-- 添加座位选择模态框 -->
        <SeatSelectionModel v-model:is-visible="seatSelectionVisible" :ticket="selectedTicket" v-if="seatSelectionVisible"
            @confirm="onSeatSelectionConfirm" />

        <ContactSelection v-model:is-visible="contactSelectionVisible" :ticket="selectedTicket" 
            v-if="contactSelectionVisible" @confirm="onContactSelectionConfirm" />
    </div>
</template>
<script setup>
import { ref } from 'vue';
import QueryTrainTicketFrom from '@/components/QueryTrainTicketFrom.vue';
import TrainList from '@/components/TrainList.vue';
import FilterForm from '@/components/FilterForm.vue';
import SeatSelectionModel from '@/components/SeatSelectionModel.vue'; // 导入座位选择组件
import ContactSelection from '@/components/ContactSelection.vue'; // 导入联系人选择组件
import { searchTrainTicketsApi, orderTicketApi } from '@/api/TrainTicketApi';
import { useCarriageStore } from '@/repo/carriageStore';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus'; 

const carriageStore = useCarriageStore();
const router = useRouter();
const filterForm = ref({
    seatType: [],
    departTime: [],
    arriveTime: [],
});
const searchForm = ref({
    departure: '',
    destination: '',
    selectedTime: new Date().toISOString().split('T')[0],
    transfer_option: false,
    departureType: '',
    destinationType: ''
});

// 添加座位选择相关状态
const seatSelectionVisible = ref(false);
const selectedTicket = ref(null);
const seatSelectionData = ref(null);

const contactSelectionVisible = ref(false);

const search = () => {
    searchTrainTicketsApi(searchForm.value)
        .then((response) => {
            console.log('搜索结果:', response.data);
            carriageStore.setCarriages(response.data);
        })
        .catch((error) => {
            console.error('搜索失败:', error);
            carriageStore.setCarriages([]);
        })
        .finally(() => {
            router.push({ name: 'trains' })
        });
};

const buyTicket = (ticket) => {
    console.log('选择车票:', ticket);
    selectedTicket.value = ticket;
    seatSelectionVisible.value = true;
};

// 座位选择确认处理
const onSeatSelectionConfirm = (selectionData) => {
    console.log('座位选择确认:', selectionData);
    seatSelectionData.value = selectionData; // 保存座位选择结果
    seatSelectionVisible.value = false;      // 关闭座位选择模态框
    contactSelectionVisible.value = true;    // 显示联系人选择模态框
};

// 联系人选择确认处理
const onContactSelectionConfirm = (data) => {
    console.log('联系人选择确认:', data);
    contactSelectionVisible.value = false;
    
    // 构造最终的订票请求
    const orderRequest = {
        carriageId: seatSelectionData.value.ticket.carriageId,
        seatIdList: seatSelectionData.value.selectedSeats,
        totalPrice: seatSelectionData.value.totalPrice,
        versionList: seatSelectionData.value.versionList,
        // 添加是否为中转票信息
        isTransfer: carriageStore.isTransfer,
        // 如果是中转票，添加额外信息
        transferSegment: seatSelectionData.value.transferSegment,
        // 添加联系人信息
        contactIdList: data.contact.contactId
    };
    
    // 调用API提交订单
    orderTicketApi(orderRequest)
        .then(response => {
            if (response.data && response.data.code === 200) {
                ElMessage.success('订票成功！');
                router.push({ name: 'orders' });
            } else {
                ElMessage.error(response.data?.message || '订票失败，请重试');
            }
        })
        .catch(error => {
            console.error('订票失败:', error);
            ElMessage.error('订票失败，请检查网络连接或重试');
        });
};
</script>

<style lang="scss" scoped>
.outside {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #f8f9fa;
}

.train-list-view-container {
    display: grid;
    grid-template-columns: 1fr 3.5fr;
    justify-content: space-between;
    justify-items: start;
    align-items: start;
    column-gap: 1.5rem;
    row-gap: 1rem;

    width: 95%;
    margin: 0 auto;
}

.form-container {
    width: 100%;
    grid-column: 1 / -1;
    height: 20rem;
    margin: 20px auto;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    background-color: #fff;
}

.filter-container {
    width: 100%;
    grid-column: span 1;
}

.list-container {
    width: 100%;
    grid-column: 2 / 3;
}
</style>