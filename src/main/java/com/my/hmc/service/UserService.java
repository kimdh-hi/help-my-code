package com.my.hmc.service;

import com.my.hmc.domain.Language;
import com.my.hmc.domain.ReviewQuestion;
import com.my.hmc.domain.User;
import com.my.hmc.domain.etype.UserRole;
import com.my.hmc.repository.LanguageRepository;
import com.my.hmc.repository.ReviewQuestionRepository;
import com.my.hmc.repository.UserRepository;
import com.my.hmc.request.SignupRequestDto;
import com.my.hmc.response.PageResponseDto;
import com.my.hmc.response.ReviewListResponseDto;
import com.my.hmc.response.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ReviewQuestionRepository reviewQuestionRepository;
    private final LanguageRepository languageRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public User saveUser(SignupRequestDto requestDto) {
//        UserRole userRole = requestDto.isReviewer() ? UserRole.ROLE_REVIEWER : UserRole.ROLE_USER;
        UserRole userRole = requestDto.isReviewer() ? UserRole.ROLE_REVIEWER : UserRole.ROLE_USER;

        log.info("saveUser role = {}", userRole);
//        log.info("savedUser isReviewer = {}", requestDto.getReviewer());

        User user = User.builder()
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .role(userRole)
                .reviewCount(0)
                .languages(new ArrayList<>())
                .build();

        User savedUser = userRepository.save(user);

        if (requestDto.getLanguages().size() > 0) {
            Set<String> languages = requestDto.getLanguages();
            for (String l : languages) {
                Language language = new Language(l);
                language.setUser(savedUser);
                languageRepository.save(language);
            }
        }


        return savedUser;
    }

    public List<String> getMyLanguage(Long userId) {
        return languageRepository.findByUserId(userId);
    }

    public ReviewListResponseDto getMyReviewRequests(int page, int size, User user) {

        Pageable pageable = PageRequest.of(page, size);

        Page<ReviewQuestion> reviewQuestions = reviewQuestionRepository.findByQuestionUser(user, pageable);

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
                .pageInfo(pageDto).build();
    }

    public ReviewResponseDto getMyReviewRequest(User user, Long id) {
        ReviewQuestion reviewQuestion = reviewQuestionRepository.findByIdAndQuestionUser(id, user).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없는 코드리뷰 요청입니다.")
        );

        return new ReviewResponseDto(
                reviewQuestion.getId(), reviewQuestion.getTitle(), reviewQuestion.getCode(), reviewQuestion.getComment(), reviewQuestion.getLanguage()
        );
    }

}
