package com.pragma.sistematutorias.chapter.domain.port.output;

import java.util.List;
import java.util.Optional;
import com.pragma.sistematutorias.chapter.domain.model.Chapter;

public interface ChapterRepository {
    Chapter save(Chapter chapter);
    List<Chapter> findAll();
    Optional<Chapter> findById(String id);
}