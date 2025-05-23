package com.setravel.swifttravel.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.setravel.swifttravel.entities.Food;

@Mapper
public interface FoodMapper extends BaseMapper<Food> {
    // This interface extends BaseMapper and can be used to define additional methods specific to Food entities.
    // For example, you can add methods for custom queries or operations related to Food entities.
    // The BaseMapper interface should already provide basic CRUD operations.
    
}
