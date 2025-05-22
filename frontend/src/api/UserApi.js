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
    const body = {
        token: localStorage.getItem('token')
    }
    try {
        const response = await api.post('/api/user/logout', body);
        return response.data;
    } catch (error) {
        console.error('Error logging out:', error);
        throw error;
    }
}

export const getUserInfo = async () => {
    try {
        const response = await api.get('/api/user/info');
        return response.data;
    } catch (error) {
        console.error('Error fetching user info:', error);
        throw error;
    }
}

export const updateUserInfo = async (user) => {
    try {
        const response = await api.put('/api/user/update', user);
        return response.data;
    } catch (error) {
        console.error('Error updating user info:', error);
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