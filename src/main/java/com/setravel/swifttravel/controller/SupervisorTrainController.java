package com.setravel.swifttravel.controller;

import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.entities.TrainNumberDetail;
import com.setravel.swifttravel.mapper.SupervisorTrainMapper;
import com.setravel.swifttravel.service.impl.SupervisorTrainService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SupervisorTrainController {

    // This class is currently empty, but it can be used to handle requests related to supervisors in the future.
    // You can add methods to handle specific routes and logic as needed.

    // Example method to handle a GET request for supervisor dashboard
    // @GetMapping("/supervisor/dashboard")
    // public String showDashboard() {
    //     return "supervisor/dashboard"; // Return the name of the view template
    // }

    public SupervisorTrainController() {
        System.out.println("YourController 初始化了！");
    }

    @Resource
    SupervisorTrainService supervisorTrainService;

    @PostMapping("/api/sup/addTrainNumber")
    public Result addTrainNumber(@RequestBody TrainNumberDetail trainnumbers_detail){
        return supervisorTrainService.addTrainNumber(trainnumbers_detail);
    }
}
