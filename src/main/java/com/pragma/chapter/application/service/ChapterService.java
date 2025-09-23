package com.pragma.chapter.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pragma.chapter.domain.model.Chapter;
import com.pragma.chapter.domain.port.input.CreateChapterUseCase;
import com.pragma.chapter.domain.port.input.FindChapterUseCase;
import com.pragma.chapter.domain.port.input.GetAllChaptersUseCase;
import com.pragma.chapter.domain.port.output.ChapterRepository;
import com.pragma.shared.service.MessageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChapterService implements CreateChapterUseCase, GetAllChaptersUseCase, FindChapterUseCase {

    private final ChapterRepository chapterRepository;
    private final MessageService messageService;

    @Override
    public Chapter createChapter(Chapter chapter) {        
        return chapterRepository.save(chapter);
    }

    @Override
    public List<Chapter> getAllChapters() {
        return chapterRepository.findAll();
    }
    
    @Override
    public Optional<Chapter> findChapterById(String id) {
        return chapterRepository.findById(id);
    }
    
    @Override
    public Optional<Chapter> findChapterByName(String name) {
        return chapterRepository.findByName(name);
    }
}