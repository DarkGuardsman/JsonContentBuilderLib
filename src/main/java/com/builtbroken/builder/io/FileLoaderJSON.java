package com.builtbroken.builder.io;

import com.builtbroken.builder.data.DataFileLoad;
import com.builtbroken.builder.data.FileSource;
import com.builtbroken.builder.loader.file.FileCheckFunction;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.sun.istack.internal.NotNull;

import javax.annotation.Nullable;
import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.function.Consumer;

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
    public void loadFile(@Nullable FileSource source, @NotNull Reader reader, @NotNull Consumer<DataFileLoad> fileConsumer, @Nullable FileCheckFunction fileCheckFunction)
    {
        JsonReader jsonReader = new JsonReader(reader);
        fileConsumer.accept(new DataFileLoad(source, Streams.parse(jsonReader)));
    }

    /**
     * Creates an json element from a string
     *
     * @param data - string data, needs to be formatted correctly,
     *             e.g. { "content" : { "more":"content"} }
     * @return json element
     * @throws JsonSyntaxException - if string is not formatted correctly
     */
    public static JsonElement createElement(String data)
    {
        JsonReader jsonReader = new JsonReader(new StringReader(data));
        return Streams.parse(jsonReader);
    }
}
