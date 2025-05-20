package com.setravel.swifttravel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.setravel.swifttravel.entities.PaymentRecords;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface PaymentRecordsMapper extends BaseMapper<PaymentRecords> {
    @Select("SELECT * FROM payment_records WHERE payment_id = #{paymentId}")
    PaymentRecords findByPaymentId(@Param("paymentId") byte[] paymentId);
}
