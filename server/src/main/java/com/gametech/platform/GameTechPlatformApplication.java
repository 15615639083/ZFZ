package com.gametech.platform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({
        "com.gametech.platform.modules.user.mapper",
        "com.gametech.platform.modules.engineer.mapper",
        "com.gametech.platform.modules.resource.mapper",
        "com.gametech.platform.modules.serviceorder.mapper"
})
public class GameTechPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameTechPlatformApplication.class, args);
    }
}
