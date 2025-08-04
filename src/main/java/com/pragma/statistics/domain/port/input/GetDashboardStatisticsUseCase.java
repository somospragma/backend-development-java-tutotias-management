package com.pragma.statistics.domain.port.input;

import com.pragma.statistics.domain.model.DashboardStatistics;

public interface GetDashboardStatisticsUseCase {
    DashboardStatistics getDashboardStatistics(String chapterId);
}