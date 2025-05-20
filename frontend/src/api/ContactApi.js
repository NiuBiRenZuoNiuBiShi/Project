import axios from 'axios';
export const getContacts = async () => {
    try {
        const response = await axios.get('/api/contacts');
        return response.data;
    } catch (error) {
        console.error('Error fetching contacts:', error);
        throw error;
    }
}

export const addContact = async (contact) => {
    try {
        const response = await axios.post('/api/contacts/add', contact);
        return response.data;
    } catch (error) {
        console.error('Error adding contact:', error);
        throw error;
    }
}

export const addContactList = async (contactList) => {
    try {
        const response = await axios.post('/api/contacts/adds', contactList);
        return response.data;
    } catch (error) {
        console.error('Error adding contact list:', error);
        throw error;
    }
}

export const updateContact = async (contact) => {
    try {
        const response = await axios.put('/api/contacts/update', contact);
        return response.data;
    } catch (error) {
        console.error('Error updating contact:', error);
        throw error;
    }
}

export const deleteContact = async (contact) => {
    try {
        const response = await axios.delete('/api/contacts/delete', { data: contact });
        return response.data;
    } catch (error) {
        console.error('Error deleting contact:', error);
        throw error;
    }
}