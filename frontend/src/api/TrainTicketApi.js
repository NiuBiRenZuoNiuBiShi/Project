import axios from "axios";
import api from "./Api";
import { base64ToArray } from "@/utils/trans";
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


    const response = await api.get(
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

    const response = await api.get(
        "/api/ticket/getCarriagesTransfer",
        { params }
    );
    return response.data;
}


/**
 * 获取座位信息
 * @param {string} trainId 列车ID
 * @returns {Promise} 座位数据数组
 */
export async function getSeatsInfoApi(trainId) {

    const params = {
        trainId
    }

    try {
        const response = await api.get(
            "/api/ticket/getSeatsInfo",
            { params }
        );

        // 检查返回的数据结构
        if (!response.data) {
            console.error("API返回数据为空");
            return [];
        }

        // 检查Result对象结构
        if (response.data.code !== 200) {
            console.error("API返回错误:", response.data.message);
            return [];
        }

        // 获取Result.data中的座位数组
        const seatsArray = response.data.data;

        // 检查seatsArray是否为数组
        if (!Array.isArray(seatsArray)) {
            console.error("API返回的座位数据不是数组:", seatsArray);
            return [];
        }

        // 处理座位数据
        const seats = seatsArray.map(seat => {
            const seatNumber = seat.seatNumber;
            const match = seatNumber.match(/^(\d+)([A-Z])$/);

            return {
                ...seat,
                place: seatNumber,
                row: match ? match[1] : null,
                column: match ? match[2] : null,
                flags: base64ToArray(seat.flags),
            };
        });

        return seats;
    } catch (error) {
        console.error("获取座位信息失败:", error);
        throw error; // 重新抛出错误以便上层组件捕获
    }
}

/**
 * 提交订票请求
 * @param {Object} data 订票数据
 * @returns {Promise} API响应
 */
export function orderTicketApi(data) {
    return request({
        url: '/api/ticket/order',
        method: 'post',
        data
    });
}

/**
 * 获取车厢信息
 * @param {string} trainId 列车ID
 * @returns {Promise} API响应
 */
export function getCarriageInfoApi(trainId) {
    return request({
        url: '/api/ticket/getCarriageInfo',
        method: 'get',
        params: { trainId }
    });
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