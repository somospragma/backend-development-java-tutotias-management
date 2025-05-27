package com.pragma.sistematutorias.chapter.infrastructure.adapter.input.rest;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.pragma.sistematutorias.chapter.domain.model.Chapter;
import com.pragma.sistematutorias.chapter.domain.port.input.CreateChapterUseCase;
import com.pragma.sistematutorias.chapter.domain.port.input.FindChapterUseCase;
import com.pragma.sistematutorias.chapter.domain.port.input.GetAllChaptersUseCase;
import com.pragma.sistematutorias.chapter.infrastructure.adapter.input.rest.dto.ChapterDto;
import com.pragma.sistematutorias.chapter.infrastructure.adapter.input.rest.dto.CreateChapterDto;
import com.pragma.sistematutorias.chapter.infrastructure.adapter.input.rest.mapper.ChapterDtoMapper;
import com.pragma.sistematutorias.shared.dto.OkResponseDto;
import com.pragma.sistematutorias.shared.service.MessageService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/chapter")
public class ChapterController {

    private final MessageService messageService;

    @Autowired
    private CreateChapterUseCase createChapterUseCase;

    @Autowired
    private GetAllChaptersUseCase getAllChaptersUseCase;

    @Autowired
    private FindChapterUseCase findChapterUseCase;

    @Autowired
    private ChapterDtoMapper chapterDtoMapper;

    ChapterController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/")
    public ResponseEntity<OkResponseDto<List<ChapterDto>>> getAllChapters() {
        List<Chapter> chapters = getAllChaptersUseCase.getAllChapters();
        List<ChapterDto> chaptersDto = chapterDtoMapper.toListDto(chapters);
        return ResponseEntity.ok(OkResponseDto.of(messageService.getMessage("general.success"),chaptersDto));
    }


    @PostMapping("/")
    public ResponseEntity<OkResponseDto<ChapterDto>> postCreate(@Valid @RequestBody CreateChapterDto createChapterDto) {
        Chapter chapter = chapterDtoMapper.toDomain(createChapterDto);
        Chapter createdChapter = createChapterUseCase.createChapter(chapter);
        ChapterDto responseDto = chapterDtoMapper.toDto(createdChapter);

        OkResponseDto<ChapterDto> okResponseDto = OkResponseDto.of("Chapter created successfully", responseDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDto.getId())
                .toUri();

        return ResponseEntity.created(location).body(okResponseDto);

    }

    @GetMapping("/{id}")
    public ResponseEntity<OkResponseDto<ChapterDto>> getFindChapter(@PathVariable String id) {
        Chapter chapter = findChapterUseCase.findChapterById(id);
        ChapterDto chapterDto = chapterDtoMapper.toDto(chapter);
        return ResponseEntity.ok(OkResponseDto.of(messageService.getMessage("general.success"),chapterDto));
    }
}