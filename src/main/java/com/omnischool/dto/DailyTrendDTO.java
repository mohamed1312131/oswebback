package com.omnischool.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyTrendDTO {
    private LocalDate date;
    private long requests;
    private long conversions;
}

