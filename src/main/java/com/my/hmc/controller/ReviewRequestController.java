package com.my.hmc.controller;

import com.my.hmc.domain.ReviewQuestion;
import com.my.hmc.domain.etype.QuestionStatus;
import com.my.hmc.request.ReviewAddRequestDto;
import com.my.hmc.request.ReviewUpdateRequestDto;
import com.my.hmc.response.BasicResponseDto;
import com.my.hmc.response.ReviewListResponseDto;
import com.my.hmc.response.ReviewResponseDto;
import com.my.hmc.response.ReviewerInfoDto;
import com.my.hmc.security.UserDetailsImpl;
import com.my.hmc.service.ReviewRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewRequestController {

    private final ReviewRequestService reviewRequestService;

    @Secured({"ROLE_USER", "ROLE_REVIEWER"})
    @PostMapping("/review")
    public BasicResponseDto addReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody ReviewAddRequestDto requestDto
    ) {
        ReviewQuestion reviewQuestion = reviewRequestService.addReview(requestDto, userDetails.getUser());

        return BasicResponseDto.builder()
                .id(reviewQuestion.getId())
                .httpStatus(HttpStatus.CREATED)
                .message("코드리뷰 작성을 완료했습니다.")
                .result("success").build();
    }

    @GetMapping("/review")
    public ResponseEntity<ReviewResponseDto> getReview(@RequestParam Long id) {
        ReviewQuestion review = reviewRequestService.getReview(id);
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
            @RequestParam int size,
            @RequestParam(required = false) QuestionStatus status
    ) {
        --page;
        return reviewRequestService.getAllReviews(page, size, status);
    }

//    @GetMapping("/reviews/status")
    public ReviewListResponseDto getAllReviewsByStatus(
            @RequestParam int page, @RequestParam int size,
            @RequestParam QuestionStatus status
    ) {
        --page;
//        QuestionStatus questionStatus = null;
//        if (status.equals("REQUESTED")) questionStatus = QuestionStatus.REQUESTED;
//        else if (status.equals("DONE")) questionStatus = QuestionStatus.DONE;
//        else if (status.equals("REJECT")) questionStatus = QuestionStatus.REJECT;

        return reviewRequestService.getAllReviewsByStatus(page, size, status);
    }

    @Secured({"ROLE_USER", "ROLE_REVIEWER"})
    @PutMapping("/review")
    public BasicResponseDto editReview(@RequestParam Long id, @RequestBody ReviewUpdateRequestDto requestDto) {

        reviewRequestService.reviewUpdate(id, requestDto);

        return new BasicResponseDto(null, "success", "수정 완료했습니다.", HttpStatus.OK);
    }

    @Secured({"ROLE_USER", "ROLE_REVIEWER"})
    @DeleteMapping("/review")
    public BasicResponseDto deleteReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam Long id
    ) {
        reviewRequestService.reviewDelete(id, userDetails.getUser());

        return new BasicResponseDto(null, "success", "삭제 완료했습니다.", HttpStatus.OK);
    }

    @Secured({"ROLE_USER", "ROLE_REVIEWER"})
    @GetMapping("/review/language/user")
    public List<ReviewerInfoDto> findReviewerByLanguage(@RequestParam String language) {
        return reviewRequestService.findReviewerByLanguage(language);
    }
}
