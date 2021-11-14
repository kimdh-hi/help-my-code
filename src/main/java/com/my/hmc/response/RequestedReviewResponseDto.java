package com.my.hmc.response;

import com.my.hmc.domain.etype.QuestionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RequestedReviewResponseDto {

    private Long id;
    private String title;
    private String code;
    private String comment;
    private String language;
    private String status;
}
