package com.builtbroken.builder;

import com.builtbroken.builder.data.DataFileLoad;
import com.builtbroken.builder.io.FileLoaderHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.Assertions;

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
        Assertions.assertTrue(file.exists() && file.isFile(), "Failed to locate test file: " + file);

        List<DataFileLoad> files = FileLoaderHandler.loadFile(file);
        Assertions.assertNotNull(files, "Failed to locate test file: " + file);
        Assertions.assertEquals(1, files.size(), "Failed to get only 1 test file: " + file);
        Assertions.assertNotNull(files.get(0), "Failed to load test file: " + file);
        Assertions.assertNotNull(files.get(0).element, "Failed to locate test json: " + file);

        return files.get(0).element;
    }



    public static JsonArray createJsonStringArray(String... items)
    {
        JsonArray array = new JsonArray();
        for (String s : items)
        {
            array.add(s);
        }
        return array;
    }

    public static JsonArray createJsonIntArray(int... items)
    {
        JsonArray array = new JsonArray();
        for (int s : items)
        {
            array.add(s);
        }
        return array;
    }
}
