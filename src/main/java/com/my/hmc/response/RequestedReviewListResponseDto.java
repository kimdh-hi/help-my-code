package com.my.hmc.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RequestedReviewListResponseDto {

    private List<RequestedReviewResponseDto> reviews;

    private PageResponseDto pageInfo;
}
