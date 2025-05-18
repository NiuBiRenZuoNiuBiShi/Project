package com.setravel.swifttravel.service.impl;

import com.setravel.swifttravel.entities.CarriageThrough;
import com.setravel.swifttravel.entities.CarriageTransfer;
import com.setravel.swifttravel.mapper.CarriagesMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.setravel.swifttravel.entities.Carriages;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Base64;

@Service
public class CarriagesService {

    @Resource
    private CarriagesMapper carriagesMapper;

    @Resource
    private TrainNumberService trainNumberService;

    public List<Carriages> getCarriagesByCC(String depCity, String arrCity, String depDate) {
        QueryWrapper<Carriages> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dep_city", depCity)
                .eq("arr_city", arrCity)
                .ge("dep_time", depDate + " 00:00:00")
                .le("dep_time", depDate + " 23:59:59")
                .eq("del", false) // 查询未删除的记录
                .orderByAsc("dep_time"); // 按照出发时间升序排列

        return carriagesMapper.selectList(queryWrapper);
    }

    public List<Carriages> getCarriagesBySS(String depStation, String arrStation, String depDate) {
        QueryWrapper<Carriages> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dep_station", depStation)
                .eq("arr_station", arrStation)
                .ge("dep_time", depDate + " 00:00:00")
                .le("dep_time", depDate + " 23:59:59")
                .eq("del", false) // 查询未删除的记录
                .orderByAsc("dep_time"); // 按照出发时间升序排列

        return carriagesMapper.selectList(queryWrapper);
    }

    public List<Carriages> getCarriagesByCS(String depCity, String arrStation, String depDate) {
        QueryWrapper<Carriages> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dep_city", depCity)
                .eq("arr_station", arrStation)
                .ge("dep_time", depDate + " 00:00:00")
                .le("dep_time", depDate + " 23:59:59")
                .eq("del", false) // 查询未删除的记录
                .orderByAsc("dep_time"); // 按照出发时间升序排列

        return carriagesMapper.selectList(queryWrapper);
    }

    public List<Carriages> getCarriagesBySC(String depStation, String arrCity, String depDate) {
        QueryWrapper<Carriages> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dep_station", depStation)
                .eq("arr_city", arrCity)
                .ge("dep_time", depDate + " 00:00:00")
                .le("dep_time", depDate + " 23:59:59")
                .eq("del", false) // 查询未删除的记录
                .orderByAsc("dep_time"); // 按照出发时间升序排列

        return carriagesMapper.selectList(queryWrapper);
    }
    
    public List<Carriages> getCarriages(String depStation, String depCity,
                                        String arrStation, String arrCity,
                                        String depDate) {
        if (depStation != null && arrStation != null) {
            return getCarriagesBySS(depStation, arrStation, depDate);
        } else if (depCity != null && arrCity != null) {
            return getCarriagesByCC(depCity, arrCity, depDate);
        } else if (depCity != null && arrStation != null) {
            return getCarriagesByCS(depCity, arrStation, depDate);
        } else if (depStation != null && arrCity != null) {
            return getCarriagesBySC(depStation, arrCity, depDate);
        } else {
            return null; // or throw an exception
        }
    }

    /**
     * 根据车厢类型获取车厢列表
     *
     * @param type      车厢类型
     * @param carriages 车厢列表
     * @return 车厢列表
     */
    public List<Carriages> getCarriageType(char type, List<Carriages> carriages) {
        if (carriages == null || carriages.isEmpty()) { // 说明没有对应类型的车辆
            return List.of();
        }
        return carriages.stream()
                .filter(carriage -> carriage.getTrainNumber() != null
                        && new String(carriage.getTrainNumber()).charAt(0) == type)
                .toList();
    }
    
    public CarriageThrough carriages2carriageThrough(Carriages carriages) {
        CarriageThrough carriageThrough = new CarriageThrough();
        carriageThrough.setId(carriages.getId());
        carriageThrough.setDepCity(carriages.getDepCity());
        carriageThrough.setArrCity(carriages.getArrCity());
        carriageThrough.setDepTime(carriages.getDepTime());
        carriageThrough.setArrTime(carriages.getArrTime());
        carriageThrough.setCostTime(LocalDate.ofEpochDay(Duration.between(carriages.getDepTime().toLocalTime(), carriages.getArrTime().toLocalTime()).toHours())); // 不确定是否正确
        carriageThrough.setDepStation(carriages.getDepStation());
        carriageThrough.setArrStation(carriages.getArrStation());
        carriageThrough.setTrainNumber(trainNumberService.getTrainNumberById(carriages.getTrainNumber()));
        carriageThrough.setSecondNumber(carriages.getSecondNumber());
        carriageThrough.setFirstNumber(carriages.getFirstNumber());
        carriageThrough.setBusinessNumber(carriages.getBusinessNumber());
        carriageThrough.setNoSeatNumber(carriages.getNoSeatNumber());
        carriageThrough.setSecondPrice(carriages.getSecondPrice());
        carriageThrough.setFirstPrice(carriages.getFirstPrice());
        carriageThrough.setBusinessPrice(carriages.getBusinessPrice());
        carriageThrough.setNoSeatPrice(carriages.getNoSeatPrice());
        carriageThrough.setType(new String(carriages.getTrainNumber()).charAt(0));
        return carriageThrough;
    }
    
