<template>
  <div class="form-box">
    <div class="inputs-container">
      <div class="locations-container">
        <PositionSearch label="出发地" placeholder="请输入出发城市" v-model="departure"/>
        <div class="exchange-icon">
          <i class="fas fa-exchange-alt"></i>
        </div>
        <PositionSearch label="目的地" placeholder="请输入到达城市" v-model="destination"/>
      </div>
      <TimeSelectForm v-model="selectedTime"/>
    </div>
    <div class="options-enter-warpper">
      <div class="transfer-option-wrapper">
        <label for="transfer-option" class="transfer-option-label">
          是否需要中转
        </label>
        <input type="checkbox" v-model="transfer_option" id="transfer-option" />
      </div>
      <div class="search-button-container">
        <SearchButton @search="emit('search')"/>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'; 
import PositionSearch from '@/components/PositionSearch.vue';
import TimeSelectForm from '@/components/TimeSelectForm.vue';
import SearchButton from '@/components/SearchButton.vue';

const emit = defineEmits(['search']);

const departure = defineModel('departure');
const destination = defineModel('destination');
const selectedTime = defineModel('selectedTime');
const transfer_option = defineModel('transfer_option');

</script>

<style lang="scss" scoped>
.form-box {
  display: flex;
  flex-direction: column;
  width: 100%;
  gap: 1rem;

  .inputs-container {
    display: flex;
    gap: 1.5rem;
    width: 100%;
    align-items: flex-start;

    @media (max-width: 992px) {
      flex-direction: column;
    }
  }

  .options-enter-warpper {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    gap: 1rem;

    @media (max-width: 992px) {
      flex-direction: column;
      align-items: center;
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

  .transfer-option-wrapper {
    display: flex;
    align-items: center;
    width: 40%;
    gap: 1rem;
    margin-top: 1rem;
    padding: 1.5rem;
    border-radius: 10px;

    .transfer-option-label {
      font-size: 1.2rem;
      color: #333;
    }

    input {
      width: 20px;
      height: 20px;
      cursor: pointer;
      border: 2px solid #999;
      border-radius: 4px;
      position: relative;
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