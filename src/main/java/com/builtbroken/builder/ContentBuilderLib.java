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
