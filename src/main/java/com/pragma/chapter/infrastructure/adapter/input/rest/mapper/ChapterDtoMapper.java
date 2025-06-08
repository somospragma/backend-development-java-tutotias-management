package com.pragma.chapter.infrastructure.adapter.input.rest.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.pragma.chapter.domain.model.Chapter;
import com.pragma.chapter.infrastructure.adapter.input.rest.dto.ChapterDto;
import com.pragma.chapter.infrastructure.adapter.input.rest.dto.CreateChapterDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChapterDtoMapper {

    @Mapping(target = "id", ignore = true)
    Chapter toDomain(CreateChapterDto dto);
    
    Chapter toDomain(ChapterDto dto); 

    ChapterDto toDto(Chapter chapter);
    
    List<ChapterDto> toListDto(List<Chapter> chapters);
}