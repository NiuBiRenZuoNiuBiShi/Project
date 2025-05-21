package com.setravel.swifttravel.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.setravel.swifttravel.entities.Hotel;

@Mapper
public interface HotelMapper extends BaseMapper<Hotel>{
    // BaseMapper provides CRUD. Custom queries if needed.
} 
