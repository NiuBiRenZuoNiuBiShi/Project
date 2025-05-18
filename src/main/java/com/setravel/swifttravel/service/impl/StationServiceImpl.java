package com.setravel.swifttravel.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.setravel.swifttravel.entities.City;
import com.setravel.swifttravel.entities.Station;
import com.setravel.swifttravel.mapper.CityMapper;
import com.setravel.swifttravel.mapper.StationMapper;
import com.setravel.swifttravel.service.StationService;


import jakarta.annotation.Resource;

/**
 * @author ZhangYi
 * @version 1.0
 */
@Service
public class StationServiceImpl implements StationService {


    @Resource
    private StationMapper stationMapper;

    @Resource
    private CityMapper cityMapper;

    /**
     * 根据车站名称模糊查询车站
     * 
     * @param stationName 车站名称
     * @return 车站列表
     */
    @Override
    public List<Station> searchStation(String stationName) {
        LambdaQueryWrapper<Station> queryWrapper = new LambdaQueryWrapper<Station>()
                            .like(Station::getStationName, stationName)
                            .eq(Station::getDel, false)
                            .select(Station::getStationName);
        return stationMapper.selectList(queryWrapper);
    }

    /**
     * 根据城市名称模糊查询车站
     * 
     * @param cityName 城市名称
     * @return 车站列表
     */
    @Override
    public List<Station> searchStationByCity(String cityName) {
        LambdaQueryWrapper<City> wrapper = new LambdaQueryWrapper<City>()
                    .like(City::getCityName, cityName)
                    .eq(City::getDel, false)
                    .select(City::getId);
        List<byte[]> cityIds = cityMapper.selectList(wrapper).stream()
                    .map(City::getId)
                    .toList();
        LambdaQueryWrapper<Station> queryWrapper = new LambdaQueryWrapper<Station>()
                    .in(Station::getCityId, cityIds)
                    .eq(Station::getDel, false)
                    .select(Station::getStationName);
        return stationMapper.selectList(queryWrapper);
    }
    
    /**
     * 随机获取5个车站
     * 
     * @return 车站列表
     */
    @Override
    public List<Station> getRandomStations() {
        LambdaQueryWrapper<Station> queryWrapper = new LambdaQueryWrapper<Station>()
                    .ne(Station::getDel, true)
                    .last("ORDER BY RAND() LIMIT 5");
        return stationMapper.selectList(queryWrapper);
    }
}
