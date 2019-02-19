package com.builtbroken.builder.io;

import com.builtbroken.builder.data.DataFileLoad;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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

    public static DataFileLoad loadFile(File file)
    {
        return null;
    }

    /**
     * Loads a files from the resource path
     *
     * @param resource - resource location
     * @return json file as a json element object
     * @throws IOException
     */
    public static void loadJsonFileFromResources(URL resource, List<DataFileLoad> dataFromFiles) throws Exception
    {
        if (resource != null)
        {
            loadJson(resource.getFile(), new InputStreamReader(resource.openStream()), dataFromFiles);
        }
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

    //Based on http://www.uofr.net/~greg/java/get-resource-listing.html
    public static List<String> getResourceListing(URL resource) throws URISyntaxException, IOException
    {
        List<String> result = new LinkedList(); //TODO check if this is faster than array list
        if (resource.getProtocol().equals("jar"))
        {
            //Get path to jar file
            String jarPath = getJarPath(resource);

            //open jar and get entities
            JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
            Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar

            //Loop entries
            while (entries.hasMoreElements())
            {
                String filePath = entries.nextElement().getName();
                if (!filePath.endsWith("/") && !result.contains(filePath))
                {
                    result.add(filePath);
                }
            }
        }
        return result;
    }

    public void loadResourcesFromFolder(final File currentFolder, final List<DataFileLoad> dataLoaded)
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

    public void loadResourcesFromFile(final File file, final List<DataFileLoad> dataLoaded)
    {
        final String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1).toLowerCase();
        if (extension.equalsIgnoreCase("jar"))
        {

        }
        else if (fileLoaders.containsKey(extension))
        {
            try
            {
                FileReader stream = new FileReader(file);
                List<JsonElement> elements = fileLoaders.get(extension).load(new BufferedReader(stream));
                for (JsonElement element : elements)
                {
                    if (element != null)
                    {
                        dataLoaded.add(new DataFileLoad(file.getAbsolutePath(), element));
                    }
                }
                stream.close();
            } catch (Exception e)
            {
                e.printStackTrace(); //TODO crash
            }
        }
    }

    public static String getJarPath(URL resource)
    {
        String path = resource.toExternalForm().replace("jar:", "").replace("file:", "");
        path = path.substring(1, path.indexOf("!")); //TODO fix need for starting at 1 for windows, as this is breaking linux paths
        //Fix for linux
        if (!path.startsWith(File.separator) && (path.indexOf(":") > 5 || path.indexOf(":") < 0))
        {
            path = File.separator + path;
        }
        return path;
    }
}
