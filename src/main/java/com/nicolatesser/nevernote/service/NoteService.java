package com.nicolatesser.nevernote.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.nicolatesser.nevernote.model.Note;
import com.nicolatesser.nevernote.model.NoteWithMetadata;
import com.nicolatesser.nevernote.repository.NoteDAO;
import com.nicolatesser.nevernote.repository.NoteRepository;
import com.nicolatesser.nevernote.repository.NotebookRepository;

@Service
public class NoteService {

    @Autowired
    private NotebookRepository notebookRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private MapperService mapperService;

    public NoteWithMetadata getNote(Long notebookId, Long noteId) {
        validateNotebookExists(notebookId);
        NoteDAO noteDAO = getNoteDAO(noteId);
        // /rest/webmvc/ResourceNotFoundException.html
        validateNotebookIdMatches(notebookId, noteDAO.getNotebookId());
        return mapperService.mapToNoteWithMetadata(noteDAO, true);
    }

    public Long createNote(Long notebookId, Note note) {
        validateNotebookExists(notebookId);
        NoteDAO noteDAO = mapperService.mapToNoteDAO(note);
        noteDAO.setCreated(LocalDateTime.now());
        noteDAO.setLastModified(LocalDateTime.now());
        noteDAO.setNotebookId(notebookId);
        return noteRepository.save(noteDAO).getId();
    }

    public Long updateNote(Long notebookId, Long noteId, Note note) {
        validateNotebookExists(notebookId);
        NoteDAO noteDAO = getNoteDAO(noteId);
        validateNotebookIdMatches(notebookId, noteDAO.getNotebookId());
        noteDAO = mapperService.mapToNoteDAO(note);
        noteDAO.setLastModified(LocalDateTime.now());
        noteDAO.setNotebookId(notebookId);
        return noteRepository.save(noteDAO).getId();
    }


    public void deleteNote(Long notebookId, Long noteId) {
        validateNotebookExists(notebookId);
        NoteDAO noteDAO = getNoteDAO(noteId);
        validateNotebookIdMatches(notebookId, noteDAO.getNotebookId());
        noteRepository.deleteById(noteId);
    }

    private NoteDAO getNoteDAO(Long noteId) {
        return noteRepository.findById(noteId)
            .orElseThrow(() -> new ResourceNotFoundException(String.format("Note Id %d not found.", noteId)));
    }

    private void validateNotebookExists(Long notebookId) {
        notebookRepository.findById(notebookId)
            .orElseThrow(() -> new ResourceNotFoundException(String.format("Notebook Id %d not found.", notebookId)));
    }

    private void validateNotebookIdMatches(Long notebookId, Long noteNotebookId) {
        Assert.isTrue(notebookId.equals(noteNotebookId), String
            .format("The note object notebookId %d does not match the notebookId %d", notebookId, noteNotebookId));
    }
}
