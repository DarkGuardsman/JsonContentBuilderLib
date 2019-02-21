package com.builtbroken.tests.io;

import com.builtbroken.builder.data.DataFileLoad;
import com.builtbroken.builder.io.FileLoaderHandler;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2/21/19.
 */
public class TestJsonLoading extends TestCase
{

    public static final String[] TEST_FOLDERS = new String[]{"", "/[test]test", "/{test}test", "/test test", "/test.test", "/test_test", "/test-test", "/.local"};

    @Test
    public void testJsonPathLoading() throws Exception
    {
        for (String folderPath : TEST_FOLDERS)
        {
            File file = new File(System.getProperty("user.dir"), "src/test/resources/test" + folderPath + "/test.json");

            assertTrue("No file with path: " + file, file.exists());

            List<DataFileLoad> elements = FileLoaderHandler.loadFile(file);
            validJsonTestFile(elements, file.toString());
        }
    }

    public static void validJsonTestFile(List<DataFileLoad> elements, String file)
    {
        assertNotNull("Should contain something for " + file, elements);
        assertEquals("Should contain 1 element for " + file, 1, elements.size());

        assertNotNull(elements.get(0));
        assertNotNull(elements.get(0).fileSource);
        assertNotNull(elements.get(0).element);

        assertTrue(elements.get(0).element.isJsonObject());
        assertEquals(1, elements.get(0).element.getAsJsonObject().entrySet().size());
        assertTrue(elements.get(0).element.getAsJsonObject().has("tag"));
        assertEquals("hello", elements.get(0).element.getAsJsonObject().getAsJsonPrimitive("tag").getAsString());

    }
}
