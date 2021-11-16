package com.my.hmc.controller;

import com.my.hmc.domain.etype.QuestionStatus;
import com.my.hmc.request.SigninRequestDto;
import com.my.hmc.request.SignupRequestDto;
import com.my.hmc.response.BasicResponseDto;
import com.my.hmc.response.ReviewListResponseDto;
import com.my.hmc.response.ReviewResponseDto;
import com.my.hmc.response.SigninResponseDto;
import com.my.hmc.security.UserDetailsImpl;
import com.my.hmc.security.UserDetailsServiceImpl;
import com.my.hmc.security.jwt.JwtUtils;
import com.my.hmc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/user/signup")
    public ResponseEntity<BasicResponseDto> signup(@RequestBody SignupRequestDto requestDto) {
        userService.saveUser(requestDto);
        BasicResponseDto responseDto = BasicResponseDto.builder()
                .result("success").httpStatus(HttpStatus.CREATED).message("회원가입에 성공했습니다.").build();

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/user/signin")
    public SigninResponseDto signin(@RequestBody SigninRequestDto requestDto) {

        return userService.signin(requestDto);
    }

    @Secured("ROLE_REVIEWER")
    @GetMapping("/user/language")
    public List<String> getMyLanguage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<String> data = new ArrayList<>();
        if (userDetails != null) {
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

            data =  userService.getMyLanguage(userDetails.getUser().getId());
        }
        return data;
    }

    @Secured({"ROLE_USER", "ROLE_REVIEWER"})
    @GetMapping("/user/reviews")
    public ReviewListResponseDto getMyReviewRequests(
            @RequestParam int page, @RequestParam int size,
            @RequestParam QuestionStatus status,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        --page;
        return userService.getMyReviewRequests(page, size, status, userDetails.getUser());
    }

    @Secured({"ROLE_USER", "ROLE_REVIEWER"})
    @GetMapping("/user/review")
    public ReviewResponseDto getMyReviewRequest(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam Long id) {
        ReviewResponseDto responseDto = new ReviewResponseDto();
        if (userDetails.getUser() != null) {
            responseDto = userService.getMyReviewRequest(userDetails.getUser(), id);
        }

        return responseDto;
    }

    @Secured({"ROLE_USER", "ROLE_REVIEWER"})
    @PostMapping("/user/logout")
    public BasicResponseDto logout() {
        SecurityContextHolder.clearContext();

        return new BasicResponseDto(null, "success", "로그아웃 완료", HttpStatus.OK);
    }
}
