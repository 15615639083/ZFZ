package com.gametech.platform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gametech.platform.modules")
public class GameTechPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameTechPlatformApplication.class, args);
    }
}
