package com.pragma.statistics.infrastructure.adapter.input.rest;

import com.pragma.statistics.domain.port.input.GetDashboardStatisticsUseCase;
import com.pragma.statistics.infrastructure.adapter.input.rest.dto.DashboardStatisticsResponseDto;
import com.pragma.statistics.infrastructure.adapter.input.rest.mapper.StatisticsRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    
    private final GetDashboardStatisticsUseCase getDashboardStatisticsUseCase;
    private final StatisticsRestMapper statisticsRestMapper;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatisticsResponseDto> getDashboardStatistics(
            @RequestParam(required = false) String chapterId) {
        var statistics = getDashboardStatisticsUseCase.getDashboardStatistics(chapterId);
        return ResponseEntity.ok(statisticsRestMapper.toResponseDto(statistics));
    }
}