package com.builtbroken.tests.locator;

import com.builtbroken.builder.data.DataFileLoad;
import com.builtbroken.builder.io.FileLoaderHandler;
import com.builtbroken.builder.loader.file.FileLocatorFiltered;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2/21/19.
 */
public class TestFilteredLocator
{

    @Test
    public void testFindA() throws Exception
    {
        File folder = new File(System.getProperty("user.dir"), "src/test/resources/test/filter/");

        FileLocatorFiltered locator = new FileLocatorFiltered(folder);
        locator.allow();
        locator.filter((file, sub) -> file.getName().equalsIgnoreCase("a.json"));

        Collection<DataFileLoad> fileLoads = locator.search();
        Assertions.assertEquals(1, fileLoads.size());
        Assertions.assertEquals("a.json", fileLoads.iterator().next().fileSource.fileName);
    }

    @Test
    public void testFindB() throws Exception
    {
        File folder = new File(System.getProperty("user.dir"), "src/test/resources/test/filter/");

        FileLocatorFiltered locator = new FileLocatorFiltered(folder);
        locator.allow();
        locator.filter((file, sub) -> file.getName().equalsIgnoreCase("b.json"));

        Collection<DataFileLoad> fileLoads = locator.search();
        Assertions.assertEquals(1, fileLoads.size());
        Assertions.assertEquals("b.json", fileLoads.iterator().next().fileSource.fileName);
    }

    @Test
    public void testFindC() throws Exception
    {
        File folder = new File(System.getProperty("user.dir"), "src/test/resources/test/filter/");

        FileLocatorFiltered locator = new FileLocatorFiltered(folder);
        locator.allow();
        locator.filter((file, sub) -> file.getName().equalsIgnoreCase("c.json"));

        Collection<DataFileLoad> fileLoads = locator.search();
        Assertions.assertEquals(1, fileLoads.size());
        Assertions.assertEquals("c.json", fileLoads.iterator().next().fileSource.fileName);
    }

    @Test
    public void testFindNotA() throws Exception
    {
        File folder = new File(System.getProperty("user.dir"), "src/test/resources/test/filter/");

        FileLocatorFiltered locator = new FileLocatorFiltered(folder);
        locator.disallow();
        locator.filter((file, sub) -> file.getName().equalsIgnoreCase("a.json"));

        Collection<DataFileLoad> fileLoads = locator.search();
        Assertions.assertEquals(2, fileLoads.size());
    }

    @Test
    public void testFindNotB() throws Exception
    {
        File folder = new File(System.getProperty("user.dir"), "src/test/resources/test/filter/");

        FileLocatorFiltered locator = new FileLocatorFiltered(folder);
        locator.disallow();
        locator.filter((file, sub) -> file.getName().equalsIgnoreCase("b.json"));

        Collection<DataFileLoad> fileLoads = locator.search();
        Assertions.assertEquals(2, fileLoads.size());
    }

    @Test
    public void testFindNotC() throws Exception
    {
        File folder = new File(System.getProperty("user.dir"), "src/test/resources/test/filter/");

        FileLocatorFiltered locator = new FileLocatorFiltered(folder);
        locator.disallow();
        locator.filter((file, sub) -> file.getName().equalsIgnoreCase("c.json"));

        Collection<DataFileLoad> fileLoads = locator.search();
        Assertions.assertEquals(2, fileLoads.size());
    }

    @Test
    public void testFindAB() throws Exception
    {
        File folder = new File(System.getProperty("user.dir"), "src/test/resources/test/filter/");

        FileLocatorFiltered locator = new FileLocatorFiltered(folder);
        locator.allow();
        locator.filter((file, sub) -> file.getName().equalsIgnoreCase("a.json"));
        locator.filter((file, sub) -> file.getName().equalsIgnoreCase("b.json"));

        Collection<DataFileLoad> fileLoads = locator.search();
        Assertions.assertEquals(2, fileLoads.size());
    }

    @Test
    public void testFindNotAB() throws Exception
    {
        File folder = new File(System.getProperty("user.dir"), "src/test/resources/test/filter/");

        FileLocatorFiltered locator = new FileLocatorFiltered(folder);
        locator.disallow();
        locator.filter((file, sub) -> file.getName().equalsIgnoreCase("a.json"));
        locator.filter((file, sub) -> file.getName().equalsIgnoreCase("b.json"));

        Collection<DataFileLoad> fileLoads = locator.search();
        Assertions.assertEquals(1, fileLoads.size());
    }

}
