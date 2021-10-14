package com.nicolatesser.nevernote.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.nicolatesser.nevernote.model.NoteWithMetadata;
import com.nicolatesser.nevernote.model.Notebook;
import com.nicolatesser.nevernote.model.NotebookWithMetadata;
import com.nicolatesser.nevernote.repository.NoteDAO;
import com.nicolatesser.nevernote.repository.NoteRepository;
import com.nicolatesser.nevernote.repository.NotebookDAO;
import com.nicolatesser.nevernote.repository.NotebookRepository;

@ExtendWith(MockitoExtension.class)
class NotebookServiceTest {

    private static final Long NOTEBOOK_ID_1 = 1L;
    private static final NotebookDAO NOTEBOOK_DAO_1 = NotebookDAO.builder().id(NOTEBOOK_ID_1).build();
    private static final Long NOTEBOOK_ID_2 = 2L;
    private static final NotebookDAO NOTEBOOK_DAO_2 = NotebookDAO.builder().id(NOTEBOOK_ID_2).build();
    private static final List<NotebookDAO> NOTEBOOK_DAO_LIST = ImmutableList.of(NOTEBOOK_DAO_1, NOTEBOOK_DAO_2);

    private static final Long NOTE_ID_1 = 1L;
    private static final NoteDAO NOTE_DAO_1 = NoteDAO.builder().id(NOTE_ID_1).notebookId(NOTEBOOK_ID_1).build();
    private static final Long NOTE_ID_2 = 2L;
    private static final NoteDAO NOTE_DAO_2 = NoteDAO.builder().id(NOTE_ID_2).notebookId(NOTEBOOK_ID_2).build();
    private static final List<NoteDAO> NOTE_DAO_LIST = ImmutableList.of(NOTE_DAO_1, NOTE_DAO_2);

    private static final String TAG1 = "tag1";
    private static final String TAG2 = "tag2";
    private static final String TAG3 = "tag3";

    private static final NoteWithMetadata NOTE_WITH_METADATA_1 = NoteWithMetadata.builder().id(
        NOTE_ID_1).tags(ImmutableSet.of(TAG1, TAG2)).build();
    private static final NoteWithMetadata NOTE_WITH_METADATA_2 = NoteWithMetadata.builder().id(
        NOTE_ID_2).tags(ImmutableSet.of(TAG1)).build();

    private static final NotebookWithMetadata NOTEBOOK_WITH_METADATA_1 = NotebookWithMetadata.builder().id(
        NOTEBOOK_ID_1).notes(ImmutableList.of(NOTE_WITH_METADATA_1, NOTE_WITH_METADATA_2)).build();
    private static final NotebookWithMetadata NOTEBOOK_WITH_METADATA_2 = NotebookWithMetadata.builder().id(
        NOTEBOOK_ID_2).build();

    private static final Notebook NOTEBOOK = Notebook.builder().build();

    private static final List<NotebookWithMetadata> NOTEBOOK_LIST = Collections.emptyList();

    @Mock
    private NotebookRepository notebookRepository;

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private MapperService mapperService;

    @InjectMocks
    private NotebookService notebookService;

    @Test
    void getNotebooks() {
        //given
        when(notebookRepository.findAll()).thenReturn(NOTEBOOK_DAO_LIST);
        when(noteRepository.findByNotebookId(NOTEBOOK_ID_1)).thenReturn(NOTE_DAO_LIST);
        when(noteRepository.findByNotebookId(NOTEBOOK_ID_2)).thenReturn(NOTE_DAO_LIST);
        when(mapperService.mapToNotebookWithMetadata(NOTEBOOK_DAO_1, NOTE_DAO_LIST, true, false)).thenReturn(
            NOTEBOOK_WITH_METADATA_1);
        when(mapperService.mapToNotebookWithMetadata(NOTEBOOK_DAO_2, NOTE_DAO_LIST, true, false)).thenReturn(
            NOTEBOOK_WITH_METADATA_2);

        //when
        List<NotebookWithMetadata> notebooks = notebookService.getNotebooks();
        //then
        assertThat(notebooks).isEqualTo(ImmutableList.of(NOTEBOOK_WITH_METADATA_1, NOTEBOOK_WITH_METADATA_2));
    }

    @Test
    void getNotebooksWithNoNotebooks() {
        //given
        when(notebookRepository.findAll()).thenReturn(Collections.emptyList());
        //when
        List<NotebookWithMetadata> notebooks = notebookService.getNotebooks();
        //then
        assertThat(notebooks).isEqualTo(Collections.emptyList());
    }

