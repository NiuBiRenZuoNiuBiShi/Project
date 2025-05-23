package com.setravel.swifttravel.service.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.entities.Users;
import com.setravel.swifttravel.entities.DTO.MailDTO;
import com.setravel.swifttravel.entities.output.LoginOutput;
import com.setravel.swifttravel.mapper.UserMapper;
import com.setravel.swifttravel.security.UserContext;
import com.setravel.swifttravel.service.UserService;
import com.setravel.swifttravel.utils.JWTUtils;
import com.setravel.swifttravel.utils.UUIDUtil;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * Implementation of UserService interface that handles user-related operations
 * such as registration and authentication.
 * <p>
 * This service provides methods to register new users and authenticate existing
 * ones. It uses BCrypt password encoding for secure password storage and JWT
 * for token generation upon successful authentication.
 *
 * @author ZhangYi
 * @version 1.0
 * @see com.setravel.swifttravel.service.UserService
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    // Implement the methods from UserService interface

    @Resource
    private UserMapper userMapper;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Resource
    private JWTUtils jwtUtils;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private MailServiceImpl mailService;

    // Regular expressions for validation
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final String PHONE_REGEX = "^1[3-9]\\d{9}$"; // Chinese phone number pattern
    private static final String ID_CARD_REGEX = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9._-]{3,}$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
    // pattern

    @Override
    @Transactional
    public Result userRegister(Users user, String verificationCode) {
        // Input validation
        Result validationResult = validateUserInput(user);
        if (validationResult != null) {
            return validationResult;
        }

        // 验证邮箱必须存在
        if (!StringUtils.hasText(user.getEmail())) {
            return Result.error("邮箱不能为空");
        }

        // 验证码必须存在
        if (!StringUtils.hasText(verificationCode)) {
            return Result.error("验证码不能为空");
        }

        // 验证邮箱验证码
        String storedCode = redisTemplate.opsForValue().get("email_verification:" + user.getEmail());
        if (storedCode == null) {
            return Result.error("验证码已过期或不存在，请重新获取");
        }

        if (!storedCode.equals(verificationCode)) {
            return Result.error("验证码不正确");
        }

        // Check if username already exists
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<Users>()
                .eq(Users::getUsername, user.getUsername()).eq(Users::getDel, false);
        Users existingUser = userMapper.selectOne(queryWrapper);
        if (existingUser != null) {
            return Result.error("Username already exists");
        }

        // 检查邮箱是否已被注册
        // ! beacause we are in Dev mode, so we don't need to check the email
        // LambdaQueryWrapper<Users> emailQuery = new
        // LambdaQueryWrapper<Users>().eq(Users::getEmail, user.getEmail())
        // .eq(Users::getDel, false);
        // Users emailUser = userMapper.selectOne(emailQuery);
        // if (emailUser != null) {
        // return Result.error("该邮箱已被注册");
        // }

        try {
            // Password hashing
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            // Set creation time
            user.setCreatedAt(LocalDateTime.now());
            // Set default deletion status
            user.setDel(false);
            user.setId(UUIDUtil.generateUUIDBytes());
            // Insert user into database
            int rows = userMapper.insert(user);

            // 清除验证码
            redisTemplate.delete("email_verification:" + user.getEmail());

            if (rows > 0) {
                return Result.success("User registered successfully");
            } else {
                return Result.error("Failed to register user");
            }
        } catch (Exception e) {
            log.error("Error during user registration: {}", e.getMessage(), e);
            return Result.error("An error occurred during registration");
        }
    }

    @Override
    public Result userLogin(String username, String password) {
        // Input validation
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            return Result.error("Username or password cannot be empty");
        }

        try {
            // Query user
            LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<Users>().eq(Users::getUsername, username)
                    .eq(Users::getDel, false);
            Users user = userMapper.selectOne(queryWrapper);

            if (user == null) {
                return Result.error("User does not exist or has been deleted");
            }

            // Verify password
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return Result.error("Invalid password");
            }

            // Generate JWT token
            String token = jwtUtils.generateToken(username);
            return Result.success(new LoginOutput().setUser(user).setToken(token));
        } catch (Exception e) {
            log.error("Error during user login: {}", e.getMessage(), e);
            return Result.error("An error occurred during login");
        }
    }

    @Override
    public Result userLogout(String token) {
        if (!StringUtils.hasText(token)) {
            return Result.error("Token cannot be empty");
        }

        try {
            // Validate token (still needed to ensure we're invalidating a valid token)
            if (!jwtUtils.verifyToken(token)) {
                return Result.error("Invalid token");
            }

            // Add token to blacklist to invalidate it
            jwtUtils.addTokenToBlockList(token);

            return Result.success("User logged out successfully");
        } catch (Exception e) {
            log.error("Error during user logout: {}", e.getMessage(), e);
            return Result.error("An error occurred during logout");
        }
    }

    @Override
    public Result getUserInfo() {
        try {
            // Get username from context instead of token
            String username = UserContext.getUsername();

            if (!StringUtils.hasText(username)) {
                return Result.error("User not authenticated");
            }

            // Fetch user info from the database
            LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<Users>().eq(Users::getUsername, username)
                    .eq(Users::getDel, false);
            Users user = userMapper.selectOne(queryWrapper);

            if (user == null) {
                return Result.error("User does not exist or has been deleted");
            }

            // Clear sensitive information before returning
            user.setPassword(null);
            return Result.success(user);
        } catch (Exception e) {
            log.error("Error fetching user info: {}", e.getMessage(), e);
            return Result.error("An error occurred while fetching user information");
        }
    }

    @Override
    @Transactional
    public Result updateUserInfo(Users user) {
        try {
            // Get username from context instead of token
            String username = UserContext.getUsername();

            if (!StringUtils.hasText(username)) {
                return Result.error("User not authenticated");
            }

            // Validate updated user info
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                if (!Pattern.matches(EMAIL_REGEX, user.getEmail())) {
                    return Result.error("Invalid email format");
                }
            }

            if (user.getPhone() != null && !user.getPhone().isEmpty()) {
                if (!Pattern.matches(PHONE_REGEX, user.getPhone())) {
                    return Result.error("Invalid phone number format");
                }
            }

            if (user.getIdCard() != null && !user.getIdCard().isEmpty()) {
                if (!Pattern.matches(ID_CARD_REGEX, user.getIdCard())) {
                    return Result.error("Invalid ID card format");
                }
            }

            // Prevent updating critical fields
            user.setUsername(null); // Cannot change username
            user.setPassword(null); // Password should be updated via a separate method
            user.setDel(null); // Deletion status should not be changed here
            user.setCreatedAt(null); // Creation time should not be modified

            // Update user info in the database
            LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<Users>().eq(Users::getUsername, username)
                    .eq(Users::getDel, false);

            int rows = userMapper.update(user, queryWrapper);
            if (rows > 0) {
                return Result.success("User info updated successfully");
            } else {
                return Result.error("User does not exist or no changes were made");
            }
        } catch (Exception e) {
            log.error("Error updating user info: {}", e.getMessage(), e);
            return Result.error("An error occurred while updating user information");
        }
    }

    @Override
    @Transactional
    public Result deleteUser() {
        try {
            // Get username from context instead of token
            String username = UserContext.getUsername();

            if (!StringUtils.hasText(username)) {
                return Result.error("User not authenticated");
            }

            // Mark user as deleted in the database (soft delete)
            LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<Users>().eq(Users::getUsername, username)
                    .eq(Users::getDel, false);

            Users userToUpdate = new Users();
            userToUpdate.setDel(true);

            int rows = userMapper.update(userToUpdate, queryWrapper);

            if (rows > 0) {
                return Result.success("User deleted successfully");
            } else {
                return Result.error("User does not exist or has already been deleted");
            }
        } catch (Exception e) {
            log.error("Error deleting user: {}", e.getMessage(), e);
            return Result.error("An error occurred while deleting the user");
        }
    }

    /**
     * Validates user input during registration
     * 
     * @param user The user to validate
     * @return Result with error message if validation fails, null if validation
     *         passes
     */
    private Result validateUserInput(Users user) {
        // Check for null user
        if (user == null) {
            return Result.error("User information cannot be null");
        }

        // Check username
        if (!StringUtils.hasText(user.getUsername())) {
            return Result.error("Username cannot be empty");
        }

        if (StringUtils.hasText(user.getUsername()) && !Pattern.matches(USERNAME_REGEX, user.getUsername())) {
            return Result.error("Username must be between 3 and 20 characters");
        }

        // Check password
        if (!StringUtils.hasText(user.getPassword())) {
            return Result.error("Password cannot be empty");
        }

        if (!Pattern.matches(PASSWORD_REGEX, user.getPassword())) {
            return Result.error("Password must be at least 8 characters long");
        }

        // Validate email if provided
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            if (!Pattern.matches(EMAIL_REGEX, user.getEmail())) {
                return Result.error("Invalid email format");
            }
        }

        // Validate phone if provided
        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
            if (!Pattern.matches(PHONE_REGEX, user.getPhone())) {
                return Result.error("Invalid phone number format");
            }
        }

        // Validate ID card if provided
        if (user.getIdCard() != null && !user.getIdCard().isEmpty()) {
            if (!Pattern.matches(ID_CARD_REGEX, user.getIdCard())) {
                return Result.error("Invalid ID card format");
            }
        }

        return null; // Validation passed
    }

    @Override
    public Result forgetPasswordSendEmail(String username, String email) {

        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<Users>().eq(Users::getUsername, username)
                .eq(Users::getEmail, email).eq(Users::getDel, false);
        try {
            Users user = userMapper.selectOne(queryWrapper);
            if (user == null) {
                return Result.error("User does not exist or email does not match");
            }
        } catch (Exception e) {
            log.error("Error during password reset: {}", e.getMessage(), e);
            return Result.error("An error occurred while resetting the password");
        }

        String validationCode = String.format("%06d", (int) (Math.random() * 1000000));

        // Send the validation code to the user's email
        MailDTO mailDTO = new MailDTO();
        mailDTO.setTo(new String[] { email });
        mailDTO.setSubject("Swift Travel Validation Code");
        mailDTO.setText("Your validation code is: " + validationCode + ".");

        redisTemplate.opsForValue().set("email_verification:" + email, validationCode, 5,
                java.util.concurrent.TimeUnit.MINUTES);

        return mailService.sendVerificationEmail(mailDTO);
    }

    @Override
    public Result changePasswordInside(String newPassword) {
        String username = UserContext.getUsername();

        if (!StringUtils.hasText(username)) {
            return Result.error("User not authenticated");
        }
        LambdaUpdateWrapper<Users> updateWrapper = new LambdaUpdateWrapper<Users>().eq(Users::getUsername, username)
                .eq(Users::getDel, false).set(Users::getPassword, newPassword);
        try {
            userMapper.update(null, updateWrapper);
        } catch (Exception e) {
            log.error("Error during password change: {}", e.getMessage(), e);
            return Result.error("An error occurred while changing the password, Sorry");
        }
        return Result.success("Password changed successfully");
    }

    @Override
    public Result changePasswordOutside(String username, String newPassword, String verificationCode) {
        // Validate the verification code
        String cachedCode = redisTemplate.opsForValue().get("email_verification:" + username);
        if (!verificationCode.equals(cachedCode)) {
            return Result.error("Invalid or expired verification code, please try again");
        }

        // Change the password
        LambdaUpdateWrapper<Users> updateWrapper = new LambdaUpdateWrapper<Users>().eq(Users::getUsername, username)
                .eq(Users::getDel, false).set(Users::getPassword, newPassword);
        try {
            userMapper.update(null, updateWrapper);
        } catch (Exception e) {
            log.error("Error during password change: {}", e.getMessage(), e);
            return Result.error("An error occurred while changing the password");
        }
        return Result.success("Password changed successfully");
    }

    @Override
    public Result registerSendEmail(String email) {
        System.out.println(email);
        // 验证邮箱格式
        if (!StringUtils.hasText(email) || !Pattern.matches(EMAIL_REGEX, email)) {
            return Result.error("请提供有效的邮箱地址");
        }

        try {
            // ! this check will add in the future
            // 检查邮箱是否已被注册
            // LambdaQueryWrapper<Users> queryWrapper = new
            // LambdaQueryWrapper<Users>().eq(Users::getEmail, email)
            // .eq(Users::getDel, false);
            // Users existingUser = userMapper.selectOne(queryWrapper);
            // if (existingUser != null) {
            // return Result.error("该邮箱已被注册");
            // }

            // 生成6位数字验证码
            String verificationCode = String.format("%06d", (int) (Math.random() * 1000000));

            // 设置验证码有效期（5分钟）
            redisTemplate.opsForValue().set("email_verification:" + email, verificationCode, 5,
                    java.util.concurrent.TimeUnit.MINUTES);

            // 发送验证码邮件
            MailDTO mailDTO = new MailDTO();
            mailDTO.setTo(new String[] { email });
            mailDTO.setSubject("Swift Travel 注册验证码");
            mailDTO.setText("您的注册验证码是：" + verificationCode + ",有效期5分钟。");

            // 使用简单邮件服务发送
            return mailService.sendVerificationEmail(mailDTO);

        } catch (Exception e) {
            log.error("发送邮箱验证码失败: {}", e.getMessage(), e);
            return Result.error("发送验证码失败，请稍后重试");
        }
    }
}