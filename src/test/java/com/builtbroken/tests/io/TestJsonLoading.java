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

    private static final String[] TEST_FOLDERS = new String[]{"", "/[test]test", "/{test}test", "/test test", "/test.test", "/test_test", "/test-test", "/.local"};

    @Test
    public void testJsonPathLoading() throws Exception
    {
        for (String folderPath : TEST_FOLDERS)
        {
            File file = new File(System.getProperty("user.dir"), "src/test/resources/test" + folderPath + "/test.json");

            assertTrue("No file with path: " + file, file.exists());

            List<DataFileLoad> elements = FileLoaderHandler.loadFile(file);
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

    @Test
    public void testJarFilePathLoading() throws Exception
    {
        for (String folderPath : TEST_FOLDERS)
        {
            File file = new File(System.getProperty("user.dir"), "src/test/resources/test" + folderPath + "/test.jar");

            assertTrue("No file with path: " + file, file.exists());

            //JsonContentLoader loader = new JsonContentLoader();

            //loader.loadResourcesFromPackage(new URL("jar:file:/" + file.getAbsolutePath() + "!/content"));
            //assertEquals("Failed for: " + folderPath, 1, loader.jsonEntries.size());
        }
    }

    @Test
    public void testJarPathDetector() throws Exception
    {
        for (String folderPath : TEST_FOLDERS)
        {
            File file = new File(System.getProperty("user.dir"), "src/test/resources/test/" + folderPath + "/test.jar");

            assertTrue(file.exists());
            URL url = new URL("jar:file:/" + file.getAbsolutePath() + "!/content");

            //List<String> files = JsonLoader.getResourceListing(url);

            //assertTrue("Failed for: " + folderPath, !files.isEmpty());
            //assertEquals("Failed for: " + folderPath, "content/test.json", files.get(0));
        }
    }
}
