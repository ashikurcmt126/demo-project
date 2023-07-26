package com.example.demoproject.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Location {
    private double latitude;
    private double longitude;
}
