package com.alee.gera.util;

import java.security.SecureRandom;
import java.util.Base64;

public class TokenGenerator {

    private static final SecureRandom secureRandom = new SecureRandom(); // 线程安全的随机数生成器
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); // URL安全的Base64编码器

    public static String generateToken() {
        long timestamp = System.currentTimeMillis(); // 获取当前时间戳
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        String token = base64Encoder.encodeToString(randomBytes);
        return token + "-" + timestamp; // 将时间戳拼接到Token后面
    }
}
