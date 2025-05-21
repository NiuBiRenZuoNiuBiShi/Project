package com.setravel.swifttravel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.setravel.swifttravel.entities.TicketsOrders;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TicketOrdersMapper extends BaseMapper<TicketsOrders> {
}