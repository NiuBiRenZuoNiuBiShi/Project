package com.setravel.swifttravel.service.impl;

import com.setravel.swifttravel.mapper.CarriagesMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.setravel.swifttravel.entities.Carriages;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarriagesService {

    @Resource
    private CarriagesMapper carriagesMapper;

    public List<Carriages> getCarriagesByCC(String depCity, String arrCity, String depDate) {
        QueryWrapper<Carriages> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dep_city", depCity)
                .eq("arr_city", arrCity)
                .ge("dep_date", depDate + " 00:00:00")
                .le("dep_date", depDate + " 23:59:59")
                .eq("del", false) // 查询未删除的记录
                .orderByAsc("dep_date"); // 按照出发时间升序排列

        return carriagesMapper.selectList(queryWrapper);
    }

    public List<Carriages> getCarriagesBySS(String depStation, String arrStation, String depDate) {
        QueryWrapper<Carriages> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dep_station", depStation)
                .eq("arr_station", arrStation)
                .ge("dep_date", depDate + " 00:00:00")
                .le("dep_date", depDate + " 23:59:59")
                .eq("del", false) // 查询未删除的记录
                .orderByAsc("dep_date"); // 按照出发时间升序排列

        return carriagesMapper.selectList(queryWrapper);
    }

    public List<Carriages> getCarriagesByCS(String depCity, String arrStation, String depDate) {
        QueryWrapper<Carriages> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dep_city", depCity)
                .eq("arr_station", arrStation)
                .ge("dep_date", depDate + " 00:00:00")
                .le("dep_date", depDate + " 23:59:59")
                .eq("del", false) // 查询未删除的记录
                .orderByAsc("dep_date"); // 按照出发时间升序排列

        return carriagesMapper.selectList(queryWrapper);
    }

    public List<Carriages> getCarriagesBySC(String depStation, String arrCity, String depDate) {
        QueryWrapper<Carriages> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dep_station", depStation)
                .eq("arr_city", arrCity)
                .ge("dep_date", depDate + " 00:00:00")
                .le("dep_date", depDate + " 23:59:59")
                .eq("del", false) // 查询未删除的记录
                .orderByAsc("dep_date"); // 按照出发时间升序排列

        return carriagesMapper.selectList(queryWrapper);
    }
}
