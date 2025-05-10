<template>
  <div class="form-box">
    <div class="inputs-container">
      <div class="locations-container">
        <PositionSearch label="出发地" placeholder="请输入出发城市" />
        <div class="exchange-icon">
          <i class="fas fa-exchange-alt"></i>
        </div>
        <PositionSearch label="目的地" placeholder="请输入到达城市" />
      </div>
      <TimeSelectForm />
    </div>
    <div class="search-button-container">
      <SearchButton />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import PositionSearch from '@/components/PositionSearch.vue';
import TimeSelectForm from '@/components/TimeSelectForm.vue';
import SearchButton from '@/components/SearchButton.vue';
</script>

<style lang="scss" scoped>
.form-box {
  display: flex;
  flex-direction: column;
  width: 100%;
  gap: 2rem;
  
  .inputs-container {
    display: flex;
    gap: 1.5rem;
    width: 100%;
    align-items: flex-start;
    
    @media (max-width: 992px) {
      flex-direction: column;
    }
  }
  
  .locations-container {
    display: flex;
    align-items: flex-start;
    flex: 2;
    gap: 0.5rem;
    
    .exchange-icon {
      display: flex;
      justify-content: center;
      align-items: center;
      background-color: #f0f8ff;
      color: #3498db;
      width: 40px;
      height: 40px;
      border-radius: 50%;
      margin-top: 2rem;
      cursor: pointer;
      transition: all 0.2s ease;

      margin-top: 2.8rem;
      padding: 0 0.5rem;
      
      &:hover {
        background-color: #3498db;
        color: white;
        transform: rotate(180deg);
      }
    }
  }
  
  .search-button-container {
    width: 100%;
    display: flex;
    justify-content: flex-end;
    margin-top: 0.5rem;
    
    @media (max-width: 992px) {
      justify-content: center;
    }
  }
}
</style>