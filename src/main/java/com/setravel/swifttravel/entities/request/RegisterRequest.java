package com.setravel.swifttravel.entities.request;

import com.setravel.swifttravel.entities.Users;

import lombok.Data;

@Data
public class RegisterRequest {
    private Users user;
    private String verificationCode;
}
