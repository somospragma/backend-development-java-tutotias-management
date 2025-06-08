package com.pragma.chapter.infrastructure.adapter.output.persistence;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.pragma.chapter.domain.model.Chapter;
import com.pragma.chapter.infrastructure.adapter.output.persistence.entity.ChapterEntity;
import com.pragma.chapter.infrastructure.adapter.output.persistence.mapper.ChapterMapper;
import com.pragma.chapter.infrastructure.adapter.output.persistence.repository.SpringDataChapterRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ChapterPersistenceAdapterTest {

    @Mock
    private ChapterMapper chapterMapper;

    @InjectMocks
    private ChapterPersistenceAdapter chapterPersistenceAdapter;

    @Mock
    private SpringDataChapterRepository chapterRepository;

    /**
     * Test the save method of ChapterPersistenceAdapter
     * This test verifies that the save method correctly maps the input Chapter to a ChapterEntity,
     * saves it to the repository, and then maps the saved entity back to a Chapter domain object.
     */
    @Test
    public void testSaveChapter() {
        // Arrange
        Chapter inputChapter = new Chapter();
        ChapterEntity inputEntity = new ChapterEntity();
        ChapterEntity savedEntity = new ChapterEntity();
        Chapter expectedChapter = new Chapter();

        when(chapterMapper.toEntity(inputChapter)).thenReturn(inputEntity);
        when(chapterRepository.save(inputEntity)).thenReturn(savedEntity);
        when(chapterMapper.toDomain(savedEntity)).thenReturn(expectedChapter);

        // Act
        Chapter result = chapterPersistenceAdapter.save(inputChapter);

        // Assert
        assertEquals(expectedChapter, result);
        verify(chapterMapper).toEntity(inputChapter);
        verify(chapterRepository).save(inputEntity);
        verify(chapterMapper).toDomain(savedEntity);
    }

    /**
     * Test case for findAll method of ChapterPersistenceAdapter
     * This test verifies that the findAll method correctly retrieves all chapters from the repository,
     * maps them to domain objects, and returns them as a list.
     */
    @Test
    public void test_findAll_retrievesAllChaptersAndMapsToDomain() {
        // Arrange
        ChapterEntity entity1 = new ChapterEntity();
        ChapterEntity entity2 = new ChapterEntity();
        List<ChapterEntity> entities = Arrays.asList(entity1, entity2);

        Chapter chapter1 = new Chapter();
        Chapter chapter2 = new Chapter();

        when(chapterRepository.findAll()).thenReturn(entities);
        when(chapterMapper.toDomain(entity1)).thenReturn(chapter1);
        when(chapterMapper.toDomain(entity2)).thenReturn(chapter2);

        // Act
        List<Chapter> result = chapterPersistenceAdapter.findAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals(chapter1, result.get(0));
        assertEquals(chapter2, result.get(1));

        verify(chapterRepository).findAll();
    }

    /**
     * Test case for findById method when a chapter exists.
     * It verifies that the method correctly retrieves and maps a chapter when it exists in the repository.
     */
    @Test
    public void test_findById_whenChapterExists() {
        // Arrange
        String chapterId = "123";
        ChapterEntity chapterEntity = new ChapterEntity();
        Chapter expectedChapter = new Chapter();

        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapterEntity));
        when(chapterMapper.toDomain(chapterEntity)).thenReturn(expectedChapter);

        // Act
        Optional<Chapter> result = chapterPersistenceAdapter.findById(chapterId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedChapter, result.get());
        verify(chapterRepository).findById(chapterId);
        verify(chapterMapper).toDomain(chapterEntity);
    }

}
