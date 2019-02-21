package com.builtbroken.builder.io;

import com.google.gson.JsonElement;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

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

    @Override
    public List<JsonElement> loadFile(File file)
    {
        List<JsonElement> elements = new ArrayList();
        try
        {
            URL url = new URL("jar:file:/" + file.getAbsolutePath());
            loadResourcesFromPackage(url, elements);
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
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
    public void loadResourcesFromPackage(Class clazz, String folder)
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
                //loadResourcesFromPackage(url);
            }
            else
            {
                //debug.error("Could not locate folder[ " + folder + " ]");
            }
        } catch (Exception e)
        {
            throw new RuntimeException("Failed to load resources from class path.  Class='" + clazz + "' folder= '" + folder + "'", e);
        }
    }

    /**
     * Loads package
     */
    public void loadResourcesFromPackage(URL url, List<JsonElement> elements) throws Exception
    {
        try
        {
            if ("jar".equals(url.getProtocol()))
            {

                //Get path to jar file
                String jarPath = getJarPath(url);
                String decodedPath = URLDecoder.decode(jarPath, "UTF-8");

                //open jar and get entities
                JarFile jar = new JarFile(decodedPath);
                Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar

                List<String> listOfFilesParsed = new LinkedList();
                //Loop entries
                while (entries.hasMoreElements())
                {
                    final JarEntry entry = entries.nextElement();
                    final String name = entry.getName();
                    final String extension = name.substring(name.lastIndexOf(".") + 1, name.length());
                    if (!"jar".equalsIgnoreCase(extension) && FileLoaderHandler.canSupport(extension))
                    {
                        IFileLoader loader = FileLoaderHandler.getLoaderFor(extension);
                        if (loader != null && loader.useReader())
                        {
                            listOfFilesParsed.add(name);
                            //url.toExternalForm() + "/" + name <- add file path>?
                            elements.addAll(loader.loadFile(new InputStreamReader(jar.getInputStream(entry))));
                        }
                    }
                }
            }
            else
            {
                walkPaths(Paths.get(url.toURI()));
            }
        } catch (Exception e)
        {
            throw new RuntimeException("Failed to detect files from URL = " + url, e);
        }
    }

    //Old method, no longer used for .jars... keep for IDE usage
    private void walkPaths(Path filePath) throws IOException
    {
        Stream<Path> walk = Files.walk(filePath, 100);
        for (Iterator<Path> it = walk.iterator(); it.hasNext(); )
        {
            Path nextPath = it.next();
            String name = nextPath.getFileName().toString();
            if (name.lastIndexOf(".") > 1)
            {
                String extension = name.substring(name.lastIndexOf(".") + 1, name.length());
                // if (extensionsToLoad.contains(extension))
                //{
                //    debug.log("Found " + name);
                //    JsonLoader.loadJson(nextPath.toAbsolutePath().toString(), Files.newBufferedReader(nextPath), jsonEntries);
                //}
            }
        }
    }

    private FileSystem getFileSystem(URI uri) throws IOException
    {
        try
        {
            return FileSystems.getFileSystem(uri);
        } catch (FileSystemNotFoundException e)
        {
            return FileSystems.newFileSystem(uri, Collections.<String, String>emptyMap());
        }
    }
}
