package com.builtbroken.builder.io;

import com.builtbroken.builder.data.DataFileLoad;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2/21/19.
 */
public class TestJsonLoading
{

    public static final String[] TEST_FOLDERS = new String[]{"", "/[test]test", "/{test}test", "/test test", "/test.test", "/test_test", "/test-test", "/.local"};

    @Test
    public void testJsonPathLoading() throws Exception
    {
        for (String folderPath : TEST_FOLDERS)
        {
            File file = new File(System.getProperty("user.dir"), "src/test/resources/test" + folderPath + "/test.json");

            Assertions.assertTrue(file.exists(),"No file with path: " + file);

            List<DataFileLoad> elements = FileLoaderHandler.loadFile(file);
            validJsonTestFile(elements, file.toString());
        }
    }

    public static void validJsonTestFile(List<DataFileLoad> elements, String file)
    {
        Assertions.assertNotNull(elements, "Should contain something for " + file);
        Assertions.assertEquals(1, elements.size(), "Should contain 1 element for " + file);

        Assertions.assertNotNull(elements.get(0));
        Assertions.assertNotNull(elements.get(0).fileSource);
        Assertions.assertNotNull(elements.get(0).element);

        Assertions.assertTrue(elements.get(0).element.isJsonObject());
        Assertions.assertEquals(1, elements.get(0).element.getAsJsonObject().entrySet().size());
        Assertions.assertTrue(elements.get(0).element.getAsJsonObject().has("tag"));
        Assertions.assertEquals("hello", elements.get(0).element.getAsJsonObject().getAsJsonPrimitive("tag").getAsString());

    }
}
