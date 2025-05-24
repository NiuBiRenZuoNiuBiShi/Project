package com.setravel.swifttravel.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        hotelQuery.eq(Hotel::getDel, false);
        // 模糊匹配酒店名称
        if (StringUtils.hasText(request.getHotelName())) {
            hotelQuery.like(Hotel::getName, request.getHotelName());
        }
        // 模糊匹配酒店地址
        if (StringUtils.hasText(request.getLocation())) {
            hotelQuery.like(Hotel::getAddress, request.getLocation());
        }
        // 获取排序参数
        String sortBy = request.getSortBy();
        boolean ascending = "asc".equalsIgnoreCase(request.getSortOrder());

        // 根据不同的排序字段处理排序逻辑
        if ("rating".equalsIgnoreCase(sortBy)) {
            if (ascending) hotelQuery.orderByAsc(Hotel::getRating);
            else hotelQuery.orderByDesc(Hotel::getRating);
            // 创建分页请求对象
            Page<Hotel> hotelPageRequest = new Page<>(request.getPageNum(), request.getPageSize());
            IPage<Hotel> resultHotelPage = this.page(hotelPageRequest, hotelQuery);

            List<HotelSummaryOutput> filteredOutputs = resultHotelPage.getRecords().stream()
                .map(hotel -> {
                    HotelSummaryOutput hotelOutput = convertToSummaryOutput(hotel);
                    if (request.getCheckinDate() != null && request.getCheckoutDate() != null) {
                        BigDecimal minPrice = calculateMinPriceForHotel(hotel, request.getCheckinDate(), request.getCheckoutDate(), request.getNumberOfPeople());
                        if (minPrice != null) {
                            hotelOutput.setMinPrice(minPrice);
                            return hotelOutput; // 只有minPrice不为null才返回output
                        } else {
                            return null; // 没有可用房间，返回null以便过滤
                        }
                    } else {
                        // 如果没有日期，视为不可用来过滤
                        return hotelOutput;
                    }
                })
                .filter(Objects::nonNull) // 过滤掉那些因为minPrice为null而返回null的项
                .collect(Collectors.toList());

            IPage<HotelSummaryOutput> finalPage = new Page<>(resultHotelPage.getCurrent(), resultHotelPage.getSize(), resultHotelPage.getTotal());
            finalPage.setRecords(filteredOutputs);
            return finalPage;

        } else if ("price".equalsIgnoreCase(sortBy)) {
            // 获取所有匹配基础条件的酒店列表
            List<Hotel> allMatchingHotels = this.list(hotelQuery); 

            if (CollectionUtils.isEmpty(allMatchingHotels)) {
                return new Page<HotelSummaryOutput>(request.getPageNum(), request.getPageSize()).setRecords(Collections.emptyList()).setTotal(0);
            }
            // 遍历所有匹配的酒店，计算最低可用价格
            List<HotelSummaryOutput> hotelOutputsWithPrice = allMatchingHotels.stream()
                .map(hotel -> {
                    HotelSummaryOutput hotelOutput = convertToSummaryOutput(hotel);
                    if (request.getCheckinDate() != null && request.getCheckoutDate() != null) {
                        BigDecimal minPrice = calculateMinPriceForHotel(hotel, request.getCheckinDate(), request.getCheckoutDate(), request.getNumberOfPeople());
                        if (minPrice != null) {
                            hotelOutput.setMinPrice(minPrice);
                            return hotelOutput;
                        } else {
                            return null;
                        }
                    } else {
                        hotelOutput.setMinPrice(null);
                        return hotelOutput;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            // 根据计算结果对酒店列表进行排序
            Comparator<HotelSummaryOutput> priceComparator = Comparator.comparing(
                HotelSummaryOutput::getMinPrice,
                Comparator.nullsLast(BigDecimal::compareTo)
            );
            if (!ascending) {
                priceComparator = priceComparator.reversed();
            }
            hotelOutputsWithPrice.sort(priceComparator);

            // 手动分页
            int total = hotelOutputsWithPrice.size();
            int fromIndex = (request.getPageNum() - 1) * request.getPageSize();
            int toIndex = Math.min(fromIndex + request.getPageSize(), total);

            List<HotelSummaryOutput> pageContent;
            if (fromIndex >= total) {
                pageContent = Collections.emptyList();
            } else {
                pageContent = hotelOutputsWithPrice.subList(fromIndex, toIndex);
            }

            IPage<HotelSummaryOutput> resultPage = new Page<>(request.getPageNum(), request.getPageSize(), total);
            resultPage.setRecords(pageContent);
            return resultPage;

        } else {
            // 默认排序逻辑——按评分降序
            hotelQuery.orderByDesc(Hotel::getRating);
            Page<Hotel> hotelPageRequest = new Page<>(request.getPageNum(), request.getPageSize());
            IPage<Hotel> resultHotelPage = this.page(hotelPageRequest, hotelQuery);

            return resultHotelPage.convert(hotel -> {
                HotelSummaryOutput hotelOutput = convertToSummaryOutput(hotel);
                if (request.getCheckinDate() != null && request.getCheckoutDate() != null) {
                    BigDecimal minPrice = calculateMinPriceForHotel(hotel, request.getCheckinDate(), request.getCheckoutDate(), request.getNumberOfPeople());
                    if (minPrice != null) {
                        hotelOutput.setMinPrice(minPrice);
                    }
                }
                return hotelOutput;
            });
        }
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
            List<RoomInformationOutput> availableRoomTypes = roomsByType.values().stream()
                // 过滤：确保该房型在整个日期范围内每天都有记录       
                .filter(rooms -> rooms.size() >= nights)
                // 映射：将满足条件的房型条目转换为 RoomInformationOutput 对象
                .map(rooms -> {
                    // 获取该房型在入住第一天的房间信息作为代表(实际上看到的价格都一样)
                    Room typicalRoom = rooms.getFirst();
                    RoomInformationOutput roomInfo = new RoomInformationOutput();
                    //这个房间id一定需要吗?
                    roomInfo.setId(UUIDUtil.bytesToString(typicalRoom.getId()));
                    roomInfo.setRoomType(typicalRoom.getRoomType());
                    roomInfo.setPrice(typicalRoom.getPrice());
                    roomInfo.setCapacity(typicalRoom.getCapacity());

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

    private BigDecimal calculateMinPriceForHotel(Hotel hotel, LocalDate checkinDate, LocalDate checkoutDate, Integer numberOfPeople) {
        // 日期有效性检验
        if (checkinDate == null || checkoutDate == null || checkinDate.isAfter(checkoutDate) || checkinDate.isEqual(checkoutDate)) {
            return null;
        }
        // 从数据库查询在指定日期范围内、符合人数要求的可用房间记录列表
        List<Room> availableRooms = roomMapper.findAvailableRooms( // Assuming this method exists
            hotel.getId(),
            checkinDate,
            checkoutDate,
            numberOfPeople
        );

        if (CollectionUtils.isEmpty(availableRooms)) {
            return null;
        }

        long nights = ChronoUnit.DAYS.between(checkinDate, checkoutDate);
        if (nights <= 0) nights = 1L;

        // 将查询到的所有可用房间记录按房型 (roomType) 进行分组
        Map<String, List<Room>> roomsByType = availableRooms.stream()
                .collect(Collectors.groupingBy(Room::getRoomType));

        final long finalNights = nights;
        Optional<BigDecimal> minPrice = roomsByType.values().stream()
                // 过滤：筛选出那些在整个入住期间每天都有可用房间记录的房型
                .filter(dailyRoomList -> dailyRoomList.size() >= finalNights)
                // 映射：对于满足条件的房型，取第一晚的价格
                .map(dailyRoomList -> dailyRoomList.getFirst().getPrice())
                // 聚合：从所有符合条件的房型中找出最低的那一个
                .min(BigDecimal::compareTo);

        return minPrice.orElse(null);
    }
}
