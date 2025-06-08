package com.pragma.chapter.domain.port.input;

import java.util.List;

import com.pragma.chapter.domain.model.Chapter;

public interface GetAllChaptersUseCase {
    List<Chapter> getAllChapters();
}