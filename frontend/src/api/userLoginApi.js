import axios from "axios";
import api from "./Api";

export const userLogin = async (user) => {
    try {
        const response = await api.post('/api/user/login', user);
        return response.data;
    } catch (error) {
        console.error('Error logging in:', error);
        throw error;
    }
}

export const userLogout = async () => {
    try {
        const response = await api.post('/api/user/logout');
        return response.data;
    } catch (error) {
        console.error('Error logging out:', error);
        throw error;
    }
}

export const userRegister = async (user) => {
    try {
        const response = await api.post('/api/user/register', user);
        return response.data;
    } catch (error) {
        console.error('Error registering:', error);
        throw error;
    }
}