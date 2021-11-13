package com.my.hmc.request;

import lombok.*;
import org.springframework.security.access.annotation.Secured;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter()
@Builder
public class SignupRequestDto {

    private String username;
    private String password;
    private boolean reviewer;
    private Set<String> languages = new HashSet<>();
}
