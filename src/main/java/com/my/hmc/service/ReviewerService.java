package com.my.hmc.service;

import com.my.hmc.domain.ReviewAnswer;
import com.my.hmc.domain.ReviewQuestion;
import com.my.hmc.domain.User;
import com.my.hmc.repository.ReviewAnswerRepository;
import com.my.hmc.repository.ReviewQuestionRepository;
import com.my.hmc.request.AddReviewDto;
import com.my.hmc.response.PageResponseDto;
import com.my.hmc.response.RequestedReviewListResponseDto;
import com.my.hmc.response.RequestedReviewResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewerService {

    private final ReviewAnswerRepository reviewAnswerRepository;
    private final ReviewQuestionRepository reviewQuestionRepository;

    @Transactional(readOnly = true)
    public RequestedReviewListResponseDto getRequestedReviewList(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewQuestion> requestedReviews = reviewQuestionRepository.findByAnswerUser(user, pageable);

        List<RequestedReviewResponseDto> collect = requestedReviews.stream().map(
                r -> new RequestedReviewResponseDto(r.getTitle(), r.getCode(), r.getComment(), r.getLanguage())
        ).collect(Collectors.toList());

        return RequestedReviewListResponseDto.builder()
                .reviewResponseDtoList(collect)
                .pageResponseDto(
                        new PageResponseDto(requestedReviews.getTotalPages(), requestedReviews.getTotalElements(), requestedReviews.getNumberOfElements(), page, size)
                ).build();
    }

    @Transactional(readOnly = true)
    public RequestedReviewResponseDto getRequestedReview(User user, Long id) {
        ReviewQuestion review = reviewQuestionRepository.findByIdAndAnswerUser(id, user);
        return RequestedReviewResponseDto.builder()
                .title(review.getTitle())
                .code(review.getCode())
                .comment(review.getComment())
                .language(review.getLanguage())
                .build();
    }

    @Transactional
    public void addReviewAndComment(User user, Long reviewId, AddReviewDto addReviewDto) {
        ReviewQuestion reviewQuestion = reviewQuestionRepository.findById(reviewId).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없는 코드리뷰 요청입니다.")
        );

        ReviewAnswer reviewAnswer = ReviewAnswer.builder()
                .title(addReviewDto.getTitle())
                .code(addReviewDto.getCode())
                .comment(addReviewDto.getComment())
                .answerUser(user)
                .build();
        ReviewAnswer savedReviewAnswer = reviewAnswerRepository.save(reviewAnswer);

        reviewQuestion.addAnswer(savedReviewAnswer);
    }

    @Transactional
    public void editReviewAndComment(User user, Long reviewId, AddReviewDto addReviewDto) {

    }
}
