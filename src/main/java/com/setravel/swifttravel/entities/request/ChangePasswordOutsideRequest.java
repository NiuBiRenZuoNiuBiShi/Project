package com.setravel.swifttravel.entities.request;

import lombok.Data;

@Data
public class ChangePasswordOutsideRequest {
    private String username;
    private String newPassword;
    private String verificationCode;
}
