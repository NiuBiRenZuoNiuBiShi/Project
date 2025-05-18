package com.setravel.swifttravel.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.service.CityService;

import jakarta.annotation.Resource;

@RestController
public class CityController {

    @Resource
    CityService cityService;

    /**
     * @param input 城市名称
     * @return 城市列表
     */
    @GetMapping("/api/city/search")
    public Result getCityList(@RequestParam("input") String input) {
        System.out.println(input);
        try {
            if (input.isEmpty() || input.isBlank())
                return Result.success(cityService.getRandomCities());
            return Result.success(cityService.searchCity(input));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
