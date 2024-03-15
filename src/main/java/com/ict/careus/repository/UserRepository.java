package com.ict.careus.repository;

import com.ict.careus.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);

    Boolean existsByUsername(String username);

    Boolean existsByPhoneNumber(String PhoneNumber);
}
