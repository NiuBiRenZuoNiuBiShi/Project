CREATE SCHEMA swift_travel;

CREATE TABLE City
(
    id        BINARY(16)  NOT NULL PRIMARY KEY,
    city_name VARCHAR(50) NOT NULL,

    del       TINYINT(1)  NOT NULL DEFAULT 0
) COMMENT '城市表';

CREATE TABLE Station
(
    id           BINARY(16)  NOT NULL PRIMARY KEY,
    station_name VARCHAR(50) NOT NULL,
    city_id      BINARY(16)  NOT NULL,

    del          TINYINT(1)  NOT NULL DEFAULT 0
) COMMENT '车站表';

CREATE TABLE TrainNumbers
(
    id           BINARY(16)  NOT NULL PRIMARY KEY,
    train_number VARCHAR(10) NOT NULL,
    dep_station  VARCHAR(50) NOT NULL,
    dep_city     VARCHAR(50) NOT NULL,
    arr_station  VARCHAR(50) NOT NULL,
    arr_city     VARCHAR(50) NOT NULL,
    dep_time     DATETIME    NOT NULL,
    arr_time     DATETIME    NOT NULL,

    del          TINYINT(1)  NOT NULL DEFAULT 0
) COMMENT '车次表';

CREATE TABLE Carriages
(
    id           BINARY(16)  NOT NULL PRIMARY KEY,
    train_number BINARY(16)  NOT NULL COMMENT 'eg. G5151',
    dep_station  VARCHAR(50) NOT NULL COMMENT '出发站',
    dep_city     VARCHAR(50) NOT NULL COMMENT '出发城市',
    arr_station  VARCHAR(50) NOT NULL COMMENT '到达站',
    arr_city     VARCHAR(50) NOT NULL COMMENT '到达城市',
    arr_time     DATETIME    NOT NULL COMMENT '到达时间',
    wait_time    TIME        NOT NULL COMMENT '等候时间',
    number       INT(10)     NOT NULL COMMENT 'how many tickets or seats on the carriage',

    del          TINYINT(1)  NOT NULL DEFAULT 0
) COMMENT '车次';

CREATE TABLE Seats
(
    id           BINARY(16)     NOT NULL PRIMARY KEY,
    train_number BINARY(16)     NOT NULL,
    coach        INT(10)        NOT NULL COMMENT '车厢号',
    seat_type    VARCHAR(10)    NOT NULL,
    price        DECIMAL(10, 2) NOT NULL,

    flags        BINARY(8)      NOT NULL COMMENT '座位状态',

    version      INT(10)        NOT NULL DEFAULT 0 COMMENT '乐观锁',
    del          TINYINT(1)     NOT NULL DEFAULT 0
);

#CREATE Table Traveler
#(
#    id           BINARY(16)  NOT NULL PRIMARY KEY,
#    bind_user_id BINARY(16)  NOT NULL COMMENT 'it is for user to know the traveler he bind
#            & if this is 0, then this is the traveler who book it',
#    name         VARCHAR(50) NOT NULL,
#    id_card      VARCHAR(20) NOT NULL,
#    phone_number VARCHAR(20) NOT NULL,
#
#    del          TINYINT(1)  NOT NULL DEFAULT 0
#);

CREATE TABLE Orders
(
    id          BINARY(16)     NOT NULL PRIMARY KEY,
    price       DECIMAL(10, 2) NOT NULL,
    seat_id     BINARY(16)     NOT NULL,
    traveler_id BINARY(16)     NOT NULL,

    create_time                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    del         TINYINT(1)     NOT NULL DEFAULT 0
);

CREATE TABLE Food
(
    id        BINARY(16)     NOT NULL PRIMARY KEY,
    trains_id BINARY(16)     NOT NULL COMMENT 'which train this food belongs to',
    food_name VARCHAR(50)    NOT NULL,
    food_type VARCHAR(10)    NOT NULL,
    price     DECIMAL(10, 2) NOT NULL,

    pic_url   VARCHAR(255)   NOT NULL,

    del       TINYINT(1)     NOT NULL DEFAULT 0
);