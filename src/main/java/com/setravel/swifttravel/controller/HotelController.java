package com.setravel.swifttravel.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.entities.output.HotelDetailOutput;
import com.setravel.swifttravel.entities.output.HotelSummaryOutput;
import com.setravel.swifttravel.entities.request.HotelSearchRequest;
import com.setravel.swifttravel.service.HotelService;

import jakarta.annotation.Resource;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/hotels")
public class HotelController {
    
    @Resource
    private HotelService hotelService;

    /*
     * 搜索酒店
     * 
     * @param location 酒店位置
     * @param hotelName 酒店名
     * @param checkinDate 入住日期
     * @param checkoutDate 退房日期
     * @param numberOfPeople 入住人数
     * @param pageNum 展示页数,默认为1
     * @param pageSize 一页展示多少项,默认为10
     * @param sortBy 排列规则
     * @param sortOrder 排列顺序
     * @return 酒店信息列表
     */
    @GetMapping("/search")
    public Result searchHotels(
        @RequestParam(required = false) String location,
        @RequestParam(required = false) String hotelName,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkinDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkoutDate,
        @RequestParam(required = false) Integer numberOfPeople,
        @RequestParam(defaultValue = "1") int pageNum,
        @RequestParam(defaultValue = "10") int pageSize,
        @RequestParam(defaultValue = "rating") String sortBy,
        @RequestParam(defaultValue = "desc") String sortOrder
    ) {
        HotelSearchRequest request = new HotelSearchRequest();
        request.setLocation(location);
        request.setHotelName(hotelName);
        request.setCheckinDate(checkinDate);
        request.setCheckoutDate(checkoutDate);
        request.setNumberOfPeople(numberOfPeople);
        request.setPageNum(pageNum);
        request.setPageSize(pageSize);
        request.setSortBy(sortBy);
        request.setSortOrder(sortOrder);

        try {
            IPage<HotelSummaryOutput> results = hotelService.searchAvailableHotels(request);
            return Result.success(results);
        } catch (Exception e) {
            return Result.error("获取酒店详情时发生错误，请稍后再试。");
        }
    }
    
    /*
     * 获取酒店详情和指定日期的可用房型
     * @param hotelIdString 酒店id
     * @param checkinDate 入住日期
     * @param checkoutDate 退房日期
     * @param numberOfPeople 入住人数
     * @return 目标酒店可选的房型列表
     * 
     */
    @GetMapping("/{hotelIdString}/availability")
    public Result getHotelDetailsWithRooms( 
            @PathVariable String hotelIdString,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkinDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkoutDate,
            @RequestParam(required = false) Integer numberOfPeople
    ) {
        try {
            HotelDetailOutput hotelDetails = hotelService.getHotelDetailsWithAvailableRooms(hotelIdString, checkinDate, checkoutDate, numberOfPeople);
            if (hotelDetails != null) {
                return Result.success(hotelDetails);
            } else {
                return Result.error("酒店未找到 (ID: " + hotelIdString + ")");
            }
        } catch (IllegalArgumentException e) {
            return Result.error("请求酒店id参数无效:"+e.getMessage());
        } catch (Exception e) {
            return Result.error("获取酒店详情时发生内部错误，请稍后再试。");
        }
    }
    
}
