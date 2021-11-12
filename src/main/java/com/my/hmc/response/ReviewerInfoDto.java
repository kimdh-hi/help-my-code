package com.my.hmc.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReviewerInfoDto {

    private Long userId;
    private String username;
    private Long languageId;
    private String language;
}
