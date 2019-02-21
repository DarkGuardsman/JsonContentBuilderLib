package com.builtbroken.builder.io;

import com.google.gson.JsonElement;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2/21/19.
 */
public class FileLoaderJar implements IFileLoader
{

    @Override
    public String getSupportedExtension()
    {
        return ".jar";
    }

    @Override
    public List<JsonElement> loadFile(Reader reader)
    {
        return null;
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
