package com.nicolatesser.nevernote.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
/**
 * This is a more generic "Note" class to use in all different read use cases.
 * Not all fields should be return in all read use cases, therefore we use JsonInclude.NOT_NULL.
 * This could have been solved by creating multiple variant of the Note class (e.g. NodeWithMetadataWithoutBody), but
 * this would led to additional mapping logic, etc. In this case I made the tradeoff to live with some null values
 * for the sake of simplicity.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoteWithMetadata extends Note {

    private Long id;
    private LocalDateTime created;
    private LocalDateTime lastModified;
    private String noteUrl;
    private String notebookUrl;
}
