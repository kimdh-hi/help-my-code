package com.my.hmc.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddReviewDto {

    private Long reviewId;
    private String title;
    private String code;
    private String comment;

}
