package com.setravel.swifttravel.controller;

import com.setravel.swifttravel.mapper.UserMapper;
import com.setravel.swifttravel.security.UserContext;
import com.setravel.swifttravel.utils.UUIDUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.entities.output.HotelReserveOutput;
import com.setravel.swifttravel.entities.request.HotelReserveRequest;
import com.setravel.swifttravel.service.ReservationService;

import jakarta.annotation.Resource;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/v1/reservations")
public class ReservationController {
    
    @Resource
    private ReservationService reservationService;

    @Resource
    private UserMapper userMapper;

    /*
     * 创建预订订单
     * POST /api/v1/reservations
     */
    @PostMapping
    public Result createReservation(@RequestBody HotelReserveRequest request) {
        try {
            // 从认证信息中获取用户id
            request.setUserId(UUIDUtil.bytesToString(UserContext.getCurrentUserId(userMapper)));
            HotelReserveOutput reservation = reservationService.createReservation(request);
            reservation.setUserName(UserContext.getCurrentUser(userMapper).getUsername());
            return Result.success("预订创建成功", reservation);
        } catch (OptimisticLockingFailureException e) {
            //logger.warn("创建预订时发生乐观锁冲突: {}", e.getMessage());
            return Result.error("您操作的资源刚刚被他人修改，请刷新后重试。"); // Result.code 默认为 500
        } catch (IllegalArgumentException e) {
            //logger.warn("创建预订参数无效: {}", e.getMessage());
            return Result.error("创建预订失败：" + e.getMessage());
        } catch (RuntimeException e) { // 包括库存不足等业务异常
            //logger.error("创建预订时发生运行时错误: {}", e.getMessage(), e);
            return Result.error("预订失败：" + e.getMessage());
        }
    }
    
    /**
     * 查询当前用户的预订历史
     * GET /api/v1/reservations/my-history?pageNum=1&pageSize=10
     */
    @GetMapping("/my-history")
    public Result getMyReservations(
        @RequestParam(defaultValue = "1") int pageNum,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        String userIdString = UUIDUtil.bytesToString(UserContext.getCurrentUserId(userMapper));
        try {
            IPage<HotelReserveOutput> reservations = reservationService.getUserReservations(userIdString, pageNum, pageSize);
            if (reservations == null || reservations.getRecords().isEmpty()) {
                return Result.success("未找到任何预订记录。", reservations);
            }
            return Result.success(reservations);
        } catch (Exception e) {
            //logger.error("查询用户 {} 的预订历史时发生错误:", userIdString, e);
            return Result.error("查询预订历史失败，请稍后再试。");
        }
    }
    
    /**
     * 获取单个预订详情 (属于当前用户)
     * GET /api/v1/reservations/{reservationIdString}
     */
    @GetMapping("/{reservationIdString}")
    public Result getReservationDetails(
        @PathVariable String reservationIdString
        // @AuthenticationPrincipal MyUserDetails userDetails
    ) {
        String userIdString = UUIDUtil.bytesToString(UserContext.getCurrentUserId(userMapper));
        try{
            HotelReserveOutput reservation = reservationService.getReservationDetails(reservationIdString, userIdString);
            if (reservation == null) {
                return Result.success("未找到任何预订记录。", null);
            }
            return Result.success(reservation);
        } catch (Exception e) {
            return Result.error("查询预订订单失败，请稍后再试。");
        }
    }


    /**
     * 取消预订
     * DELETE /api/v1/reservations/{reservationIdString}
     */

    @DeleteMapping("/{reservationIdString}")
    public Result cancelReservation(
            @PathVariable String reservationIdString
    ) {
        String userIdString = UUIDUtil.bytesToString(UserContext.getCurrentUserId(userMapper));
        try {
            boolean cancelled = reservationService.cancelReservation(reservationIdString, userIdString);
            if (cancelled) {
                return Result.success("预订 (ID: " + reservationIdString + ") 已成功取消。");
            } else {
                // 如果 service 层对于某些逻辑失败情况返回 false 而不是抛异常，则会进入此分支
                // 但根据当前 service 实现，失败通常是抛异常
                //logger.warn("尝试取消预订 {} (用户 {}) 返回 false，可能业务逻辑未成功执行但未抛异常。", reservationIdString, userIdString);
                return Result.error("取消预订操作未完全成功，请联系客服。");
            }
        } catch (OptimisticLockingFailureException e) {
            //logger.warn("取消预订 {} (用户 {}) 时发生乐观锁冲突: {}", reservationIdString, userIdString, e.getMessage());
            return Result.error("您操作的资源刚刚被他人修改，请刷新后重试。");
        } catch (IllegalArgumentException e) { // 例如无效的ID
            //logger.warn("取消预订参数无效 (ID: {}): {}", reservationIdString, e.getMessage());
            // 之前可能返回 HTTP 400 或 403
            return Result.error("取消预订失败：" + e.getMessage());
        } catch (RuntimeException e) { // 例如"预订不存在"或"状态不允许取消"
            //logger.warn("取消预订 {} (用户 {}) 时发生运行时错误: {}", reservationIdString, userIdString, e.getMessage());
            // 之前可能返回 HTTP 404 或 409 (Conflict)
            return Result.error("取消预订失败：" + e.getMessage());
        } catch (Exception e) {
            //logger.error("取消预订 {} (用户 {}) 时发生未知错误:", reservationIdString, userIdString, e);
            return Result.error("取消预订过程中发生意外错误，请稍后再试。");
        }
    }
}
