package com.my.hmc.testdata;

import com.my.hmc.domain.Language;
import com.my.hmc.domain.User;
import com.my.hmc.domain.etype.UserRole;
import com.my.hmc.request.ReviewAddRequestDto;
import com.my.hmc.request.SignupRequestDto;
import com.my.hmc.service.ReviewRequestService;
import com.my.hmc.service.ReviewerService;
import com.my.hmc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class TestDataInit implements ApplicationRunner {

    @Autowired
    UserService userService;

    @Autowired
    ReviewerService reviewerService;

    @Autowired
    ReviewRequestService reviewRequestService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User testUser1 = createUser("testUser1", "1234");
        User testUser2 = createUser("testUser2", "1234");
        User reviewer1 = createReviewer("JavaGod", "1234", "Java");
        User reviewer2 = createReviewer("PythonGod", "1234", "Python");

        createReview("요청제목1", "리뷰코드1", "도와주세요ㅠ", "Java", testUser1, reviewer1);
        createReview("요청제목2", "리뷰코드2", "도와주세요ㅠ", "Java", testUser1, reviewer1);
        createReview("요청제목3", "리뷰코드3", "도와주세요ㅠ", "Java", testUser1, reviewer1);
        createReview("요청제목4", "리뷰코드4", "도와주세요ㅠ", "Java", testUser1, reviewer1);
        createReview("요청제목5", "리뷰코드5", "도와주세요ㅠ", "Java", testUser1, reviewer1);
        createReview("요청제목6", "리뷰코드6", "도와주세요ㅠ", "Java", testUser1, reviewer1);
        createReview("요청제목7", "리뷰코드7", "도와주세요ㅠ", "Java", testUser1, reviewer1);

    }

    private User createUser(String username, String password) {

        SignupRequestDto dto = SignupRequestDto.builder()
                .username(username)
                .password(password)
                .reviewer(false)
                .languages(Collections.emptySet())
                .build();

        return userService.saveUser(dto);
    };

    private User createReviewer(String username, String password, String... languages) {
        SignupRequestDto dto = SignupRequestDto.builder()
                .username(username)
                .password(password)
                .reviewer(true)
                .languages(Arrays.stream(languages).collect(Collectors.toSet()))
                .build();

        return userService.saveUser(dto);
    }

    private void createReview(String title, String code, String comment, String language, User user, User reviewer) {
        ReviewAddRequestDto dto = ReviewAddRequestDto.builder()
                .title(title)
                .code(code)
                .comment(comment)
                .language(language)
                .reviewerId(reviewer.getId()).build();

        reviewRequestService.addReview(dto, user);
    }
}

