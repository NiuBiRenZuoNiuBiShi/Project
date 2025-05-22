import axios from "axios"
import api from "./Api"

/** @typedef {import('@/types/FoodType').FoodType} Food */

/**
 * Adds a list of food items.
 * @param {Food[]} foodList 
 */
export const addFoodList = async (foodList) => {
    try {
        const response = await api.post("/api/food/adds", foodList);
        if (response.data.code !== 200) {
            throw new Error(response.data.message || "添加食物失败");
        }
        return response.data.data;
    } catch (error) {
        console.error("Error adding food list:", error);
        throw error;
    }
};

/**
 * Adds a food item.
 * @param {Food} food 
 */
export const addFood = async (food) => {
    try {
        const response = await api.post("/api/food/add", food);
        if (response.data.code !== 200) {
            throw new Error(response.data.message || "添加食物失败");
        }
        return response.data.data;
    } catch (error) {
        console.error("Error adding food:", error);
        throw error;
    }
};

/**
 * Deletes a list of food items.
 * @param {String[]} foodIds 
 * @returns {Promise<void>}
 */
export const deleteFoods = async (foodIds) => {
    try {
        const response = await api.post("/api/food/delete", foodIds);
        if (response.data.code !== 200) {
            throw new Error(response.data.message || "删除食物失败");
        }
        return response.data.data;
    } catch (error) {
        console.error("Error deleting food:", error);
        throw error;
    }
}

/**
 * Fetches a list of food items by their IDs.
 * @param {String[]} foodIds 
 * @returns {Promise<Food[]>} A promise that resolves to an array of food items.
 */
export const getFoodListByIds = async (foodIds) => {
    try {
        const response = await api.post("/api/food/getByIds", foodIds);
        if (response.data.code !== 200) {
            throw new Error(response.data.message || "获取食物失败");
        }
        return response.data.data;
    } catch (error) {
        console.error("Error fetching food list by IDs:", error);
        throw error;
    }
}

/**
 * Fetches a list of food items by their train ID.
 * @param {String} trainId 
 * @returns {Promise<Food[]>} A promise that resolves to an array of food items.
 */
export const getFoodListByTrainId = async (trainId) => {
    try {
        const response = await api.post("/api/food/getByTrainId", trainId);
        if (response.data.code !== 200) {
            throw new Error(response.data.message || "获取食物失败");
        }
        return response.data.data;
    } catch (error) {
        console.error("Error fetching food list by train ID:", error);
        throw error;
    }
}

/**
 * Processes the purchase of a list of food items.
 * @param {Food[]} foodList - Array of food items with purchase quantities
 * @returns {Promise<any>} A promise that resolves to the purchase result
 */
export const buyFoodList = async (foodList) => {
    try {
        // 验证购买数据
        if (!Array.isArray(foodList) || foodList.length === 0) {
            throw new Error("购买列表不能为空");
        }

        // 确保每个食物项都有必要的字段
        const validatedFoodList = foodList.map(item => {
            if (!item.id || !item.foodName || !item.price || !item.number) {
                throw new Error(`食物信息不完整: ${item.foodName || '未知食物'}`);
            }
            
            if (item.number <= 0) {
                throw new Error(`食物数量必须大于0: ${item.foodName}`);
            }

            return {
                id: item.id,
                trainsId: item.trainsId,
                foodName: item.foodName,
                foodType: item.foodType || 'main',
                price: item.price,
                lunch: item.lunch !== undefined ? item.lunch : true,
                dinner: item.dinner !== undefined ? item.dinner : true,
                picUrl: item.picUrl || '',
                del: false,
                number: parseInt(item.number)
            };
        });

        const response = await api.post("/api/food/buy", validatedFoodList);
        
        if (response.data.code !== 200) {
            throw new Error(response.data.message || "购买食物失败");
        }
        
        return response.data.data;
    } catch (error) {
        console.error("Error buying food list:", error);
        throw error;
    }
}

/**
 * 获取用户的订单历史
 * @param {String} userId - 用户ID
 * @returns {Promise<any[]>} 订单历史列表
 */
export const getUserOrderHistory = async (userId) => {
    try {
        const response = await api.get("/api/food/orders", { 
            params: { userId } 
        });
        
        if (response.data.code !== 200) {
            throw new Error(response.data.message || "获取订单历史失败");
        }
        
        return response.data.data;
    } catch (error) {
        console.error("Error fetching user order history:", error);
        throw error;
    }
}

