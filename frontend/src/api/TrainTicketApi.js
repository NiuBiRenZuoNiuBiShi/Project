import axios from "axios";
/** @typedef {import('@/types/formType').TrainSearchForm} TrainSearchForm */

/**
 * 根据表单数据搜索列车票
 * @param {TrainSearchForm} formData 搜索表单数据
 * @returns {Promise<Object>} 搜索结果
 */
export const searchTrainTicketsApi = async (formData) => {
    if (!formData.transfer_option)
        return searchTrainTicketsNoTransfer(formData);
    else
        return searchTrainTicketsTransfer(formData);
};

/**
 * 搜索直达车次
 * @param {TrainSearchForm} formData 搜索表单数据
 * @returns {Promise<Object>} 搜索结果
 */
const searchTrainTicketsNoTransfer = async (formData) => {
    const params = {
        depStation: formData.departureType === "station" ? formData.departure : null,
        depCity: formData.departureType === "city" ? formData.departure : null,
        arrStation: formData.destinationType === "station" ? formData.destination : null,
        arrCity: formData.destinationType === "city" ? formData.destination : null,
        depDate: formData.selectedTime
    };

    console.log(params);


    const response = await axios.get(
        "/api/ticket/getCarriages",
        { params }
    );
    return response.data;
}

/**
 * 搜索中转车次
 * @param {TrainSearchForm} formData 搜索表单数据
 * @returns {Promise<Object>} 搜索结果
 */
const searchTrainTicketsTransfer = async (formData) => {
    const params = {
        depCity: formData.departure,
        arrCity: formData.destination,
        depDate: formData.selectedTime
    };

    const response = await axios.get(
        "/api/ticket/getCarriagesTransfer",
        { params }
    );
    return response.data;
}

/**
 * TrainSearchForm 类型定义
 * @typedef {Object} TrainSearchForm
 * @property {string} departure - 出发地(站点或城市)
 * @property {string} departureType - 出发地类型('station'或'city')
 * @property {string} destination - 目的地(站点或城市)
 * @property {string} destinationType - 目的地类型('station'或'city')
 * @property {string} formType - 表单类型
 * @property {string} selectedTime - 选择的出发日期
 * @property {boolean} transfer_option - 是否允许中转
 */