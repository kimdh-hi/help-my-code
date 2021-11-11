package com.my.hmc.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReviewResponseDto {

    private Long id;
    private String title;
    private String code;
    private String comment;
    private String language;
}
