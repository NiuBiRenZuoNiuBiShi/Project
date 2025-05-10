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
    id              BINARY(16)  NOT NULL PRIMARY KEY,
    train_number    BINARY(16)  NOT NULL COMMENT 'eg. G5151',
    dep_station     VARCHAR(50) NOT NULL COMMENT '出发站',
    dep_city        VARCHAR(50) NOT NULL COMMENT '出发城市',
    arr_station     VARCHAR(50) NOT NULL COMMENT '到达站',
    arr_city        VARCHAR(50) NOT NULL COMMENT '到达城市',
    arr_time        DATETIME    NOT NULL COMMENT '到达时间',
    wait_time       TIME        NOT NULL COMMENT '等候时间',

    all_number      INT         NOT NULL COMMENT 'how many tickets or seats on the carriage',
    first_number    INT         NOT NULL COMMENT 'how many first class tickets or seats on the carriage',
    second_number   INT         NOT NULL,
    business_number INT         NOT NULL,
    no_seat_number  INT         NOT NULL,


    del             TINYINT(1)  NOT NULL DEFAULT 0
) COMMENT '车次';

CREATE TABLE Seats
(
    id           BINARY(16)     NOT NULL PRIMARY KEY,
    train_number BINARY(16)     NOT NULL,
    coach        INT            NOT NULL COMMENT '车厢号',
    seat_type    VARCHAR(10)    NOT NULL,
    price        DECIMAL(10, 2) NOT NULL,

    flags        BINARY(8)      NOT NULL COMMENT '座位状态',

    version      INT            NOT NULL DEFAULT 0 COMMENT '乐观锁',
    del          TINYINT(1)     NOT NULL DEFAULT 0
);

CREATE TABLE Food
(
    id        BINARY(16)     NOT NULL PRIMARY KEY,
    trains_id BINARY(16)     NOT NULL COMMENT 'which train this food belongs to',
    food_name VARCHAR(50)    NOT NULL,
    food_type VARCHAR(10)    NOT NULL,
    price     DECIMAL(10, 2) NOT NULL,

    lunch     BOOLEAN        NOT NULL DEFAULT 0 COMMENT '是否是午餐',
    dinner    BOOLEAN        NOT NULL DEFAULT 0,

    pic_url   VARCHAR(255),

    del       TINYINT(1)     NOT NULL DEFAULT 0
);

ALTER TABLE Seats
    DROP COLUMN price;

ALTER TABLE Carriages
    ADD COLUMN
        (business_price DECIMAL(10, 2) NOT NULL DEFAULT 0 COMMENT '商务座票价',
         first_price DECIMAL(10, 2) NOT NULL DEFAULT 0 COMMENT '一等座票价',
         second_price DECIMAL(10, 2) NOT NULL DEFAULT 0 COMMENT '二等座票价',
         no_seat_price DECIMAL(10, 2) NOT NULL DEFAULT 0 COMMENT '无座票价'
        );


ALTER TABLE Carriages
    ADD COLUMN
        (dep_time DATETIME NOT NULL COMMENT '这个Carriage出发时间');

ALTER TABLE carriages
    ADD COLUMN
        (flag BINARY(8) NOT NULL COMMENT '方便Seats的修改')