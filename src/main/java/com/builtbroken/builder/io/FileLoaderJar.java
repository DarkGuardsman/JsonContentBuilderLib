package com.builtbroken.builder.io;

import com.google.gson.JsonElement;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2/21/19.
 */
public class FileLoaderJar implements IFileLoader
{

    @Override
    public String getSupportedExtension()
    {
        return "jar";
    }

    @Override
    public List<JsonElement> loadFile(File file)
    {
        List<JsonElement> elements = new ArrayList();
        try
        {
            //Loop all entries in jar
            ZipFile zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements())
            {
                //For each entry see if its a valid file we can load
                ZipEntry entry = entries.nextElement();
                if (!entry.isDirectory())
                {
                    final String fileName = entry.getName();
                    final String extension = FileLoaderHandler.getExtension(fileName);
                    if (FileLoaderHandler.canSupport(extension))
                    {
                        //If valid load only if it can use the reader
                        IFileLoader loader = FileLoaderHandler.getLoaderFor(extension);
                        if (loader.useReader())
                        {
                            InputStreamReader stream = new InputStreamReader(zipFile.getInputStream(entry));
                            elements.addAll(loader.loadFile(stream));
                            stream.close();
                        }
                    }
                }
            }
            zipFile.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return elements;
    }

    @Override
    public boolean useReader()
    {
        return false;
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

    /**
     * Loads package
     *
     * @param folder - package your looking to load data from
     */
    public List<JsonElement> loadResourcesFromPackage(Class clazz, String folder)
    {
        //Actual load process
        try
        {
            URL url = clazz.getClassLoader().getResource(folder);
            if (url == null)
            {
                url = clazz.getResource(folder);
            }
            if (url != null)
            {
                return loadResourcesFromPackage(url);
            }
            else
            {
                System.out.println("Could not locate folder[ " + folder + " ]"); //TODO logger or error handler
            }
        } catch (Exception e)
        {
            throw new RuntimeException("Failed to load resources from class path.  Class='" + clazz + "' folder= '" + folder + "'", e);
        }
        return new ArrayList();
    }

    /**
     * Loads package
     */
    public List<JsonElement> loadResourcesFromPackage(URL url) throws Exception
    {
        try
        {
            if ("jar".equals(url.getProtocol()))
            {
                //Get path to jar file
                String jarPath = getJarPath(url);
                String decodedPath = URLDecoder.decode(jarPath, "UTF-8");

                //open jar and get entities
                return loadFile(new File(decodedPath));
            }
            else
            {
                return walkPaths(Paths.get(url.toURI()));
            }
        } catch (Exception e)
        {
            throw new RuntimeException("Failed to detect files from URL = " + url, e);
        }
    }

    //Old method, no longer used for .jars... keep for IDE usage
    private List<JsonElement> walkPaths(Path filePath) throws IOException
    {
        List<JsonElement> elements = new ArrayList();
        Stream<Path> walk = Files.walk(filePath, 100);
        for (Iterator<Path> it = walk.iterator(); it.hasNext(); )
        {
            Path nextPath = it.next();
            String name = nextPath.getFileName().toString();
            if (name.lastIndexOf(".") > 1)
            {
                final String extension = FileLoaderHandler.getExtension(name);
                if (FileLoaderHandler.canSupport(extension))
                {
                    //If valid load only if it can use the reader
                    IFileLoader loader = FileLoaderHandler.getLoaderFor(extension);
                    if (loader.useReader())
                    {
                        BufferedReader stream = Files.newBufferedReader(nextPath);
                        elements.addAll(loader.loadFile(stream));
                        stream.close();
                    }
                }
            }
        }
        return elements;
    }
}
