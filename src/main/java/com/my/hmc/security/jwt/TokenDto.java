package com.my.hmc.security.jwt;

import com.my.hmc.domain.etype.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TokenDto {
    private String username;
}
