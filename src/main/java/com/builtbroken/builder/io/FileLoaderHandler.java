package com.builtbroken.builder.io;

import com.builtbroken.builder.data.DataFileLoad;
import com.google.gson.JsonElement;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.function.Function;

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
            loadResourcesFromFolder(file, dataEntries, null);
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
        List<JsonElement> elements = loadFileJsonElements(resource);
        if (elements != null)
        {
            for (JsonElement element : elements)
            {
                if (element != null)
                {
                    dataFromFiles.add(new DataFileLoad(resource.getFile(), element));
                }
            }
        }
    }

    public static List<JsonElement> loadFileJsonElements(URL resource)
    {
        if (resource != null)
        {
            final String extension = resource.toString().substring(resource.toString().lastIndexOf(".") + 1).toLowerCase();

            try
            {
                return getLoaderFor(extension).loadFile(new InputStreamReader(resource.openStream()));
            } catch (Exception e)
            {
                e.printStackTrace(); //TODO crash
            }
        }
        return null;
    }

    public static IFileLoader getLoaderFor(String extension)
    {
        return fileLoaders.get(extension.toLowerCase());
    }

    public static void loadResourcesFromFolder(final File currentFolder, final List<DataFileLoad> dataLoaded, Function<File, Boolean> shouldPathFolder)
    {
        for (File subFolderFile : currentFolder.listFiles())
        {
            if (subFolderFile.isDirectory())
            {
                if (shouldPathFolder == null || shouldPathFolder.apply(subFolderFile))
                {
                    loadResourcesFromFolder(subFolderFile, dataLoaded, shouldPathFolder);
                    //TODO build path from main search folder and pass that into check instead
                    //Ex: /contents instead of C:/user/install/folder/contents
                }
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
        List<JsonElement> elements = null;
        if (fileLoaders.containsKey(extension))
        {
            final IFileLoader loader = fileLoaders.get(extension);
            if (loader.useReader())
            {
                try (BufferedReader stream = new BufferedReader(new FileReader(file)))
                {
                    elements = loader.loadFile(stream);

                } catch (Exception e)
                {
                    e.printStackTrace(); //TODO crash
                    return;
                }
            }
            else
            {
                elements = loader.loadFile(file);
            }
        }

        //Load json into output
        if (elements != null)
        {
            for (JsonElement element : elements)
            {
                if (element != null)
                {
                    dataLoaded.add(new DataFileLoad(file.getAbsolutePath(), element));
                }
            }
        }
    }

    public static boolean canSupport(String extension)
    {
        return fileLoaders.containsKey(extension.toLowerCase());
    }
}
