package com.example.demoproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users") // Specify the table name explicitly
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private LocalDateTime createdOn;
    private String email;
    private String firstName;
    private String secondName;

    @PrePersist
    public void prePersist() {
        createdOn = LocalDateTime.now();
    }
}
