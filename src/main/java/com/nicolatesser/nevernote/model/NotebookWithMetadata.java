package com.nicolatesser.nevernote.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
/**
 * This is a more generic "Notebook" class to use in all different read use cases.
 * Not all fields should be return in all read use cases, therefore we use JsonInclude.NOT_NULL.
 * This could have been solved by creating multiple variant of the Notebook class (e.g. NotebookWithNotesCount,
 * NotebookWithNotesMetadata), but this would led to additional mapping logic, etc. In this case I made the tradeoff
 * to live with some null values for the sake of simplicity.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotebookWithMetadata extends Notebook {

    private Long id;
    private Long numberOfNotes;
    private List<NoteWithMetadata> notes;
    private String notebookUrl;


}
