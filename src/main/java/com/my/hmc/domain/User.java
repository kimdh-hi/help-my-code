package com.my.hmc.domain;

import com.my.hmc.domain.etype.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User extends Timestamped {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private Integer reviewCount;

    @OneToMany(mappedBy = "user")
    private List<Language> languages = new ArrayList<>();

    public User(String username, String password, UserRole role, Integer reviewCount) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.reviewCount = reviewCount;
    }
}
