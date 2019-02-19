package com.builtbroken.builder.io;

import com.google.gson.JsonElement;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;

import java.io.Reader;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2/19/19.
 */
public class FileLoaderJSON implements IFileLoader
{

    @Override
    public String getSupportedExtension()
    {
        return "json";
    }

    @Override
    public JsonElement load(Reader reader)
    {
        JsonReader jsonReader = new JsonReader(reader);
        return Streams.parse(jsonReader);
    }
}
