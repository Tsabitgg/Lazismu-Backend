package com.ict.careus.repository;

import com.ict.careus.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByNoPhone(String noPhone);
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByNoPhone(String noPhone);
}
