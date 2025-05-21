package com.setravel.swifttravel.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.*;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.setravel.swifttravel.mapper.HotelMapper;
import com.setravel.swifttravel.mapper.RoomMapper;
import com.setravel.swifttravel.service.HotelService;
import com.setravel.swifttravel.utils.UUIDUtil;

import jakarta.annotation.Resource;

import com.setravel.swifttravel.entities.*;
import com.setravel.swifttravel.entities.output.HotelDetailOutput;
import com.setravel.swifttravel.entities.output.HotelSummaryOutput;
import com.setravel.swifttravel.entities.output.RoomInformationOutput;
import com.setravel.swifttravel.entities.request.HotelSearchRequest;


@Service
public class HotelServiceImpl extends ServiceImpl<HotelMapper, Hotel> implements HotelService{
    
    @Resource
    private RoomMapper roomMapper;

    @Override
    public IPage<HotelSummaryOutput> searchAvailableHotels(HotelSearchRequest request) {
        LambdaQueryWrapper<Hotel> hotelQuery = new LambdaQueryWrapper<>();
        hotelQuery.eq(Hotel::getDel, false); //在del=false的项中查找
        if (StringUtils.hasText(request.getHotelName())) {
            hotelQuery.like(Hotel::getName, request.getHotelName()); //模糊匹配酒店名
        }
        if (StringUtils.hasText((request.getLocation()))) {
            hotelQuery.like(Hotel::getAddress, request.getLocation()); //模糊匹配酒店位置
        }

        hotelQuery.orderByDesc(Hotel::getRating); //默认按照评分从高到低排序
        
        // 填充分页信息
        Page<Hotel> hotelPage = new Page<>(request.getPageNum(), request.getPageSize());
        IPage<Hotel> resultHotelPage = this.page(hotelPage, hotelQuery);

        // 转换并填充可用房间的最低价格
        return resultHotelPage.convert(hotel -> {
            HotelSummaryOutput hotelOutput = convertToSummaryOutput(hotel);
            if (request.getCheckinDate() != null && request.getCheckoutDate() != null) {
                // 查找该酒店在指定日期范围内的最低可用房间价格
                List<Room> availableRooms = roomMapper.findAvailableRooms(
                    hotel.getId(), 
                    request.getCheckinDate(), 
                    request.getCheckoutDate(), 
                    request.getNumberOfPeople()
                );
                if (!CollectionUtils.isEmpty(availableRooms)) {
                    // 检查是否每天都有房
                    long calnights = ChronoUnit.DAYS.between(request.getCheckinDate(), request.getCheckoutDate());
                    // if (nights <= 0) nights = 1;
                    final long nights = (calnights <= 0) ? 1L : calnights;
                    
                    // 将查询到的所有可用房间记录按房型进行分组
                    Map<String, List<Room>> roomsByType = availableRooms.stream()
                        .collect(Collectors.groupingBy(Room::getRoomType));
                    
                    Optional<BigDecimal> minPrice = roomsByType.values().stream()
                        // 过滤：筛选出那些在整个入住期间每天都有房的房型
                        .filter(dailyRoomList -> dailyRoomList.size() >= nights)
                        // 映射：对于满足条件的房型，计算其在入住期间的平均每晚价格
                        .map(dailyRoomList -> {
                            BigDecimal totPrice = BigDecimal.ZERO;
                            for (int i = 0; i < nights; i++) {
                                totPrice = totPrice.add(dailyRoomList.get(i).getPrice());
                            }
                            return totPrice.divide(BigDecimal.valueOf(nights), 2, java.math.RoundingMode.HALF_UP);
                        }).min(BigDecimal::compareTo); // 从各个房型的平均价格中找出最低的那个
                    
                    // 如果找到了非空的最低价格
                    minPrice.ifPresent(hotelOutput::setMinPrice);
                }
            }
            return hotelOutput;
        });
    }

    @Override
    public HotelDetailOutput getHotelDetailsWithAvailableRooms(String hotelIdString, LocalDate checkinDate,
            LocalDate checkoutDate, Integer numberOfPeople) {
        if (hotelIdString == null) {
            throw new IllegalArgumentException("无效的酒店ID格式");
        }

        // 查询目标酒店基本信息
        Hotel hotel = this.getOne(new LambdaQueryWrapper<Hotel>()
                            .eq(Hotel::getId, hotelIdString) //匹配酒店id
                            .eq(Hotel::getDel, false));    
        //将酒店实体转换为酒店详情
        HotelDetailOutput detailOutput = convertToDetailOutput(hotel);

        // 检查输入的日期：退房日期必须在入住日期之后，即入住晚数必须大于0
        if (checkinDate != null && checkoutDate != null) {
            long nights = ChronoUnit.DAYS.between(checkinDate, checkoutDate);
            if (nights <= 0) {
                throw new IllegalArgumentException("退房日期必须在入住日期之后");
            }
        
            // 查询指定日期范围内符合人数要求的可用房间记录
            List<Room> availableRooms = roomMapper.findAvailableRooms(hotel.getId(), checkinDate, checkoutDate, numberOfPeople);

            // 按房型分组
            Map<String, List<Room>> roomsByType = availableRooms.stream().collect(Collectors.groupingBy(Room::getRoomType));
            
            //筛选出在整个入住期间每天都有库存的房型
            List<RoomInformationOutput> availableRoomTypes = roomsByType.entrySet().stream()
                // 过滤：确保该房型在整个日期范围内每天都有记录       
                .filter(entry -> entry.getValue().size() >= nights)
                // 映射：将满足条件的房型条目转换为 RoomInformationOutput 对象
                .map(entry -> {
                    // 获取该房型在入住第一天的房间信息作为代表(实际上看到的价格都一样)
                    Room typicalRoom = entry.getValue().get(0);
                    RoomInformationOutput roomInfo = new RoomInformationOutput();
                    //这个房间id一定需要吗?
                    roomInfo.setId(UUIDUtil.bytesToString(typicalRoom.getId())); 
                    roomInfo.setRoomType(typicalRoom.getRoomType());
                    roomInfo.setPrice(typicalRoom.getPrice());

                    return roomInfo;
                }).collect(Collectors.toList()); // 将处理后的对象都放到一个列表中
            detailOutput.setAvailableRooms(availableRoomTypes);
        } else {
            // 如果没有提供入住和退房日期，则返回空列表
            detailOutput.setAvailableRooms(Collections.emptyList());
        }
        return detailOutput;
    }

    private HotelSummaryOutput convertToSummaryOutput(Hotel hotel) {
        if (hotel == null) return null;
        HotelSummaryOutput summaryOutput = new HotelSummaryOutput();
        summaryOutput.setId(UUIDUtil.bytesToString(hotel.getId())); 
        summaryOutput.setName(hotel.getName());
        summaryOutput.setAddress(hotel.getAddress());
        summaryOutput.setRating(hotel.getRating());
        summaryOutput.setPicUrl(hotel.getPicUrl());
        return summaryOutput;
    }

    private HotelDetailOutput convertToDetailOutput(Hotel hotel) {
        if (hotel == null) return null;
        HotelDetailOutput detailOutput = new HotelDetailOutput();
        BeanUtils.copyProperties(convertToSummaryOutput(hotel), detailOutput);
        detailOutput.setDescription(hotel.getDescription());
        return detailOutput;
    }
}
