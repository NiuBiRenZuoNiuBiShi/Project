package com.setravel.swifttravel.controller;

import org.springframework.web.bind.annotation.RestController;

import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.service.StationService;

import jakarta.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 车站搜索
 * 
 * @author ZhangYi
 * @version 1.0
 */
@RestController
public class StationSearchController {

    @Resource
    private StationService stationService;

    /**
     * 根据车站名称模糊查询车站
     * 
     * @param input 车站名称
     * @return 车站列表
     */
    @GetMapping("/api/station/search")
    public Result searchStation(@RequestParam("input") String input) {
        try {
            if (input.isEmpty() || input.isBlank())
                return Result.success(stationService.getRandomStations());
            return Result.success(stationService.searchStation(input));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据城市名称模糊查询车站
     * 
     * @param input CityNameInput
     * @return StationList
     */
    @GetMapping("/api/station/city_search")
    public Result getMethodName(@RequestParam("input") String input) {
        try {
            return Result.success(stationService.searchStationByCity(input));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

}
