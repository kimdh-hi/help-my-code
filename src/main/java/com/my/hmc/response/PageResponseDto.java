package com.my.hmc.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PageResponseDto {

    private int totalPages;
    private long totalElements;
    private int numberOfElements;
    private int page;
    private int size;
}
