package com.nicolatesser.nevernote.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nicolatesser.nevernote.model.Note;
import com.nicolatesser.nevernote.model.NoteWithMetadata;
import com.nicolatesser.nevernote.service.NoteService;

@ExtendWith(MockitoExtension.class)
class NoteControllerTest {

    private static final List<NoteWithMetadata> NOTE_LIST = Collections.emptyList();
    private static final Long NOTEBOOK_ID = 1L;
    private static final String TAG = "tag";
    private static final Long NOTE_ID = 2L;
    private static final Note NOTE = Note.builder().build();
    private static final NoteWithMetadata NOTE_WITH_METADATA = NoteWithMetadata.builder().build();

    @Mock
    private NoteService noteService;

    @InjectMocks
    private NoteController noteController;

    @Test
    void getNote() {
        //given
        when(noteService.getNote(NOTEBOOK_ID, NOTE_ID)).thenReturn(NOTE_WITH_METADATA);
        //when
        NoteWithMetadata note = noteController.getNote(NOTEBOOK_ID, NOTE_ID);
        //then
        assertThat(note).isEqualTo(NOTE_WITH_METADATA);
    }

    @Test
    void getNoteWithServiceException() {
        //given
        when(noteService.getNote(NOTEBOOK_ID, NOTE_ID)).thenThrow(new IllegalArgumentException());
        //when / then
        assertThatIllegalArgumentException().isThrownBy(() -> noteController.getNote(NOTEBOOK_ID, NOTE_ID));
    }

    @Test
    void deleteNote() {
        //given / when
        noteController.deleteNote(NOTEBOOK_ID, NOTE_ID);
        //then
        verify(noteService).deleteNote(NOTEBOOK_ID, NOTE_ID);
    }

    @Test
    void deleteNoteWithServiceException() {
        //given
        doThrow(new IllegalArgumentException()).when(noteService).deleteNote(NOTEBOOK_ID, NOTE_ID);
        //when / then
        assertThatIllegalArgumentException().isThrownBy(() -> noteController.deleteNote(NOTEBOOK_ID, NOTE_ID));
    }

    @Test
    void createNote() {
        //given
        when(noteService.createNote(NOTEBOOK_ID, NOTE)).thenReturn(NOTE_ID);
        //when
        Long noteId = noteController.createNote(NOTEBOOK_ID, NOTE);
        //then
        assertThat(noteId).isEqualTo(NOTE_ID);
    }

    @Test
    void createNoteWithServiceException() {
        //given
        when(noteService.createNote(NOTEBOOK_ID, NOTE)).thenThrow(new IllegalArgumentException());
        //when / then
        assertThatIllegalArgumentException().isThrownBy(() -> noteController.createNote(NOTEBOOK_ID, NOTE));
    }

    @Test
    void updateNote() {
        //given
        when(noteService.updateNote(NOTEBOOK_ID, NOTE_ID, NOTE)).thenReturn(NOTE_ID);
        //when
        Long noteId = noteController.updateNote(NOTEBOOK_ID, NOTE_ID, NOTE);
        //then
        assertThat(noteId).isEqualTo(NOTE_ID);
    }

    @Test
    void updateNoteWithServiceException() {
        //given
        when(noteService.updateNote(NOTEBOOK_ID, NOTE_ID, NOTE)).thenThrow(new IllegalArgumentException());
        //when / then
        assertThatIllegalArgumentException().isThrownBy(() -> noteController.updateNote(NOTEBOOK_ID, NOTE_ID, NOTE));
    }
}