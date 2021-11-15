package com.my.hmc.domain;

import com.my.hmc.domain.etype.QuestionStatus;
import com.my.hmc.request.ReviewUpdateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ReviewQuestion {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String code;

    private String comment;

    @Column(nullable = false)
    private String language;

    @Enumerated(EnumType.STRING)
    private QuestionStatus status;

    @JoinColumn(name = "q_user_id")
    @OneToOne(fetch = FetchType.LAZY)
    private User questionUser;

    @JoinColumn(name = "a_user_id")
    @OneToOne(fetch = FetchType.LAZY)
    private User answerUser;

    @JoinColumn(name = "review_answer_id")
    @OneToOne(fetch = FetchType.LAZY)
    private ReviewAnswer reviewAnswer;

    public void updateReviewQuestion(ReviewUpdateRequestDto dto) {
        this.title = dto.getTitle() != null ? dto.getTitle() : this.getTitle();
        this.code = dto.getCode() != null ? dto.getCode() : this.getCode();
        this.comment = dto.getComment() != null ? dto.getComment() : this.getComment();
        this.language = dto.getLanguage() != null ? dto.getLanguage() : this.getLanguage();
    }

    public void addAnswer(ReviewAnswer reviewAnswer) {
        this.reviewAnswer = reviewAnswer;
        this.status = QuestionStatus.DONE;
    }

    public void reject() {
        this.status = QuestionStatus.REJECT;
    }
}
