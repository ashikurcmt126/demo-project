package com.example.demoproject.dto.response;

import com.example.demoproject.entity.Location;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class UserLocationResponse {
    private Long userId;

    private LocalDateTime createdOn;
    private String email;
    private String firstName;
    private String secondName;

    private Location location;

}
