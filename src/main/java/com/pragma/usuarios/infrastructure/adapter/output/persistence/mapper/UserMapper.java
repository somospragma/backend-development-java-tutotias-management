package com.pragma.usuarios.infrastructure.adapter.output.persistence.mapper;

import com.pragma.chapter.infrastructure.adapter.output.persistence.mapper.ChapterMapper;
import com.pragma.chapter.infrastructure.adapter.output.persistence.repository.SpringDataChapterRepository;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.infrastructure.adapter.output.persistence.entity.UsersEntity;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", 
        uses = {ChapterMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserMapper {

    @Autowired
    private SpringDataChapterRepository chapterRepository;
    
    @Autowired
    private ChapterMapper chapterMapper;

    @Mapping(target = "chapter", ignore = true)
    public abstract UsersEntity toEntity(User user);

    public abstract User toDomain(UsersEntity entity);

    @AfterMapping
    protected void setChapterReference(User user, @MappingTarget UsersEntity entity) {
        if (user.getChapter() != null && user.getChapter().getId() != null) {
            chapterRepository.findById(user.getChapter().getId())
                .ifPresent(entity::setChapter);
        }
    }
    
    @AfterMapping
    protected void mapChapterToDomain(UsersEntity entity, @MappingTarget User user) {
        if (entity.getChapter() != null) {
            user.setChapter(chapterMapper.toDomain(entity.getChapter()));
        }
    }
}