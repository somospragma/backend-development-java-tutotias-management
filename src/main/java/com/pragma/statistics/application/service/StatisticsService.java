package com.pragma.statistics.application.service;

import com.pragma.statistics.domain.model.DashboardStatistics;
import com.pragma.statistics.domain.port.input.GetDashboardStatisticsUseCase;
import com.pragma.statistics.domain.port.output.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsService implements GetDashboardStatisticsUseCase {
    
    private final StatisticsRepository statisticsRepository;

    @Override
    public DashboardStatistics getDashboardStatistics(String chapterId) {
        return new DashboardStatistics(
            statisticsRepository.countRequestsByStatus(chapterId),
            statisticsRepository.countTutoringsByStatus(chapterId),
            statisticsRepository.countActiveTutorsByChapter(chapterId)
        );
    }
}