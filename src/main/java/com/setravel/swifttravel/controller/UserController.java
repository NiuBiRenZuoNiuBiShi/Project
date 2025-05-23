package com.setravel.swifttravel.controller;

import org.springframework.web.bind.annotation.RestController;

import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.entities.Users;
import com.setravel.swifttravel.entities.request.ChangePasswordOutsideRequest;
import com.setravel.swifttravel.entities.request.ForgetPasswordSendEmailRequest;
import com.setravel.swifttravel.entities.request.LoginRequest;
import com.setravel.swifttravel.entities.request.RegisterRequest;
import com.setravel.swifttravel.service.UserService;

import jakarta.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author ZhangYi
 * @version 1.0
 * @description UserController handles user-related operations such as
 *              registration, login, and logout.
 */
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/api/user/register")
    public Result userRegister(@RequestBody RegisterRequest registerRequest) {
        return userService.userRegister(registerRequest.getUser(), registerRequest.getVerificationCode());
    }

    @PostMapping("/api/user/register/sendEmail")
    public Result userRegisterSendEmail(@RequestBody String email) {
        return userService.registerSendEmail(email);
    }

    @PostMapping("/api/user/login")
    public Result userLogin(@RequestBody LoginRequest loginRequest) {
        return userService.userLogin(loginRequest.getUserName(), loginRequest.getPassword());
    }

    @PostMapping("/api/user/logout")
    public Result userLogout(@RequestBody String token) {
        return userService.userLogout(token);
    }

    @GetMapping("/api/user/info")
    public Result getUserInfo() {
        return userService.getUserInfo();
    }

    @PostMapping("/api/user/update")
    public Result updateUserInfo(@RequestBody Users user) {
        return userService.updateUserInfo(user);
    }

    @PostMapping("/api/user/delete")
    public Result deleteUser() {
        return userService.deleteUser();
    }

    @PostMapping("/api/user/forgetPassword/sendEmail")
    public Result forgetPasswordSendEmail(@RequestBody ForgetPasswordSendEmailRequest request) {
        return userService.forgetPasswordSendEmail(request.getUsername(), request.getEmail());
    }

    @PostMapping("/api/user/changePassword/inside")
    public Result changePasswordInside(@RequestBody String password) {
        return userService.changePasswordInside(password);
    }

    @PostMapping("/api/user/changePassword/outside")
    public Result changePasswordOutside(@RequestBody ChangePasswordOutsideRequest request) {
        return userService.changePasswordOutside(request.getUsername(), request.getNewPassword(),
                request.getVerificationCode());
    }
}