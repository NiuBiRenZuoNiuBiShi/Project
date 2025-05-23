package com.setravel.swifttravel.controller;

import com.setravel.swifttravel.entities.Notifications;
import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.mapper.NotificationsMapper;
import com.setravel.swifttravel.mapper.UserMapper;
import com.setravel.swifttravel.security.UserContext;
import com.setravel.swifttravel.service.NotificationService;
import com.setravel.swifttravel.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationsMapper notificationsMapper;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserMapper userMapper;

    /**
     * 根据用户ID查询该用户的所有通知（按时间倒序）
     * @return
     */
    @GetMapping
    public Result getUserNotifications() {
        try{
            byte[] userId = UserContext.getCurrentUserId(userMapper);
            List<Notifications> notifications = notificationsMapper.selectByUserId(userId);
            return Result.success("查询成功", notifications);
        }catch (Exception e){
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户所有未读消息
     * @return
     */
    @GetMapping("/unread")
    public Result getUnread(){
        try {
            byte[] userId = UserContext.getCurrentUserId(userMapper);
            List<Notifications> list = notificationService.getUnreadNotifications(userId);
            return Result.success("查询成功", list);
        }catch (Exception e){
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 标记消息为已读
     */
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

    /**
     * 查看消息详情并标记为已读
     * @param messageIdStr
     * @return
     */
    @GetMapping("/detail/{messageId}")
    public Result getDetailAndMarkRead(@PathVariable("messageId") String messageIdStr) {
        try{
            byte[] messageId = UUIDUtil.uuidToBytes(UUID.fromString(messageIdStr));
            return notificationService.getMessageDetailAndMarkRead(messageId);
        }catch (Exception e){
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 全部标记为已读
     * @return
     */
    @PostMapping("/readAll")
    public Result markAllAsRead() {
        try{
            byte[] userId = UserContext.getCurrentUserId(userMapper);
            return notificationService.markAllAsRead(userId);
        }catch (Exception e){
            return Result.error("批量更新失败" + e.getMessage());
        }
    }

    /**
     * 分页
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/page")
    public Result getNotificationByPage(
                                        @RequestParam(defaultValue = "1")int page,
                                        @RequestParam(defaultValue = "10")int size) {
        try{
            byte[] userId = UserContext.getCurrentUserId(userMapper);
            List<Notifications> list = notificationService.getNotificationByPage(userId, page, size);
            int total = notificationService.countUserNotifications(userId);
            return Result.success("分页查询成功", Map.of(
                "data", list,
                "page", page,
                "size", size,
                "total", total
            ));
        }catch (Exception e){
            return Result.error("分页查询失败" + e.getMessage());
        }
    }
}