    @Test
    void getNotebook() {
        //given
        when(notebookRepository.findById(NOTEBOOK_ID_1)).thenReturn(Optional.of(NOTEBOOK_DAO_1));
        when(noteRepository.findByNotebookId(NOTEBOOK_ID_1)).thenReturn(NOTE_DAO_LIST);
        when(mapperService.mapToNotebookWithMetadata(NOTEBOOK_DAO_1, NOTE_DAO_LIST, false, true)).thenReturn(
            NOTEBOOK_WITH_METADATA_1);
        //when
        NotebookWithMetadata notebook = notebookService.getNotebook(NOTEBOOK_ID_1);
        //then
        assertThat(notebook).isEqualTo(NOTEBOOK_WITH_METADATA_1);
    }

    @Test
    void getNotebookWithNoNotebook() {
        //given
        when(notebookRepository.findById(NOTEBOOK_ID_1)).thenReturn(Optional.empty());
        //when/then
        assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(
            () -> notebookService.getNotebook(NOTEBOOK_ID_1));
    }


    @Test
    void getNotebookWithNotesFilteredByTag1() {
        //given
        when(notebookRepository.findById(NOTEBOOK_ID_1)).thenReturn(Optional.of(NOTEBOOK_DAO_1));
        when(noteRepository.findByNotebookId(NOTEBOOK_ID_1)).thenReturn(NOTE_DAO_LIST);
        when(mapperService.mapToNotebookWithMetadata(NOTEBOOK_DAO_1, NOTE_DAO_LIST, false, true)).thenReturn(
            NOTEBOOK_WITH_METADATA_1);
        //when
        NotebookWithMetadata notebook = notebookService.getNotebookWithNotesFilteredByTag(NOTEBOOK_ID_1, TAG1);
        //then
        assertThat(notebook).isEqualTo(NOTEBOOK_WITH_METADATA_1);
    }

    @Test
    void getNotebookWithNotesFilteredByTag2() {
        //given
        when(notebookRepository.findById(NOTEBOOK_ID_1)).thenReturn(Optional.of(NOTEBOOK_DAO_1));
        when(noteRepository.findByNotebookId(NOTEBOOK_ID_1)).thenReturn(NOTE_DAO_LIST);
        when(mapperService.mapToNotebookWithMetadata(NOTEBOOK_DAO_1, NOTE_DAO_LIST, false, true)).thenReturn(
            NOTEBOOK_WITH_METADATA_1);
        //when
        NotebookWithMetadata notebook = notebookService.getNotebookWithNotesFilteredByTag(NOTEBOOK_ID_1, TAG2);
        //then
        assertThat(notebook).isEqualTo(
            NOTEBOOK_WITH_METADATA_1.toBuilder().notes(ImmutableList.of(NOTE_WITH_METADATA_1)).build());
    }

    @Test
    void getNotebookWithNotesFilteredByTag3() {
        //given
        when(notebookRepository.findById(NOTEBOOK_ID_1)).thenReturn(Optional.of(NOTEBOOK_DAO_1));
        when(noteRepository.findByNotebookId(NOTEBOOK_ID_1)).thenReturn(NOTE_DAO_LIST);
        when(mapperService.mapToNotebookWithMetadata(NOTEBOOK_DAO_1, NOTE_DAO_LIST, false, true)).thenReturn(
            NOTEBOOK_WITH_METADATA_1);
        //when
        NotebookWithMetadata notebook = notebookService.getNotebookWithNotesFilteredByTag(NOTEBOOK_ID_1, TAG3);
        //then
        assertThat(notebook).isEqualTo(
            NOTEBOOK_WITH_METADATA_1.toBuilder().notes(Collections.emptyList()).build());
    }

    @Test
    void createNotebook() {
        //given
        when(mapperService.mapToNotebookDAO(NOTEBOOK)).thenReturn(
            NOTEBOOK_DAO_1);
        when(notebookRepository.save(NOTEBOOK_DAO_1)).thenReturn(NOTEBOOK_DAO_1);
        //when
        Long notebookId = notebookService.createNotebook(NOTEBOOK);
        //then
        assertThat(notebookId).isEqualTo(NOTEBOOK_ID_1);
    }

    @Test
    void deleteNotebook() {
        //given/when
        notebookService.deleteNotebook(NOTEBOOK_ID_1);
        //then
        verify(notebookRepository).deleteById(NOTEBOOK_ID_1);
    }
}