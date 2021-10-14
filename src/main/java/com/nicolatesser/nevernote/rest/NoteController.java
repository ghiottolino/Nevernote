package com.nicolatesser.nevernote.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nicolatesser.nevernote.model.Note;
import com.nicolatesser.nevernote.model.NoteWithMetadata;
import com.nicolatesser.nevernote.service.NoteService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/notebooks/{notebookId}/notes")
@Slf4j
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping("/{id}")
    public NoteWithMetadata getNote(@PathVariable("notebookId") Long notebookId, @PathVariable("id") Long id) {
        log.info("Received getNote request with notebookId {} and noteId {}", notebookId, id);
        return noteService.getNote(notebookId, id);
    }

    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable("notebookId") Long notebookId, @PathVariable("id") Long id) {
        log.info("Received deleteNotebook request with notebookId {} and noteId {}", notebookId, id);
        noteService.deleteNote(notebookId, id);
    }

    @PostMapping
    public Long createNote(@PathVariable("notebookId") Long notebookId, @RequestBody Note note) {
        log.info("Received createNote request with notebookId {} and note {}", notebookId, note);
        return noteService.createNote(notebookId, note);
    }

    @PutMapping("/{id}")
    public Long updateNote(@PathVariable("notebookId") Long notebookId, @PathVariable("id") Long noteId,
        @RequestBody Note note) {
        log.info("Received updateNote request with notebookId {} and noteId {} and note {}", notebookId, noteId, note);
        return noteService.updateNote(notebookId, noteId, note);
    }
}
