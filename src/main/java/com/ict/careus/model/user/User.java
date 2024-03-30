package com.ict.careus.model.user;

import java.util.Date;

import com.ict.careus.enumeration.ERole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "phoneNumber")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    private String phoneNumber;

    private String password;

    private String profilePicture;

    private String address;

    @Enumerated(EnumType.STRING)
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Role role;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    public User(String username, String phoneNumber, String password, String address, Role role) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.createdAt = new Date();
        this.address = address;
        this.role = role;
    }
}