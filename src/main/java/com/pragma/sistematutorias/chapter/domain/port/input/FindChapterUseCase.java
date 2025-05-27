package com.pragma.sistematutorias.chapter.domain.port.input;

import com.pragma.sistematutorias.chapter.domain.model.Chapter;

public interface FindChapterUseCase {
    Chapter findChapterById(String id);
}