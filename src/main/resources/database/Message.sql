CREATE TABLE payment_records (
                                 payment_id       BINARY(16) PRIMARY KEY COMMENT '支付ID',
                                 order_id         BINARY(16) NOT NULL COMMENT '关联订单ID',
                                 user_id          BINARY(16) NOT NULL COMMENT '支付用户ID',
                                 pay_type         VARCHAR(20) COMMENT '购买对象类型',
                                 amount           DECIMAL(10,2) NOT NULL COMMENT '支付金额',
                                 method           VARCHAR(20) COMMENT '支付方式',
                                 status           VARCHAR(20) DEFAULT '待支付' COMMENT '支付状态',
                                 created_time       DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发起支付时间',
                                 updated_time       DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',

                                 del             TINYINT(1) DEFAULT 0
);

CREATE TABLE notifications (
                               message_id      BINARY(16) PRIMARY KEY COMMENT '消息ID',
                               user_id         BINARY(16) NOT NULL COMMENT '接收用户ID',
                               order_id        BINARY(16) DEFAULT NULL COMMENT '关联订单ID',
                               message_type    VARCHAR(20) NOT NULL COMMENT '消息类型',
                               content         TEXT NOT NULL COMMENT '消息内容',
                               channel         VARCHAR(20) DEFAULT '站内信' COMMENT '发送渠道',
                               is_read         BOOLEAN DEFAULT FALSE COMMENT '是否已读',
                               send_status     VARCHAR(20) DEFAULT '待发送' COMMENT '发送状态',
                               send_time       DATETIME DEFAULT NULL COMMENT '发送时间',
                               created_time    DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

                               del             TINYINT(1) DEFAULT 0
);

CREATE TABLE tickets_orders
(
    id          BINARY(16)     NOT NULL PRIMARY KEY,
    pay_id      BINARY(16)     NOT NULL,
    user_id     BINARY(16)     NOT NULL,
    contacts_id BINARY(16)     NOT NULL,
    train_id    BINARY(16)     NOT NULL COMMENT '车次编号',
    seat_id     BINARY(16)     NOT NULL,

    del         TINYINT(1)     NOT NULL DEFAULT 0
);

CREATE TABLE hotel_orders
(
    id          BINARY(16)     NOT NULL PRIMARY KEY,
    pay_id      BINARY(16)     NOT NULL,
    user_id     BINARY(16)     NOT NULL,
    reservation_id BINARY(16),

    del         TINYINT(1)     NOT NULL DEFAULT 0
);

CREATE TABLE food_orders
(
    id          BINARY(16)     NOT NULL PRIMARY KEY,
    pay_id      BINARY(16)     NOT NULL,
    user_id     BINARY(16)     NOT NULL,

    del         TINYINT(1)     NOT NULL DEFAULT 0
);

CREATE TABLE food_order_items (
                                  id          BINARY(16) NOT NULL PRIMARY KEY, -- 每个明细的唯一ID
                                  order_id    BINARY(16) NOT NULL,             -- 外键，关联 food_orders.id
                                  food_id     BINARY(16) NOT NULL,
                                  food_number INT(10)    NOT NULL
)COMMENT '食物订单明细';

ALTER TABLE notifications
    ADD UNIQUE KEY uniq_user_order_type (user_id, order_id, message_type);
