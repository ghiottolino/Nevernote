package com.nicolatesser.nevernote.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotebookRepository extends CrudRepository<NotebookDAO, Long> {}
