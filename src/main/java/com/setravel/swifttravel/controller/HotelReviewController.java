package com.setravel.swifttravel.controller;

import com.setravel.swifttravel.mapper.UserMapper;
import com.setravel.swifttravel.security.UserContext;
import com.setravel.swifttravel.utils.UUIDUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.entities.output.ReviewOutput;
import com.setravel.swifttravel.entities.request.CreateReviewRequest;
import com.setravel.swifttravel.service.HotelReviewService;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("/api/v1/reviews")
public class HotelReviewController {
    
    @Resource
    private HotelReviewService hotelReviewService;

    @Resource
    private UserMapper userMapper;

    /**
     * 用户创建酒店评价
     * POST /api/v1/reviews
     */
    @PostMapping
    public Result createReview(@Validated @RequestBody CreateReviewRequest request) {
        request.setUserId(UUIDUtil.bytesToString(UserContext.getCurrentUserId(userMapper)));
        try {
            ReviewOutput createdReview = hotelReviewService.createReview(request);
            // 使用 Result.success(data) 返回成功响应，HTTP状态码默认为200 OK
            return Result.success("评价创建成功", createdReview);
        } catch (IllegalArgumentException e) {
            // 参数校验失败
            return Result.error("400", e.getMessage());
        } catch (RuntimeException e) {
            // 更通用的运行时异常
            return Result.error("创建评价失败: " + e.getMessage());
        }
    }

    /**
     * 获取指定酒店的评价列表 (分页)
     * GET /api/v1/reviews/hotel/{hotelIdString}?pageNum=1&pageSize=10
     */
    @GetMapping("/hotel/{hotelIdString}")
    public Result getReviewsForHotel(
            @PathVariable String hotelIdString,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            IPage<ReviewOutput> reviewPage = hotelReviewService.getReviewsByHotelId(hotelIdString, pageNum, pageSize);
            return Result.success(reviewPage);
        } catch (Exception e) {
            return Result.error("获取酒店评价列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取指定用户的评价列表 (分页)
     * GET /api/v1/reviews/userReview?pageNum=1&pageSize=10
     */
    @GetMapping("/userReview")
    public Result getReviewsByUser(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        String userIdString = UUIDUtil.bytesToString(UserContext.getCurrentUserId(userMapper));
        try {
            IPage<ReviewOutput> reviewPage = hotelReviewService.getReviewsByUserId(userIdString, pageNum, pageSize);
            return Result.success(reviewPage);
        } catch (Exception e) {
            return Result.error("获取用户评价列表失败: " + e.getMessage());
        }
    }

    /**
     * 用户删除自己的评价
     * DELETE /api/v1/reviews/{reviewIdString}
     */
    @DeleteMapping("/{reviewIdString}")
    public Result deleteReview(
        @PathVariable String reviewIdString
    ) {
        String userIdString = UUIDUtil.bytesToString(UserContext.getCurrentUserId(userMapper));
        try {
            boolean deleted = hotelReviewService.deleteReview(reviewIdString, userIdString);
            if (deleted) {
                return Result.success("评价已成功删除。");
            } else {
                // 此情况不常见，如果service层在未成功时抛出异常但deleteReview返回false
                return Result.error("删除评价操作未成功执行。");
            }
        } catch (SecurityException e) { // 无权操作
            return Result.error("403", e.getMessage()); // 使用 403 业务码
        } catch (IllegalArgumentException e) { // 参数错误，如ID无效
            return Result.error("400", e.getMessage());
        } catch (RuntimeException e) { // 例如评价未找到，或其他运行时错误
            // 对于“未找到”这类情况，更细致的错误码可能是404
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("不存在")) {
                return Result.error("404", e.getMessage());
            }
            return Result.error("删除评价失败: " + e.getMessage());
        }
    }
}
