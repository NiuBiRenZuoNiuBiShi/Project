-- 酒店表
CREATE TABLE Hotel (
    id           BINARY(16)  NOT NULL PRIMARY KEY,
    name         VARCHAR(50) NOT NULL,
    address      VARCHAR(50) NOT NULL,
    rating       DECIMAL(2, 1),
    description  VARCHAR(255),
    pic_url      VARCHAR(255),

    del          TINYINT(1)  NOT NULL DEFAULT 0
);

-- 房间表
CREATE TABLE Room (
    id           BINARY(16)  NOT NULL PRIMARY KEY,
    hotel_id     BINARY(16)  NOT NULL,
    room_type    VARCHAR(50),
    price        DECIMAL(10, 2),
    room_date    DATE,
    availability BOOLEAN DEFAULT TRUE,

    del          TINYINT(1)  NOT NULL DEFAULT 0
);

-- 预订表
CREATE TABLE Reservation (
    id            BINARY(16)  NOT NULL PRIMARY KEY,
    user_id       BINARY(16),
    hotel_id      BINARY(16),
    room_id       BINARY(16),
    checkin_date  DATE,
    checkout_date DATE,
    status        VARCHAR(50) DEFAULT 'Booked',
    booking_time  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    del          TINYINT(1)  NOT NULL DEFAULT 0
);
