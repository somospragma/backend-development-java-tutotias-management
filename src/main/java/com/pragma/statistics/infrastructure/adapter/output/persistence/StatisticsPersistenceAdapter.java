package com.pragma.statistics.infrastructure.adapter.output.persistence;

import com.pragma.statistics.domain.port.output.StatisticsRepository;
import com.pragma.tutorings.domain.model.enums.TutoringStatus;
import com.pragma.tutorings.infrastructure.adapter.output.persistence.repository.SpringDataTutoringRepository;
import com.pragma.tutorings_requests.domain.model.enums.RequestStatus;
import com.pragma.tutorings_requests.infrastructure.adapter.output.persistence.repository.SpringDataTutoringRequestRepository;
import com.pragma.usuarios.domain.model.enums.RolUsuario;
import com.pragma.usuarios.infrastructure.adapter.output.persistence.repository.SpringDataUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StatisticsPersistenceAdapter implements StatisticsRepository {
    
    private final SpringDataTutoringRequestRepository tutoringRequestRepository;
    private final SpringDataTutoringRepository tutoringRepository;
    private final SpringDataUserRepository userRepository;
    private final EntityManager entityManager;

    @Override
    public Map<String, Long> countRequestsByStatus(String chapterId) {
        Map<String, Long> result = new HashMap<>();
        for (RequestStatus status : RequestStatus.values()) {
            String jpql = "SELECT COUNT(tr) FROM TutoringRequestsEntity tr WHERE tr.requestStatus = :status";
            if (chapterId != null) {
                jpql += " AND tr.tutee.chapter.id = :chapterId";
            }
            Query query = entityManager.createQuery(jpql);
            query.setParameter("status", status);
            if (chapterId != null) {
                query.setParameter("chapterId", chapterId);
            }
            Long count = (Long) query.getSingleResult();
            result.put(status.name(), count);
        }
        return result;
    }

    @Override
    public Map<String, Long> countTutoringsByStatus(String chapterId) {
        Map<String, Long> result = new HashMap<>();
        for (TutoringStatus status : TutoringStatus.values()) {
            String jpql = "SELECT COUNT(t) FROM TutoringEntity t WHERE t.status = :status";
            if (chapterId != null) {
                jpql += " AND (t.tutorId.chapter.id = :chapterId OR t.tuteeId.chapter.id = :chapterId)";
            }
            Query query = entityManager.createQuery(jpql);
            query.setParameter("status", status);
            if (chapterId != null) {
                query.setParameter("chapterId", chapterId);
            }
            Long count = (Long) query.getSingleResult();
            result.put(status.name(), count);
        }
        return result;
    }

    @Override
    public Map<String, Long> countActiveTutorsByChapter(String chapterId) {
        String jpql;
        if (chapterId != null) {
            jpql = "SELECT COUNT(u.id) " +
                "FROM UsersEntity u " +
                "WHERE u.rol = :tutorRole AND u.chapter.id = :chapterId";
        } else {
            jpql = "SELECT c.name, COUNT(u.id) " +
                "FROM UsersEntity u " +
                "JOIN u.chapter c " +
                "WHERE u.rol = :tutorRole " +
                "GROUP BY c.name";
        }
        
        Query query = entityManager.createQuery(jpql);
        query.setParameter("tutorRole", RolUsuario.Tutor);
        if (chapterId != null) {
            query.setParameter("chapterId", chapterId);
        }
        
        Map<String, Long> result = new HashMap<>();
        if (chapterId != null) {
            Long count = (Long) query.getSingleResult();
            result.put("activeTutors", count);
        } else {
            @SuppressWarnings("unchecked")
            List<Object[]> results = query.getResultList();
            for (Object[] row : results) {
                result.put((String) row[0], (Long) row[1]);
            }
        }
        return result;
    }
}