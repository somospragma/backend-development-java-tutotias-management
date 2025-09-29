package com.pragma.tutorings.infrastructure.adapter.input.rest.mapper;

import com.pragma.feedbacks.domain.model.Feedback;
import com.pragma.feedbacks.infrastructure.adapter.input.rest.dto.FeedbackDto;
import com.pragma.feedbacks.infrastructure.adapter.input.rest.mapper.FeedbackDtoMapper;
import com.pragma.skills.infrastructure.adapter.input.rest.mapper.SkillDtoMapper;
import com.pragma.tutoring_sessions.domain.model.TutoringSession;
import com.pragma.tutoring_sessions.infrastructure.adapter.input.rest.dto.TutoringSessionDto;
import com.pragma.tutoring_sessions.infrastructure.adapter.input.rest.mapper.TutoringSessionDtoMapper;
import com.pragma.tutorings.domain.model.Tutoring;
import com.pragma.tutorings.infrastructure.adapter.input.rest.dto.TutoringDetailDto;
import com.pragma.usuarios.infrastructure.adapter.input.rest.mapper.UserDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserDtoMapper.class, SkillDtoMapper.class, TutoringSessionDtoMapper.class, FeedbackDtoMapper.class})
public interface TutoringDetailDtoMapper {

    @Mapping(target = "sessions", source = "sessions")
    @Mapping(target = "feedbacks", source = "feedbacks")
    TutoringDetailDto toDetailDto(Tutoring tutoring, List<TutoringSession> sessions, List<Feedback> feedbacks);
}