    public List<CarriageThrough> getCarriageThrough(String depStation, String depCity,
                                                    String arrStation, String arrCity,
                                                    String depDate) {
        List<Carriages> carriages = getCarriages(depStation, depCity, arrStation, arrCity, depDate);
        if (carriages == null || carriages.isEmpty()) { // 没有对应车次
            return null;
        }
        List<CarriageThrough> carriageThroughs = new ArrayList<>();
        for (Carriages carriage : carriages) {
            carriageThroughs.add(carriages2carriageThrough(carriage));
        }
        return carriageThroughs;
    }

    public List<Carriages> getCarriagesByStartCity(String depCity, String depDate) {
        QueryWrapper<Carriages> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dep_city", depCity)
                .ge("dep_time", depDate + " 00:00:00")
                .le("dep_time", depDate + " 23:59:59")
                .eq("del", false) // 查询未删除的记录
                .orderByAsc("dep_time"); // 按照出发时间升序排列

        return carriagesMapper.selectList(queryWrapper);
    }

    public List<Carriages> getCarriagesByEndCity(String arrCity, String depDate) {
        QueryWrapper<Carriages> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("arr_city", arrCity)
                .ge("dep_time", depDate + " 00:00:00")
                .le("dep_time", depDate + " 23:59:59")
                .eq("del", false) // 查询未删除的记录
                .orderByAsc("dep_time"); // 按照出发时间升序排列

        return carriagesMapper.selectList(queryWrapper);
    }

    public List<CarriageTransfer> getCarriageTransferByCarriages(List<Carriages> firstCarriages, List<Carriages> secondCarriages) {
        List<CarriageTransfer> carriageTransfers = new ArrayList<>();
        for (Carriages firstCarriage : firstCarriages) {
            for (Carriages secondCarriage : secondCarriages) {
                if (firstCarriage.getArrCity().equals(secondCarriage.getDepCity())) {
                    CarriageTransfer carriageTransfer = new CarriageTransfer();
                    carriageTransfer.setFirstCarriage(carriages2carriageThrough(firstCarriage));
                    carriageTransfer.setSecondCarriage(carriages2carriageThrough(secondCarriage));
                    carriageTransfers.add(carriageTransfer);
                }
            }
        }
        return carriageTransfers;
    }

    public List<CarriageTransfer> getCarriageTransfer(String depCity, String arrCity, String depDate) {
        List<Carriages> firstCarriages = getCarriagesByStartCity(depCity, depDate);
        List<Carriages> secondCarriages = getCarriagesByEndCity(arrCity, depDate);
        List<Carriages> firstCarriagesFiltered = new ArrayList<>();
        List<Carriages> secondCarriagesFiltered = new ArrayList<>();
        for (Carriages firstCarriage : firstCarriages) {
            for (Carriages secondCarriage : secondCarriages) {
                if (firstCarriage.getArrCity().equals(secondCarriage.getDepCity()) &&
                        firstCarriage.getDepTime().isBefore(secondCarriage.getDepTime())) {
                    firstCarriagesFiltered.add(firstCarriage);
                    secondCarriagesFiltered.add(secondCarriage);
                }
            }
        }
        return getCarriageTransferByCarriages(firstCarriagesFiltered, secondCarriagesFiltered);
    }

    public Carriages getCarriageByTrainId(String trainId) {
        QueryWrapper<Carriages> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("train_id", trainId)
                .eq("del", false); // 查询未删除的记录
        return carriagesMapper.selectOne(queryWrapper);
    }

    /**
     * 更新车厢信息
     * @param carriage 车厢信息
     */
    public void updateCarriage(Carriages carriage) {
        QueryWrapper<Carriages> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("train_id", new String(carriage.getId()))
                .eq("del", false); // 查询未删除的记录
        carriagesMapper.update(carriage, queryWrapper);
    }
}
