package com.builtbroken.builder;

import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.converter.primitives.*;
import com.builtbroken.builder.converter.strut.array.*;
import com.builtbroken.builder.loader.ContentLoader;


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

    private static boolean hasLoaded = false;
    private static boolean hasSetup = false;

    /**
     * Call to setup all the defaults for the main loader
     */
    public static void setup()
    {
        if (!hasSetup)
        {
            //Primitives
            getMainConverter().addConverter(new JsonConverterByte());
            getMainConverter().addConverter(new JsonConverterShort());
            getMainConverter().addConverter(new JsonConverterInt());
            getMainConverter().addConverter(new JsonConverterLong());

            getMainConverter().addConverter(new JsonConverterFloat());
            getMainConverter().addConverter(new JsonConverterDouble());

            getMainConverter().addConverter(new JsonConverterString());

            //Arrays
            getMainConverter().addConverter(new JsonConverterArray());
            getMainConverter().addConverter(new JsonConverterArrayString());

            getMainConverter().addConverter(new JsonConverterArrayByte());
            getMainConverter().addConverter(new JsonConverterArrayInt());
            getMainConverter().addConverter(new JsonConverterArrayShort());
            getMainConverter().addConverter(new JsonConverterArrayLong());

            getMainConverter().addConverter(new JsonConverterArrayFloat());
            getMainConverter().addConverter(new JsonConverterArrayDouble());
        }
    }

    /**
     * Call to trigger the main loader
     */
    public static void load()
    {
        if (!hasSetup)
        {
            setup();
        }
        if (!hasLoaded)
        {
            getMainLoader().init();
            getMainLoader().loadComplete();
        }
    }

    /**
     * Used entirely by JUnit testing to destroy
     * the main loader after each test
     */
    public static void destroy()
    {
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

    public static ConversionHandler getMainConverter()
    {
        if (MAIN_CONVERTER == null)
        {
            MAIN_CONVERTER = new ConversionHandler(null, "main");
        }
        return MAIN_CONVERTER;
    }

    public static ContentLoader getMainLoader()
    {
        if (MAIN_LOADER == null)
        {
            MAIN_LOADER = new ContentLoader("main");
        }
        return MAIN_LOADER;
    }
}
