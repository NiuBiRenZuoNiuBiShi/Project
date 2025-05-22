package com.setravel.swifttravel.service;

import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.entities.Users;

/**
 * Service interface for user management operations within the Swift Travel
 * system. This interface defines methods for user authentication, registration,
 * profile management, and account security operations.
 *
 * @author ZhangYi
 * @version 1.0
 */
public interface UserService {

    /**
     * Registers a new user in the system. Creates a new user account with the
     * provided information after validation.
     *
     * @param user             The user entity containing registration details
     * @param verificationCode  The verification code sent to the user's email for
     *                          account verification
     * @return Result object indicating success or failure with appropriate message
     */
    Result userRegister(Users user, String verificationCode);

    /**
     * Authenticates a user and generates a JWT token for session management.
     * Validates credentials and creates a session token upon successful
     * authentication.
     *
     * @param username The username or email for login
     * @param password The user's password
     * @return Result containing the JWT token on success, or error details on
     *         failure
     */
    Result userLogin(String username, String password);

    /**
     * Logs out a user by invalidating their authentication token. This terminates
     * the user's current session.
     *
     * @param token The JWT token to invalidate
     * @return Result indicating success or failure of the logout operation
     */
    Result userLogout(String token);

    /**
     * Retrieves current user information based on the authenticated session. Uses
     * the token from the current security context to identify the user.
     *
     * @return Result containing the user information on success, or error details
     *         on failure
     */
    public Result getUserInfo();

    /**
     * Updates the current user's profile information. Modifies user details based
     * on the provided information.
     *
     * @param user The user entity containing updated information
     * @return Result indicating success or failure of the update operation
     */
    public Result updateUserInfo(Users user);

    /**
     * Soft deletes the current user's account from the system. Uses the token from
     * the current security context to identify the user.
     *
     * @return Result indicating success or failure of the delete operation
     */
    public Result deleteUser();

    /**
     * Initiates the password recovery process for a user. Typically sends a
     * recovery link or code to the user's registered email.
     *
     * @param username The username or email associated with the account
     * @param email    The email address for sending recovery instructions
     * @return Result indicating whether the recovery process was initiated
     *         successfully
     */
    Result forgetPasswordSendEmail(String username, String email);

    /**
     * Infact, this method is used to send email to the user for password recovery.
     * Then the real change password is in `changePasswordOutside` method.
     *
     * @param newPassword The new password to be set
     * @return Result indicating success or failure of the password change operation
     */
    Result changePasswordInside(String newPassword);

    /**
     * When a user is not logged in, this method allows them to change their
     * password
     *
     * @param username         The username or email associated with the account
     * @param newPassword      The new password to be set
     * @param verificationCode The verification code sent to the user's email for
     *                         password recovery
     * @return Result indicating success or failure of the password change operation
     */
    Result changePasswordOutside(String username, String newPassword, String verificationCode);

    /**
     * 发送邮箱验证码
     * 
     * @param email 邮箱地址
     * @return 发送结果
     */
    Result registerSendEmail(String email);
}