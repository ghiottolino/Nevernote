package com.nicolatesser.nevernote.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends CrudRepository<NoteDAO, Long> {

    List<NoteDAO> findByNotebookId(Long notebookId);


}

