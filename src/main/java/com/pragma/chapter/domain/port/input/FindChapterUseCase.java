package com.pragma.chapter.domain.port.input;

import com.pragma.chapter.domain.model.Chapter;

import java.util.Optional;

public interface FindChapterUseCase {
    Optional<Chapter> findChapterById(String id);
}