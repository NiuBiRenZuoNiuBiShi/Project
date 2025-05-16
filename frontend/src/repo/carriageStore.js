import { defineStore } from "pinia";
import { ref } from "vue";

export const useCarriageStore = defineStore("carriage", () => {
    const carriages = ref([]);

    const setCarriages = (newCarriages) => {
        //   {
        //     departTime: '06:27',
        //     arriveTime: '13:12',
        //     departStation: '上海虹橋',
        //     arriveStation: '北京南',
        //     trainNumber: 'G104',
        //     duration: '6小時45分',
        //     price: '641.44'
        //   }     
        carriages.value = newCarriages.map((carriage) => {
            return {
                departTime: carriage.depTime,
                arriveTime: carriage.arrTime,
                departStation: carriage.depStation,
                arriveTime: carriage.appStation,
                trainNumber: carriage.trainNumber,
                duration: carriage.arrTime - carriage.depTime,
                businessPrice: carriage.businessPrice,
                firstPrice: carriage.firstPrice,
                secondPrice: carriage.secondPrice,
                noSeatPrice: carriage.noSeatPrice,
            }
        });
    }

    return {
        carriages,
        setCarriages
    };
})