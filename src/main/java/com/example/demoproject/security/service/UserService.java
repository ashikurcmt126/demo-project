package com.example.demoproject.security.service;

import com.example.demoproject.security.entity.User;

public interface UserService {
    User saveApplicationUser(User user);
    User getUserByUsername(String username);
}
