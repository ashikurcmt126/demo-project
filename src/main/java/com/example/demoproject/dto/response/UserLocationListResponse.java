package com.example.demoproject.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class UserLocationListResponse {
    private Long userId;
    private List<LocationItem> locations;
}
