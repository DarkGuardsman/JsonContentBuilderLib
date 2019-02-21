package com.builtbroken.builder.io;

import com.builtbroken.builder.data.DataFileLoad;
import com.google.gson.JsonElement;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * Handles loading all files from the file system into data structures for use
 * Created by Dark(DarkGuardsman, Robert) on 2/19/19.
 */
public class FileLoaderHandler
{

    private static HashMap<String, IFileLoader> fileLoaders = new HashMap();

    static
    {
        addFileLoader(new FileLoaderJSON());
    }

    public static void addFileLoader(IFileLoader fileLoader)
    {
        fileLoaders.put(fileLoader.getSupportedExtension(), fileLoader);
    }

    public static List<DataFileLoad> loadFile(File file)
    {
        List<DataFileLoad> dataEntries = new ArrayList();
        if (file.isDirectory())
        {
            loadResourcesFromFolder(file, dataEntries);
        }
        else
        {
            loadResourcesFromFile(file, dataEntries);
        }
        return dataEntries;
    }

    public static List<DataFileLoad> loadFile(URL resource)
    {
        List<DataFileLoad> dataEntries = new ArrayList();
        loadFile(resource, dataEntries);
        return dataEntries;
    }

    /**
     * Loads a files from the resource path
     *
     * @param resource - resource location
     * @return json file as a json element object
     * @throws IOException
     */
    public static void loadFile(URL resource, List<DataFileLoad> dataFromFiles)
    {
        if (resource != null)
        {
            final String extension = resource.toString().substring(resource.toString().lastIndexOf(".") + 1).toLowerCase();

            try
            {
                List<JsonElement> elements = fileLoaders.get(extension).loadFile(new InputStreamReader(resource.openStream()));
                for (JsonElement element : elements)
                {
                    if (element != null)
                    {
                        dataFromFiles.add(new DataFileLoad(resource.getFile(), element));
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace(); //TODO crash
            }
        }
    }

    public static void loadResourcesFromFolder(final File currentFolder, final List<DataFileLoad> dataLoaded)
    {
        for (File subFolderFile : currentFolder.listFiles())
        {
            if (subFolderFile.isDirectory())
            {
                loadResourcesFromFolder(subFolderFile, dataLoaded);
            }
            else
            {
                loadResourcesFromFile(currentFolder, dataLoaded);
            }
        }
    }

    public static void loadResourcesFromFile(final File file, final List<DataFileLoad> dataLoaded)
    {
        final String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1).toLowerCase();
        if (fileLoaders.containsKey(extension))
        {
            try (BufferedReader stream = new BufferedReader(new FileReader(file)))
            {
                List<JsonElement> elements = fileLoaders.get(extension).loadFile(stream);
                for (JsonElement element : elements)
                {
                    if (element != null)
                    {
                        dataLoaded.add(new DataFileLoad(file.getAbsolutePath(), element));
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace(); //TODO crash
            }
        }
    }
}
