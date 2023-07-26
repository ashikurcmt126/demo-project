package com.example.demoproject.controller;

import com.example.demoproject.entity.Users;
import com.example.demoproject.entity.UserLocation;
import com.example.demoproject.dto.response.UserLocationListResponse;
import com.example.demoproject.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    // Save operation
    @PostMapping("/send-location")

    public UserLocation saveDepartment(
            @Valid @RequestBody UserLocation userLocation)
    {
        return userService.saveUserLocation(userLocation);
    }

    @PostMapping("/create-user")
    public Users saveUser(
            @Valid @RequestBody Users user)
    {
        return userService.saveUser(user);
    }

    // Update operation
    @PutMapping("/update-user/{id}")

    public Users
    updateUser(@RequestBody Users user,
                     @PathVariable("id") Long userId)
    {
        return userService.updateUser(
                user, userId);
    }

    @GetMapping("/getUserData/{userId}")
    public ResponseEntity<Object> getUserData(@PathVariable Long userId) {
        return userService.getUserData(userId);
    }

    @GetMapping("/getUserLocationsByDateTimeRange")
    public ResponseEntity<UserLocationListResponse> getUserLocationsByDateTimeRange(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime
    ){
        return userService.getUserLocationsByDateTimeRange(userId, startTime, endTime);
    }


}
