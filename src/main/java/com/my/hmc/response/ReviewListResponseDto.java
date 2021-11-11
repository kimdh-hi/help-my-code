package com.my.hmc.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReviewListResponseDto {

    List<ReviewResponseDto> reviews = new ArrayList<>();

    private PageResponseDto pageResponseDto;
}
