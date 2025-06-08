package com.pragma.usuarios.infrastructure.adapter.input.rest.mapper;

import com.pragma.chapter.domain.model.Chapter;
import com.pragma.chapter.domain.port.input.FindChapterUseCase;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.UpdateUserRequestDto;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class UpdateUserMapper {

    @Autowired
    private FindChapterUseCase findChapterUseCase;

    @Mapping(target = "email", ignore = true)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "activeTutoringLimit", ignore = true)
    @Mapping(target = "chapter", ignore = true)
    public abstract User toModel(UpdateUserRequestDto dto);

    @AfterMapping
    protected void findAndSetChapter(UpdateUserRequestDto dto, @MappingTarget User user) {
        if (dto.getChapterId() != null && !dto.getChapterId().isEmpty()) {
            Optional<Chapter> chapterOpt = findChapterUseCase.findChapterById(dto.getChapterId());
            chapterOpt.ifPresent(user::setChapter);
        }
    }
}