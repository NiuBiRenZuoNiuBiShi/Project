<template>
  <div class="train-ticket-container">
    <div class="filter-container">
      <div class="filter-item" :class="{ 'filter-active': filter === item.name }" @click="changeFilter(item.name)"
        v-for="item in orderByList" :key="item.id">
        {{ item.name }}
      </div>
    </div>

    <div class="ticket-list">
      <div v-if="store.carriages.length === 0" class="no-tickets">
        <div class="no-tickets-icon">🚄</div>
        <div class="no-tickets-text">没有找到符合条件的车票</div>
        <div class="no-tickets-subtext">请尝试修改筛选条件或搜索其他日期</div>
      </div>
      <template v-else>
        <!-- 直达车票 -->
        <TrainTicketItem 
          v-for="(ticket, index) in ticketList" 
          :key="index" 
          :ticket="ticket"
          @buy-ticket="(ticket) => emit('buyTicket', ticket)" 
          v-if="!store.isTransfer"
        />
        
        <!-- 中转车票 -->
        <TrainTicketTransferItem 
          v-for="(ticket, index) in ticketList" 
          :key="index" 
          :ticket="ticket"
          @buy-ticket="(ticket) => emit('buyTicket', ticket)" 
          v-if="store.isTransfer"
        />
      </template>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, computed, onMounted } from 'vue';
import TrainTicketItem from './TrainTicketItem.vue';
import TrainTicketTransferItem from './TrainTicketTransferItem.vue';
import { useCarriageStore } from '@/repo/carriageStore';

const emit = defineEmits(['buyTicket']);
const store = useCarriageStore();

const orderByList = ref([
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

const filter = ref('最早');
const originalList = ref([]);
const ticketList = ref([]);

// 初始化数据并应用默认过滤
onMounted(() => {
  originalList.value = [...store.carriages];
  applyFilter('最早');
});

// 监听车票数据变化
watch(() => store.carriages, (newCarriages) => {
  originalList.value = [...newCarriages];
  applyFilter(filter.value);
}, { deep: true });

// 监听过滤条件变化
watch(filter, (newFilter) => {
  console.log('筛选条件变化:', newFilter);
  applyFilter(newFilter);
});

const timeStrToMinutes = (timeStr) => {
  if (!timeStr) return 0;
  const [hours, minutes] = timeStr.split(':').map(Number);
  return hours * 60 + minutes;
};

const applyFilter = (filterName) => {
  if (originalList.value.length === 0) {
    ticketList.value = [];
    return;
  }

  if (filterName === '只限高铁') {
    // 根据是否为中转车票采用不同的过滤逻辑
    if (store.isTransfer) {
      ticketList.value = originalList.value.filter(ticket => ticket.type1 === 'G' && ticket.type2 === 'G');
    } else {
      ticketList.value = originalList.value.filter(ticket => ticket.trainNumber?.startsWith('G'));
    }
  } else if (filterName === '最早') {
    // 按出发时间排序
    ticketList.value = [...originalList.value].sort((a, b) => {
      const timeA = store.isTransfer ? timeStrToMinutes(a.departTime) : timeStrToMinutes(a.departTime);
      const timeB = store.isTransfer ? timeStrToMinutes(b.departTime) : timeStrToMinutes(b.departTime);
      return timeA - timeB;
    });
  } else if (filterName === '最快') {
    // 提取数字部分并按用时排序
    ticketList.value = [...originalList.value].sort((a, b) => {
      // 从格式化的时间字符串中提取时间
      const extractMinutes = (durationStr) => {
        if (!durationStr) return 0;
        let total = 0;
        if (durationStr.includes('小时')) {
          const hourPart = durationStr.split('小时')[0];
          total += parseInt(hourPart) * 60;
          const minutePart = durationStr.split('小时')[1]?.replace('分', '') || '0';
          total += parseInt(minutePart);
        } else {
          total = parseInt(durationStr.replace('分', ''));
        }
        return total;
      };
      
      return extractMinutes(a.duration) - extractMinutes(b.duration);
    });
  } else {
    // 没有匹配到任何过滤条件，重置为原始列表
    ticketList.value = [...originalList.value];
  }
};

const changeFilter = (filterName) => {
  filter.value = filterName;
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

.train-ticket-container {
  max-width: 1000px;
  margin: 0 auto;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
}

.filter-container {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 1.2rem;
  padding: 1.5rem 2rem;
  max-width: 800px;
  width: 100%;
  margin: 0 auto 1.5rem auto;
  border-radius: 16px;
  background: linear-gradient(145deg, rgba($gradient-start, 0.05), rgba($gradient-mid, 0.1));
  box-shadow: 0 8px 20px $shadow;
  position: relative;
  border: 1px solid rgba($border, 0.8);
  backdrop-filter: blur(10px);
}

.filter-item {
  background: $glass-bg;
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
  color: $text;
  font-size: 1rem;
  font-weight: 500;
  border: 1px solid rgba($border, 0.8);

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
    box-shadow: 0 6px 16px $shadow;

    &::after {
      background: $primary-light;
    }

    color: $primary;
  }

  &:active {
    transform: translateY(1px);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  }
}

.filter-active {
  background: linear-gradient(145deg, $primary, $primary-dark);
  box-shadow: 0 6px 16px rgba($primary, 0.2);
  color: white;
  border: none;

  &::after {
    background: transparent;
  }

  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 20px rgba($primary, 0.3);
    color: white;
  }
}

.ticket-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.no-tickets {
  text-align: center;
  padding: 3rem;
  background: $glass-bg;
  border-radius: 16px;
  box-shadow: 0 4px 16px $shadow;
  backdrop-filter: blur(10px);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
  
  .no-tickets-icon {
    font-size: 3rem;
    margin-bottom: 0.5rem;
  }
  
  .no-tickets-text {
    font-size: 1.5rem;
    font-weight: 600;
    color: $text;
  }
  
  .no-tickets-subtext {
    font-size: 1rem;
    color: $text-light;
  }
}

@media (max-width: 480px) {
  .filter-container {
    grid-template-columns: 1fr;
    gap: 0.8rem;
    padding: 1rem;
  }

  .filter-item {
    padding: 0.8rem;
    font-size: 0.9rem;
  }
}
</style>