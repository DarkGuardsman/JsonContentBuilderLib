package com.builtbroken.tests.io;

import com.builtbroken.builder.data.DataFileLoad;
import com.builtbroken.builder.io.FileLoaderHandler;
import com.builtbroken.builder.io.FileLoaderJar;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2/21/19.
 */
public class TestJarLoading extends TestCase
{

    @Test
    public void testJarPathDetector() throws Exception
    {
        for (String folderPath : TestJsonLoading.TEST_FOLDERS)
        {
            File file = new File(System.getProperty("user.dir"), "src/test/resources/test/" + folderPath + "/test.jar");

            assertTrue(file.exists());
            URL url = new URL("jar:file:/" + file.getAbsolutePath() + "!/content");

            List<String> files = FileLoaderJar.getResourceListing(url);

            assertNotNull(files);
            assertTrue("Failed for: " + folderPath, !files.isEmpty());
            assertEquals("Failed for: " + folderPath, "content/test.json", files.get(0));
        }
    }


    @Test
    public void testJarFilePathLoading() throws Exception
    {
        for (String folderPath : TestJsonLoading.TEST_FOLDERS)
        {
            File file = new File(System.getProperty("user.dir"), "src/test/resources/test" + folderPath + "/test.jar");

            assertTrue("No file with path: " + file, file.exists());

            List<DataFileLoad> files = FileLoaderHandler.loadFile(new URL("jar:file:/" + file.getAbsolutePath() + "!/content"));

            TestJsonLoading.validJsonTestFile(files, file.toString());
        }
    }
}
