package com.builtbroken.builder.loader;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.converter.strut.ConverterObjectBuilder;
import com.builtbroken.builder.data.DataFileLoad;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.handler.JsonObjectHandlerRegistry;
import com.builtbroken.builder.mapper.JsonMappingHandler;
import com.builtbroken.builder.pipe.PipeLine;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;
import java.util.function.Supplier;

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

    /**
     * Locators to find files that contain, or can be turned into, JSON data for loading
     */
    protected List<IFileLocator> fileLocators = new ArrayList();

    /**
     * Files loaded using the locators
     */
    protected Queue<DataFileLoad> loadedFiles = new LinkedList();

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

    public <C extends IJsonGeneratedObject> void registerObject(String type, Class<C> clazz, Function<JsonElement, C> factory)
    {
        if (clazz == null)
        {
            throw new IllegalArgumentException("ContentLoader: Can not provide an object type[" + type + "] for loading without a class");
        }
        else if (factory == null)
        {
            throw new IllegalArgumentException("ContentLoader: Can not provide an object type[" + type + "] for loading without a factory to create objects");
        }
        else if (type == null || type.trim().isEmpty())
        {
            throw new IllegalArgumentException("ContentLoader: Can not provide an object type[" + type + "] with an empty or null type name");
        }
        else if (type.split(":").length == 0)
        {
            throw new IllegalArgumentException("ContentLoader: Can not provide an object type[" + type + "] with an prefixing it with the package name. " +
                    "Ex: java:int, armory:gun. This helps keep the keys unique between packages. If you wish to bypass this register a converter directly.");
        }

        //Register
        conversionHandler.addConverter(new ConverterObjectBuilder<C>(type, clazz, factory));
        jsonMappingHandler.register(clazz, type);

        //Make sure we have a handler
        jsonObjectHandlerRegistry.createOrGetHandler(type);
    }

    /**
     * Call to setup the loader and get it ready
     * to called {@link #load()}
     */
    public void setup()
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
     * Called to load files using this content loader.
     * Make sure to called {@link #setup()}
     */
    public void load()
    {
        locateFiles();
        processFiles();
    }

    protected void locateFiles()
    {
        //TODO thread locators
        //TODO thread loading, use a concurrent queue to pass data between several threads

        for (IFileLocator locator : fileLocators)
        {
            loadedFiles.addAll(locator.search());
        }
    }

    protected void processFiles()
    {
        for (DataFileLoad fileLoad : loadedFiles)
        {
            //TODO validate
            //TODO try-catch with file details
            List<Object> out = pipeLine.handle(fileLoad.element, null); //TODO add metadata
        }
    }

    /**
     * Called to init the loader, use
     * this to reference resources, objects,
     * and validate settings.
     */
    protected void init()
    {
        pipeLine.init();
    }

    /**
     * Called to finish loading the loader, use
     * this to reference resources, objects,
     * and validate settings.
     */
    protected void loadComplete()
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
        clean();

        pipeLine.destroy();
        conversionHandler.destroy();
        jsonObjectHandlerRegistry.destroy();
        jsonMappingHandler.destroy();
    }

    /**
     * Called to clear extra data
     */
    public void clean()
    {
        loadedFiles.clear();
    }
}
