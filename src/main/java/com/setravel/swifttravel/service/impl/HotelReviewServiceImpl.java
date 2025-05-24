package com.setravel.swifttravel.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.setravel.swifttravel.mapper.UserMapper;
import com.setravel.swifttravel.security.UserContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.setravel.swifttravel.entities.Hotel;
import com.setravel.swifttravel.entities.HotelReview;
import com.setravel.swifttravel.entities.Reservation;
import com.setravel.swifttravel.entities.output.ReviewOutput;
import com.setravel.swifttravel.entities.request.CreateReviewRequest;
import com.setravel.swifttravel.mapper.HotelMapper;
import com.setravel.swifttravel.mapper.HotelReviewMapper;
import com.setravel.swifttravel.mapper.ReservationMapper;
import com.setravel.swifttravel.service.HotelReviewService;
import com.setravel.swifttravel.utils.UUIDUtil;

import jakarta.annotation.Resource;

@Service
public class HotelReviewServiceImpl extends ServiceImpl<HotelReviewMapper, HotelReview> implements HotelReviewService {

    @Resource
    private HotelMapper hotelMapper;

    @Resource
    private ReservationMapper reservationMapper;

    @Resource
    private HotelReviewMapper hotelReviewMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    @Transactional
    public ReviewOutput createReview(CreateReviewRequest request) {
        byte[] hotelIdBytes = UUIDUtil.StringToBytes(request.getHotelId());
        byte[] userIdBytes = UUIDUtil.StringToBytes(request.getUserId());
        byte[] reservationIdBytes = StringUtils.hasText(request.getReservationId()) ?
                                    UUIDUtil.StringToBytes(request.getReservationId()) : null;

        if (hotelIdBytes == null || userIdBytes == null) {
            throw new IllegalArgumentException("酒店ID或用户ID不能为空。");
        }

        // 判断酒店是否存在
        Hotel hotel = hotelMapper.selectOne(new LambdaQueryWrapper<Hotel>()
                .eq(Hotel::getId, hotelIdBytes)
                .eq(Hotel::getDel, false));
        if (hotel == null) {
            throw new IllegalArgumentException("评价的酒店不存在。");
        }
        // 校验用户是否有资格评价
        // 基于预订id,订单状态,一个订单只能评价一次
        if (reservationIdBytes != null) {
            Reservation reservation = reservationMapper.selectOne(new LambdaQueryWrapper<Reservation>()
                    .eq(Reservation::getId, reservationIdBytes)
                    .eq(Reservation::getUserId, userIdBytes) // 预订属于该用户
                    .eq(Reservation::getHotelId, hotelIdBytes) // 预订的是这家酒店
                    .eq(Reservation::getDel, false));
            if (reservation == null) {
                throw new IllegalArgumentException("无效的预订信息，无法评价。");
            }
            // 还可以进一步检查预订状态
            // if (!"COMPLETED".equalsIgnoreCase(reservation.getStatus())) {
            //     throw new IllegalArgumentException("只有已完成的预订才能评价。");
            // }

            // 检查是否已对此预订进行过评价
            long existingReviewsForReservation = hotelReviewMapper.selectCount(new LambdaQueryWrapper<HotelReview>()
                .eq(HotelReview::getReservationId, reservationIdBytes)
                .eq(HotelReview::getDel, false));
            if (existingReviewsForReservation > 0) {
                throw new IllegalArgumentException("您已针对此预订发表过评价。");
            }
        } else {
            // 检查该用户是否曾经预订过这家酒店
            long userHotelBookings = reservationMapper.selectCount(new LambdaQueryWrapper<Reservation>()
                .eq(Reservation::getUserId, userIdBytes)
                .eq(Reservation::getHotelId, hotelIdBytes)
                .eq(Reservation::getDel, false)
                // .eq(Reservation::getStatus, "COMPLETED") // 检查是否有已完成的预订
            );
            if (userHotelBookings == 0) {
                throw new IllegalArgumentException("您尚未预订或完成此酒店的入住，暂时无法评价。");
            }
        }

        HotelReview review = new HotelReview();
        review.setId(UUIDUtil.generateUUIDBytes());
        review.setHotelId(hotelIdBytes);
        review.setUserId(userIdBytes);
        review.setReservationId(reservationIdBytes);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setReviewTime(LocalDateTime.now());
        review.setDel(false);

        this.save(review);

        // 更新酒店平均评分
        updateHotelAverageRating(hotelIdBytes);

        return convertToOutput(review);
    }

