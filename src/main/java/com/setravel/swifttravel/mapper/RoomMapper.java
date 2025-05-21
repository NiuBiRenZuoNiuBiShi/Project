package com.setravel.swifttravel.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.setravel.swifttravel.entities.Room;

@Mapper
public interface RoomMapper extends BaseMapper<Room>{
    /*
     * 查询指定酒店在日期范围内可用的、符合人数要求的房间记录
     * 这个查询会返回每一天符合条件的房间记录
     * @param hotelId 酒店ID
     * @param startDate 开始日期 (inclusive)
     * @param endDate 结束日期 (exclusive)
     * @param minCapacity 最低容量要求 (如果 numberOfPeople 不为null)
     * @return List of Room entities
     */
    List<Room> findAvailableRooms(
        @Param("hotelId") byte[] hotelId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("minCapacity") Integer minCapacity 
    );
}
