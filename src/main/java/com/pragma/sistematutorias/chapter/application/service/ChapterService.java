package com.pragma.sistematutorias.chapter.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pragma.sistematutorias.chapter.domain.model.Chapter;
import com.pragma.sistematutorias.chapter.domain.port.input.CreateChapterUseCase;
import com.pragma.sistematutorias.chapter.domain.port.input.FindChapterUseCase;
import com.pragma.sistematutorias.chapter.domain.port.input.GetAllChaptersUseCase;
import com.pragma.sistematutorias.chapter.domain.port.output.ChapterRepository;
import com.pragma.sistematutorias.shared.service.MessageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChapterService implements CreateChapterUseCase, GetAllChaptersUseCase, FindChapterUseCase {

    private final ChapterRepository chapterRepository;
    private final MessageService messageService;

    @Override
    public Chapter createChapter(Chapter chapter) {
        if (chapter == null) throw new NullPointerException("Chapter cannot be null");
        
        return chapterRepository.save(chapter);
    }

    @Override
    public List<Chapter> getAllChapters() {
        return chapterRepository.findAll();
    }
    
    @Override
    public Chapter findChapterById(String id) {
        return chapterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(messageService.getMessage("chapter.not.found", id)));
    }
}