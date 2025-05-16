<template>
  <div class="train-ticket-container">
    <div class="filter-container">
      <div
        class="filter-item"
        :class="{ 'filter-active': filter === item.name }"
        @click="changeFilter(item.name)"
        v-for="item in filterList"
        :key="item.id"
      >
        {{ item.name }}
      </div>
    </div>

    <div class="ticket-list">
      <TrainTicketItem
        v-for="(ticket, index) in ticketList"
        :key="index"
        :ticket="ticket"
        @buyTicket="(ticket) => emit('buyTicket', ticket)"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import TrainTicketItem from './TrainTicketItem.vue';

const emit = defineEmits(['buyTicket']);

const filterList = ref([
  {
    id: 1,
    name: '只限高铁'
  },
  {
    id: 2,
    name: '最早'
  },
  {
    id: 3,
    name: '最快'
  }
]);

const filter = defineModel('orderBy', '最早');
watch(filter, (newFilter) => {
  console.log('筛选条件变化:', newFilter);
  // 在这里可以调用父组件或store的方法来应用筛选
});

const changeFilter = (filterName) => {
  filter.value = filterName;
};


import { useCarriageStore } from '@/repo/carriageStore';
const store = useCarriageStore();
const ticketList = store.carriages;

// const ticketList = ref([
//   {
//     departTime: '06:27',
//     arriveTime: '13:12',
//     departStation: '上海虹橋',
//     arriveStation: '北京南',
//     trainNumber: 'G104',
//     duration: '6小時45分',
//     price: '641.44'
//   },
//   {
//     departTime: '06:37',
//     arriveTime: '12:38',
//     departStation: '上海虹橋',
//     arriveStation: '北京南',
//     trainNumber: 'G102',
//     duration: '6小時1分',
//     price: '618.87'
//   },
//   {
//     departTime: '07:00',
//     arriveTime: '11:36',
//     departStation: '上海',
//     arriveStation: '北京南',
//     trainNumber: 'G2',
//     duration: '4小時36分',
//     price: '717.72'
//   },
//   {
//     departTime: '07:22',
//     arriveTime: '13:22',
//     departStation: '上海虹橋',
//     arriveStation: '北京南',
//     trainNumber: 'G106',
//     duration: '6小時',
//     price: '618.87'
//   },
//   {
//     departTime: '07:27',
//     arriveTime: '13:36',
//     departStation: '上海虹橋',
//     arriveStation: '北京南',
//     trainNumber: 'G108',
//     duration: '6小時9分',
//     price: '618.87'
//   },
//   {
//     departTime: '07:38',
//     arriveTime: '13:32',
//     departStation: '上海虹橋',
//     arriveStation: '北京南',
//     trainNumber: 'G110',
//     duration: '5小時54分',
//     price: '618.87'
//   }
// ]);
</script>

<style lang="scss" scoped>
.train-ticket-container {
  max-width: 1000px;
  margin: 0 auto;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
}

/* 筛选条件栏样式 - Retained in parent as it belongs to the filter section */
.filter-container {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 1.2rem;
  padding: 1.5rem;
  max-width: 800px;
  width: 100%;
  margin: 0 auto 1.5rem auto;
  border-radius: 16px;
  background-color: #f5f7fa;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  position: relative;
  border: 1px solid rgba(0, 0, 0, 0.05);
}

/* 筛选条件栏样式 - Updated to create a container effect */
.filter-container {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 2rem;
  padding: 1.5rem 3rem;
  max-width: 900px;
  width: 100%;
  margin: 0 auto 1.5rem auto;
  border-radius: 16px;
  background-color: #f5f7fa;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  position: relative;
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.filter-item {
  background: #fff;
  width: 100%;
  grid-column: span 1;
  padding: 1.2rem 0.8rem;
  text-align: center;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  cursor: pointer;
  color: #555;
  font-size: 1.6rem;
  font-weight: 500;

  &::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 3px;
    background: transparent;
    transition: all 0.3s ease;
  }

  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);

    &::after {
      background: #6ea8ff;
    }

    color: #6ea8ff;
  }

  &:active {
    transform: translateY(1px);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  }
}

.filter-active {
  background: linear-gradient(145deg, #6ea8ff, #5a9eff);
  box-shadow: 0 6px 16px rgba(110, 168, 255, 0.2);
  color: #fff;

  &::after {
    background: transparent;
    height: 4px;
  }

  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 20px rgba(110, 168, 255, 0.25);
    color: #fff;
  }
}

.ticket-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

@media (max-width: 480px) {
  .filter-container {
    grid-template-columns: 1fr;
    gap: 0.8rem;
  }

  .filter-item {
    padding: 0.8rem;
  }
}
</style>