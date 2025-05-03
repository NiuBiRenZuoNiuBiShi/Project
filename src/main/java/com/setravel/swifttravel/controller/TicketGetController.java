package com.setravel.swifttravel.controller;

import com.setravel.swifttravel.entities.Carriages;
import com.setravel.swifttravel.service.impl.CarriagesService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 火车购票
 * @author Tengpaz
 */
@RestController
@RequestMapping("api/ticket")
public class TicketGetController {

    @Resource
    private CarriagesService carriagesService;

    @GetMapping("/getCarriages")
    public List<Carriages> getCarriages(@RequestParam String depStation, @RequestParam String depCity,
                                        @RequestParam String arrStation, @RequestParam String arrCity,
                                        @RequestParam String depDate) {
        if (depStation != null && arrStation != null) {
            return carriagesService.getCarriagesBySS(depStation, arrStation, depDate);
        } else if (depCity != null && arrCity != null) {
            return carriagesService.getCarriagesByCC(depCity, arrCity, depDate);
        } else if (depCity != null && arrStation != null) {
            return carriagesService.getCarriagesByCS(depCity, arrStation, depDate);
        } else if (depStation != null && arrCity != null) {
            return carriagesService.getCarriagesBySC(depStation, arrCity, depDate);
        } else {
            return null; // or throw an exception
        }
    }
}
