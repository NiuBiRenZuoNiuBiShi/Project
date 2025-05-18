import { defineStore } from "pinia";
import { ref } from "vue";

export const useCarriageStore = defineStore("carriage", () => {
    const carriages = ref([]);
    const isTransfer = ref(false);

    /**
     * 将时间字符串转换为分钟数
     * @param {string} timeStr 时间字符串，格式为 "HH:mm"
     * @returns {number} 分钟数
     */
    const timeStrToMinutes = (timeStr) => {
        if (!timeStr) return 0;
        // 处理DateTime格式 (2023-05-17T06:27:00) 或 时间格式 (06:27)
        const timePart = timeStr.includes('T') 
            ? timeStr.split('T')[1].substring(0, 5) 
            : timeStr.substring(0, 5);
        
        const [hours, minutes] = timePart.split(':').map(Number);
        return hours * 60 + minutes;
    };

    /**
     * 格式化持续时间为小时和分钟
     * @param {number} minutes 总分钟数
     * @returns {string} 格式化后的时间，如 "6小时45分"
     */
    const formatDuration = (minutes) => {
        const hours = Math.floor(minutes / 60);
        const mins = minutes % 60;
        
        if (hours > 0 && mins > 0) {
            return `${hours}小时${mins}分`;
        } else if (hours > 0) {
            return `${hours}小时`;
        } else {
            return `${mins}分`;
        }
    };

    /**
     * 格式化LocalDateTime为时间字符串 "HH:mm"
     * @param {string} dateTimeStr ISO格式的日期时间字符串
     * @returns {string} 格式化后的时间字符串
     */
    const formatTimeStr = (dateTimeStr) => {
        if (!dateTimeStr) return "";
        return dateTimeStr.includes('T') 
            ? dateTimeStr.split('T')[1].substring(0, 5)
            : dateTimeStr.substring(0, 5);
    };

    /**
     * 设置车次数据，自动识别是直达还是中转车次
     * @param {Array} newCarriages 车次数据数组
     */
    const setCarriages = (newCarriages) => {
        if (!newCarriages || newCarriages.length === 0) {
            carriages.value = [];
            return;
        }
        
        // 通过检查对象属性判断是直达车次还是中转车次
        const isTransferData = 'depTimeStart' in newCarriages[0];
        isTransfer.value = isTransferData;
        
        if (isTransferData) {
            // 处理中转车次数据
            carriages.value = newCarriages.map((carriage) => {
                // 计算总行程时间
                const startMinutes = timeStrToMinutes(carriage.depTimeStart);
                const endMinutes = timeStrToMinutes(carriage.arrTimeEnd);
                const duration = endMinutes - startMinutes;
                
                return {
                    isTransfer: true,
                    // 第一段行程信息
                    departTime: formatTimeStr(carriage.depTimeStart),
                    midArriveTime: formatTimeStr(carriage.arrTimeMiddle),
                    departStation: carriage.depStation,
                    midStation1: carriage.midStation1,
                    // 第二段行程信息
                    midDepartTime: formatTimeStr(carriage.depTimeMiddle),
                    arriveTime: formatTimeStr(carriage.arrTimeEnd),
                    midStation2: carriage.midStation2,
                    arriveStation: carriage.arrStation,
                    // 中转信息
                    transferCity: carriage.midCity,
                    transferTime: carriage.transferTime ? formatDuration(timeStrToMinutes(carriage.transferTime)) : "",
                    message: carriage.message,
                    // 行程时间
                    duration: formatDuration(duration),
                    // 第一段价格信息
                    businessPrice1: carriage.businessPrice1,
                    firstPrice1: carriage.firstPrice1,
                    secondPrice1: carriage.secondPrice1,
                    noSeatPrice1: carriage.noSeatPrice1,
                    // 第二段价格信息
                    businessPrice2: carriage.businessPrice2,
                    firstPrice2: carriage.firstPrice2,
                    secondPrice2: carriage.secondPrice2,
                    noSeatPrice2: carriage.noSeatPrice2,
                    // 车型信息
                    type1: carriage.type1,
                    type2: carriage.type2,
                };
            });
        } else {
            // 处理直达车次数据
            carriages.value = newCarriages.map((carriage) => {
                // 计算行程时间
                const departMinutes = timeStrToMinutes(carriage.depTime);
                const arriveMinutes = timeStrToMinutes(carriage.arrTime);
                const duration = arriveMinutes - departMinutes;
                
                return {
                    isTransfer: false,
                    departTime: formatTimeStr(carriage.depTime),
                    arriveTime: formatTimeStr(carriage.arrTime),
                    departStation: carriage.depStation,
                    arriveStation: carriage.arrStation,
                    trainNumber: carriage.trainNumber,
                    duration: formatDuration(duration),
                    type: carriage.type,
                    // 座位价格
                    businessPrice: carriage.businessPrice,
                    firstPrice: carriage.firstPrice,
                    secondPrice: carriage.secondPrice,
                    noSeatPrice: carriage.noSeatPrice,
                    // 座位数量
                    businessNumber: carriage.businessNumber,
                    firstNumber: carriage.firstNumber,
                    secondNumber: carriage.secondNumber,
                    noSeatNumber: carriage.noSeatNumber,
                };
            });
        }
    };

    return {
        carriages,
        isTransfer,
        setCarriages
    };
});