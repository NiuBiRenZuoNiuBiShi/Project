package com.setravel.swifttravel.entities.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
