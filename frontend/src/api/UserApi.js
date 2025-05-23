import api from "./Api";

export const userLogin = async (user) => {
    try {
        const response = await api.post('/api/user/login', user);
        
        // 如果登录成功，存储token到localStorage
        if (response.data.success && response.data.data?.token) {
            localStorage.setItem('token', response.data.data.token);
        }
        
        return response.data;
    } catch (error) {
        console.error('Error logging in:', error);
        throw error;
    }
}

export const userLogout = async () => {
    const token = localStorage.getItem('token');
    if (!token) {
        // 如果没有token，直接清除本地存储
        localStorage.removeItem('token');
        return { success: true };
    }

    try {
        const response = await api.post('/api/user/logout', { token });
        
        // 无论后端返回什么，都清除本地token
        localStorage.removeItem('token');
        
        return response.data;
    } catch (error) {
        console.error('Error logging out:', error);
        // 即使登出API失败，也要清除本地token
        localStorage.removeItem('token');
        throw error;
    }
}

export const getUserInfo = async () => {
    try {
        const response = await api.get('/api/user/info');
        return response.data;
    } catch (error) {
        console.error('Error fetching user info:', error);
        
        // 如果获取用户信息失败（可能是token无效），清除本地token
        if (error.response?.status === 401 || error.response?.status === 403) {
            localStorage.removeItem('token');
        }
        
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