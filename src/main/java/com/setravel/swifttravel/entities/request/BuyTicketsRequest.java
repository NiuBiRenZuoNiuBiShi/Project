package com.setravel.swifttravel.entities.request;

import java.util.List;

import lombok.Data;

@Data
public class BuyTicketsRequest {
    private List<String> seatIdList;
    private String carriageId;
    private List<Integer> versionList;
}
