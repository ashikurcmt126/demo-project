package com.example.demoproject.service.impl;

import com.example.demoproject.dto.response.LocationItem;
import com.example.demoproject.dto.response.UserLocationListResponse;
import com.example.demoproject.dto.response.UserLocationResponse;
import com.example.demoproject.entity.*;
import com.example.demoproject.exception.BadRequestException;
import com.example.demoproject.exception.ResourceNotFoundException;
import com.example.demoproject.repository.UserLocationRepo;
import com.example.demoproject.repository.UserRepo;
import com.example.demoproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserLocationRepo userLocationRepo;
    @Autowired
    private UserRepo userRepo;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserLocation saveUserLocation(UserLocation userLocation) {
        if (userLocation == null || userLocation.getUserId() == null || userLocation.getLocation() == null) {
            throw new BadRequestException("Invalid user location data.");
        }
        logger.info("saveUserLocation called with userLocation={}", userLocation);

        UserLocation savedUserLocation = userLocationRepo.save(userLocation);
        logger.info("User location saved successfully. userLocation={}", savedUserLocation);

        return savedUserLocation;
    }

    @Override
    public Users saveUser(Users user) {
        if (user == null || user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new BadRequestException("Invalid user data.");
        }
        boolean emailExists = userRepo.existsByEmail(user.getEmail());
        if (emailExists) {
            throw new BadRequestException("Email already exists. Please use a different email.");
        }

        logger.info("saveUser called with user={}", user);
        Users savedUser = userRepo.save(user);

        logger.info("User saved successfully. user={}", savedUser);
        return savedUser;
    }

    @Override
    public Users updateUser(Users user, Long userId) {
        if (user == null || user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new BadRequestException("Invalid user data.");
        }

        Users existingUser = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No user found for the specific userId."));

        logger.info("updateUser called with userId={} and updated user={}", userId, user);

        existingUser.setEmail(user.getEmail());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setSecondName(user.getSecondName());

        Users updatedUser = userRepo.save(existingUser);

        // Logging: Log successful user update
        logger.info("User with userId={} updated successfully. updatedUser={}", userId, updatedUser);

        return updatedUser;
    }

    @Override
    public ResponseEntity<Object> getUserData(Long userId) {

        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No user found for the specific userId."));

        logger.info("getUserData called with userId={}", userId);

        UserLocation latestLocation = getLatestUserLocation(userId);

        if (latestLocation == null) {
            logger.warn("No latest location found for userId={}", userId);
            return ResponseEntity.ok(user);
        } else {
            UserLocationResponse response = new UserLocationResponse();
            response.setUserId(user.getUserId());
            response.setEmail(user.getEmail());
            response.setFirstName(user.getFirstName());
            response.setSecondName(user.getSecondName());
            response.setCreatedOn(user.getCreatedOn());
            response.setLocation(latestLocation.getLocation());

            logger.info("Successfully retrieved user data for userId={}", userId);

            return ResponseEntity.ok(response);
        }
    }

    @Override
    public UserLocation getLatestUserLocation(Long userId) {
        if (userId == null) {
            throw new BadRequestException("User ID cannot be null.");
        }
        logger.info("getLatestUserLocation called with userId={}", userId);

        UserLocation latestLocation = userLocationRepo.findLatestByUserId(userId);

        if (latestLocation == null) {
            logger.warn("No latest location found for userId={}", userId);
        }

        logger.info("Successfully retrieved latest user location for userId={}. LatestLocation={}", userId, latestLocation);

        return latestLocation;
    }

    @Override
    public ResponseEntity<UserLocationListResponse> getUserLocationsByDateTimeRange(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        if (userId == null) {
            throw new BadRequestException("User ID cannot be null.");
        }

        // Validation: Check if startTime is before endTime
        if (startTime.isAfter(endTime)) {
            throw new BadRequestException("startTime must be before endTime.");
        }
        logger.info("getUserLocationsByDateTimeRange called with userId={}, startTime={}, endTime={}", userId, startTime, endTime);

        List<UserLocation> userLocations = userLocationRepo.findUserLocationsByDateTimeRange(userId, startTime, endTime);
        if (userLocations.isEmpty()) {
            logger.warn("No user locations found for the specified criteria. userId={}, startTime={}, endTime={}", userId, startTime, endTime);

            throw new ResourceNotFoundException("No user locations found for the specified criteria.");
        }

        // Create the response with user locations
        UserLocationListResponse response = new UserLocationListResponse();
        response.setUserId(userId);
        List<LocationItem> locationItems = userLocations.stream()
                .map(userLocation -> {
                    LocationItem locationItem = new LocationItem();
                    locationItem.setCreatedOn(userLocation.getCreatedOn());
                    locationItem.setLocation(userLocation.getLocation());
                    return locationItem;
                })
                .collect(Collectors.toList());
        response.setLocations(locationItems);

        logger.info("Successfully retrieved user locations for userId={} within the specified criteria. Response={}", userId, response);

        return ResponseEntity.ok(response);
    }
}
