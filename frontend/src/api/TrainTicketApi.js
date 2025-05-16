import axios from "axios";
/** @typedef {import('@/types/formType').TrainSearchForm} TrainSearchForm */

/**
 * @param {TrainSearchForm} formData
 */
export const searchTrainTicketsApi = async (formData) => {
    const params = {
        time: formData.selectedTime,
        depCity: formData.departureType === 'city' ? formData.departure : null,
        depStation: formData.departureType === 'station' ? formData.departure : null,
        arrCity: formData.destinationType === 'city' ? formData.destination : null,
        arrStation: formData.destinationType === 'station' ? formData.destination : null,
        transfer: formData.transfer_option
    };

    const response = await axios.get("http://localhost:8080/api/apigetCarriages", {
        params,
    });

    return response.data;
};