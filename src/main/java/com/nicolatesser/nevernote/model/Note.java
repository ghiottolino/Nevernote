package com.nicolatesser.nevernote.model;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
/**
 * This is the base "Note" class, the one used to create/update a note, without metadata.
 */
public class Note {

    private String title;
    private String body;
    private Set<String> tags;

}
