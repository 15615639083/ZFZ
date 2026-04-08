package com.gametech.platform.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public final class IdGenerator {

    private static final DateTimeFormatter ORDER_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private IdGenerator() {
    }

    public static String orderNo(String prefix) {
        return prefix + ORDER_TIME_FORMATTER.format(LocalDateTime.now()) + randomSuffix();
    }

    public static String token(String prefix) {
        return prefix + UUID.randomUUID().toString().replace("-", "");
    }

    private static String randomSuffix() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
    }
}
