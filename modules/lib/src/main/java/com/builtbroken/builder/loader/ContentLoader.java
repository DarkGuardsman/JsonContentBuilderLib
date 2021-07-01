package com.builtbroken.builder.loader;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.converter.strut.ConverterObjectBuilder;
import com.builtbroken.builder.data.DataFileLoad;
import com.builtbroken.builder.data.GeneratedObject;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.events.EventSystem;
import com.builtbroken.builder.events.imp.FileLocatorAddedEvent;
import com.builtbroken.builder.handler.JsonObjectHandlerRegistry;
import com.builtbroken.builder.loader.file.IFileLocator;
import com.builtbroken.builder.mapper.JsonMappingHandler;
import com.builtbroken.builder.mapper.anno.JsonTemplate;
import com.builtbroken.builder.mapper.builder.IJsonBuilder;
import com.builtbroken.builder.mapper.mappers.JsonClassMapper;
import com.builtbroken.builder.pipe.PipeLine;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Function;

/**
 * Instance of a content loader.
 * <p>
 * Handles locating files, loading files as JSON, converting files to objects, and doing
 * any other tasks. This is done using sub objects such as the {@link PipeLine} to handle
 * all JSON and conversion process.
 * <p>
 * Created by Robin Seifert on 2/19/19.
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
    protected final PipeLine objectCreationPipeline;

    /** Additional pipelines to run after objects are created. Each pipe feeds the next. */
    protected final List<PipeLine> pipelines = new ArrayList();

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
    public final List<Object> generatedObjects = new ArrayList();

    public final EventSystem eventSystem = new EventSystem();

    public int filesLocated = 0;
    public int filesProcessed = 0;
    public int objectsGenerated = 0;

    private boolean hasSetup = false;
    private boolean hasLoaded = false;

    /**
     * Creates a new content loader
     *
     * @param name                   - unique name of the loader
     * @param objectCreationPipeline - pipe line ot use
     */
    public ContentLoader(String name, PipeLine objectCreationPipeline)
    {
        this.name = name;
        this.objectCreationPipeline = objectCreationPipeline;
        this.jsonObjectHandlerRegistry = new JsonObjectHandlerRegistry();
        this.objectCreationPipeline.contentLoader = this;
        this.conversionHandler = new ConversionHandler(ContentBuilderLib.getMainConverter(), name);
        this.jsonMappingHandler = new JsonMappingHandler(this);
    }

    /**
     * Called to register a class as an object template
     *
     * @param clazz - clazz that the template is created as, must contain {@link JsonTemplate}
     * @param <C>   - its expected the class be instance of IJsonGeneratedObject
     */
    public <C extends IJsonGeneratedObject> void registerObjectTemplate(Class<C> clazz)
    {
        final JsonTemplate jsonConstructor = clazz.getAnnotation(JsonTemplate.class);
        if (jsonConstructor != null)
        {
            final String registryID = !jsonConstructor.registry().trim().isEmpty() ? jsonConstructor.registry() : jsonConstructor.value();
            registerObjectTemplate(jsonConstructor.value(), registryID, clazz, null);
        }
        else
        {
            throw new RuntimeException("ContentLoader: Registering an object template with only a class requires the class to use the JsonTemplate annotation");
        }
    }

    /**
     * Called to register a class as an object template
     *
     * @param templateID - unique id of the template, Ex: armory:sword
     * @param registryID - unique id of the registry to use, this can be shared between templates but only 1 registry should exist per ID
     * @param clazz      - clazz that the template is created as
     * @param factory    - builder to generate the template, can be null if the class uses {@link com.builtbroken.builder.mapper.anno.JsonConstructor}
     * @param <C>        - its expected the class be instance of IJsonGeneratedObject
     */
    public <C extends IJsonGeneratedObject> void registerObjectTemplate(final String templateID, final String registryID, final Class<C> clazz, final Function<JsonElement, C> factory)
    {
        if (clazz == null)
        {
            throw new IllegalArgumentException(String.format("ContentLoader: clazz is required for registering a template of type[%s]", templateID));
        }
        else if (templateID == null || templateID.trim().isEmpty())
        {
            throw new IllegalArgumentException(String.format("ContentLoader: templateID is required for registering a template for class[%s]", clazz));
        }
        else if (templateID.split(":").length <= 1)
        {
            throw new IllegalArgumentException(String.format("ContentLoader: templateID is required to have a prefix of 'domain:' for registering a template for class[%s]", clazz));
        }
        else if (templateID.chars().anyMatch(Character::isUpperCase))
        {
            throw new IllegalArgumentException(String.format("ContentLoader: templateID is required to be lower cased for registering a template for class[%s]", clazz));
        }
        else if (registryID == null || registryID.trim().isEmpty())
        {
            throw new IllegalArgumentException(String.format("ContentLoader: registryID is required for registering a template for class[%s]", clazz));
        }
        else if (registryID.split(":").length <= 1)
        {
            throw new IllegalArgumentException(String.format("ContentLoader: registryID is required to have a prefix of 'domain:' for registering a template for class[%s]", clazz));
        }
        else if (registryID.chars().anyMatch(Character::isUpperCase))
        {
            throw new IllegalArgumentException(String.format("ContentLoader: registryID is required to be lower cased for registering a template for class[%s]", clazz));
        }

        //Map
        final JsonClassMapper mapper = jsonMappingHandler.register(clazz, templateID);

        //Check for factory
        if (factory == null)
        {
            final IJsonBuilder builder = mapper.getBuilder(templateID);
            if (builder == null)
            {
                throw new IllegalArgumentException("ContentLoader: Can not provide an object type[" + templateID + "] for loading without a factory to create objects");
            }

            //Generate factory for class
            conversionHandler.addConverter(new ConverterObjectBuilder<C>(templateID.toLowerCase(), clazz, (jsonElement) ->
            {
                try
                {
                    return (C) builder.newObject(jsonElement, conversionHandler);
                }
                catch (Exception e)
                {
                    throw new RuntimeException("Failed to build object of type[" + templateID + "]" + clazz, e);
                }
            }));
        }
        else
        {
            conversionHandler.addConverter(new ConverterObjectBuilder<C>(templateID, clazz, factory));
        }

        //Make sure we have a handler
        jsonObjectHandlerRegistry.createOrGetHandler(registryID);

        //DEBUG-LOGGING
        //TODO add event getLogger().accept("registerObject", String.format("TemplateID: '%s' RegistryID: '%s' Class: '%s'", templateID, registryID, clazz));
    }

    /**
     * Adds a file locator to search for json files
     *
     * @param locator
     */
    public void addFileLocator(IFileLocator locator)
    {
        fileLocators.add(locator);
        eventSystem.fireEvent(new FileLocatorAddedEvent(this, locator));
    }

    /**
     * Call to setup the loader and get it ready
     * to called {@link #load()}
     */
    public void setup()
    {
        //DEBUG-LOGGING
        //TODO add event getLogger().accept("setup", "start");

        //Trigger setup defaults if not run
        if (!hasSetup)
        {
            //DEBUG-LOGGING
            //TODO add event getLogger().accept("setup", "setup phase has not run, applying defaults");

            //Defaults
            ContentBuilderLib.setupDefault(this);

            hasSetup = true;
        }

        //Trigger loading if not run
        if (!hasLoaded)
        {
            //DEBUG-LOGGING
            //TODO add event getLogger().accept("setup", "load phase has not run, triggering init");

            //Run
            init();
            loadComplete();

            hasLoaded = true;
        }

        //DEBUG-LOGGING
        //TODO add event getLogger().accept("setup", "end");
    }

    /**
     * Called to load files using this content loader.
     * Make sure to called {@link #setup()}
     */
    public void load()
    {
        //DEBUG-LOGGING
        //TODO add event getLogger().accept("load", "start");

        //Run
        locateFiles();
        processFiles();
        processObjects();

        //DEBUG-LOGGING
        //TODO add event getLogger().accept("load", "end");
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
        //TODO add event getLogger().accept("locateFiles", "start");

        //Loop loaders to find files to process
        for (IFileLocator locator : fileLocators)
        {
            //DEBUG-LOGGING
            final int prev = loadedFiles.size();
            //TODO add event getLogger().accept("locateFiles", locator.toString() + " start search");

            //Do search
            loadedFiles.addAll(locator.search());

            //DEBUG-LOGGING
            final int found = (loadedFiles.size() - prev);
            //TODO add event getLogger().accept("locateFiles", locator.toString() + " found " + found + " new file" + (found > 1 ? "s" : ""));

            //TODO add event getLogger().accept("locateFiles", locator.toString() + " end search");
        }

        filesLocated = loadedFiles.size();

        //DEBUG-LOGGING
        //TODO add event getLogger().accept("locateFiles", "located " + filesLocated + " file" + (filesLocated > 1 ? "s" : ""));
        //TODO add event getLogger().accept("locateFiles", "end");
    }

    protected void processFiles()
    {
        for (DataFileLoad fileLoad : loadedFiles)
        {
            try
            {
                //DEBUG-LOGGING
                //TODO add event getLogger().accept("processing", "[" + filesProcessed + "]  start processing data load: " + fileLoad);

                final List<Object> out = objectCreationPipeline.handle(fileLoad.element, null, null); //TODO add metadata

                //Add to global list of generated objects
                out.stream().filter(Objects::nonNull).forEach(generatedObjects::add);

                //count generated objects for debug
                objectsGenerated += out.size();

                //DEBUG-LOGGING
                //TODO add event getLogger().accept("processing", "[" + filesProcessed + "]  generated " + out.size() + " object" + (out.size() > 1 ? "s" : ""));

                //DEBUG-LOGGING
                //TODO add event getLogger().accept("processing", "[" + filesProcessed + "] end processing data load: " + fileLoad);

                //Count file is completed
                filesProcessed += 1;
            }
            catch (Exception e)
            {
                throw new RuntimeException("ContentLoader: Unexpected error while processing data file load: " + fileLoad, e);
            }
        }
    }

    protected void processObjects()
    {
        for (PipeLine pipeLine : pipelines)
        {
            int index = 0;
            for (Object object : generatedObjects)
            {
                //DEBUG-LOGGING
                //TODO add event getLogger().accept("post-processing", "[" + (index++) + "]  processing: " + object);

                List<Object> out;

                if (object instanceof GeneratedObject)
                {
                    out = pipeLine.handle(((GeneratedObject) object).jsonUsed, object, null); //TODO add metadata
                }
                else
                {
                    out = pipeLine.handle(null, object, null); //TODO add metadata
                }

                //Add to global list of generated objects
                out.stream()
                        .filter(Objects::nonNull)
                        .filter(o -> o != object)
                        .forEach(generatedObjects::add);

                //count generated objects for debug
                objectsGenerated += out.stream().filter(o -> o != object).count();

                //DEBUG-LOGGING
                //TODO add event getLogger().accept("processing", "[" + filesProcessed + "]  generated " + out.size() + " object" + (out.size() > 1 ? "s" : ""));


                //DEBUG-LOGGING
                //TODO add event getLogger().accept("post-processing", "[" + (index++) + "]  ending: " + object);
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
        //TODO add event getLogger().accept("init", "start");

        addPipes();
        objectCreationPipeline.init();
        objectCreationPipeline.contentLoader = this;
        pipelines.forEach(PipeLine::init);
        pipelines.forEach(pipe -> pipe.contentLoader = this);

        //DEBUG-LOGGING
        //TODO add event getLogger().accept("init", "end");
    }

    protected void addPipes()
    {

    }

    /**
     * Called to finish loading the loader, use
     * this to reference resources, objects,
     * and validate settings.
     */
    protected void loadComplete()
    {
        //DEBUG-LOGGING
        //TODO add event getLogger().accept("loadComplete", "start");

        objectCreationPipeline.loadComplete();
        pipelines.forEach(PipeLine::loadComplete);

        //DEBUG-LOGGING
        //TODO add event getLogger().accept("loadComplete", "end");
    }

    /**
     * Called to destroy the loader. Mainly
     * exists to cleanup for JUnit or after
     * loader is no longer needed.
     */
    public void destroy()
    {
        //DEBUG-LOGGING
        //TODO add event getLogger().accept("destroy", "start");

        clean();

        objectCreationPipeline.destroy();
        pipelines.forEach(PipeLine::destroy);
        conversionHandler.destroy();
        jsonObjectHandlerRegistry.destroy();
        jsonMappingHandler.destroy();

        //DEBUG-LOGGING
        //TODO add event getLogger().accept("destroy", "end");
    }

    /**
     * Called to clear extra data
     */
    public void clean()
    {
        //DEBUG-LOGGING
        //TODO add event getLogger().accept("clean", "start");

        loadedFiles.clear();

        //DEBUG-LOGGING
        //TODO add event getLogger().accept("clean", "end");
    }
}
