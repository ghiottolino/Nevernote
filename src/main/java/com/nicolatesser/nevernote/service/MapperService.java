package com.nicolatesser.nevernote.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nicolatesser.nevernote.model.Note;
import com.nicolatesser.nevernote.model.NoteWithMetadata;
import com.nicolatesser.nevernote.model.Notebook;
import com.nicolatesser.nevernote.model.NotebookWithMetadata;
import com.nicolatesser.nevernote.repository.NoteDAO;
import com.nicolatesser.nevernote.repository.NotebookDAO;

@Service
public class MapperService {

    public NotebookDAO mapToNotebookDAO(Notebook notebook) {
        return NotebookDAO.builder().name(notebook.getName()).build();
    }

    public NotebookWithMetadata mapToNotebookWithMetadata(NotebookDAO notebookDAO,
        List<NoteDAO> noteDAOListByNotebookId, boolean includeNotesCount, boolean includeNotes) {
        NotebookWithMetadata.NotebookWithMetadataBuilder builder = NotebookWithMetadata.builder()
            .id(notebookDAO.getId())
            .name(notebookDAO.getName())
            .notebookUrl(getNotebookResourceUrl(notebookDAO.getId()));
        if (includeNotesCount) {
            builder.numberOfNotes(Long.valueOf(noteDAOListByNotebookId.size()));
        }
        if (includeNotes) {
            builder.notes(mapToNoteMetadataList(notebookDAO, noteDAOListByNotebookId));
        }
        return builder.build();
    }

    private List<NoteWithMetadata> mapToNoteMetadataList(NotebookDAO notebookDAO,
        List<NoteDAO> noteDAOListByNotebookId) {
        return noteDAOListByNotebookId.stream()
            .map(noteDAO -> mapToNoteWithMetadata(noteDAO, false))
            .collect(Collectors.toList());
    }


    public NoteWithMetadata mapToNoteWithMetadata(NoteDAO noteDAO, boolean includeBody) {
        NoteWithMetadata.NoteWithMetadataBuilder builder = NoteWithMetadata.builder()
            .id(noteDAO.getId())
            .title(noteDAO.getTitle())
            .tags(noteDAO.getTags())
            .created(noteDAO.getCreated())
            .lastModified(noteDAO.getLastModified())
            .noteUrl(getNoteResourceUrl(noteDAO.getNotebookId(), noteDAO.getId()))
            .notebookUrl(getNotebookResourceUrl(noteDAO.getNotebookId()));

        if (includeBody) {
            builder.body(noteDAO.getBody());
        }
        return builder.build();
    }

    public NoteDAO mapToNoteDAO(Note note) {
        return NoteDAO.builder()
            .title(note.getTitle())
            .body(note.getBody())
            .tags(note.getTags())
            .build();
    }

    private String getNotebookResourceUrl(Long id) {
        //TODO: depending on the stage the base url will be different
        return String.format("http://localhost:8080/notebooks/%d", id);
    }

    private String getNoteResourceUrl(Long notebookId, Long noteId) {
        //TODO: depending on the stage the base url will be different
        return String.format("http://localhost:8080/notebooks/%d/notes/%d", notebookId, noteId);
    }


}
