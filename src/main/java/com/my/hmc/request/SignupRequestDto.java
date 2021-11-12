package com.my.hmc.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SignupRequestDto {

    private String username;
    private String password;
    private boolean isReviewer;
    private Set<String> languages = new HashSet<>();
}
