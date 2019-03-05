package com.builtbroken.builder;

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

    public static final ContentLoader MAIN_LOADER = new ContentLoader("main");

    private static boolean hasLoaded = false;

    public static void load()
    {
        if (!hasLoaded)
        {

        }
    }

}
