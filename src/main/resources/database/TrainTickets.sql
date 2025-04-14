CREATE SCHEMA Trains;

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
    FOREIGN KEY (city_id) REFERENCES City (id),

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
    number       INT(10)     NOT NULL COMMENT 'how many tickets or seats on the carriage',

    del          TINYINT(1)  NOT NULL DEFAULT 0
) COMMENT '车次表';

CREATE TABLE Carriages
(
    id              BINARY(16)  NOT NULL PRIMARY KEY,
    train_number_id BINARY(16)  NOT NULL  COMMENT 'eg. G5151',
    dep_station     VARCHAR(50) NOT NULL,
    dep_city        VARCHAR(50) NOT NULL,
    arr_station     VARCHAR(50) NOT NULL,
    arr_city        VARCHAR(50) NOT NULL,
    arr_time        DATETIME    NOT NULL,
    wait_time       TIME        NOT NULL,

    del             TINYINT(1)  NOT NULL DEFAULT 0
);

CREATE TABLE Seats
(
    id              BINARY(16)     NOT NULL PRIMARY KEY,
    train_number_id BINARY(16)     NOT NULL,
    coach           INT(10)        NOT NULL,
    seat_type       VARCHAR(10)    NOT NULL,
    price           DECIMAL(10, 2) NOT NULL,

    flags           BINARY(8)      NOT NULL,

    version         INT(10)        NOT NULL,
    del             TINYINT(1)     NOT NULL DEFAULT 0
);

CREATE Table Traveler
(
    id           BINARY(16)  NOT NULL PRIMARY KEY,
    bind_user_id BINARY(16)  NOT NULL COMMENT 'its for user to know the traveler he bind' ,
    name         VARCHAR(50) NOT NULL,
    id_card      VARCHAR(20) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,

    del          TINYINT(1)  NOT NULL DEFAULT 0
);