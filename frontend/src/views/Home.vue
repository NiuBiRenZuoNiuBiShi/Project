<script setup>
import { ref } from 'vue';
import QueryBox from '@/components/QueryBox.vue';
</script>

<template>
    <div class="query-box-container">
        <QueryBox />
    </div>
</template>

<style scoped lang="scss">
.query-box-container {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 20rem; // Will delete later
    height: 40vh;
    width: 100%;
}
</style>