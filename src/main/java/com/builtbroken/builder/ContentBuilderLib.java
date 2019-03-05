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

    public static final ConversionHandler MAIN_CONVERTER = new ConversionHandler(null, "main");
    public static final ContentLoader MAIN_LOADER = new ContentLoader("main");

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
            MAIN_CONVERTER.addConverter(new JsonConverterByte());
            MAIN_CONVERTER.addConverter(new JsonConverterShort());
            MAIN_CONVERTER.addConverter(new JsonConverterInt());
            MAIN_CONVERTER.addConverter(new JsonConverterLong());

            MAIN_CONVERTER.addConverter(new JsonConverterFloat());
            MAIN_CONVERTER.addConverter(new JsonConverterDouble());

            MAIN_CONVERTER.addConverter(new JsonConverterString());

            //Arrays
            MAIN_CONVERTER.addConverter(new JsonConverterArray());
            MAIN_CONVERTER.addConverter(new JsonConverterString());

            MAIN_CONVERTER.addConverter(new JsonConverterArrayByte());
            MAIN_CONVERTER.addConverter(new JsonConverterArrayInt());
            MAIN_CONVERTER.addConverter(new JsonConverterArrayShort());
            MAIN_CONVERTER.addConverter(new JsonConverterArrayLong());

            MAIN_CONVERTER.addConverter(new JsonConverterArrayFloat());
            MAIN_CONVERTER.addConverter(new JsonConverterArrayDouble());
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

        }
    }

}
