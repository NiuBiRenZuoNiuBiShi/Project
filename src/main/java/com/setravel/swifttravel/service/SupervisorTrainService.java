package com.setravel.swifttravel.service;

import com.setravel.swifttravel.entities.City;
import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.entities.Station;
import com.setravel.swifttravel.entities.request.TrainNumberDetail;

import java.util.List;

public interface SupervisorTrainService {
    Result addTrainNumber(TrainNumberDetail trainnumbers_detail);

    Result addCities(List<City> cities);

    Result addStations(List<Station> stations, List<String> cities);
}
