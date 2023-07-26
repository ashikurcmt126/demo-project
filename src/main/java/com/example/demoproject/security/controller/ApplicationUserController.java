package com.example.demoproject.security.controller;

import com.example.demoproject.security.entity.User;
import com.example.demoproject.security.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationUserController {
    @Autowired
    private UserService userService;

    @PostMapping("/createUser")
    public User saveUser(
            @Valid @RequestBody User user)
    {
        return userService.saveApplicationUser(user);
    }
}
