package com.builtbroken.tests;

import com.builtbroken.builder.data.DataFileLoad;
import com.builtbroken.builder.io.FileLoaderHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import junit.framework.TestCase;

import java.io.File;
import java.util.List;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-04.
 */
public class UnitTestHelpers
{

    /**
     * Gets the test file from the test resource folder. Does some
     * basic asserts to validate the file.
     *
     * @param path - path (ex: test.json, "clearTests/test1.json")
     * @return JsonElement read from the file
     */
    public static JsonElement getJsonFromTestFolder(String path)
    {
        File file = new File(System.getProperty("user.dir"), "src/test/resources/test/" + path);
        TestCase.assertTrue("Failed to locate test file: " + file, file.exists() && file.isFile());

        List<DataFileLoad> files = FileLoaderHandler.loadFile(file);
        TestCase.assertNotNull("Failed to locate test file: " + file, files);
        TestCase.assertEquals("Failed to get only 1 test file: " + file, 1, files.size());
        TestCase.assertNotNull("Failed to load test file: " + file, files.get(0));
        TestCase.assertNotNull("Failed to locate test json: " + file, files.get(0).element);

        return files.get(0).element;
    }

    public static JsonArray createJsonStringArray(String... items)
    {
        JsonArray array = new JsonArray();
        for(String s : items)
        {
            array.add(s);
        }
        return array;
    }

    public static JsonArray createJsonIntArray(int... items)
    {
        JsonArray array = new JsonArray();
        for(int s : items)
        {
            array.add(s);
        }
        return array;
    }
}
