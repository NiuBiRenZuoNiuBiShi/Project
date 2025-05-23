import axios from 'axios';
import api from './Api';

/** @typedef {import('@/types/contactType').Contact} Contact */

export const getContacts = async () => {
    try {
        const response = await api.get('/api/contact/get');
        if (response.data.code !== 200) {
            return [];
        }
        return response.data.data;
    } catch (error) {
        console.error('Error fetching contacts:', error);
        throw error;
    }
}

/** @param {Contact} contact */
export const addContact = async (contact) => {
    try {
        const response = await api.post('/api/contact/add', contact);
        if (response.data.code !== 200) {
            throw new Error(response.data.message || '添加联系人失败');
        }
        return response.data.data;
    } catch (error) {
        console.error('Error adding contact:', error);
        throw error;
    }
}

/** @param {Contact[]} contactList */
export const addContactList = async (contactList) => {
    try {
        const response = await api.post('/api/contact/adds', contactList);
        if (response.data.code !== 200) {
            throw new Error(response.data.message || '批量添加联系人失败');
        }
        return response.data.data;
    } catch (error) {
        console.error('Error adding contact list:', error);
        throw error;
    }
}

/** @param {Contact} contact */
export const updateContact = async (contact) => {
    try {
        const response = await api.put('/api/contact/update', contact);
        if (response.data.code !== 200) {
            throw new Error(response.data.message || '更新联系人失败');
        }
        return response.data.data;
    } catch (error) {
        console.error('Error updating contact:', error);
        throw error;
    }
}

/** @param {Contact} contact */
export const deleteContact = async (contact) => {
    try {
        const response = await api.delete('/api/contact/delete', { data: contact });
        if (response.data.code !== 200) {
            throw new Error(response.data.message || '删除联系人失败');
        }
        return response.data.data;
    } catch (error) {
        console.error('Error deleting contact:', error);
        throw error;
    }
}