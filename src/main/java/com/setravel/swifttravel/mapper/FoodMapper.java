package com.setravel.swifttravel.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.setravel.swifttravel.entities.Food;

@Mapper
public interface FoodMapper extends BaseMapper<Food> {
    // This interface extends BaseMapper and can be used to define additional methods specific to Food entities.
    // For example, you can add methods for custom queries or operations related to Food entities.
    // The BaseMapper interface should already provide basic CRUD operations.
    
    int decreaseFoodsWithCheck(@Param("list") List<Food> list);

    List<Food> selectFoodsForUpdate(@Param("list") List<byte[]> list);
}
