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
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Handles loading all files from the file system into data structures for use
 * Created by Dark(DarkGuardsman, Robert) on 2/19/19.
 */
public class FileLoaderHandler
{

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
     * Loads a json file from the resource path
     *
     * @param file - file to read from
     * @return json file as a json element object
     * @throws IOException
     */
    public static void loadJsonFile(File file, List<DataFileLoad> dataFromFiles) throws IOException
    {
        if (file.exists() && file.isFile())
        {
            FileReader stream = new FileReader(file);
            loadJson(file.getName(), new BufferedReader(stream), dataFromFiles);
            stream.close();
        }
    }

    /**
     * Loads a json file from a reader
     *
     * @param fileName - file the reader loaded from, used only for error logs
     * @param reader   - reader with the data
     * @param dataFromFiles  - place to put json entries into
     */
    public static void loadJson(String fileName, Reader reader, List<DataFileLoad> dataFromFiles)
    {
        try
        {
            JsonReader jsonReader = new JsonReader(reader);
            JsonElement element = Streams.parse(jsonReader);
            dataFromFiles.add(new DataFileLoad(fileName, element));
        } catch (Exception e)
        {
            throw new RuntimeException("Failed to parse file '" + fileName + "'", e);
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

    public void loadResourcesFromFolder(final File currentFolder)
    {
        for (File subFolderFile : currentFolder.listFiles())
        {
            if (subFolderFile.isDirectory())
            {
                loadResourcesFromFolder(subFolderFile);
            }
            else
            {
                String extension = subFolderFile.getName().substring(subFolderFile.getName().lastIndexOf(".") + 1, subFolderFile.getName().length());
                if (extension.equalsIgnoreCase("jar"))
                {

                }
                //else if (extensionsToLoad.contains(extension))
                //{

                //}
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