/**
 * 取消订单
 * @param {String} orderId - 订单ID
 * @returns {Promise<any>} 取消结果
 */
export const cancelOrder = async (orderId) => {
    try {
        const response = await api.post("/api/food/cancel", { orderId });
        
        if (response.data.code !== 200) {
            throw new Error(response.data.message || "取消订单失败");
        }
        
        return response.data.data;
    } catch (error) {
        console.error("Error cancelling order:", error);
        throw error;
    }
}

/**
 * 获取特定分类的食物
 * @param {String} trainId - 列车ID
 * @param {String} category - 食物分类 ('main', 'snack', 'drink', 'hot')
 * @returns {Promise<Food[]>} 分类食物列表
 */
export const getFoodByCategory = async (trainId, category) => {
    try {
        const response = await api.get("/api/food/category", { 
            params: { trainId, category } 
        });
        
        if (response.data.code !== 200) {
            throw new Error(response.data.message || "获取分类食物失败");
        }
        
        return response.data.data;
    } catch (error) {
        console.error("Error fetching food by category:", error);
        throw error;
    }
}

/**
 * 搜索食物
 * @param {String} trainId - 列车ID
 * @param {String} keyword - 搜索关键词
 * @returns {Promise<Food[]>} 搜索结果
 */
export const searchFood = async (trainId, keyword) => {
    try {
        if (!keyword || keyword.trim().length === 0) {
            return [];
        }

        const response = await api.get("/api/food/search", { 
            params: { trainId, keyword: keyword.trim() } 
        });
        
        if (response.data.code !== 200) {
            throw new Error(response.data.message || "搜索食物失败");
        }
        
        return response.data.data;
    } catch (error) {
        console.error("Error searching food:", error);
        throw error;
    }
}

/**
 * 获取推荐食物
 * @param {String} trainId - 列车ID
 * @param {Number} limit - 返回数量限制，默认为10
 * @returns {Promise<Food[]>} 推荐食物列表
 */
export const getRecommendedFood = async (trainId, limit = 10) => {
    try {
        const response = await api.get("/api/food/recommended", { 
            params: { trainId, limit } 
        });
        
        if (response.data.code !== 200) {
            throw new Error(response.data.message || "获取推荐食物失败");
        }
        
        return response.data.data;
    } catch (error) {
        console.error("Error fetching recommended food:", error);
        throw error;
    }
}

/**
 * 检查食物库存
 * @param {String[]} foodIds - 食物ID列表
 * @returns {Promise<Object>} 库存信息 { foodId: stock }
 */
export const checkFoodStock = async (foodIds) => {
    try {
        const response = await api.post("/api/food/stock", foodIds);
        
        if (response.data.code !== 200) {
            throw new Error(response.data.message || "检查库存失败");
        }
        
        return response.data.data;
    } catch (error) {
        console.error("Error checking food stock:", error);
        throw error;
    }
}

/**
 * 获取食物详情
 * @param {String} foodId - 食物ID
 * @returns {Promise<Food>} 食物详情
 */
export const getFoodDetail = async (foodId) => {
    try {
        const response = await api.get(`/api/food/detail/${foodId}`);
        
        if (response.data.code !== 200) {
            throw new Error(response.data.message || "获取食物详情失败");
        }
        
        return response.data.data;
    } catch (error) {
        console.error("Error fetching food detail:", error);
        throw error;
    }
}

// 工具函数：格式化价格
export const formatPrice = (price) => {
    return `¥${Number(price).toFixed(2)}`;
};

// 工具函数：验证食物数据
export const validateFoodData = (food) => {
    const errors = [];
    
    if (!food.foodName || food.foodName.trim().length === 0) {
        errors.push("食物名称不能为空");
    }
    
    if (!food.price || isNaN(Number(food.price)) || Number(food.price) <= 0) {
        errors.push("价格必须是大于0的数字");
    }
    
    if (!food.trainsId || food.trainsId.trim().length === 0) {
        errors.push("列车ID不能为空");
    }
    
    return {
        isValid: errors.length === 0,
        errors
    };
};

// 工具函数：计算购物车总价
export const calculateCartTotal = (cartItems, deliveryFee = 0) => {
    const subtotal = cartItems.reduce((sum, item) => {
        return sum + (Number(item.price) * (item.count || item.number));
    }, 0);
    
    return {
        subtotal,
        deliveryFee,
        total: subtotal + deliveryFee
    };
};