package com.setravel.swifttravel.controller;

import com.setravel.swifttravel.entities.Notifications;
import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.mapper.NotificationsMapper;
import com.setravel.swifttravel.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationsMapper notificationsMapper;

    /**
     * 根据用户ID查询该用户的所有通知（按时间倒序）
     * @param userIdStr
     * @return
     */
    @GetMapping("/{userId}")
    public Result getUserNotifications(@PathVariable("userId") String userIdStr) {
        try{
            byte[] userId = UUIDUtil.uuidToBytes(UUID.fromString(userIdStr));
            List<Notifications> notifications = notificationsMapper.selectByUserId(userId);
            return Result.success("查询成功", notifications);
        }catch (Exception e){
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    @PostMapping("/read/{messageId}")
    public Result markRead(@PathVariable("messageId") String messageIdStr) {
        try{
            byte[] messageId = UUIDUtil.uuidToBytes(UUID.fromString(messageIdStr));
            Notifications notification = notificationsMapper.selectById(messageId);
            if(notification == null){
                return Result.error("消息不存在");
            }

            notification.setRead(true);
            notificationsMapper.updateById(notification);
            return Result.success("消息标记为已读");

        }catch (Exception e){
            return Result.error("更新失败：" + e.getMessage());
        }
    }

}
