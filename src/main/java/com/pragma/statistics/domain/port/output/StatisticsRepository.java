package com.pragma.statistics.domain.port.output;

import java.util.Map;

public interface StatisticsRepository {
    Map<String, Long> countRequestsByStatus(String chapterId);
    Map<String, Long> countTutoringsByStatus(String chapterId);
    Map<String, Long> countActiveTutorsByChapter(String chapterId);
}