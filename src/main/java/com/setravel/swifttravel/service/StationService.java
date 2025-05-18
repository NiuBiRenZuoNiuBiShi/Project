package com.setravel.swifttravel.service;

import java.util.List;

import com.setravel.swifttravel.entities.Station;

public interface StationService {
    /**
     * 根据车站名称模糊查询车站
     * 
     * @param stationName 车站名称
     * @return 车站列表
     */
    List<Station> searchStation(String stationName);

    /**
     * 根据城市名称模糊查询车站
     * 
     * @param cityName 城市名称
     * @return 车站列表
     */
    List<Station> searchStationByCity(String cityName);

    /**
     * 随机获取5个车站
     * 
     * @return 车站列表
     */
    List<Station> getRandomStations();
}
