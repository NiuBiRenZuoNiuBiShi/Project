CREATE TABLE Users
(
    id         BINARY(16)   NOT NULL PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL,
    password   VARCHAR(255) NOT NULL,
    id_card     VARCHAR(20) NOT NULL,
    phone      VARCHAR(20) NOT NULL,
    email      VARCHAR(100) NOT NULL,

    created_at TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,

    del        TINYINT(1) DEFAULT 0,
    UNIQUE (username),
    UNIQUE (email)
) COMMENT '用户表';