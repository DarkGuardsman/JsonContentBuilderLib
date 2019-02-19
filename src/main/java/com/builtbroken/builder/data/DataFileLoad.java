package com.builtbroken.builder.data;

import com.google.gson.JsonElement;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2/19/19.
 */
public class DataFileLoad
{

    public final FileSource fileSource;
    public final JsonElement element;

    public DataFileLoad(String fileName, JsonElement element)
    {
        fileSource = new FileSource();
        fileSource.file = fileName;
        this.element = element;
    }
    public DataFileLoad(FileSource fileSource, JsonElement element)
    {
        this.fileSource = fileSource;
        this.element = element;
    }
}
