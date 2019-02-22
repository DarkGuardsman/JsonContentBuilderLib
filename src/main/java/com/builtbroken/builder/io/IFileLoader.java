package com.builtbroken.builder.io;

import com.google.gson.JsonElement;

import java.io.File;
import java.io.Reader;
import java.util.List;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2/19/19.
 */
public interface IFileLoader
{

    String getSupportedExtension();

    default List<JsonElement> loadFile(Reader reader)
    {
        return null;
    }

    default List<JsonElement> loadFile(File file)
    {
        return null;
    }

    default boolean useReader()
    {
        return true;
    }
}
