package com.pragma.chapter.domain.port.input;

import com.pragma.chapter.domain.model.Chapter;

public interface CreateChapterUseCase {
    Chapter createChapter(Chapter chapter);
}