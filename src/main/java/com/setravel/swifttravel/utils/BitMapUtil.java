package com.setravel.swifttravel.utils;

public class BitMapUtil {
    public static byte[] and(byte[] a, byte[] b) {
        checkLength(a, b);
        byte[] result = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = (byte) (a[i] & b[i]);
        }
        return result;
    }

    // 按位或（OR）返回新数组
    public static byte[] or(byte[] a, byte[] b) {
        checkLength(a, b);
        byte[] result = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = (byte) (a[i] | b[i]);
        }
        return result;
    }

    // 按位异或（XOR）返回新数组
    public static byte[] xor(byte[] a, byte[] b) {
        checkLength(a, b);
        byte[] result = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = (byte) (a[i] ^ b[i]);
        }
        return result;
    }

    // 按位非（NOT）返回新数组
    public static byte[] not(byte[] bitmap) {
        byte[] result = new byte[bitmap.length];
        for (int i = 0; i < bitmap.length; i++) {
            result[i] = (byte) ~bitmap[i];
        }
        return result;
    }

    // 设置某一位为 1 或 0，返回新数组
    public static byte[] setBit(byte[] bitmap, int bitIndex, boolean value) {
        byte[] result = bitmap.clone();
        int byteIndex = bitIndex / 8;
        int bitInByte = bitIndex % 8;
        if (value) {
            result[byteIndex] |= (byte) (1 << bitInByte);
        } else {
            result[byteIndex] &= (byte) ~(1 << bitInByte);
        }
        return result;
    }

    // 获取某一位的值（true 或 false）
    public static boolean getBit(byte[] bitmap, int bitIndex) {
        int byteIndex = bitIndex / 8;
        int bitInByte = bitIndex % 8;
        return (bitmap[byteIndex] & (1 << bitInByte)) != 0;
    }

    // 私有：检查长度一致性
    private static void checkLength(byte[] a, byte[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Bitmaps must be the same length");
        }
    }


    public static byte[] rangeBitsSet(int i, int j, int size) {
        if (i < 0 || j > 128 || i > j) {
            throw new IllegalArgumentException("Invalid bit range: i=" + i + ", j=" + j);
        }
        byte[] result = new byte[size]; // 16 bytes = 128 bits

        for (int bit = i; bit < j; bit++) {
            int byteIndex = bit / 8;
            int bitInByte = bit % 8;
            result[byteIndex] |= (byte) (1 << bitInByte);
        }

        return result;
    }
}
