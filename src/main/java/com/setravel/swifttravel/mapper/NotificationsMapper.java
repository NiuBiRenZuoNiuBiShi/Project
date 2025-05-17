package com.setravel.swifttravel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.setravel.swifttravel.entities.Notifications;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NotificationsMapper extends BaseMapper<Notifications> {

    //查询所有消息
    @Select("SELECT * FROM notifications WHERE user_id = #{userId} " +
        "AND del = 0 AND is_read = false ORDER BY send_time DESC")
    List<Notifications> selectByUserId(@Param("userId") byte[] userId);

    //查询未读消息
    @Select("SELECT * FROM notifications WHERE user_id = #{userId} " +
        "AND is_read = false AND del = 0 ORDER BY send_time DESC")
    List<Notifications> selectUnreadByUserId(@Param("userId") byte[] userId);
}
