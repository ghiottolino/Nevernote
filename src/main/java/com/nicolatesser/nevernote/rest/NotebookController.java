package com.nicolatesser.nevernote.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nicolatesser.nevernote.model.Notebook;
import com.nicolatesser.nevernote.model.NotebookWithMetadata;
import com.nicolatesser.nevernote.service.NotebookService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/notebooks")
@Slf4j
public class NotebookController {

    @Autowired
    private NotebookService notebookService;

    @GetMapping
    public List<NotebookWithMetadata> getNotebooks() {
        //TODO: this method could be easily extended with filtering possibility according to the notebook metadata (e
        // .g. owners, creation date, etc.)
        //TODO: add pagination functionalities.
        log.info("Received getNotebooks request");
        return notebookService.getNotebooks();
    }

    @GetMapping("/{id}")
    public NotebookWithMetadata getNotebook(@PathVariable("id") Long id,
        @RequestParam(value = "tag", defaultValue = "") String tag) {
        log.info("Received getNotebook request with id {} and tag {}", id, tag);
        if (!StringUtils.hasText(tag)) {
            return notebookService.getNotebook(id);
        } else {
            return notebookService.getNotebookWithNotesFilteredByTag(id, tag);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteNotebook(@PathVariable("id") Long id) {
        log.info("Received deleteNotebook request with id {}", id);
        notebookService.deleteNotebook(id);
    }

    @PostMapping
    public Long createNotebook(@RequestBody Notebook notebook) {
        log.info("Received createNotebook request with notebook {}", notebook);
        return notebookService.createNotebook(notebook);
    }


}
