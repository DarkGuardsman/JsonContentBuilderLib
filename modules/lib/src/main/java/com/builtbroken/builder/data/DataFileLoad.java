package com.builtbroken.builder.data;

import com.google.gson.JsonElement;

import java.io.File;
import java.net.URL;

/**
 * Created by Robin Seifert on 2/19/19.
 */
public class DataFileLoad
{

    public final FileSource fileSource;
    public final JsonElement element;

    public DataFileLoad(File file, JsonElement element)
    {
        fileSource = new FileSource(file);
        this.element = element;
    }

    public DataFileLoad(URL file, JsonElement element)
    {
        fileSource = new FileSource(file);
        this.element = element;
    }

    public DataFileLoad(FileSource fileSource, JsonElement element)
    {
        this.fileSource = fileSource;
        this.element = element;
    }

    @Override
    public String toString()
    {
        return "DataFileLoad[" + fileSource + "]@" + hashCode();
    }
}
