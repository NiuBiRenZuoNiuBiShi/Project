package com.setravel.swifttravel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.setravel.swifttravel.entities.HotelReview;
import com.setravel.swifttravel.entities.output.ReviewOutput;
import com.setravel.swifttravel.entities.request.CreateReviewRequest;

public interface HotelReviewService extends IService<HotelReview> {
    /**
     * 创建一条新的酒店评价
     * @param request 创建评价的请求DTO
     * @return 创建成功的评价DTO
    */
    ReviewOutput createReview(CreateReviewRequest request);

    /**
     * 根据酒店ID分页获取评价列表
     * @param hotelId 酒店ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页的评价信息DTO
     */
    IPage<ReviewOutput> getReviewsByHotelId(String hotelId, int pageNum, int pageSize);

    /**
     * 根据用户ID分页获取其发表的评价列表
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页的评价信息DTO
     */
    IPage<ReviewOutput> getReviewsByUserId(String userId, int pageNum, int pageSize);

    /**
     * 用户删除自己的评价 (逻辑删除)
     * @param reviewId 评价ID
     * @param userId 执行删除操作的用户ID(用于权限校验
     * @return 操作是否成功
     */
    boolean deleteReview(String reviewId, String userId);
}
