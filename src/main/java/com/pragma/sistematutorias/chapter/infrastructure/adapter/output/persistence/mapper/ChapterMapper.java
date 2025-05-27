package com.pragma.sistematutorias.chapter.infrastructure.adapter.output.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.pragma.sistematutorias.chapter.domain.model.Chapter;
import com.pragma.sistematutorias.chapter.infrastructure.adapter.output.persistence.entity.ChapterEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChapterMapper {

    ChapterEntity toEntity(Chapter chapter);
    
    Chapter toDomain(ChapterEntity entity);
}