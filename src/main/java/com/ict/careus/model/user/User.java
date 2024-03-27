package com.ict.careus.model.user;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.ict.careus.model.user.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "phoneNumber")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    private String username;

    private String phoneNumber;

    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(  name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    public User() {
    }

    public User(String username, String phoneNumber, String password) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.createdAt = new Date();
    }
}