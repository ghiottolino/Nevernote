package com.nicolatesser.nevernote.repository;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class NoteDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String body;
    @ElementCollection
    private Set<String> tags;
    private LocalDateTime created;
    private LocalDateTime lastModified;

    // I´m aware that JPA could take care of links between entities but I don´t want to get into this business now because 1. it´s a long time I am not using JPA, 2. I am pretending those repositories are not necessarily relational and at the point I would let the application layer take care of the joining.
    private Long notebookId;
}
