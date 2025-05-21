package com.setravel.swifttravel.service.impl;

import com.setravel.swifttravel.mapper.TrainNumberMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class TrainNumberService {

    @Resource
    private TrainNumberMapper trainNumberMapper;

    public String getTrainNumberById(byte[] id) {
        return trainNumberMapper.selectById(id).getTrainNumber();
    }
}
