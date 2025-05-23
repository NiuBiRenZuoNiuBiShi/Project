package com.setravel.swifttravel.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 酒店评价表实体类
 * </p>
 *
 * @author Swift_Travel_Team
 * @since 2025-05-22
 */
@Getter
@Setter
@ToString
@TableName("hotel_review")
@Accessors(chain = true)
public class HotelReview extends Model<HotelReview> {

    private static final long serialVersionUID = 1L;

    /**
     * 评价ID
     */
    @TableId("id")
    private byte[] id;

    /**
     * 酒店ID
     */
    @TableField("hotel_id")
    private byte[] hotelId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private byte[] userId;

    /**
     * 关联的预订ID (可选)
     */
    @TableField("reservation_id")
    private byte[] reservationId;

    /**
     * 评分 (例如1-5)
     */
    @TableField("rating")
    private BigDecimal rating;

    /**
     * 评价内容
     */
    @TableField("comment")
    private String comment;

    /**
     * 评价时间
     */
    @TableField("review_time")
    private LocalDateTime reviewTime;

    /**
     * 逻辑删除标记
     */
    @TableField("del")
    private Boolean del;

    public static final String ID = "id";
    public static final String HOTEL_ID = "hotel_id";
    public static final String USER_ID = "user_id";
    public static final String RESERVATION_ID = "reservation_id";
    public static final String RATING = "rating";
    public static final String COMMENT = "comment";
    public static final String REVIEW_TIME = "review_time";
    public static final String DEL = "del";

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
