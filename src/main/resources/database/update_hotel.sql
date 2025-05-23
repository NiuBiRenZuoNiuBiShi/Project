ALTER TABLE Hotel
ADD COLUMN version INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号';

ALTER TABLE Room
ADD COLUMN capacity INT NOT NULL COMMENT '房间容量',
ADD COLUMN version INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
ADD COLUMN current_reservation_id BINARY(16) NULL DEFAULT NULL COMMENT '当前占用此房间记录的预订ID',
ADD CONSTRAINT fk_room_current_reservation FOREIGN KEY (current_reservation_id) REFERENCES Reservation(id) ON DELETE SET NULL,
ADD INDEX idx_room_current_reservation_id (current_reservation_id); -- 为方便查询添加索引??

ALTER TABLE Reservation
ADD COLUMN version INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号';

-- 酒店评价表
CREATE TABLE hotel_review (
    id BINARY(16) NOT NULL PRIMARY KEY COMMENT '评价ID',
    hotel_id BINARY(16) NOT NULL COMMENT '酒店ID',
    user_id BINARY(16) NOT NULL COMMENT '用户ID',
    reservation_id BINARY(16) NOT NULL COMMENT '关联的预订ID (用于校验评价资格和唯一性)',
    rating DECIMAL(2, 1) NOT NULL COMMENT '评分1-5',
    comment TEXT COMMENT '评价内容',
    review_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评价提交时间',
    del BOOLEAN NOT NULL DEFAULT FALSE COMMENT '逻辑删除标记',
    INDEX idx_hotel_id (hotel_id),
    INDEX idx_user_id (user_id),
    INDEX idx_reservation_id (reservation_id) -- 为reservation_id创建索引，用于快速检查是否已评价
);