ALTER TABLE Seats
DROP COLUMN price;

ALTER TABLE Carriages
    ADD COLUMN
    (business_price DECIMAL(10, 2) NOT NULL DEFAULT 0 COMMENT '商务座票价',
         first_price DECIMAL(10, 2) NOT NULL DEFAULT 0 COMMENT '一等座票价',
         second_price DECIMAL(10, 2) NOT NULL DEFAULT 0 COMMENT '二等座票价',
         no_seat_price DECIMAL(10, 2) NOT NULL DEFAULT 0 COMMENT '无座票价'
        )