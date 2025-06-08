package com.pragma.chapter.domain.port.output;

import java.util.List;
import java.util.Optional;

import com.pragma.chapter.domain.model.Chapter;

public interface ChapterRepository {
    Chapter save(Chapter chapter);
    List<Chapter> findAll();
    Optional<Chapter> findById(String id);
}