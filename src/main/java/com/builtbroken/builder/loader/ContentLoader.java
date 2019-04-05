package com.builtbroken.builder.loader;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.handler.JsonObjectHandlerRegistry;
import com.builtbroken.builder.mapper.JsonMappingHandler;
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
     * Deals with registering and tracking handlers of objects
     */
    public final JsonObjectHandlerRegistry jsonObjectHandlerRegistry;

    /**
     * Deals with mapping data to json objects
     */
    public final JsonMappingHandler jsonMappingHandler;

    private boolean hasSetup = false;
    private boolean hasLoaded = false;

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
        this.jsonObjectHandlerRegistry = new JsonObjectHandlerRegistry();
        this.pipeLine.contentLoader = this;
        this.conversionHandler = new ConversionHandler(ContentBuilderLib.getMainConverter(), name);
        this.jsonMappingHandler = new JsonMappingHandler(this);
    }

    /**
     * Call to trigger the main loader
     */
    public void load()
    {
        if (!hasSetup)
        {
            ContentBuilderLib.setupDefault(this);
        }
        if (!hasLoaded)
        {
            init();
            loadComplete();
        }
    }

    /**
     * Called to init the loader, use
     * this to reference resources, objects,
     * and validate settings.
     */
    public void init()
    {
        pipeLine.init();
    }

    /**
     * Called to finish loading the loader, use
     * this to reference resources, objects,
     * and validate settings.
     */
    public void loadComplete()
    {
        pipeLine.loadComplete();
    }

    /**
     * Called to destroy the loader. Mainly
     * exists to cleanup for JUnit or after
     * loader is no longer needed.
     */
    public void destroy()
    {
        pipeLine.destroy();
        conversionHandler.destroy();
        jsonObjectHandlerRegistry.destroy();
        jsonMappingHandler.destroy();
    }
}
