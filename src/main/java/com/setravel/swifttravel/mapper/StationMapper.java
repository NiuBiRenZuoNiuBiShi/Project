package com.setravel.swifttravel.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.setravel.swifttravel.entities.Station;

@Mapper
public interface StationMapper extends BaseMapper<Station> {
}