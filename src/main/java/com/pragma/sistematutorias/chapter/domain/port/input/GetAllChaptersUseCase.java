package com.pragma.sistematutorias.chapter.domain.port.input;

import java.util.List;
import com.pragma.sistematutorias.chapter.domain.model.Chapter;

public interface GetAllChaptersUseCase {
    List<Chapter> getAllChapters();
}