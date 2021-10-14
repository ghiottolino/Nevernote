package com.nicolatesser.nevernote.service;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.nicolatesser.nevernote.model.Note;
import com.nicolatesser.nevernote.model.NoteWithMetadata;
import com.nicolatesser.nevernote.repository.NoteDAO;
import com.nicolatesser.nevernote.repository.NoteRepository;
import com.nicolatesser.nevernote.repository.NotebookDAO;
import com.nicolatesser.nevernote.repository.NotebookRepository;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    private static final Long NOTEBOOK_ID_1 = 1L;
    private static final NotebookDAO NOTEBOOK_DAO_1 = NotebookDAO.builder().id(NOTEBOOK_ID_1).build();
    private static final Long NOTEBOOK_ID_2 = 2L;

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

    private static final Note NOTE = Note.builder().build();


    @Mock
    private NotebookRepository notebookRepository;

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private MapperService mapperService;

    @InjectMocks
    private NoteService noteService;

    @Captor
    ArgumentCaptor<NoteDAO> noteDAOArgumentCaptor;

    @Test
    void getNote() {
        //given
        when(notebookRepository.findById(NOTEBOOK_ID_1)).thenReturn(Optional.of(NOTEBOOK_DAO_1));
        when(noteRepository.findById(NOTE_ID_1)).thenReturn(Optional.of(NOTE_DAO_1));
        when(mapperService.mapToNoteWithMetadata(NOTE_DAO_1, true)).thenReturn(NOTE_WITH_METADATA_1);

        //when
        NoteWithMetadata note = noteService.getNote(NOTEBOOK_ID_1, NOTE_ID_1);
        //then
        assertThat(note).isEqualTo(NOTE_WITH_METADATA_1);
    }

    @Test
    void getNoteWithNonExisitingNotebook() {
        //given
        when(notebookRepository.findById(NOTEBOOK_ID_1)).thenReturn(Optional.empty());
        //when/then
        assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(
            () -> noteService.getNote(NOTEBOOK_ID_1, NOTE_ID_1));
    }

    @Test
    void getNoteWithNonExisitingNote() {
        //given
        when(notebookRepository.findById(NOTEBOOK_ID_1)).thenReturn(Optional.of(NOTEBOOK_DAO_1));
        when(noteRepository.findById(NOTE_ID_1)).thenReturn(Optional.empty());
        //when/then
        assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(
            () -> noteService.getNote(NOTEBOOK_ID_1, NOTE_ID_1));
    }

    @Test
    void getNoteWithMismatchingNoteAndNotebookIds() {
        //given
        when(notebookRepository.findById(NOTEBOOK_ID_1)).thenReturn(Optional.of(NOTEBOOK_DAO_1));
        when(noteRepository.findById(NOTE_ID_1)).thenReturn(
            Optional.of(NOTE_DAO_2)); //NOTE_DAO_2 belongs to notebook2 not notebook1

        //when/then
        assertThatIllegalArgumentException().isThrownBy(
            () -> noteService.getNote(NOTEBOOK_ID_1, NOTE_ID_1));
    }

    @Test
    void createNote() {
        //given
        when(notebookRepository.findById(NOTEBOOK_ID_1)).thenReturn(Optional.of(NOTEBOOK_DAO_1));
        when(mapperService.mapToNoteDAO(NOTE)).thenReturn(NOTE_DAO_1);
        when(noteRepository.save(noteDAOArgumentCaptor.capture())).thenReturn(NOTE_DAO_1);
        //when
        Long noteId = noteService.createNote(NOTEBOOK_ID_1, NOTE);
        //then
        assertThat(noteId).isEqualTo(NOTE_ID_1);
        NoteDAO savedNoteDao = noteDAOArgumentCaptor.getValue();
        assertThat(savedNoteDao.getNotebookId()).isEqualTo(NOTEBOOK_ID_1);
        assertThat(savedNoteDao.getCreated()).isCloseTo(LocalDateTime.now(), within(5, SECONDS));
        assertThat(savedNoteDao.getLastModified()).isCloseTo(LocalDateTime.now(), (within(5, SECONDS)));
    }

    @Test
    void createNoteWithNonExistingNotebook() {
        //given
        when(notebookRepository.findById(NOTEBOOK_ID_1)).thenReturn(Optional.empty());
        //when/then
        assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(
            () -> noteService.createNote(NOTEBOOK_ID_1, NOTE));
    }

    @Test
    void updateNote() {
        //given
        when(notebookRepository.findById(NOTEBOOK_ID_1)).thenReturn(Optional.of(NOTEBOOK_DAO_1));
        when(noteRepository.findById(NOTE_ID_1)).thenReturn(Optional.of(NOTE_DAO_1));
        when(mapperService.mapToNoteDAO(NOTE)).thenReturn(NOTE_DAO_1);
        when(noteRepository.save(noteDAOArgumentCaptor.capture())).thenReturn(NOTE_DAO_1);
        //when
        Long noteId = noteService.updateNote(NOTEBOOK_ID_1, NOTE_ID_1, NOTE);
        //then
        assertThat(noteId).isEqualTo(NOTE_ID_1);
        NoteDAO savedNoteDao = noteDAOArgumentCaptor.getValue();
        assertThat(savedNoteDao.getNotebookId()).isEqualTo(NOTEBOOK_ID_1);
        assertThat(savedNoteDao.getLastModified()).isCloseTo(LocalDateTime.now(), (within(5, SECONDS)));
    }

    @Test
    void updateNoteWithNonExistingNotebook() {
        //given
        when(notebookRepository.findById(NOTEBOOK_ID_1)).thenReturn(Optional.empty());
        //when/then
        assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(
            () -> noteService.updateNote(NOTEBOOK_ID_1, NOTE_ID_1, NOTE));
    }

    @Test
    void updateNoteWithNonExistingNote() {
        //given
        when(notebookRepository.findById(NOTEBOOK_ID_1)).thenReturn(Optional.of(NOTEBOOK_DAO_1));
        when(noteRepository.findById(NOTE_ID_1)).thenReturn(Optional.empty());
        //when/then
        assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(
            () -> noteService.updateNote(NOTEBOOK_ID_1, NOTE_ID_1, NOTE));
    }

    @Test
    void updateNoteWithMismatchingNoteAndNotebookIds() {
        //given
        when(notebookRepository.findById(NOTEBOOK_ID_1)).thenReturn(Optional.of(NOTEBOOK_DAO_1));
        when(noteRepository.findById(NOTE_ID_1)).thenReturn(Optional.of(NOTE_DAO_2));
        //when/then
        assertThatIllegalArgumentException().isThrownBy(
            () -> noteService.updateNote(NOTEBOOK_ID_1, NOTE_ID_1, NOTE));
    }

    @Test
    void deleteNote() {
        //given
        when(notebookRepository.findById(NOTEBOOK_ID_1)).thenReturn(Optional.of(NOTEBOOK_DAO_1));
        when(noteRepository.findById(NOTE_ID_1)).thenReturn(Optional.of(NOTE_DAO_1));
        //when
        noteService.deleteNote(NOTEBOOK_ID_1, NOTE_ID_1);
        //then
        verify(noteRepository).deleteById(NOTE_ID_1);
    }

    @Test
    void deleteNoteWithNonExistingNotebook() {
        //given
        when(notebookRepository.findById(NOTEBOOK_ID_1)).thenReturn(Optional.empty());
        //when/then
        assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(
            () -> noteService.deleteNote(NOTEBOOK_ID_1, NOTE_ID_1));
    }

    @Test
    void deleteNoteWithNonExistingNote() {
        //given
        when(notebookRepository.findById(NOTEBOOK_ID_1)).thenReturn(Optional.of(NOTEBOOK_DAO_1));
        when(noteRepository.findById(NOTE_ID_1)).thenReturn(Optional.empty());
        //when/then
        assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(
            () -> noteService.deleteNote(NOTEBOOK_ID_1, NOTE_ID_1));
    }

    @Test
    void deleteNoteWithMismatchingNoteAndNotebookIds() {
        //given
        when(notebookRepository.findById(NOTEBOOK_ID_1)).thenReturn(Optional.of(NOTEBOOK_DAO_1));
        when(noteRepository.findById(NOTE_ID_1)).thenReturn(Optional.of(NOTE_DAO_2));
        //when/then
        assertThatIllegalArgumentException().isThrownBy(
            () -> noteService.deleteNote(NOTEBOOK_ID_1, NOTE_ID_1));
    }
}