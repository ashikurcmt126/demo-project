package com.example.demoproject.security.service.impl;

import com.example.demoproject.exception.BadRequestException;
import com.example.demoproject.security.entity.User;
import com.example.demoproject.security.repository.ApplicationUserRepo;
import com.example.demoproject.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserServiceImpl implements UserService {
    @Autowired
    private ApplicationUserRepo userRepo;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Override
    public User saveApplicationUser(User user) {
        String email = user.getEmail();
        if (userRepo.existsByEmail(email)) {
            throw new BadRequestException("Email already exists.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepo.findUserByUsername(username);
    }
}