<template>
  <div class="message-detail">
    <h3>{{ getTypeLabel(message.messageType) }}</h3>
    <p class="timestamp">{{ formatTime(message.sendTime) }}</p>
    <div class="full-content">{{ message.content }}</div>
    <button @click="$router.push('/message-center')">返回列表</button>
  </div>
</template>