    @Override
    public IPage<ReviewOutput> getReviewsByHotelId(String hotelId, int pageNum, int pageSize) {
        byte[] hotelIdBytes = UUIDUtil.StringToBytes(hotelId);
        if (hotelIdBytes == null) {
            return new Page<ReviewOutput>(pageNum, pageSize).setRecords(java.util.Collections.emptyList()).setTotal(0);
        }

        Page<HotelReview> page = new Page<>(pageNum, pageSize);
        IPage<HotelReview> reviewPage = this.page(page, new LambdaQueryWrapper<HotelReview>()
                .eq(HotelReview::getHotelId, hotelIdBytes)
                .eq(HotelReview::getDel, false)
                .orderByDesc(HotelReview::getReviewTime) // 按评价时间降序
        );
        return reviewPage.convert(this::convertToOutput);
    }

    @Override
    public IPage<ReviewOutput> getReviewsByUserId(String userId, int pageNum, int pageSize) {
        byte[] userIdBytes = UUIDUtil.StringToBytes(userId);
        if (userIdBytes == null) {
            return new Page<ReviewOutput>(pageNum, pageSize).setRecords(java.util.Collections.emptyList()).setTotal(0);
        }

        Page<HotelReview> page = new Page<>(pageNum, pageSize);
        IPage<HotelReview> reviewPage = this.page(page, new LambdaQueryWrapper<HotelReview>()
                .eq(HotelReview::getUserId, userIdBytes)
                .eq(HotelReview::getDel, false)
                .orderByDesc(HotelReview::getReviewTime)
        );
        return reviewPage.convert(this::convertToOutput);
    }

    @Override
    @Transactional
    public boolean deleteReview(String reviewId, String userId) {
        byte[] reviewIdBytes = UUIDUtil.StringToBytes(reviewId);
        byte[] userIdBytes = UUIDUtil.StringToBytes(userId);

        if (reviewIdBytes == null || userIdBytes == null) {
            throw new IllegalArgumentException("评价ID或用户ID无效。");
        }

        HotelReview review = this.getOne(new LambdaQueryWrapper<HotelReview>()
                .eq(HotelReview::getId, reviewIdBytes)
                .eq(HotelReview::getDel, false)); // 确保评价存在且未被删除
        
        if (review == null) {
            throw new RuntimeException("要删除的评价不存在。");
        }

        // 权限校验：确保是用户自己的评价才能删除
        if (!Arrays.equals(review.getUserId(), userIdBytes)) {
            // 也可以允许管理员删除，那需要额外的角色判断逻辑
            throw new SecurityException("无权删除该评价。");
        }

        review.setDel(true); // 逻辑删除
        boolean success = this.updateById(review);

        // 如果删除了评价,也需要更新酒店的评分
        if (success) {
            updateHotelAverageRating(review.getHotelId());
        }
        return success;
    }
    
    private ReviewOutput convertToOutput(HotelReview review) {
        if (review == null) return null;
        ReviewOutput output = new ReviewOutput();
        BeanUtils.copyProperties(review, output);
        output.setReviewId(UUIDUtil.bytesToString(review.getId()));
        output.setHotelId(UUIDUtil.bytesToString(review.getHotelId()));
        output.setUserId(UUIDUtil.bytesToString(review.getUserId()));
        output.setUserName(UserContext.getCurrentUser(userMapper).getUsername());
        return output;
    }

    private void updateHotelAverageRating(byte[] hotelIdBytes) {
        // 查询该酒店所有未删除的有效评分
        List<HotelReview> reviews = this.list(new LambdaQueryWrapper<HotelReview>() // 'this.list' is from ServiceImpl
                .eq(HotelReview::getHotelId, hotelIdBytes)
                .isNotNull(HotelReview::getRating) // 确保评分不为null
                .eq(HotelReview::getDel, false));

        // 如果酒店的评价都被删除，使用默认的评分
        if (reviews.isEmpty()) {
            Hotel hotel = hotelMapper.selectById(hotelIdBytes);
            if (hotel != null) {
                hotel.setRating(BigDecimal.valueOf(1.0));
                hotelMapper.updateById(hotel);
            }
            return;
        }

        // 计算平均分并保留一位小数（最终结果为 BigDecimal 类型）
        BigDecimal averageRating = reviews.stream()
            .map(HotelReview::getRating)
            .filter(rating -> !Objects.equals(rating, BigDecimal.ZERO)) // 过滤可能的 0 值
            .reduce(BigDecimal.ZERO, BigDecimal::add) // 累加评分
            .divide(
                new BigDecimal(reviews.size()), // 避免除以 0
                1, // 保留一位小数
                RoundingMode.HALF_UP // 四舍五入
            );

        // 更新Hotel表中的评分
        Hotel hotel = hotelMapper.selectById(hotelIdBytes); // Fetches hotel again
        if (hotel != null) {
            hotel.setRating(averageRating);
            hotelMapper.updateById(hotel);
        }
    }
}
