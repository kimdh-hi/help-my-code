package com.my.hmc.service;

import com.my.hmc.domain.Language;
import com.my.hmc.domain.ReviewQuestion;
import com.my.hmc.domain.User;
import com.my.hmc.domain.etype.UserRole;
import com.my.hmc.repository.LanguageRepository;
import com.my.hmc.repository.ReviewQuestionRepository;
import com.my.hmc.repository.UserRepository;
import com.my.hmc.request.SignupRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public Long saveUser(SignupRequestDto requestDto) {
        UserRole userRole = requestDto.isReviewer() ? UserRole.ROLE_REVIEWER : UserRole.ROLE_USER;
        User user = User.builder()
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .role(userRole)
                .reviewCount(0)
                .languages(new ArrayList<>())
                .build();

        User savedUser = userRepository.save(user);

        Set<String> languages = requestDto.getLanguages();
        for (String l : languages) {
            Language language = new Language(l);
            language.setUser(savedUser);
            languageRepository.save(language);
        }


        return savedUser.getId();
    }

    public List<String> getMyLanguage(Long userId) {
        return languageRepository.findByUserId(userId);
    }

    public void getMyReviewRequests(User user) {
        List<ReviewQuestion> reviews = reviewQuestionRepository.findByQuestionUser(user);

    }

}