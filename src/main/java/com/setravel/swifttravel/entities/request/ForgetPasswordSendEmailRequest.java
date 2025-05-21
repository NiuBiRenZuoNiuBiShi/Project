package com.setravel.swifttravel.entities.request;

import lombok.Data;

@Data
public class ForgetPasswordSendEmailRequest {
    private String username;
    private String email;
}
