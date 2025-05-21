package com.setravel.swifttravel.entities;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain=true)
public class Result {
    private int code;
    private String message;
    private Object data;

    public Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return this.code == 200;
    }

    public boolean isError() {
        return this.code != 200;
    }

    public static Result success() {
        return Result.builder()
                .code(200)
                .message("Success")
                .build();
    }

    public static Result success(Object data) {
        return Result.builder()
                .code(200)
                .message("Success")
                .data(data)
                .build();
    }

    public static Result success(String message, Object data) {
        return Result.builder()
                .code(200)
                .message(message)
                .data(data)
                .build();
    }

    public static Result error() {
        return Result.builder()
                .code(500)
                .message("Error")
                .build();
    }

    public static Result error(String message) {
        return Result.builder()
                .code(500)
                .message(message)
                .build();
    }

    public static Result error(String message, Object data) {
        return Result.builder()
                .code(500)
                .message(message)
                .data(data)
                .build();
    }
}
