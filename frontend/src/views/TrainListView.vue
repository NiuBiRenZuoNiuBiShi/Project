<template>
    <div class="outside">
        <div class="train-list-view-container">
            <div class="form-container">
                <QueryTrainTicketFrom v-model:departure="searchForm.departure"
                    v-model:destination="searchForm.destination" v-model:selectedTime="searchForm.selectedTime"
                    v-model:transfer_option="searchForm.transfer_option" @search="search" />
            </div>
            <div class="filter-container">
                <FilterForm v-model:seatType="filterForm.seatType" v-model:departTime="filterForm.departTime"
                    v-model:arriveTime="filterForm.arriveTime" />
            </div>
            <div class="list-container">
                <TrainList v-model:orderBy="orderBy" @buy-ticket="buyTicket"/>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import QueryTrainTicketFrom from '@/components/QueryTrainTicketFrom.vue';
import TrainList from '@/components/TrainList.vue';
import FilterForm from '@/components/FilterForm.vue';

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
});

const orderBy = ref('最早');

const search = () => {
    console.log(searchForm.value);
};

const buyTicket = (ticket) => {
    console.log('购买车票:', ticket);
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
    column-gap: 1rem;
    row-gap: 1rem;

    width: 80%;
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