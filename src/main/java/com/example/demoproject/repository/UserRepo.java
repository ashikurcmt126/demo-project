package com.example.demoproject.repository;

import com.example.demoproject.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {
    boolean existsByEmail(String email);
}
