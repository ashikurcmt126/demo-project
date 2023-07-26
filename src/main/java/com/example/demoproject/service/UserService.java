package com.example.demoproject.service;

import com.example.demoproject.entity.Users;
import com.example.demoproject.entity.UserLocation;
import com.example.demoproject.dto.response.UserLocationListResponse;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public interface UserService {
    UserLocation saveUserLocation(UserLocation userLocation);
    Users saveUser(Users user);
    Users updateUser(Users user, Long userId);

    ResponseEntity<Object> getUserData(Long userId);
    UserLocation getLatestUserLocation(Long userId);

    ResponseEntity<UserLocationListResponse> getUserLocationsByDateTimeRange(Long userId, LocalDateTime startTime, LocalDateTime endTime);

}
