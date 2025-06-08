package com.pragma.chapter.infrastructure.adapter.output.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.pragma.chapter.domain.model.Chapter;
import com.pragma.chapter.infrastructure.adapter.output.persistence.entity.ChapterEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChapterMapper {

    ChapterEntity toEntity(Chapter chapter);
    
    Chapter toDomain(ChapterEntity entity);
}