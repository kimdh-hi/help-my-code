package com.my.hmc.controller;

import com.my.hmc.domain.ReviewQuestion;
import com.my.hmc.request.ReviewAddRequestDto;
import com.my.hmc.request.ReviewUpdateRequestDto;
import com.my.hmc.response.BasicResponseDto;
import com.my.hmc.response.PageResponseDto;
import com.my.hmc.response.ReviewListResponseDto;
import com.my.hmc.response.ReviewResponseDto;
import com.my.hmc.security.UserDetailsImpl;
import com.my.hmc.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Secured({"ROLE_USER", "ROLE_REVIEWER"})
    @PostMapping("/review")
    public BasicResponseDto addReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody ReviewAddRequestDto requestDto
    ) {
        ReviewQuestion reviewQuestion = reviewService.addReview(requestDto, userDetails.getUser());

        return BasicResponseDto.builder()
                .id(reviewQuestion.getId())
                .httpStatus(HttpStatus.CREATED)
                .message("코드리뷰 작성을 완료했습니다.")
                .result("success").build();
    }

    @GetMapping("/review")
    public ResponseEntity<ReviewResponseDto> getReview(@RequestParam Long reviewId) {
        ReviewQuestion review = reviewService.getReview(reviewId);
        ReviewResponseDto responseDto = ReviewResponseDto.builder()
                .id(review.getId())
                .title(review.getTitle())
                .comment(review.getComment())
                .language(review.getLanguage())
                .code(review.getCode()).build();

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/reviews")
    public ReviewListResponseDto getAllReviews(
            @RequestParam int page,
            @RequestParam int size
    ) {
        return reviewService.getAllReviews(page, size);
    }

    @PutMapping("/review")
    public BasicResponseDto editReview(@RequestParam Long reviewId, @RequestBody ReviewUpdateRequestDto requestDto) {

        reviewService.reviewUpdate(reviewId, requestDto);

        return new BasicResponseDto(null, "success", "수정 완료했습니다.", HttpStatus.OK);
    }

    @Secured({"ROLE_USER", "ROLE_REVIEWER"})
    @DeleteMapping("/review")
    public BasicResponseDto deleteReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam Long reviewId
    ) {
        reviewService.reviewDelete(reviewId, userDetails.getUser());

        return null;
    }
}
