package com.builtbroken.builder.io;

import com.builtbroken.builder.data.DataFileLoad;
import com.builtbroken.builder.data.FileSource;
import com.builtbroken.builder.loader.file.FileCheckFunction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.Reader;
import java.util.function.Consumer;

/**
 * Created by Robin Seifert on 2/19/19.
 */
public interface IFileLoader
{

    /**
     * Extension this loader can support
     *
     * @return
     */
    String getSupportedExtension();

    /**
     * Called to load a file using a reader
     *
     * @param fileSource        - file source information
     * @param reader            - reader stream
     * @param fileConsumer      - handler for loaded files
     * @param fileCheckFunction - function to use to verify if the file or part of the file should be loaded
     * @return list of json read
     */
    default void loadFile(@Nullable FileSource fileSource, @Nonnull Reader reader, @Nonnull Consumer<DataFileLoad> fileConsumer, @Nullable FileCheckFunction fileCheckFunction)
    {

    }

    /**
     * Called to load a file
     *
     * @param file              - file to load and read
     * @param fileConsumer      - handler for loaded files
     * @param fileCheckFunction - function to use to verify if the file or part of the file should be loaded
     */
    default void loadFile(@Nonnull File file, @Nonnull Consumer<DataFileLoad> fileConsumer, @Nullable FileCheckFunction fileCheckFunction)
    {

    }

    default boolean useReader()
    {
        return true;
    }
}
