package com.nicolatesser.nevernote.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder(toBuilder = true)
/**
 * This is the base "Notebook" class, the one used to create/update a notebook, without metadata.
 */
public class Notebook {

    private String name;

}
