package com.gametech.platform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gametech.platform.modules.**.mapper") // 扫描所有模块的 mapper
public class GameTechPlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(GameTechPlatformApplication.class, args);
    }
}