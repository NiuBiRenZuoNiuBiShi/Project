package com.setravel.swifttravel.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.setravel.swifttravel.entities.City;
import com.setravel.swifttravel.mapper.CityMapper;
import com.setravel.swifttravel.service.CityService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class CityServiceImpl implements CityService {

    @Resource
    CityMapper cityMapper;

    @Override
    public List<String> searchCity(String input) {
        QueryWrapper<City> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("city_name", input)
                .eq("del", false)
                .select("city_name");
        List<City> cities = cityMapper.selectList(queryWrapper);
        return cities.stream()
                .map(City::getCityName)
                .toList();
    }
}
