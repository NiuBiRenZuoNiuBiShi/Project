package com.setravel.swifttravel.service;

import java.util.List;

import com.setravel.swifttravel.entities.City;

public interface CityService {
    /**
     * 根据城市名称模糊查询城市
     * @param input 城市名称
     * @return 城市列表
     */
    List<City> searchCity(String input);

    /**
     * 随机获取5个城市
     * @return 城市列表
     */
    List<City> getRandomCities();
}
