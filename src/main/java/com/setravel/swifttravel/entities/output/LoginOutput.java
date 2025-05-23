package com.setravel.swifttravel.entities.output;

import com.setravel.swifttravel.entities.Users;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginOutput {
    private Users user;
    private String token;
}
