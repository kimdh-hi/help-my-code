package com.my.hmc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Language {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    String name;

    @JsonIgnore
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Language(String name) {
        this.name = name;
    }

    public void setUser(User user) {
        this.user = user;
        this.user.getLanguages().add(this);
    }
}
