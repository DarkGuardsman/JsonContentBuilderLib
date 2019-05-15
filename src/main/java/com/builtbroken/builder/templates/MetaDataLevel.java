package com.builtbroken.builder.templates;

/**
 * Types of metadata
 * <p>
 * Used to track the differences in usage of some information. While allowing
 * reuse of a single JsonObject template for several use cases.
 * <p>
 * Example of this is author data which may be attached to a single file,
 * an entire folder, or an entire project.
 * <p>
 * Reason for this method is that JSON objects never know what file or folders
 * they were generated from orginally. This is due to the ability to switch out
 * the files at runtime and load them from several sources. Including from
 * local storage, remove storage, runtime generated, databases, REST APIs, and
 * many more options. This results in the inability to define the information
 * in terms of location requiring additional metadata to be used.
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-15.
 */
public enum MetaDataLevel
{
    FILE,
    FOLDER,
    PROJECT,
    OBJECT
}
