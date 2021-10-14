package com.nicolatesser.nevernote.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.nicolatesser.nevernote.model.Note;
import com.nicolatesser.nevernote.model.NoteWithMetadata;
import com.nicolatesser.nevernote.model.Notebook;
import com.nicolatesser.nevernote.model.NotebookWithMetadata;
import com.nicolatesser.nevernote.repository.NoteDAO;
import com.nicolatesser.nevernote.repository.NotebookDAO;

@ExtendWith(MockitoExtension.class)
class MapperServiceTest {

    private static final Long NOTEBOOK_ID = 1L;

    private static final String NOTEBOOK_NAME = "notebookName";
    private static final Notebook NOTEBOOK = Notebook.builder()
        .name(NOTEBOOK_NAME)
        .build();
    private static final NotebookDAO NOTEBOOK_DAO = NotebookDAO.builder()
        .name(NOTEBOOK_NAME)
        .build();

    private static final NotebookDAO NOTEBOOK_DAO_WITH_METADATA = NotebookDAO.builder()
        .id(NOTEBOOK_ID)
        .name(NOTEBOOK_NAME)
        .build();

    private static final String TITLE = "title";
    private static final String BODY = "body";
    private static final String TAG = "tag";
    private static final Set<String> TAGS = ImmutableSet.of(TAG);

    private static final Note NOTE = Note.builder()
        .title(TITLE)
        .body(BODY)
        .tags(TAGS)
        .build();

    private static final NoteDAO NOTE_DAO = NoteDAO.builder()
        .title(TITLE)
        .body(BODY)
        .tags(TAGS)
        .build();

    private static final Long NOTE_ID = 1L;
    private static final LocalDateTime CREATED = LocalDateTime.ofEpochSecond(100, 0, ZoneOffset.UTC);
    private static final LocalDateTime LAST_MODIFIED = LocalDateTime.ofEpochSecond(200, 0, ZoneOffset.UTC);

    private static final NoteDAO NOTE_DAO_WITH_METADATA = NoteDAO.builder()
        .id(NOTE_ID)
        .title(TITLE)
        .body(BODY)
        .tags(TAGS)
        .notebookId(NOTEBOOK_ID)
        .created(CREATED)
        .lastModified(LAST_MODIFIED)
        .build();

    @InjectMocks
    private MapperService mapperService;

    @Test
    void mapToNotebookDAO() {
        //given/when
        NotebookDAO notebookDAO = mapperService.mapToNotebookDAO(NOTEBOOK);
        //then
        assertThat(notebookDAO).isEqualTo(NOTEBOOK_DAO);
    }

    @Test
    void mapToNotebookWithMetadataWithCountsAndNotes() {
        //given/when
        NotebookWithMetadata notebookWithMetadata = mapperService.mapToNotebookWithMetadata(NOTEBOOK_DAO_WITH_METADATA,
            ImmutableList.of(NOTE_DAO_WITH_METADATA), true, true);

        //then
        NoteWithMetadata expectedNoteWithMetadata = NoteWithMetadata.builder()
            .id(NOTE_ID)
            .title(TITLE)
            .tags(TAGS)
            .created(CREATED)
            .lastModified(LAST_MODIFIED)
            .noteUrl("http://localhost:8080/notebooks/" + NOTEBOOK_ID + "/notes/" + NOTE_ID)
            .notebookUrl("http://localhost:8080/notebooks/" + NOTE_ID)
            .body(null)
            .build();

        NotebookWithMetadata expectedNotebookWithMetadata = NotebookWithMetadata.builder()
            .id(NOTEBOOK_ID)
            .name(NOTEBOOK_NAME)
            .notebookUrl("http://localhost:8080/notebooks/" + NOTE_ID)
            .numberOfNotes(1L)
            .notes(ImmutableList.of(expectedNoteWithMetadata))
            .build();

        assertThat(notebookWithMetadata).isEqualTo(expectedNotebookWithMetadata);
    }

    @Test
    void mapToNotebookWithMetadataWithoutCountsAndNotes() {
        //given/when
        NotebookWithMetadata notebookWithMetadata = mapperService.mapToNotebookWithMetadata(NOTEBOOK_DAO_WITH_METADATA,
            ImmutableList.of(NOTE_DAO_WITH_METADATA), false, false);

        //then
        NotebookWithMetadata expectedNotebookWithMetadata = NotebookWithMetadata.builder()
            .id(NOTEBOOK_ID)
            .name(NOTEBOOK_NAME)
            .notebookUrl("http://localhost:8080/notebooks/" + NOTE_ID)
            .numberOfNotes(null)
            .notes(null)
            .build();

        assertThat(notebookWithMetadata).isEqualTo(expectedNotebookWithMetadata);
    }


    @Test
    void mapToNoteWithMetadataWithBody() {
        //given/when
        NoteWithMetadata noteWithMetadata = mapperService.mapToNoteWithMetadata(NOTE_DAO_WITH_METADATA, true);
        //then
        NoteWithMetadata expectedNoteWithMetadata = NoteWithMetadata.builder()
            .id(NOTE_ID)
            .title(TITLE)
            .tags(TAGS)
            .created(CREATED)
            .lastModified(LAST_MODIFIED)
            .noteUrl("http://localhost:8080/notebooks/" + NOTEBOOK_ID + "/notes/" + NOTE_ID)
            .notebookUrl("http://localhost:8080/notebooks/" + NOTE_ID)
            .body(BODY)
            .build();

        assertThat(noteWithMetadata).isEqualTo(expectedNoteWithMetadata);
    }

    @Test
    void mapToNoteWithMetadataWithoutBody() {
        //given/when
        NoteWithMetadata noteWithMetadata = mapperService.mapToNoteWithMetadata(NOTE_DAO_WITH_METADATA, false);
        //then
        NoteWithMetadata expectedNoteWithMetadata = NoteWithMetadata.builder()
            .id(NOTE_ID)
            .title(TITLE)
            .tags(TAGS)
            .created(CREATED)
            .lastModified(LAST_MODIFIED)
            .noteUrl("http://localhost:8080/notebooks/" + NOTEBOOK_ID + "/notes/" + NOTE_ID)
            .notebookUrl("http://localhost:8080/notebooks/" + NOTE_ID)
            .body(null)
            .build();

        assertThat(noteWithMetadata).isEqualTo(expectedNoteWithMetadata);
    }

    @Test
    void mapToNoteDAO() {
        //given/when
        NoteDAO noteDAO = mapperService.mapToNoteDAO(NOTE);
        //then
        assertThat(noteDAO).isEqualTo(NOTE_DAO);
    }
}