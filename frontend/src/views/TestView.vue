<template>
    <div>
        <FoodItem :foodPic="foodImageUrl" />
    </div>
</template>

<script setup>
import FoodItem from '@/components/FoodItem.vue';
import foodImageUrl from '@/assets/pic/food/anh-nguyen-kcA-c3f_3FE-unsplash.jpg';

</script>

<style lang="scss" scoped>

</style>