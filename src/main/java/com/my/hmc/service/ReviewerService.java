package com.my.hmc.service;

import com.my.hmc.domain.ReviewAnswer;
import com.my.hmc.domain.ReviewQuestion;
import com.my.hmc.domain.User;
import com.my.hmc.domain.etype.QuestionStatus;
import com.my.hmc.repository.ReviewAnswerRepository;
import com.my.hmc.repository.ReviewQuestionRepository;
import com.my.hmc.request.AddReviewDto;
import com.my.hmc.response.*;
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
    public RequestedReviewListResponseDto getRequestedReviewList(User user, int page, int size, QuestionStatus status) {
        Pageable pageable = PageRequest.of(page, size);

        if (!status.toString().equals("ALL")) {
            return getAllReviewsByUserAndStatus(user, status, pageable);
        }

        Page<ReviewQuestion> requestedReviews = reviewQuestionRepository.findByAnswerUser(user, pageable);

        List<RequestedReviewResponseDto> collect = requestedReviews.stream().map(
                r -> new RequestedReviewResponseDto(r.getId(), r.getTitle(), r.getCode(), r.getComment(), r.getLanguage(), r.getStatus().toString())
        ).collect(Collectors.toList());

        return RequestedReviewListResponseDto.builder()
                .reviews(collect)
                .pageInfo(
                        new PageResponseDto(requestedReviews.getTotalPages(), requestedReviews.getTotalElements(), requestedReviews.getNumberOfElements(), page, size)
                ).build();
    }

    @Transactional(readOnly = true)
    public RequestedReviewListResponseDto getAllReviewsByUserAndStatus(
            User questionUser, QuestionStatus status, Pageable pageable) {

        Page<ReviewQuestion> reviewQuestions
                = reviewQuestionRepository.findByAnswerUserAndStatus(questionUser, status, pageable);

        List<RequestedReviewResponseDto> reviewResponseDtoList = reviewQuestions.stream().map(
                r -> new RequestedReviewResponseDto(r.getId(), r.getTitle(), r.getCode(), r.getComment(), r.getLanguage(), r.getStatus().toString())
        ).collect(Collectors.toList());

        PageResponseDto pageDto = PageResponseDto.builder()
                .page(reviewQuestions.getNumber())
                .size(reviewQuestions.getSize())
                .totalPages(reviewQuestions.getTotalPages())
                .totalElements(reviewQuestions.getTotalElements())
                .numberOfElements(reviewQuestions.getNumberOfElements()).build();

        return RequestedReviewListResponseDto.builder()
                .reviews(reviewResponseDtoList)
                .pageInfo(pageDto)
                .build();
    }

    @Transactional(readOnly = true)
    public RequestedReviewResponseDto getRequestedReview(User user, Long id) {
        ReviewQuestion review = reviewQuestionRepository.findByIdAndAnswerUser(id, user);
        return RequestedReviewResponseDto.builder()
                .title(review.getTitle())
                .code(review.getCode())
                .comment(review.getComment())
                .language(review.getLanguage())
                .status(review.getStatus().toString())
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

    @Transactional
    public void rejectReviewRequest(Long id) {
        ReviewQuestion reviewQuestion = reviewQuestionRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 리뷰요청입니다.")
        );

        reviewQuestion.reject();
    }
}
