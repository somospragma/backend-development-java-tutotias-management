package com.pragma.usuarios.infrastructure.adapter.input.rest.mapper;

import com.pragma.chapter.domain.model.Chapter;
import com.pragma.chapter.domain.port.input.FindChapterUseCase;
import com.pragma.chapter.infrastructure.adapter.input.rest.mapper.ChapterDtoMapper;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.model.enums.RolUsuario;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.CreateUserDto;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.UpdateUserRequestDto;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.UserDto;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.UserWithTutoringCountDto;
import com.pragma.usuarios.infrastructure.adapter.output.external.dto.PragmaUserDto;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(componentModel = "spring", 
        uses = {ChapterDtoMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class UserDtoMapper {

    @Autowired
    private FindChapterUseCase findChapterUseCase;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "activeTutoringLimit", ignore = true)
    @Mapping(target = "chapter", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "seniority", ignore = true)
    @Mapping(target = "slackId", ignore = true)
    public abstract User toModel(CreateUserDto dto);

    @Mapping(target = "email", ignore = true)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "activeTutoringLimit", ignore = true)
    @Mapping(target = "googleUserId", ignore = true)
    @Mapping(target = "chapter", ignore = true)
    public abstract User toModel(UpdateUserRequestDto dto);
    
    public abstract UserDto toDto(User user);
    
    @Mapping(target = "tutoringsAsTutor", ignore = true)
    @Mapping(target = "tutoringsAsTutee", ignore = true)
    public abstract UserWithTutoringCountDto toUserWithTutoringCountDto(User user);

    public User toModelFromExternal(CreateUserDto dto, PragmaUserDto externalUser) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setGoogleUserId(dto.getGoogleUserId());
        user.setRol(RolUsuario.Tutorado);
        user.setActiveTutoringLimit(0);
        
        // Extract names from fullName
        String[] names = externalUser.getFullName().split(" ", 2);
        user.setFirstName(names[0]);
        user.setLastName(names.length > 1 ? names[1] : "");
        
        // Set chapter and seniority from first chapter in the list
        if (externalUser.getChapters() != null && !externalUser.getChapters().isEmpty()) {
            PragmaUserDto.Chapter firstChapter = externalUser.getChapters().get(0);
            
            // Find chapter by name
            Optional<Chapter> chapterOpt = findChapterUseCase.findChapterByName(firstChapter.getChapterName());
            chapterOpt.ifPresent(user::setChapter);
            
            // Set seniority
            if (firstChapter.getSeniorityId() != null) {
                user.setSeniority(firstChapter.getSeniorityId());
            }
        }
        
        return user;
    }

    @AfterMapping
    protected void findAndSetChapterForUpdate(UpdateUserRequestDto dto, @MappingTarget User user) {
        if (dto.getChapterId() != null && !dto.getChapterId().isEmpty()) {
            Optional<Chapter> chapterOpt = findChapterUseCase.findChapterById(dto.getChapterId());
            chapterOpt.ifPresent(user::setChapter);
        }
    }
}