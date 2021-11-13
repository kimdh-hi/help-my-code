package com.my.hmc.controller;

import com.my.hmc.domain.User;
import com.my.hmc.request.AddReviewDto;
import com.my.hmc.response.BasicResponseDto;
import com.my.hmc.response.RequestedReviewListResponseDto;
import com.my.hmc.response.RequestedReviewResponseDto;
import com.my.hmc.security.UserDetailsImpl;
import com.my.hmc.service.ReviewerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 자신에게 요청된 코드리뷰요청 목록 확인
 * 자신에게 요청된 코드리뷰요청 상세내용 확인
 * 자신에게 요청된 코드리뷰요청에 리뷰코드 및 코멘트 달기
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class ReviewerController {

    private final ReviewerService reviewerService;

    @Secured("ROLE_REVIEWER")
    @GetMapping("/reviewer/review")
    public RequestedReviewListResponseDto getRequestedReviewList(
            @RequestParam int page, @RequestParam int size,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        --page;
        User user = userDetails.getUser();
        return reviewerService.getRequestedReviewList(user, page, size);
    }

    @Secured("ROLE_REVIEWER")
    @GetMapping("/reviewer/reviews")
    public RequestedReviewResponseDto getRequestedReview(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return reviewerService.getRequestedReview(user, id);
    }

    @Secured("ROLE_REVIEWER")
    @PostMapping("/reviewer/review")
    public BasicResponseDto addReviewAndComment(
            @RequestParam Long id,
            @RequestBody AddReviewDto addReviewDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails.getUser();
        reviewerService.addReviewAndComment(user, id, addReviewDto);

        return new BasicResponseDto(id, "success", "코드리뷰를 작성했습니다.", HttpStatus.OK);
    }

    @Secured("ROLE_REVIEWER")
    @PutMapping("/reviewer/review")
    public void editReviewAndComment(
            @RequestParam Long id,
            @RequestBody AddReviewDto addReviewDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

    }
}
