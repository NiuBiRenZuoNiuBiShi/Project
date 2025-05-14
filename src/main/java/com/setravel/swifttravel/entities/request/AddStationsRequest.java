package com.setravel.swifttravel.entities.request;

import com.setravel.swifttravel.entities.Station;
import lombok.Data;

import java.util.List;

@Data
public class AddStationsRequest {
    private List<Station> stationList;
    private List<String> cityNameList;
}
