package com.pragma.chapter.infrastructure.adapter.output.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pragma.chapter.infrastructure.adapter.output.persistence.entity.ChapterEntity;

@Repository
public interface SpringDataChapterRepository extends CrudRepository<ChapterEntity, String> {
}