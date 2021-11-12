package com.my.hmc.service;

import com.my.hmc.domain.Language;
import com.my.hmc.domain.ReviewQuestion;
import com.my.hmc.domain.User;
import com.my.hmc.domain.etype.QuestionStatus;
import com.my.hmc.repository.LanguageRepository;
import com.my.hmc.repository.ReviewAnswerRepository;
import com.my.hmc.repository.ReviewQuestionRepository;
import com.my.hmc.repository.UserRepository;
import com.my.hmc.request.ReviewAddRequestDto;
import com.my.hmc.request.ReviewUpdateRequestDto;
import com.my.hmc.response.PageResponseDto;
import com.my.hmc.response.ReviewListResponseDto;
import com.my.hmc.response.ReviewResponseDto;
import com.my.hmc.response.ReviewerInfoDto;
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
public class ReviewRequestService {

    private final UserRepository userRepository;
    private final ReviewQuestionRepository reviewQuestionRepository;
    private final ReviewAnswerRepository reviewAnswerRepository;
    private final LanguageRepository languageRepository;

    @Transactional
    public ReviewQuestion addReview(ReviewAddRequestDto requestDto, User user) {

        Long reviewerId = requestDto.getReviewerId();
        User reviewer = userRepository.findById(reviewerId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 리뷰어 입니다.")
        );

        ReviewQuestion reviewQuestion = ReviewQuestion.builder()
                .title(requestDto.getTitle())
                .code(requestDto.getCode())
                .comment(requestDto.getComment())
                .questionUser(user)
                .language(requestDto.getLanguage())
                .answerUser(reviewer)
                .status(QuestionStatus.REQUESTED)
                .build();

        return reviewQuestionRepository.save(reviewQuestion);
    }

    @Transactional(readOnly = true)
    public ReviewQuestion getReview(Long reviewId) {
        return reviewQuestionRepository.findById(reviewId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 코드리뷰 입니다.")
        );
    }

    @Transactional(readOnly = true)
    public ReviewListResponseDto getAllReviews(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewQuestion> reviewQuestions = reviewQuestionRepository.findAll(pageable);

        List<ReviewResponseDto> reviewResponseDtoList = reviewQuestions.stream().map(
                r -> new ReviewResponseDto(r.getId(), r.getTitle(), r.getCode(), r.getComment(), r.getLanguage())
        ).collect(Collectors.toList());

        PageResponseDto pageDto = PageResponseDto.builder()
                .page(page)
                .size(size)
                .totalPages(reviewQuestions.getTotalPages())
                .totalElements(reviewQuestions.getTotalElements())
                .numberOfElements(reviewQuestions.getNumberOfElements()).build();

        return ReviewListResponseDto.builder()
                .reviews(reviewResponseDtoList)
                .pageResponseDto(pageDto).build();
    }

    @Transactional
    public void reviewUpdate(Long reviewId, ReviewUpdateRequestDto requestDto) {
        ReviewQuestion reviewQuestion = reviewQuestionRepository.findById(reviewId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 코드리뷰 입니다.")
        );

        reviewQuestion.updateReviewQuestion(requestDto);
    }

    @Transactional
    public void reviewDelete(Long reviewId, User user) {

        if (reviewQuestionRepository.existsByUserAndId(user.getId(), reviewId)) {
            reviewQuestionRepository.deleteById(reviewId);
        }
    }

    @Transactional(readOnly = true)
    public List<ReviewerInfoDto> findReviewerByLanguage(String language) {
        List<Language> languages = languageRepository.findAllByName(language);
        return languages.stream().map(
                l -> ReviewerInfoDto.builder()
                            .userId(l.getUser().getId())
                            .username(l.getUser().getUsername())
                            .languageId(l.getId())
                            .language(l.getName())
                            .build()
        ).collect(Collectors.toList());
    }
}

