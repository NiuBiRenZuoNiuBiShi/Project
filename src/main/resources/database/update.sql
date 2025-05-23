ALTER TABLE Seats
DROP COLUMN price;

ALTER TABLE Carriages
    ADD COLUMN
    (business_price DECIMAL(10, 2) NOT NULL DEFAULT 0 COMMENT '商务座票价',
         first_price DECIMAL(10, 2) NOT NULL DEFAULT 0 COMMENT '一等座票价',
         second_price DECIMAL(10, 2) NOT NULL DEFAULT 0 COMMENT '二等座票价',
         no_seat_price DECIMAL(10, 2) NOT NULL DEFAULT 0 COMMENT '无座票价'
        )

ALTER TABLE tickets_orders
    ADD pay_status VARCHAR(20) NOT NULL DEFAULT '未支付' COMMENT '支付状态（未支付/已支付/已取消）';

ALTER TABLE food_orders
    ADD pay_status VARCHAR(20) NOT NULL DEFAULT '未支付' COMMENT '支付状态（未支付/已支付/已取消）';

ALTER TABLE hotel_orders
    ADD pay_status VARCHAR(20) NOT NULL DEFAULT '未支付' COMMENT '支付状态（未支付/已支付/已取消）';