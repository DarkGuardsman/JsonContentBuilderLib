package com.builtbroken.builder.pipe.nodes;

/**
 * Created by Robin Seifert on 2/27/19.
 */
public enum NodeType
{
    /**
     * Node that only edits the input JSON. Generates no objects outside of rare cases of
     * generating JSON from template inputs or splitting.
     */
    JSON_EDITOR,
    /**
     * Takes the JSON and uses it to generate objects
     */
    BUILDER,
    /**
     * Uses the object and JSON to map out information. As well link objects to each other.
     */
    MAPPER,
    /**
     * Runs after everything has been loaded, built, and mapped. Handles linking objects
     * and doing post validation of objects. As well data completion if needed
     */
    POST
}
