package com.builtbroken.builder.io;

import com.builtbroken.builder.data.DataFileLoad;
import com.builtbroken.builder.data.FileSource;
import com.builtbroken.builder.loader.file.FileCheckFunction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * Handles loading all files from the file system into data structures for use
 * Created by Dark(DarkGuardsman, Robert) on 2/19/19.
 */
public class FileLoaderHandler //TODO make this instance based instead of global
{

    private static HashMap<String, IFileLoader> fileLoaders = new HashMap();

    static
    {
        addFileLoader(new FileLoaderJSON());
        addFileLoader(new FileLoaderJar());
    }

    /**
     * Called to add a new handler for loading a file type
     *
     * @param fileLoader
     */
    public static void addFileLoader(@Nonnull final IFileLoader fileLoader)
    {
        fileLoaders.put(fileLoader.getSupportedExtension(), fileLoader);
    }

    /**
     * Called to locate and load all files
     *
     * @param file - file or folder
     * @return list of loaded files with their read in json data
     */
    @Deprecated
    public static List<DataFileLoad> loadFile(@Nonnull final File file)
    {
        List<DataFileLoad> dataEntries = new ArrayList();
        loadFile(file, (dataLoad) -> dataEntries.add(dataLoad), null);
        return dataEntries;
    }

    /**
     * Called to locate and load all files
     *
     * @param file         - file or folder
     * @param fileConsumer - handler for loaded files
     * @return list of loaded files with their read in json data
     */
    public static void loadFile(@Nonnull final File file,
                                @Nonnull final Consumer<DataFileLoad> fileConsumer,
                                @Nullable final FileCheckFunction fileCheckFunction)
    {
        if (file.isDirectory())
        {
            loadFolder(file, fileConsumer, fileCheckFunction);
        }
        else if (fileCheckFunction == null || fileCheckFunction.loadFile(file, null))
        {
            loadResourcesFromFile(file, fileConsumer, fileCheckFunction);
        }
    }

    /**
     * Called to locate and load all files in the current folder
     *
     * @param currentFolder     - current folder
     * @param fileConsumer      - handler for loaded files
     * @param fileCheckFunction - function to check if a folder or file should be loaded
     */
    public static void loadFolder(@Nonnull final File currentFolder,
                                  @Nonnull final Consumer<DataFileLoad> fileConsumer,
                                  @Nullable final FileCheckFunction fileCheckFunction)
    {
        for (File subFolderFile : currentFolder.listFiles())
        {
            if (subFolderFile.isDirectory())
            {
                if (fileCheckFunction == null || fileCheckFunction.loadFile(subFolderFile, null))
                {
                    loadFolder(subFolderFile, fileConsumer, fileCheckFunction);
                    //TODO build path from main search folder and pass that into check instead
                    //Ex: /contents instead of C:/user/install/folder/contents
                }
            }
            else if (fileCheckFunction == null || fileCheckFunction.loadFile(subFolderFile, null))
            {
                loadResourcesFromFile(subFolderFile, fileConsumer, fileCheckFunction);
            }
        }
    }

    /**
     * Loads a files from the resource path
     *
     * @param resource - resource location
     * @return json file as a json element object
     * @throws IOException
     */
    public static void loadFile(@Nonnull final URL resource,
                                @Nonnull final Consumer<DataFileLoad> fileConsumer,
                                @Nullable final FileCheckFunction fileCheckFunction)
    {
        if (resource != null)
        {
            final String extension = resource.toString().substring(resource.toString().lastIndexOf(".") + 1).toLowerCase();

            try
            {
                getLoaderFor(extension).loadFile(null, new InputStreamReader(resource.openStream()),
                        fileConsumer, fileCheckFunction);
            }
            catch (Exception e)
            {
                throw new RuntimeException("FileLaoderHandler: Unexpected error reading url resource " + resource);
            }
        }
    }

    public static IFileLoader getLoaderFor(@Nonnull final String extension)
    {
        return fileLoaders.get(extension.toLowerCase());
    }


    public static String getExtension(@Nonnull final File file)
    {
        return getExtension(file.getName());
    }

    public static String getExtension(@Nonnull final String fileName)
    {
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * Loads resources from a file
     *
     * @param file         - file location
     * @param fileConsumer - handler for the files once loaded
     */
    public static void loadResourcesFromFile(@Nonnull final File file,
                                             @Nonnull final Consumer<DataFileLoad> fileConsumer,
                                             @Nullable final FileCheckFunction fileCheckFunction)
    {
        final String extension = getExtension(file);
        if (fileLoaders.containsKey(extension))
        {
            final IFileLoader loader = fileLoaders.get(extension);
            if (loader.useReader())
            {
                try (BufferedReader stream = new BufferedReader(new FileReader(file)))
                {
                    loader.loadFile(new FileSource(file), stream, fileConsumer, fileCheckFunction);

                }
                catch (Exception e)
                {
                    throw new RuntimeException("FileLoaderHandler: Unexpected error reading file " + file);
                }
            }
            else
            {
                loader.loadFile(file, fileConsumer, fileCheckFunction);
            }
        }
        else
        {
            System.out.println("No Matching loader for extension: " + extension);//TODO move to logger or error handler;
        }
    }

    public static boolean canSupport(@Nonnull final String extension)
    {
        return fileLoaders.containsKey(extension.toLowerCase());
    }
}
