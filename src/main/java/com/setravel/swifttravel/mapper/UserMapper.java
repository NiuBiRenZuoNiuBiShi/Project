package com.setravel.swifttravel.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.setravel.swifttravel.entities.Users;

@Mapper
public interface UserMapper extends BaseMapper<Users> {
    
}
