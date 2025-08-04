package com.pragma.statistics.infrastructure.adapter.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class DashboardStatisticsResponseDto {
    private final Map<String, Long> requestsByStatus;
    private final Map<String, Long> tutoringsByStatus;
    private final Map<String, Long> activeTutorsByChapter;
}