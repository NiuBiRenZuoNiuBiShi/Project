package com.setravel.swifttravel.utils;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UUIDUtil {
    /**
     * 生成标准 UUID 字符串（带短横线），例如：550e8400-e29b-41d4-a716-446655440000
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成无短横线的 UUID 字符串，例如：550e8400e29b41d4a716446655440000
     */
    public static String generateUUIDWithoutDash() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成 16 字节的 UUID（128 bit），适合用作二进制存储
     */
    public static byte[] generateUUIDBytes() {
        UUID uuid = UUID.randomUUID();
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return buffer.array();
    }

    /**
     * UUID → byte[]（写入数据库）
     */
    public static byte[] uuidToBytes(UUID uuid) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return buffer.array();
    }

    /**
     * byte[] → UUID（从数据库读取）
     */
    public static UUID bytesToUUID(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return new UUID(buffer.getLong(), buffer.getLong());
    }
}
