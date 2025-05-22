package com.setravel.swifttravel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.setravel.swifttravel.entities.TicketsOrders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TicketOrdersMapper extends BaseMapper<TicketsOrders> {
    @Select("""
    SELECT * FROM tickets_orders\s
    WHERE pay_status = '已支付'
      AND del = 0
      AND train_id IN (
        SELECT id FROM trainnumbers\s
        WHERE dep_time BETWEEN NOW() AND #{time}
      )
    """)
    List<TicketsOrders> selectPendingDepartureWithin(@Param("time") LocalDateTime time);
}