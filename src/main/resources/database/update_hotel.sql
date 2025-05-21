ALTER TABLE Room
ADD COLUMN capacity INT NOT NULL COMMENT '房间容量',
ADD COLUMN version INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
ADD COLUMN current_reservation_id BINARY(16) NULL DEFAULT NULL COMMENT '当前占用此房间记录的预订ID',
ADD CONSTRAINT fk_room_current_reservation FOREIGN KEY (current_reservation_id) REFERENCES Reservation(id) ON DELETE SET NULL,
ADD INDEX idx_room_current_reservation_id (current_reservation_id); -- 为方便查询添加索引??


ALTER TABLE Reservation
ADD COLUMN version INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号';