package com.my.hmc.controller;

import com.my.hmc.request.SigninRequestDto;
import com.my.hmc.request.SignupRequestDto;
import com.my.hmc.security.UserDetailsImpl;
import com.my.hmc.security.UserDetailsServiceImpl;
import com.my.hmc.security.jwt.JwtUtils;
import com.my.hmc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/user/signup")
    public void signup(@RequestBody SignupRequestDto requestDto) {
        userService.saveUser(requestDto);
    }

    @PostMapping("/user/signin")
    public String signin(@RequestBody SigninRequestDto requestDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("로그인에 실패했습니다.");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(requestDto.getUsername());
        String token = jwtUtils.createToken(userDetails.getUsername());

        return token;
    }

    @Secured("ROLE_REVIEWER")
    @GetMapping("/user/language")
    public List<String> getMyLanguage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<String> data = new ArrayList<>();
        if (userDetails != null) {
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

            for (GrantedAuthority authority : authorities) {
                log.info("language 권한: {}", authority.getAuthority());
            }

            data =  userService.getMyLanguage(userDetails.getUser().getId());
        }
        return data;
    }

    @Secured("ROLE_USER")
    @GetMapping("/user/review")
    public void getMyReviewRequests(@AuthenticationPrincipal UserDetailsImpl userDetails) {

    }
}
