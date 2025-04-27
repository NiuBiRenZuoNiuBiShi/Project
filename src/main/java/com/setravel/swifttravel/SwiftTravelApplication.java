package com.setravel.swifttravel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.setravel.swifttravel.mapper")
public class SwiftTravelApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwiftTravelApplication.class, args);
    }

}
