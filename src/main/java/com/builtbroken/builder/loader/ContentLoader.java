package com.builtbroken.builder.loader;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.converter.strut.ConverterObjectBuilder;
import com.builtbroken.builder.data.DataFileLoad;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.handler.JsonObjectHandlerRegistry;
import com.builtbroken.builder.loader.file.IFileLocator;
import com.builtbroken.builder.mapper.JsonMappingHandler;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.builtbroken.builder.mapper.builder.IJsonBuilder;
import com.builtbroken.builder.mapper.mappers.JsonClassMapper;
import com.builtbroken.builder.pipe.PipeLine;
import com.google.gson.JsonElement;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.BiConsumer;
import java.util.function.Function;

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

    /**
     * Temp cache of objects generated, will be cleared on complete
     * and is only designed for loaders that might only load 1 object type
     */
    protected List<Object> generatedObjects = new ArrayList();

    /**
     * Function to use for outputting logs. Allows switching
     * the builder's logger without hard deps on any logger
     * implementation.
     */
    protected BiConsumer<String, String> logger;

    public int filesLocated = 0;
    public int filesProcessed = 0;
    public int objectsGenerated = 0;

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
        logger = (prefix, msg) -> System.out.println("ContentLoader[" + name + "]:" + prefix + " >> " + msg);
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
     * Called to register a class as an object template
     *
     * @param type    - name of the template, Ex: armory:sword
     * @param clazz   - clazz that the template is created as
     * @param factory - builder to generate the template, can be null if the class uses {@link com.builtbroken.builder.mapper.anno.JsonConstructor}
     * @param <C>     - its expected the class be instance of IJsonGeneratedObject
     */
    public <C extends IJsonGeneratedObject> void registerObjectTemplate(String type, Class<C> clazz, Function<JsonElement, C> factory)
    {
        if (clazz == null)
        {
            throw new IllegalArgumentException("ContentLoader: Can not provide an object type[" + type + "] for loading without a class");
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

        //Map
        final JsonClassMapper mapper = jsonMappingHandler.register(clazz, type);

        //Check for factory
        if (factory == null)
        {
            final IJsonBuilder builder = mapper.getBuilder(type);
            if (builder == null)
            {
                throw new IllegalArgumentException("ContentLoader: Can not provide an object type[" + type + "] for loading without a factory to create objects");
            }

            //Generate factory for class
            factory = (jsonElement) ->
            {
                try
                {
                    return (C) builder.newObject(jsonElement, conversionHandler);
                } catch (Exception e)
                {
                    throw new RuntimeException("Failed to build object of type[" + type + "]" + clazz, e);
                }
            };
        }

        //Register
        conversionHandler.addConverter(new ConverterObjectBuilder<C>(type, clazz, factory));


        //Make sure we have a handler
        jsonObjectHandlerRegistry.createOrGetHandler(type);

        //DEBUG-LOGGING
        logger.accept("registerObject", "Type: '" + type + "' Class: '" + clazz + "'");
    }

    /**
     * Adds a file locator to search for json files
     *
     * @param locator
     */
    public void addFileLocator(IFileLocator locator)
    {
        logger.accept("addFileLocator", "" + locator);
        fileLocators.add(locator);
    }

    /**
     * Call to setup the loader and get it ready
     * to called {@link #load()}
     */
    public void setup()
    {
        //DEBUG-LOGGING
        logger.accept("setup", "start");

        //Trigger setup defaults if not run
        if (!hasSetup)
        {
            //DEBUG-LOGGING
            logger.accept("setup", "setup phase has not run, applying defaults");

            //Defaults
            ContentBuilderLib.setupDefault(this);
        }

        //Trigger loading if not run
        if (!hasLoaded)
        {
            //DEBUG-LOGGING
            logger.accept("setup", "load phase has not run, triggering init");

            //Run
            init();
            loadComplete();
        }

        //DEBUG-LOGGING
        logger.accept("setup", "end");
    }

    /**
     * Called to load files using this content loader.
     * Make sure to called {@link #setup()}
     */
    public void load()
    {
        //DEBUG-LOGGING
        logger.accept("load", "start");

        //Run
        locateFiles();
        processFiles();

        //DEBUG-LOGGING
        logger.accept("load", "end");
    }

    protected void locateFiles()
    {
        //TODO thread locators, each one should be able to run on its own
        //TODO thread loading, use a concurrent queue to pass data between several worker threads
        //  If threaded, mapper has to wait until all objects are created or else it can fail
        //      Idea would be to use the thread to allow dumping file data early to save memory
        //      As well allow speeding up loading of JSON data and converting it into objects
        //      Only issue is that the autowiring has to wait until the end since it could need
        //          objects not loaded yet or currently being loaded in another thread. Same
        //          goes for any information that might depend on accessing or editing other objects.

        //DEBUG-LOGGING
        logger.accept("locateFiles", "start");

        //Loop loaders to find files to process
        for (IFileLocator locator : fileLocators)
        {
            //DEBUG-LOGGING
            final int prev = loadedFiles.size();
            logger.accept("locateFiles", locator.toString() + " start search");

            //Do search
            loadedFiles.addAll(locator.search());

            //DEBUG-LOGGING
            final int found = (loadedFiles.size() - prev);
            logger.accept("locateFiles", locator.toString() + " found " + found + " new file" + (found > 1 ? "s" : ""));

            logger.accept("locateFiles", locator.toString() + " end search");
        }

        filesLocated = loadedFiles.size();

        //DEBUG-LOGGING
        logger.accept("locateFiles", "located " + filesLocated + " file" + (filesLocated > 1 ? "s" : ""));
        logger.accept("locateFiles", "end");
    }

    protected void processFiles()
    {
        for (DataFileLoad fileLoad : loadedFiles)
        {
            try
            {
                //DEBUG-LOGGING
                logger.accept("processing", "[" + filesProcessed + "]  start processing data load: " + fileLoad);

                final List<Object> out = pipeLine.handle(fileLoad.element, null); //TODO add metadata

                //Add to global list of generated objects
                out.stream().filter(obj -> obj != null).forEach(obj -> generatedObjects.add(obj));

                //count generated objects for debug
                objectsGenerated += out.size();

                //DEBUG-LOGGING
                logger.accept("processing", "[" + filesProcessed + "]  generated " + out.size() + " object" + (out.size() > 1 ? "s" : ""));

                //DEBUG-LOGGING
                logger.accept("processing", "[" + filesProcessed + "] end processing data load: " + fileLoad);

                //Count file is completed
                filesProcessed++;
            } catch (Exception e)
            {
                new RuntimeException("ContentLoader: Unexpected error while processing data file load: " + fileLoad, e);
            }
        }
    }

    /**
     * Called to init the loader, use
     * this to reference resources, objects,
     * and validate settings.
     */
    protected void init()
    {
        //DEBUG-LOGGING
        logger.accept("init", "start");

        pipeLine.init();

        //DEBUG-LOGGING
        logger.accept("init", "end");
    }

    /**
     * Called to finish loading the loader, use
     * this to reference resources, objects,
     * and validate settings.
     */
    protected void loadComplete()
    {
        //DEBUG-LOGGING
        logger.accept("loadComplete", "start");

        pipeLine.loadComplete();

        //DEBUG-LOGGING
        logger.accept("loadComplete", "end");
    }

    /**
     * Called to destroy the loader. Mainly
     * exists to cleanup for JUnit or after
     * loader is no longer needed.
     */
    public void destroy()
    {
        //DEBUG-LOGGING
        logger.accept("destroy", "start");

        clean();

        pipeLine.destroy();
        conversionHandler.destroy();
        jsonObjectHandlerRegistry.destroy();
        jsonMappingHandler.destroy();

        //DEBUG-LOGGING
        logger.accept("destroy", "end");
    }

    /**
     * Called to clear extra data
     */
    public void clean()
    {
        //DEBUG-LOGGING
        logger.accept("clean", "start");

        loadedFiles.clear();

        //DEBUG-LOGGING
        logger.accept("clean", "end");
    }
}
