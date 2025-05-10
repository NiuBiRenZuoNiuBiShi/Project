package com.setravel.swifttravel.mapper;

import com.setravel.swifttravel.entities.City;
import com.setravel.swifttravel.entities.TrainNumberDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SupervisorTrainMapper {
    void insertTrainNumber(@Param("detail") TrainNumberDetail detail);

    List<City> selectCitiesByStations(@Param("stations") List<String> stationsNames);

}
