package com.nicolatesser.nevernote.dev;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableSet;
import com.nicolatesser.nevernote.model.Note;
import com.nicolatesser.nevernote.model.Notebook;
import com.nicolatesser.nevernote.service.NoteService;
import com.nicolatesser.nevernote.service.NotebookService;

@Service
@Profile("dev")
/**
 * This class has a "dev" profile in it, it will be loaded only if spring boot is initiated with
 * ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
 * see README.md for more information.
 */
public class TestDataInitializer {

    private static final String TAG1 = "tag1";
    private static final String TAG2 = "tag2";
    private static final String TAG3 = "tag3";

    @Autowired
    private NotebookService notebookService;

    @Autowired
    private NoteService noteService;

    @PostConstruct
    public void init() {
        Long notebook1 = notebookService.createNotebook(Notebook.builder().name("Nicola's notebook").build());
        Long notebook2 = notebookService.createNotebook(Notebook.builder().name("A's notebook").build());
        Long notebook3 = notebookService.createNotebook(Notebook.builder().name("B's notebook").build());
        Long notebook4 = notebookService.createNotebook(Notebook.builder().name("C's notebook").build());

        noteService.createNote(notebook1,
            Note.builder()
                .title("My first note")
                .body("My first note. Not a very long one.")
                .tags(ImmutableSet.of(TAG1, TAG2))
                .build());
        noteService.createNote(notebook1,
            Note.builder()
                .title("My second note")
                .body("My second note. Not a very long one.")
                .tags(ImmutableSet.of(TAG1, TAG3))
                .build());

        noteService.createNote(notebook2,
            Note.builder()
                .title("A's first note")
                .body("A's first note. Not a very long one.")
                .tags(ImmutableSet.of(TAG2))
                .build());

        noteService.createNote(notebook3,
            Note.builder()
                .title("B's first note")
                .body("B's first note. Not a very long one.")
                .tags(ImmutableSet.of(TAG2, TAG3))
                .build());
        noteService.createNote(notebook3,
            Note.builder()
                .title("B's second note")
                .body("B's second note. Not a very long one.")
                .tags(ImmutableSet.of())
                .build());

    }
}
