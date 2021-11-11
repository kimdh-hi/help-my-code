package com.my.hmc.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.lang.invoke.LambdaConversionException;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ReviewAnswer {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String code;

    private String comment;

    @JoinColumn(name = "a_user_id")
    @OneToOne(fetch = FetchType.LAZY)
    private User answerUser;
}
