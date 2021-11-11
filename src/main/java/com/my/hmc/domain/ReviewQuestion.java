package com.my.hmc.domain;

import com.my.hmc.domain.etype.QuestionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ReviewQuestion {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    private QuestionStatus status;

    @JoinColumn(name = "q_user_id")
    @OneToOne(fetch = FetchType.LAZY)
    private User questionUser;

    @JoinColumn(name = "a_user_id")
    @OneToOne(fetch = FetchType.LAZY)
    private User answerUser;
}
