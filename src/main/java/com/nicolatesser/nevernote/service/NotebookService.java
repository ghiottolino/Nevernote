package com.nicolatesser.nevernote.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import com.nicolatesser.nevernote.model.NoteWithMetadata;
import com.nicolatesser.nevernote.model.Notebook;
import com.nicolatesser.nevernote.model.NotebookWithMetadata;
import com.nicolatesser.nevernote.repository.NoteDAO;
import com.nicolatesser.nevernote.repository.NoteRepository;
import com.nicolatesser.nevernote.repository.NotebookDAO;
import com.nicolatesser.nevernote.repository.NotebookRepository;

@Service
public class NotebookService {

    @Autowired
    private NotebookRepository notebookRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private MapperService mapperService;

    public List<NotebookWithMetadata> getNotebooks() {
        return
            Streamable.of(notebookRepository.findAll())
                .stream()
                .map(notebookDAO -> mapperService
                    .mapToNotebookWithMetadata(notebookDAO,
                        noteRepository.findByNotebookId(notebookDAO.getId()), true, false))
                .collect(Collectors.toList());
    }

    public NotebookWithMetadata getNotebook(Long notebookId) {
        NotebookDAO notebookDAO = notebookRepository.findById(notebookId)
            .orElseThrow(() -> new ResourceNotFoundException(String.format("Notebook Id %d not found.",
                notebookId)));
        List<NoteDAO> noteDAOListByNotebookId = noteRepository.findByNotebookId(notebookDAO.getId());
        return mapperService.mapToNotebookWithMetadata(notebookDAO, noteDAOListByNotebookId, false, true);
    }

    public NotebookWithMetadata getNotebookWithNotesFilteredByTag(Long notebookId, String tag) {
        NotebookWithMetadata notebook = getNotebook(notebookId);
        return notebook.toBuilder()
            .notes(getNotesFilteredByTag(notebook.getNotes(), tag))
            .build();
    }

    /**
     * Filtering would be more efficient implemented at the DAO level, however I haven´t quickly found the right
     * approach with JPA and @ElementCollection therefore fallback to application layer filtering.
     */
    private List<NoteWithMetadata> getNotesFilteredByTag(List<NoteWithMetadata> notes, String tag) {
        return notes.stream()
            .filter(note -> note.getTags().contains(tag))
            .collect(Collectors.toList());
    }

    public Long createNotebook(Notebook notebook) {
        NotebookDAO notebookDAO = mapperService.mapToNotebookDAO(notebook);
        return notebookRepository.save(notebookDAO).getId();
    }

    public void deleteNotebook(Long notebookId) {
        //TODO: validate that there are no notes before to delete a notebook (or delete all the notes).
        notebookRepository.deleteById(notebookId);
    }


}
