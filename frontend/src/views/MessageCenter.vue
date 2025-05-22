
<template>
  <div class="message-center-container">
    <h2 class="title">消息中心</h2>

    <div class="tabs">
      <button :class="{ active: currentTab === 'all' }" @click="switchTab('all')">全部消息</button>
      <button :class="{ active: currentTab === 'unread' }" @click="switchTab('unread')">未读消息</button>
      <button class="mark-all-btn" @click="markAllAsRead">全部标记为已读</button>
    </div>

    <div class="message-list">
      <div v-for="msg in messages" :key="msg.messageId" class="message-card" @click="viewMessage(msg.messageId)">
        <div class="header">
          <span class="type">{{ msg.messageType }}</span>
          <span class="status" v-if="!msg.read">未读</span>
        </div>
        <div class="content">{{ msg.content.slice(0, 50) }}...</div>
        <div class="time">{{ formatDate(msg.sendTime) }}</div>
      </div>
    </div>

    <div class="pagination-controls">
      <button @click="prevPage" :disabled="currentPage === 1">← 上一页</button>
      <span class="page-info">第 {{ currentPage }} 页 / 共 {{ totalPages }} 页</span>
      <button @click="nextPage" :disabled="currentPage === totalPages">下一页 →</button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      currentTab: 'all',
      currentPage: 1,
      totalPages: 1,
      messages: [],
      userId: localStorage.getItem('userId')
    };
  },
  methods: {
    async fetchMessages() {
      const url =
          this.currentTab === 'all'
              ? `/api/notifications/page?userId=${this.userId}&page=${this.currentPage}`
              : `/api/notifications/page/unread?userId=${this.userId}&page=${this.currentPage}`;
      const res = await fetch(url);
      const result = await res.json();
      if (result.code === 200) {
        this.messages = result.data.records;
        this.totalPages = result.data.totalPages;
      }
    },
    switchTab(tab) {
      this.currentTab = tab;
      this.currentPage = 1;
      this.fetchMessages();
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.fetchMessages();
      }
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.fetchMessages();
      }
    },
    async markAllAsRead() {
      const res = await fetch(`/api/notifications/${this.userId}/readAll`, { method: 'POST' });
      const result = await res.json();
      if (result.code === 200) {
        alert('所有消息已标记为已读');
        this.fetchMessages();
      }
    },
    async viewMessage(messageId) {
      this.$router.push({ name: 'MessageDetail', params: { id: messageId } });
    },
    formatDate(time) {
      return new Date(time).toLocaleString();
    }
  },
  mounted() {
    this.fetchMessages();
  }
};
</script>

<style scoped lang="scss">
.message-center-container {
  width: 90%;
  height: 90%;
  margin: 4rem auto;
  background: #fff;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.05);
}

.title {
  font-size: 2.4rem;
  font-weight: 700;
  margin-bottom: 2rem;
  text-align: center;
}

.tabs {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-bottom: 1.5rem;

  button {
    padding: 0.6rem 1.5rem;
    font-size: 1.4rem;
    border: none;
    border-radius: 6px;
    background: #eee;
    cursor: pointer;
    transition: 0.2s;

    &.active {
      background: #4a8eff;
      color: #fff;
    }

    &:hover {
      background: #4a8eff;
      color: #fff;
    }
  }

  .mark-all-btn {
    margin-left: auto;
    background: #28a745;
    color: white;

    &:hover {
      background: #218838;
    }
  }
}

.message-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;

  .message-card {
    border: 1px solid #eee;
    border-radius: 8px;
    padding: 1rem;
    background: #f9f9f9;
    cursor: pointer;
    transition: 0.2s;

    &:hover {
      background: #f1f8ff;
    }

    .header {
      display: flex;
      justify-content: space-between;
      font-weight: bold;
      font-size: 1.3rem;
      margin-bottom: 0.5rem;

      .status {
        color: red;
      }
    }

    .content {
      font-size: 1.4rem;
      color: #444;
      margin-bottom: 0.5rem;
    }

    .time {
      font-size: 1.2rem;
      color: #888;
    }
  }
}

.pagination-controls {
  position: fixed;
  bottom: 8%;
  left: 50%;
  justify-content: center;
  align-items: center;
  gap: 1.5rem;
  margin-top: 2rem;


  button {
    background-color: #4a8eff;
    border: none;
    color: white;
    padding: 0.6rem 1.2rem;
    border-radius: 6px;
    font-size: 1.4rem;
    cursor: pointer;
    transition: 0.3s;

    &:disabled {
      background-color: #ccc;
      cursor: not-allowed;
    }

    &:hover:not(:disabled) {
      background-color: #3a76d0;
    }
  }

  .page-info {
    font-size: 1.4rem;
    color: #444;
  }
}
</style>
