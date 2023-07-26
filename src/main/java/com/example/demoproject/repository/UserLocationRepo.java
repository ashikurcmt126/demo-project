package com.example.demoproject.repository;

import com.example.demoproject.entity.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserLocationRepo extends JpaRepository<UserLocation, Long> {
    @Query("SELECT ul FROM UserLocation ul WHERE ul.userId = ?1 AND ul.createdOn = (SELECT MAX(ul2.createdOn) FROM UserLocation ul2 WHERE ul2.userId = ?1)")
    UserLocation findLatestByUserId(Long userId);

    @Query("SELECT ul FROM UserLocation ul WHERE ul.userId = ?1 AND ul.createdOn BETWEEN ?2 AND ?3")
    List<UserLocation> findUserLocationsByDateTimeRange(Long userId, LocalDateTime startTime, LocalDateTime endTime);
}
