package com.pragma.sistematutorias.chapter.domain.port.input;

import com.pragma.sistematutorias.chapter.domain.model.Chapter;

public interface CreateChapterUseCase {
    Chapter createChapter(Chapter chapter);
}