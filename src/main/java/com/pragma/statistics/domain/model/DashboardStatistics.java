package com.pragma.statistics.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class DashboardStatistics {
    private final Map<String, Long> requestsByStatus;
    private final Map<String, Long> tutoringsByStatus;
    private final Map<String, Long> activeTutorsByChapter;
}