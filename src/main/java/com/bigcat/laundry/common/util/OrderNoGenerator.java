package com.bigcat.laundry.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * 订单编号生成工具类
 */
public class OrderNoGenerator {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final Random RANDOM = new Random();

    /**
     * 生成订单编号
     *
     * @return 订单编号
     */
    public static String generate() {
        // 格式：年月日时分秒 + 4位随机数
        return LocalDateTime.now().format(FORMATTER) + String.format("%04d", RANDOM.nextInt(10000));
    }

    /**
     * 生成取衣码
     *
     * @return 取衣码
     */
    public static String generatePickupCode() {
        // 生成6位数字取衣码
        return String.format("%06d", RANDOM.nextInt(1000000));
    }
} 