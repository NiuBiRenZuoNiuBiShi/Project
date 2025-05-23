package com.setravel.swifttravel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
//@MapperScan("com.setravel.swifttravel.mapper")
public class SwiftTravelApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwiftTravelApplication.class, args);
    }

}
