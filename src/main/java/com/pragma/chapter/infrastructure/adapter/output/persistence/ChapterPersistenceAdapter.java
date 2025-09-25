package com.pragma.chapter.infrastructure.adapter.output.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.pragma.chapter.domain.model.Chapter;
import com.pragma.chapter.domain.port.output.ChapterRepository;
import com.pragma.chapter.infrastructure.adapter.output.persistence.entity.ChapterEntity;
import com.pragma.chapter.infrastructure.adapter.output.persistence.mapper.ChapterMapper;
import com.pragma.chapter.infrastructure.adapter.output.persistence.repository.SpringDataChapterRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChapterPersistenceAdapter implements ChapterRepository {

  
    private final SpringDataChapterRepository chapterRepository;
    private final ChapterMapper chapterMapper;

    @Override
    public Chapter save(Chapter chapter) {
        ChapterEntity chapterEntity = chapterMapper.toEntity(chapter);
        ChapterEntity savedEntity = chapterRepository.save(chapterEntity);
        return chapterMapper.toDomain(savedEntity);
    }

    @Override
    public List<Chapter> findAll() {
        return StreamSupport.stream(chapterRepository.findAll().spliterator(), false)
                .map(chapterMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<Chapter> findById(String id) {
        return chapterRepository.findById(id)
                .map(chapterMapper::toDomain);
    }
    
    @Override
    public Optional<Chapter> findByName(String name) {
        return chapterRepository.findByName(name)
                .map(chapterMapper::toDomain);
    }
}