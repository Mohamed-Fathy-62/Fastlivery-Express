package com.Fastlivery_Express.user.repository;

import com.Fastlivery_Express.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.Fastlivery_Express.user.entity.Driver;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByMobileNumber(String mobileNumber);
    Optional<User> findByUserId(String keycloakId);

    @Query("select d from Driver d where d.isAvailable = true and d.currentLatitude is not null and d.currentLongitude is not null")
    List<Driver> findAvailableDriversWithLocation();

}
