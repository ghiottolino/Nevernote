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

import com.nicolatesser.nevernote.model.Notebook;
import com.nicolatesser.nevernote.model.NotebookWithMetadata;
import com.nicolatesser.nevernote.service.NotebookService;

@ExtendWith(MockitoExtension.class)
class NotebookControllerTest {

    private static final List<NotebookWithMetadata> NOTEBOOK_LIST = Collections.emptyList();
    private static final Long NOTEBOOK_ID = 1L;
    private static final NotebookWithMetadata NOTEBOOK_WITH_METADATA = NotebookWithMetadata.builder().build();
    private static final Notebook NOTEBOOK = Notebook.builder().build();
    private static final String TAG = "tag";

    @Mock
    private NotebookService notebookService;

    @InjectMocks
    private NotebookController notebookController;

    @Test
    void getNotebooks() {
        //given
        when(notebookService.getNotebooks()).thenReturn(NOTEBOOK_LIST);
        //when
        List<NotebookWithMetadata> notebooks = notebookController.getNotebooks();
        //then
        assertThat(notebooks).isEqualTo(NOTEBOOK_LIST);
    }

    @Test
    void getNotebooksWithServiceException() {
        //given
        when(notebookService.getNotebooks()).thenThrow(new IllegalArgumentException());
        //when / then
        assertThatIllegalArgumentException().isThrownBy(() -> notebookController.getNotebooks());
    }

    @Test
    void getNotebook() {
        //given
        when(notebookService.getNotebook(NOTEBOOK_ID)).thenReturn(NOTEBOOK_WITH_METADATA);
        //when
        NotebookWithMetadata notebook = notebookController.getNotebook(NOTEBOOK_ID, "");
        //then
        assertThat(notebook).isEqualTo(NOTEBOOK_WITH_METADATA);
    }

    @Test
    void getNotebookWithTag() {
        //given
        when(notebookService.getNotebookWithNotesFilteredByTag(NOTEBOOK_ID, TAG)).thenReturn(NOTEBOOK_WITH_METADATA);
        //when
        NotebookWithMetadata notebook = notebookController.getNotebook(NOTEBOOK_ID, TAG);
        //then
        assertThat(notebook).isEqualTo(NOTEBOOK_WITH_METADATA);
    }

    @Test
    void getNotebookWithServiceException() {
        //given
        when(notebookService.getNotebook(NOTEBOOK_ID)).thenThrow(new IllegalArgumentException());
        //when / then
        assertThatIllegalArgumentException().isThrownBy(() -> notebookController.getNotebook(NOTEBOOK_ID, ""));
    }

    @Test
    void deleteNotebook() {
        //given / when
        notebookController.deleteNotebook(NOTEBOOK_ID);
        //then
        verify(notebookService).deleteNotebook(NOTEBOOK_ID);
    }


    @Test
    void deleteNotebookWithServiceException() {
        //given
        doThrow(new IllegalArgumentException()).when(notebookService).deleteNotebook(NOTEBOOK_ID);
        //when / then
        assertThatIllegalArgumentException().isThrownBy(() -> notebookController.deleteNotebook(NOTEBOOK_ID));
    }


    @Test
    void createNotebook() {
        //given
        when(notebookService.createNotebook(NOTEBOOK)).thenReturn(NOTEBOOK_ID);
        //when
        Long notebookId = notebookController.createNotebook(NOTEBOOK);
        //then
        assertThat(notebookId).isEqualTo(NOTEBOOK_ID);
    }

    @Test
    void createNotebookWithServiceException() {
        //given
        when(notebookService.createNotebook(NOTEBOOK)).thenThrow(new IllegalArgumentException());
        //when / then
        assertThatIllegalArgumentException().isThrownBy(() -> notebookController.createNotebook(NOTEBOOK));
    }
}