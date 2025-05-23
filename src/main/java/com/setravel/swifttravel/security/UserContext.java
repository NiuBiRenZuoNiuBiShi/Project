package com.setravel.swifttravel.security;

import java.util.List;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.setravel.swifttravel.entities.Contacts;
import com.setravel.swifttravel.entities.Users;
import com.setravel.swifttravel.mapper.ContactMapper;
import com.setravel.swifttravel.mapper.UserMapper;

public class UserContext {
    private static final ThreadLocal<String> currentUsername = new ThreadLocal<>();

    public static void setUsername(String username) {
        currentUsername.set(username);
    }

    public static String getUsername() {
        return currentUsername.get();
    }

    public static void clear() {
        currentUsername.remove();
    }

    public static Users getCurrentUser(UserMapper userMapper) {
        String username = getUsername();
        if (username == null) {
            throw new RuntimeException("User not logged in");
        }

        Users user = userMapper
                .selectOne(new LambdaQueryWrapper<Users>().eq(Users::getUsername, username).eq(Users::getDel, false));

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return user;
    }

    public static byte[] getCurrentUserId(UserMapper userMapper) {
        String username = getUsername();
        if (username == null) {
            throw new RuntimeException("User not logged in");
        }
        Users user;
        try {
            user = userMapper.selectOne(new LambdaQueryWrapper<Users>().eq(Users::getUsername, username)
                    .eq(Users::getDel, false).select(Users::getId));
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve user ID: " + e.getMessage(), e);
        }
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user.getId();
    }

    public static List<Contacts> getCurrentUserContacts(UserMapper userMapper, ContactMapper contactMapper) {
        byte[] userId = getCurrentUserId(userMapper);
        if (userId == null) {
            throw new RuntimeException("User not logged in");
        }
        List<Contacts> contactsList;
        try {
            contactsList = contactMapper.selectList(new LambdaQueryWrapper<Contacts>()
                    .eq(Contacts::getUserId, userId).eq(Contacts::getDel, false));
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve contacts: " + e.getMessage(), e);
        }
        if (contactsList == null || contactsList.isEmpty()) {
            throw new RuntimeException("No contacts found");
        }
        return contactsList;
    }
}
