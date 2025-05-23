<template>
  <div class="message-detail-container">
    <div class="card">
      <h2 class="title">{{ getTypeLabel(message.messageType) }}</h2>
      <p class="time">{{ formatTime(message.sendTime) }}</p>
      <div class="content">{{ message.content }}</div>
      <button class="back-btn" @click="$router.push('/notifications')">← 返回消息列表</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MessageDetail',
  props: ['id'],
  data() {
    return {
      message: {}
    };
  },
  async mounted() {
    try {
      const res = await fetch(`/api/notifications/detail/${this.id}`);
      const result = await res.json();
      if (result.code === 200) {
        this.message = result.data;
      } else {
        alert('获取消息失败：' + result.message);
      }
    } catch (err) {
      alert('网络错误，请稍后再试');
    }
  },
  methods: {
    getTypeLabel(type) {
      const map = {
        PAY_SUCCESS: '支付成功',
        ORDER_CANCEL: '订单取消',
        DEP_REMINDER: '发车提醒'
      };
      return map[type] || '系统通知';
    },
    formatTime(time) {
      return new Date(time).toLocaleString();
    }
  }
};
</script>

<style scoped lang="scss">
.message-detail-container {
  max-width: 900px;
  height: 90%;
  margin: 5rem auto;
  padding: 2rem;
  display: flex;
  justify-content: center;
}

.card {
  width: 100%;
  background: white;
  border-radius: 16px;
  padding: 3rem;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.06);
  text-align: left;

  .title {
    font-size: 2.4rem;
    font-weight: 700;
    margin-bottom: 1rem;
    color: #2c3e50;
  }

  .time {
    font-size: 1.2rem;
    color: #888;
    margin-bottom: 2rem;
  }

  .content {
    font-size: 1.5rem;
    color: #333;
    line-height: 1.8;
    white-space: pre-wrap;
    margin-bottom: 3rem;
  }

  .back-btn {
    background-color: #4a8eff;
    color: white;
    border: none;
    padding: 0.8rem 2rem ;
    border-radius: 8px;
    font-size: 1.4rem;
    cursor: pointer;
    transition: 0.3s;
    position: fixed;
    bottom: 10rem;

    &:hover {
      background-color: #3a76d0;
    }
  }
}
</style>
