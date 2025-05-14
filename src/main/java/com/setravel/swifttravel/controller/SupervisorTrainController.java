package com.setravel.swifttravel.controller;

import com.setravel.swifttravel.entities.City;
import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.entities.Station;
import com.setravel.swifttravel.entities.TrainNumberDetail;
import com.setravel.swifttravel.entities.request.AddStationsRequest;
import com.setravel.swifttravel.service.SupervisorTrainService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * SupervisorTrainController
 * Can directly Change Most of Stuff about TrainTickets
 * 
 * @Author ZhangYi
 */
@RestController
public class SupervisorTrainController {

    public SupervisorTrainController() {
        System.out.println("SupervisorTrainController 初始化了！");
    }

    @Resource
    SupervisorTrainService supervisorTrainService;

    @PostMapping("/api/sup/addTrainNumber")
    public Result addTrainNumber(@RequestBody TrainNumberDetail trainnumbers_detail) {
        return supervisorTrainService.addTrainNumber(trainnumbers_detail);
    }

    @PostMapping("/api/sup/addCities")
    public Result addCities(@RequestBody List<City> cityList) {
        return supervisorTrainService.addCities(cityList);
    }

    @PostMapping("/api/sup/addStations")
    public Result addStations(@RequestBody AddStationsRequest addStationsRequest) {
        List<Station> stationList = addStationsRequest.getStationList();
        List<String> cityNameList = addStationsRequest.getCityNameList();
        return supervisorTrainService.addStations(stationList, cityNameList);
    }
}
