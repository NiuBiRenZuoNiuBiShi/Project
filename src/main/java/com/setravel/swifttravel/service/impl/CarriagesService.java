package com.setravel.swifttravel.service.impl;

import com.setravel.swifttravel.entities.CarriageThrough;
import com.setravel.swifttravel.entities.CarriageTransfer;
import com.setravel.swifttravel.mapper.CarriagesMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.setravel.swifttravel.entities.Carriages;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Service
public class CarriagesService {

    @Resource
    private CarriagesMapper carriagesMapper;

    @Resource
    private TrainNumberService trainNumberService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

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
        carriageThrough.setId(Base64.getEncoder().encodeToString(carriages.getId()));
        carriageThrough.setTrainNumberId(Base64.getEncoder().encodeToString( carriages.getTrainNumber()));
        carriageThrough.setCarriageId(Base64.getEncoder().encodeToString(carriages.getId()));
        carriageThrough.setFlag(Base64.getEncoder().encodeToString(carriages.getFlag()));
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

//    public List<CarriageTransfer> getCarriageTransfer(String depCity, String arrCity, String depDate) {
//        List<Carriages> firstCarriages = getCarriagesByStartCity(depCity, depDate);
//        List<Carriages> secondCarriages = getCarriagesByEndCity(arrCity, depDate);
//        List<Carriages> firstCarriagesFiltered = new ArrayList<>();
//        List<Carriages> secondCarriagesFiltered = new ArrayList<>();
//        for (Carriages firstCarriage : firstCarriages) {
//            for (Carriages secondCarriage : secondCarriages) {
//                if (firstCarriage.getArrCity().equals(secondCarriage.getDepCity()) &&
//                        firstCarriage.getDepTime().isBefore(secondCarriage.getDepTime())) {
//                    firstCarriagesFiltered.add(firstCarriage);
//                    secondCarriagesFiltered.add(secondCarriage);
//                }
//            }
//        }
//        return getCarriageTransferByCarriages(firstCarriagesFiltered, secondCarriagesFiltered);
//    }

    public List<CarriageTransfer> getCarriageTransfer(String depCity, String arrCity, String depDate) {
        // 生成缓存键
        String cacheKey = "carriageTransfer:" + depCity + ":" + arrCity + ":" + depDate;

        // 从 Redis 获取缓存数据
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        List<CarriageTransfer> cachedResult = (List<CarriageTransfer>) valueOperations.get(cacheKey);

        if (cachedResult != null) {
            return cachedResult; // 如果缓存命中，直接返回
        }

        // 缓存未命中，查询数据库
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
        List<CarriageTransfer> result = getCarriageTransferByCarriages(firstCarriagesFiltered, secondCarriagesFiltered);

        // 将结果存入 Redis，设置过期时间（例如 1 小时）
        valueOperations.set(cacheKey, result, 1, TimeUnit.HOURS);

        return result;
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

    /**
     * 获取按时长排序的车厢列表
     * @param depStation
     * @param depCity
     * @param arrStation
     * @param arrCity
     * @param depDate
     * @return
     */
    public List<CarriageThrough> getCarriagesOrderedByTime(String depStation, String depCity, String arrStation, String arrCity, String depDate) {
        List<CarriageThrough> carriageThroughs = getCarriageThrough(depStation, depCity, arrStation, arrCity, depDate);
        if (carriageThroughs == null || carriageThroughs.isEmpty()) {
            return null;
        }
        return carriageThroughs.stream()
                .sorted((c1, c2) -> {
                    long duration1 = Duration.between(c1.getDepTime(), c1.getArrTime()).toMinutes();
                    long duration2 = Duration.between(c2.getDepTime(), c2.getArrTime()).toMinutes();
                    return Long.compare(duration1, duration2);
                })
                .toList();
    }
}
