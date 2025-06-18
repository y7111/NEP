package com.nep.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA512Util {
    /**
     * 使用SHA-512算法和手机号作为盐值进行加密
     * @param plaintext 待加密的明文
     * @param phoneNumber 手机号（用作盐值）
     * @return 加密后的十六进制字符串
     * @throws IllegalArgumentException 如果明文或手机号为空
     */
    public static String encrypt(String plaintext, String phoneNumber) {
        if (plaintext == null || phoneNumber == null) {
            throw new IllegalArgumentException("明文和手机号均不能为空");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");

            // 使用手机号作为盐值
            byte[] salt = phoneNumber.getBytes(StandardCharsets.UTF_8);
            digest.update(salt);

            // 计算哈希值
            byte[] hashBytes = digest.digest(plaintext.getBytes(StandardCharsets.UTF_8));

            // 转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            // 理论上不会发生，因为SHA-512是Java标准算法
            throw new RuntimeException("不支持SHA-512算法", e);
        }
    }
}