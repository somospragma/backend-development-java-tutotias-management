package com.pragma.chapter.domain.port.input;

import com.pragma.chapter.domain.model.Chapter;

public interface FindChapterUseCase {
    Chapter findChapterById(String id);
}