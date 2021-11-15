package com.my.hmc.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SigninResponseDto {
    private String token;
    private String authority;
    private HttpStatus httpStatus;
    private String message;
}
