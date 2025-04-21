CREATE TABLE orders (
                        order_id         BINARY(16) PRIMARY KEY COMMENT '订单ID',
                        user_id          BINARY(16) NOT NULL COMMENT '用户ID',
                        train_id         BINARY(16) NOT NULL COMMENT '车次编号',
                        seat_type        VARCHAR(20) NOT NULL COMMENT '座位类型（一等、二等等）',
                        coach_number     INT NOT NULL COMMENT '车厢号',
                        seat_number      VARCHAR(10) NOT NULL COMMENT '座位号',
                        departure_date   DATE NOT NULL COMMENT '出发日期',
                        departure_time   TIME NOT NULL COMMENT '出发时间',
                        start_station    VARCHAR(50) NOT NULL COMMENT '出发站',
                        end_station      VARCHAR(50) NOT NULL COMMENT '到达站',
                        passenger_name   VARCHAR(50) NOT NULL COMMENT '乘客姓名',
                        passenger_id     VARCHAR(18) NOT NULL COMMENT '乘客身份证号',
                        ticket_type      ENUM('成人票', '学生票', '儿童票', '残疾军人票') DEFAULT '成人票' COMMENT '车票类型',
                        is_insured       BOOLEAN DEFAULT FALSE COMMENT '是否购买保险',
                        order_status     ENUM('未支付', '已支付', '已取消', '已退款') DEFAULT '未支付' COMMENT '订单状态',
                        latest_payment_id BIGINT DEFAULT NULL COMMENT '最近一次成功支付ID',
                        created_time       DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        paid_time          DATETIME DEFAULT NULL COMMENT '支付时间',
                        del             TINYINT(1) DEFAULT 0
);

CREATE TABLE payment_records (
                                 payment_id       BINARY(16) PRIMARY KEY AUTO_INCREMENT COMMENT '支付ID',
                                 order_id         BINARY(16) NOT NULL COMMENT '关联订单ID',
                                 user_id          BINARY(16) NOT NULL COMMENT '支付用户ID',
                                 amount           DECIMAL(10,2) NOT NULL COMMENT '支付金额',
                                 method           ENUM('微信', '支付宝', '银行卡', '云闪付') COMMENT '支付方式',
                                 status           ENUM('待支付', '已支付', '已退款', '支付失败') DEFAULT '待支付' COMMENT '支付状态',
                                 transaction_no   VARCHAR(64) COMMENT '第三方平台支付编号',
                                 created_time       DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发起支付时间',
                                 updated_time       DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间'
    ,
                                 del             TINYINT(1) DEFAULT 0
);

CREATE TABLE notifications (
                               message_id      BINARY(16) PRIMARY KEY AUTO_INCREMENT COMMENT '消息ID',
                               user_id         BINARY(16) NOT NULL COMMENT '接收用户ID',
                               order_id        BINARY(16) DEFAULT NULL COMMENT '关联订单ID',
                               message_type    ENUM('支付成功', '订单状态变更', '退款成功', '系统公告') NOT NULL COMMENT '消息类型',
                               content         TEXT NOT NULL COMMENT '消息内容',
                               channel         ENUM('站内信', '短信', '邮件', '微信') DEFAULT '站内信' COMMENT '发送渠道',
                               is_read         BOOLEAN DEFAULT FALSE COMMENT '是否已读',
                               send_status     ENUM('待发送', '已发送', '发送失败') DEFAULT '待发送' COMMENT '发送状态',
                               send_time       DATETIME DEFAULT NULL COMMENT '发送时间',
                               created_time    DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               del             TINYINT(1) DEFAULT 0
);

CREATE TABLE FoodOrders
(
    id          BINARY(16)     NOT NULL PRIMARY KEY,
    order_id    BINARY(16)     NOT NULL,
    food_id     BINARY(16)     NOT NULL,
    food_number INT(10)        NOT NULL,
    price       DECIMAL(10, 2) NOT NULL,

    del         TINYINT(1)     NOT NULL DEFAULT 0
);