package com.pragma.statistics.infrastructure.adapter.input.rest.mapper;

import com.pragma.statistics.domain.model.DashboardStatistics;
import com.pragma.statistics.infrastructure.adapter.input.rest.dto.DashboardStatisticsResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatisticsRestMapper {
    DashboardStatisticsResponseDto toResponseDto(DashboardStatistics dashboardStatistics);
}