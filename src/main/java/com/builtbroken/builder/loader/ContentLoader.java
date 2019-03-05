package com.builtbroken.builder.loader;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.pipe.PipeLine;

/**
 * Instance of a content loader.
 * <p>
 * Handles locating files, loading files as JSON, converting files to objects, and doing
 * any other tasks. This is done using sub objects such as the {@link PipeLine} to handle
 * all JSON and conversion process.
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 2/19/19.
 */
public class ContentLoader
{

    /**
     * Unique name of the content loader
     */
    public final String name;

    /**
     * Main pipe line to use for handling new JSON files
     */
    public final PipeLine pipeLine;

    /**
     * Conversion handler for this loader, wrappers to global
     */
    public final ConversionHandler conversionHandler;

    /**
     * Creates a new content loader with a default pipe line.
     * <p>
     * The defult pipe is designed to work with a set format
     * of JSON files. It expects to take in single JsonObjects
     * or arrays of JsonObjects. Each object should define a
     * type and some data to load.
     *
     * @param name - unique name of the loader
     */
    public ContentLoader(String name)
    {
        this(name, PipeLine.newDefault());
    }

    /**
     * Creates a new content loader
     *
     * @param name     - unique name of the loader
     * @param pipeLine - pipe line ot use
     */
    public ContentLoader(String name, PipeLine pipeLine)
    {
        this.name = name;
        this.pipeLine = pipeLine;
        this.pipeLine.contentLoader = this;
        this.conversionHandler = new ConversionHandler(ContentBuilderLib.MAIN_CONVERTER, name);
    }
}
