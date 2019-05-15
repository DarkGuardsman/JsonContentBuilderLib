package com.builtbroken.builder;

import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.converter.primitives.*;
import com.builtbroken.builder.converter.strut.array.*;
import com.builtbroken.builder.converter.strut.map.JsonConverterMap;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.loader.ContentLoader;
import com.builtbroken.builder.mapper.anno.JsonTemplate;

import java.util.HashMap;
import java.util.ServiceLoader;


/**
 * Main class to handle all interaction with the system.
 * <p>
 * Use this to trigger the loading or request manual loading of files. It will be
 * able to locate the exact sub-systems to use for loading the data.
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 2/19/19.
 */
public class ContentBuilderLib
{

    private static ConversionHandler MAIN_CONVERTER;
    private static ContentLoader MAIN_LOADER;

    private static final HashMap<String, ContentLoader> loaders = new HashMap();

    /**
     * Call to setup all the defaults for the main loader
     */
    public static void setupDefault(ContentLoader loader)
    {
        //Primitives
        loader.conversionHandler.addConverter(new JsonConverterByte());
        loader.conversionHandler.addConverter(new JsonConverterShort());
        loader.conversionHandler.addConverter(new JsonConverterInt());
        loader.conversionHandler.addConverter(new JsonConverterLong());

        loader.conversionHandler.addConverter(new JsonConverterFloat());
        loader.conversionHandler.addConverter(new JsonConverterDouble());

        loader.conversionHandler.addConverter(new JsonConverterString());

        //Arrays
        loader.conversionHandler.addConverter(new JsonConverterArray());
        loader.conversionHandler.addConverter(new JsonConverterArrayString());

        loader.conversionHandler.addConverter(new JsonConverterArrayByte());
        loader.conversionHandler.addConverter(new JsonConverterArrayInt());
        loader.conversionHandler.addConverter(new JsonConverterArrayShort());
        loader.conversionHandler.addConverter(new JsonConverterArrayLong());

        loader.conversionHandler.addConverter(new JsonConverterArrayFloat());
        loader.conversionHandler.addConverter(new JsonConverterArrayDouble());

        //Data Structures
        loader.conversionHandler.addConverter(new JsonConverterMap());
    }

    /**
     * Called to do the setup of the entire system and all loaders
     */
    public static void setup()
    {
        //Make sure main loader exists
        getMainLoader();

        //Load templates from services
        final ServiceLoader<IJsonGeneratedObject> templates = ServiceLoader.load(IJsonGeneratedObject.class);
        for (IJsonGeneratedObject generatedObject : templates)
        {
            final Class<? extends IJsonGeneratedObject> clazz = generatedObject.getClass();
            final JsonTemplate jsonConstructor = clazz.getAnnotation(JsonTemplate.class);
            if (jsonConstructor != null)
            {
                getMainLoader().registerObjectTemplate(jsonConstructor.type(), clazz, null);
            }
            else
            {
                getMainLoader().registerObjectTemplate(generatedObject.getJsonType(), clazz, null);
            }
        }

        loaders.values().forEach(loader -> loader.setup());
    }

    /**
     * Called to trigger all loaders to load data
     */
    public static void load()
    {
        loaders.values().forEach(loader -> loader.load());
    }

    /**
     * Used entirely by JUnit testing to destroy
     * the main loader after each test
     */
    public static void destroy()
    {
        loaders.values().forEach(loader -> loader.destroy());
        loaders.clear();

        if (MAIN_LOADER != null)
        {
            MAIN_LOADER.destroy();
        }
        if (MAIN_CONVERTER != null)
        {
            MAIN_CONVERTER.destroy();
        }
        MAIN_LOADER = null;
        MAIN_CONVERTER = null;
    }

    /**
     * Called to add a loader to the public space. This is
     * required for some automatic features to function.
     * Such as {@link com.builtbroken.builder.mapper.anno.JsonTemplate}
     * automatic registration to a loader.
     *
     * @param loader - loader to add
     */
    public static void addLoader(ContentLoader loader)
    {
        loaders.put(loader.name.toLowerCase(), loader);
    }

    /**
     * Gets publicly exposed loaders
     *
     * @param id - unique id of the loader
     * @return loader, "main" will always return {@link #getMainLoader()}
     */
    public static ContentLoader getLoader(String id)
    {
        if (id.equalsIgnoreCase(ContentBuilderRefs.MAIN_LOADER))
        {
            return getMainLoader();
        }
        return loaders.get(id.toLowerCase());
    }

    public static ConversionHandler getMainConverter()
    {
        if (MAIN_CONVERTER == null)
        {
            MAIN_CONVERTER = new ConversionHandler(null, ContentBuilderRefs.MAIN_LOADER);
        }
        return MAIN_CONVERTER;
    }

    public static ContentLoader getMainLoader()
    {
        if (MAIN_LOADER == null)
        {
            MAIN_LOADER = new ContentLoader(ContentBuilderRefs.MAIN_LOADER);
            addLoader(MAIN_LOADER);
        }
        return MAIN_LOADER;
    }
}
