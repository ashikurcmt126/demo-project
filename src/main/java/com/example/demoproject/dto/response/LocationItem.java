package com.example.demoproject.dto.response;


import com.example.demoproject.entity.Location;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LocationItem {
    private LocalDateTime createdOn;
    private Location location;
}
