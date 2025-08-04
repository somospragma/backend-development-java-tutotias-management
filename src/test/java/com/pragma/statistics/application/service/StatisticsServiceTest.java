package com.pragma.statistics.application.service;

import com.pragma.statistics.domain.port.output.StatisticsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    @Mock
    private StatisticsRepository statisticsRepository;

    @InjectMocks
    private StatisticsService statisticsService;

    @Test
    void shouldReturnDashboardStatistics() {
        // Given
        Map<String, Long> requestsByStatus = Map.of("Enviada", 5L, "Aprobada", 3L);
        Map<String, Long> tutoringsByStatus = Map.of("Activa", 10L, "Completada", 8L);
        Map<String, Long> activeTutorsByChapter = Map.of("IT", 15L, "HR", 8L);

        when(statisticsRepository.countRequestsByStatus(null)).thenReturn(requestsByStatus);
        when(statisticsRepository.countTutoringsByStatus(null)).thenReturn(tutoringsByStatus);
        when(statisticsRepository.countActiveTutorsByChapter(null)).thenReturn(activeTutorsByChapter);

        // When
        var result = statisticsService.getDashboardStatistics(null);

        // Then
        assertEquals(requestsByStatus, result.getRequestsByStatus());
        assertEquals(tutoringsByStatus, result.getTutoringsByStatus());
        assertEquals(activeTutorsByChapter, result.getActiveTutorsByChapter());
    }
}