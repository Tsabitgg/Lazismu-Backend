package com.ict.careus.repository;

import com.ict.careus.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);

    Boolean existsByUsername(String username);

    Boolean existsByPhoneNumber(String PhoneNumber);

    @Query(value = "SELECT COUNT(*) AS total_users FROM users INNER JOIN user_roles\n" +
            "ON users.id = user_roles.user_id WHERE user_roles.role_id != 1", nativeQuery = true)
    long getTotalUser();
}
